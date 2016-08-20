package datamonster;

import com.google.gson.Gson;
import datamonster.dto.Product;
import org.junit.Test;

public class CheckerBuilderTest {

    @Test
    public void ShouldTestCheckerBuilder() throws Exception {

        String temp = "{\"storeId\":0,\"minSalePrice\":160,\"minListPrice\":159.95,\"title\":\"Sentry Leather Watch - Men's Matte Black/Gold, One\",\"currencyType\":\"USD\",\"timestamp\":1471175991740,\"availability\":\"UNKNOWN\",\"priceType\":\"NORMAL\",\"countryCode\":\"US\"}";
        Gson gson = new Gson();

        Product product = gson.fromJson(temp, Product.class);

        CheckerBuilder checkerBuilder = new CheckerBuilder();
        checkerBuilder.checkAndNotify(product);
    }
}