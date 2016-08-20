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

import static datamonster.Constants.STRICT_COMPARATOR;
import static datamonster.Constants.WITHIN_COMPARATOR;

public class CheckerAndNotifierService {

    private List<Rule> rules = new ArrayList<Rule>();

    private SlackNotifier slackNotifier;
    private SMSNotifier smsNotifier;

    public CheckerAndNotifierService() throws IOException {
        slackNotifier = new SlackNotifier();

        smsNotifier = new SMSNotifier();

        YamlReader reader = new YamlReader(new FileReader("product_rules.yml"));
        while (true) {
            Rule rule = reader.read(Rule.class);
            if (rule == null) break;
            rules.add(rule);
        }
    }

    public void checkAndNotify(Object object) throws Exception {
        Checker checker;
        for (Rule rule : rules) {

            if (STRICT_COMPARATOR.equals(rule.getType())) {
                checker = new StrictChecker();
            }else if(WITHIN_COMPARATOR.equals(rule.getType())){
                checker = new WithinChecker();
            }else{
                throw new Exception();
            }
            System.out.println(rule.getName());
            System.out.println(checker.check(rule,object));
        }
    }

}
