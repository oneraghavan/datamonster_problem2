package datamonster;

import com.fasterxml.jackson.databind.ObjectMapper;
import datamonster.dto.Product;
import org.junit.Test;

public class CheckerAndNotifierServiceTest {

    @Test
    public void ShouldTestCheckerBuilder() throws Exception {

        String temp = "{\"storeId\":0,\"minSalePrice\":160,\"minListPrice\":159.95,\"title\":\"Sentry Leather Watch - Men's Matte Black/Gold, One\",\"currencyType\":\"USD\",\"timestamp\":1471175991740,\"availability\":\"UNKNOWN\",\"priceType\":\"NORMAL\",\"countryCode\":\"US\"}";
        ObjectMapper objectMapper = new ObjectMapper();

        Product product = objectMapper.readValue(temp, Product.class);

        CheckerAndNotifierService checkerAndNotifierService = new CheckerAndNotifierService();
        Runnable checkAndNotifyRunnable = checkerAndNotifierService.getCheckAndNotifyRunnable(product);
        checkAndNotifyRunnable.run();
    }
}