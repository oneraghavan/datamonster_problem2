package datamonster;

import static datamonster.Constants.*;

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
        }else{
            return false;
        }

    }

}
