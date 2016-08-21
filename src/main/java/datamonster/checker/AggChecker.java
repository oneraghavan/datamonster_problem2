package datamonster.checker;

import datamonster.dto.Product;
import datamonster.dto.Rule;

import java.util.concurrent.PriorityBlockingQueue;

public interface AggChecker {

    public boolean check(Rule rule, Product object, PriorityBlockingQueue<Product> queue) throws Exception;

}
