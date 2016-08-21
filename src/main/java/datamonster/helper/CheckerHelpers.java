package datamonster.helper;

import datamonster.checker.RowChecker;
import datamonster.checker.StrictChecker;
import datamonster.checker.WithinChecker;
import datamonster.dto.Rule;

import static datamonster.helper.Constants.*;

public class CheckerHelpers {

    public static boolean checkTheRule(double valueToBeCompared, double valueToBeComparedWith, String comparator) {
        if (LESS_THAN_OR_EQUAL.equals(comparator)) {
            return valueToBeCompared <= valueToBeComparedWith;
        } else if (LESS_THAN.equals(comparator)) {
            return valueToBeCompared < valueToBeComparedWith;
        } else if (GREAT_THAN_OR_EQUAL.equals(comparator)) {
            return valueToBeCompared >= valueToBeComparedWith;
        } else if (GREAT_THAN.equals(comparator)) {
            return valueToBeCompared > valueToBeComparedWith;
        } else {
            return false;
        }
    }

    public static RowChecker getRowChecker(Rule rule) throws Exception {
        RowChecker checker;
        if (STRICT_COMPARATOR.equals(rule.getType())) {
            checker = new StrictChecker();
        } else if (WITHIN_COMPARATOR.equals(rule.getType())) {
            checker = new WithinChecker();
        } else {
            throw new Exception();
        }
        return checker;
    }


}
