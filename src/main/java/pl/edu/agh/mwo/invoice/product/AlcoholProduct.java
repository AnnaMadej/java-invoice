package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public class AlcoholProduct extends OtherProduct {

    public AlcoholProduct(String name, BigDecimal price) {
        super(name, price, new BigDecimal("5.56"));
    }
    

}
