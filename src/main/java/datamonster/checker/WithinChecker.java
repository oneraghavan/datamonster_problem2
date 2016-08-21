package datamonster.checker;

import datamonster.helper.CheckerHelpers;
import datamonster.dto.Rule;

import java.lang.reflect.Field;

public class WithinChecker implements RowChecker {
    @Override
    public boolean check(Rule rule, Object object) throws NoSuchFieldException, IllegalAccessException {
        Class<?> objectClass = object.getClass();

        Field fieldToBeCompared = objectClass.getDeclaredField(rule.getField());
        fieldToBeCompared.setAccessible(true);

        double valueToBeChecked = fieldToBeCompared.getDouble(object);

        Field fieldToBeComparedWith = objectClass.getDeclaredField(rule.getCompared());
        fieldToBeComparedWith.setAccessible(true);

        double valueToBeCheckedWith = fieldToBeComparedWith.getDouble(object);


        return CheckerHelpers.checkTheRule(valueToBeChecked, valueToBeCheckedWith, rule.getComparator());
    }
}
