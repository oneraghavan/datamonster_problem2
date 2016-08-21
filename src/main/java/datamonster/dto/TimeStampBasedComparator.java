package datamonster.dto;

import java.util.Comparator;

public class TimeStampBasedComparator implements Comparator<Product> {
    @Override
    public int compare(Product x, Product y) {
        if (x.getTimestamp() < y.getTimestamp()) {
            return -1;
        }
        if (x.getTimestamp() > y.getTimestamp()) {
            return 1;
        }
        return 0;
    }
}
