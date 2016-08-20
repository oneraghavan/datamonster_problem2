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

    @Override
    public String toString() {
        return "Product{" +
                "storeId=" + storeId +
                ", minSalePrice=" + minSalePrice +
                ", minListPrice=" + minListPrice +
                ", title='" + title + '\'' +
                ", currencyType='" + currencyType + '\'' +
                ", availability='" + availability + '\'' +
                ", priceType='" + priceType + '\'' +
                ", countryCode='" + countryCode + '\'' +
                '}';
    }
}
