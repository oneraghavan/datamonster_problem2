package datamonster.checker;

import datamonster.CheckerHelpers;
import datamonster.dto.Rule;

import java.lang.reflect.Field;

public class StrictChecker implements RowChecker {
    @Override
    public boolean check(Rule rule, Object object) throws NoSuchFieldException, IllegalAccessException {
        Class<?> objectClass = object.getClass();

        Field fieldToBeCompared = objectClass.getDeclaredField(rule.getField());
        fieldToBeCompared.setAccessible(true);

        double valueToBeChecked = fieldToBeCompared.getDouble(object);

        double valueToBeCheckedWith = Double.parseDouble(rule.getCompared());

        return CheckerHelpers.checkTheRule(valueToBeChecked, valueToBeCheckedWith, rule.getComparator());
    }
}
