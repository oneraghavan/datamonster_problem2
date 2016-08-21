package datamonster.checker;

import datamonster.dto.Rule;

public interface RowChecker {

    public  boolean check(Rule rule,Object object) throws NoSuchFieldException, IllegalAccessException;

}
