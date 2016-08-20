package datamonster;

import com.esotericsoftware.yamlbeans.YamlReader;
import datamonster.checker.Checker;
import datamonster.checker.StrictChecker;
import datamonster.checker.WithinChecker;
import datamonster.dto.Rule;
import datamonster.notifier.SMSNotifier;
import datamonster.notifier.SlackNotifier;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static datamonster.Constants.*;

public class CheckerAndNotifierService {

    private List<Rule> rules = new ArrayList<Rule>();

    private SlackNotifier slackNotifier;
    private SMSNotifier smsNotifier;

    CheckerAndNotifierService() throws IOException {
        slackNotifier = new SlackNotifier();

        smsNotifier = new SMSNotifier();

        YamlReader reader = new YamlReader(new FileReader("product_rules.yml"));
        while (true) {
            Rule rule = reader.read(Rule.class);
            if (rule == null) break;
            rules.add(rule);
        }
    }

    private void checkAndNotify(Object object) throws Exception {
        Checker checker;
        for (Rule rule : rules) {

            if (STRICT_COMPARATOR.equals(rule.getType())) {
                checker = new StrictChecker();
            }else if(WITHIN_COMPARATOR.equals(rule.getType())){
                checker = new WithinChecker();
            }else{
                throw new Exception();
            }
            if(checker.check(rule,object)){
                if(SLACK_NOTIFICATION.equals(rule.getNotification())){
                    slackNotifier.notify(makeMessage(rule,object));
                }else if(SMS_NOTIFICATION.equals(rule.getNotification())){
                    smsNotifier.notify(makeMessage(rule,object));
                }
            }
        }
    }

    Runnable getCheckAndNotifyRunnable(final Object object){
        return new Runnable() {
            public void run() {
                try {
                    checkAndNotify(object);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }


    private String makeMessage(Rule rule,Object object){
        return rule.getName() + "failed for object " + object.toString();
    }

}
