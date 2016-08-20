package datamonster;

import datamonster.notifier.SMSNotifier;
import org.junit.Test;

public class CheckerAndNotifierServiceTest {

    @Test
    public void ShouldTestCheckerBuilder() throws Exception {

//        String temp = "{\"storeId\":0,\"minSalePrice\":160,\"minListPrice\":159.95,\"title\":\"Sentry Leather Watch - Men's Matte Black/Gold, One\",\"currencyType\":\"USD\",\"timestamp\":1471175991740,\"availability\":\"UNKNOWN\",\"priceType\":\"NORMAL\",\"countryCode\":\"US\"}";
//        Gson gson = new Gson();
//
//        Product product = gson.fromJson(temp, Product.class);
//
//        CheckerAndNotifierService checkerAndNotifierService = new CheckerAndNotifierService();
//        checkerAndNotifierService.checkAndNotify(product);

        SMSNotifier smsNotifier = new SMSNotifier();
        smsNotifier.notify("sdjgjkdsjkgkjd");
    }
}