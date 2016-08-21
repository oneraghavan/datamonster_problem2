package datamonster;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.twilio.sdk.TwilioRestException;
import datamonster.checker.AggCountBasedChecker;
import datamonster.checker.AggTimeBasedChecker;
import datamonster.checker.RowChecker;
import datamonster.dto.Product;
import datamonster.dto.Rule;
import datamonster.dto.TimeStampBasedComparator;
import datamonster.notifier.SMSNotifier;
import datamonster.notifier.SlackNotifier;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

import static datamonster.CheckerHelpers.getRowChecker;
import static datamonster.Constants.SLACK_NOTIFICATION;
import static datamonster.Constants.SMS_NOTIFICATION;

public class CheckerAndNotifierService {

    private List<Rule> rowRules = new ArrayList<Rule>();
    private List<Rule> aggRules = new ArrayList<Rule>();

    private SlackNotifier slackNotifier;
    private SMSNotifier smsNotifier;
    TimeStampBasedComparator timeStampBasedComparator = new TimeStampBasedComparator();
    PriorityBlockingQueue<Product> countBasedPriorityBlockingQueue = new PriorityBlockingQueue<Product>(1, timeStampBasedComparator);
    PriorityBlockingQueue<Product> timePriorityBlockingQueue = new PriorityBlockingQueue<Product>(1, timeStampBasedComparator);

    AggCountBasedChecker aggCountBasedChecker = new AggCountBasedChecker();
    AggTimeBasedChecker aggTimeBasedChecker = new AggTimeBasedChecker();

    ExecutorService notifierExecutorService = Executors.newFixedThreadPool(1);

    CheckerAndNotifierService() throws IOException {
        slackNotifier = new SlackNotifier();
        smsNotifier = new SMSNotifier();

        YamlReader reader = new YamlReader(new FileReader("product_rules.yml"));
        while (true) {
            Rule rule = reader.read(Rule.class);
            if (rule == null) break;
            if (rule.getIsAgg()) {
                aggRules.add(rule);
            } else {
                rowRules.add(rule);
            }
        }
    }

    private void checkAndAsyncNotify(Object object) throws Exception {
        RowChecker checker;
        for (Rule rule : rowRules) {
            checker = getRowChecker(rule);
            if (checker.check(rule, object)) {
                notifyAsynchronously(object, rule);
            }
        }

        for (Rule rule : aggRules) {
            if (rule.getAggType().equals("count")) {
                if (aggCountBasedChecker.check(rule, (Product) object, countBasedPriorityBlockingQueue)) {
                    notifyAsynchronously(object, rule);
                }
            } else if (rule.getAggType().equals("time")) {
                if (aggTimeBasedChecker.check(rule, (Product) object, timePriorityBlockingQueue)) {
                    notifyAsynchronously(object, rule);
                }
            }
        }
    }

    private void notifyAsynchronously(final Object object, final Rule rule) throws TwilioRestException {
        notifierExecutorService.submit(new Runnable() {
            public void run() {
                try {
                    for (String notificaiton : rule.getNotifications()) {
                        if (SLACK_NOTIFICATION.equals(notificaiton)) {
                            slackNotifier.notify(makeMessage(rule, object));
                        } else if (SMS_NOTIFICATION.equals(notificaiton)) {
                            smsNotifier.notify(makeMessage(rule, object));
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    Runnable getCheckAndNotifyRunnable(final Object object) {
        return new Runnable() {
            public void run() {
                try {
                    checkAndAsyncNotify(object);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }


    private String makeMessage(Rule rule, Object object) {
        return rule.getName() + "failed for object " + object.toString();
    }

}
