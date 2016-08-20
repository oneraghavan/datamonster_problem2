package datamonster.checker;

import datamonster.dto.Rule;

public interface Checker {

    public  boolean check(Rule rule,Object object) throws NoSuchFieldException, IllegalAccessException;

}
