package datamonster.checker;

import datamonster.CheckerHelpers;
import datamonster.dto.Product;
import datamonster.dto.Rule;

import java.util.concurrent.PriorityBlockingQueue;

public class AggCountBasedChecker implements AggChecker {


    @Override
    public boolean check(Rule rule, Product product, PriorityBlockingQueue<Product> queue) throws Exception {
        Long sizeLimitForCountAgg = rule.getLimit();
        queue.add(product);
        if (queue.size() > sizeLimitForCountAgg) {
            queue.remove();
        }
        Boolean isFailing = true;
        RowChecker rowChecker = CheckerHelpers.getRowChecker(rule);
        if (queue.size() == sizeLimitForCountAgg) {
            for (Object element : queue) {
                isFailing = isFailing && rowChecker.check(rule, element);
            }
        } else {
            isFailing = false;
        }
        return isFailing;
    }
}
