package datamonster.checker;

import datamonster.helper.CheckerHelpers;
import datamonster.dto.Product;
import datamonster.dto.Rule;

import java.time.Instant;
import java.util.concurrent.PriorityBlockingQueue;

public class AggTimeBasedChecker implements AggChecker {


    @Override
    public boolean check(Rule rule, Product product, PriorityBlockingQueue<Product> queue) throws Exception {
        queue.add(product);
        long now = Instant.now().toEpochMilli();
        long deltaSec = rule.getLimit();
        for (Product element : queue) {
            if (now - element.getTimestamp() > deltaSec) {
                queue.remove(element);
            }
        }
        Boolean isFailing = true;
        RowChecker rowChecker = CheckerHelpers.getRowChecker(rule);
        for (Object element : queue) {
            isFailing = isFailing && rowChecker.check(rule, element);
        }
        return isFailing;
    }
}
