package datamonster.service;

import com.esotericsoftware.yamlbeans.YamlReader;
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
import java.util.concurrent.PriorityBlockingQueue;

import static datamonster.helper.CheckerHelpers.getRowChecker;

public class CheckerAndNotifyService {

    private List<Rule> rowRules = new ArrayList<Rule>();
    private List<Rule> aggRules = new ArrayList<Rule>();

    private SlackNotifier slackNotifier;
    private SMSNotifier smsNotifier;
    TimeStampBasedComparator timeStampBasedComparator = new TimeStampBasedComparator();
    PriorityBlockingQueue<Product> countBasedPriorityBlockingQueue = new PriorityBlockingQueue<Product>(1, timeStampBasedComparator);
    PriorityBlockingQueue<Product> timePriorityBlockingQueue = new PriorityBlockingQueue<Product>(1, timeStampBasedComparator);

    AggCountBasedChecker aggCountBasedChecker = new AggCountBasedChecker();
    AggTimeBasedChecker aggTimeBasedChecker = new AggTimeBasedChecker();

    NotifierService notifierService = new NotifierService();

    public CheckerAndNotifyService() throws IOException {
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
                notifierService.notifyAsynchronously(object, rule);
            }
        }

        for (Rule rule : aggRules) {
            boolean check;
            if (rule.getAggType().equals("count")) {
                check = aggCountBasedChecker.check(rule, (Product) object, countBasedPriorityBlockingQueue);
            } else if (rule.getAggType().equals("time")) {
                check = aggTimeBasedChecker.check(rule, (Product) object, timePriorityBlockingQueue);
            } else {
                throw new Exception();
            }
            if (check) {
                notifierService.notifyAsynchronously(object, rule);
            }
        }
    }


    public Runnable getCheckAndNotifyRunnable(final Object object) {
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



}
