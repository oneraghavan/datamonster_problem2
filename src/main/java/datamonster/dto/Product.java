package datamonster.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {

    private int storeId;
    private double minSalePrice;
    private double minListPrice;
    private String title;
    private String currencyType;
    private String availability;
    private String  priceType;
    private String countryCode;


}
