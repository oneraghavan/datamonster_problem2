package datamonster.service;

import com.twilio.sdk.TwilioRestException;
import datamonster.checker.AggCountBasedChecker;
import datamonster.checker.AggTimeBasedChecker;
import datamonster.dto.Rule;
import datamonster.notifier.SMSNotifier;
import datamonster.notifier.SlackNotifier;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static datamonster.helper.Constants.SLACK_NOTIFICATION;
import static datamonster.helper.Constants.SMS_NOTIFICATION;


public class NotifierService {

    ExecutorService notifierExecutorService = Executors.newFixedThreadPool(1);

    private SlackNotifier slackNotifier;
    private SMSNotifier smsNotifier;

    AggCountBasedChecker aggCountBasedChecker;
    AggTimeBasedChecker aggTimeBasedChecker;

    public NotifierService() throws IOException {
        slackNotifier = new SlackNotifier();
        smsNotifier = new SMSNotifier();

        aggCountBasedChecker = new AggCountBasedChecker();
        aggTimeBasedChecker = new AggTimeBasedChecker();

    }

    void notifyAsynchronously(final Object object, final Rule rule) throws TwilioRestException {
        for (final String notificaiton : rule.getNotifications()) {
            notifierExecutorService.submit(new Runnable() {
                public void run() {
                    try {
                        if (SLACK_NOTIFICATION.equals(notificaiton)) {
                            slackNotifier.notify(makeMessage(rule, object));
                        } else if (SMS_NOTIFICATION.equals(notificaiton)) {
                            smsNotifier.notify(makeMessage(rule, object));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private String makeMessage(Rule rule, Object object) {
        return rule.getName() + "failed for object " + object.toString();
    }
}
