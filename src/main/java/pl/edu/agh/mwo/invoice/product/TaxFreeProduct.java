package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public class TaxFreeProduct extends Product {
    public TaxFreeProduct(String name, BigDecimal price) {
        this(name, price, BigDecimal.ZERO);
    }
    
    protected TaxFreeProduct(String name, BigDecimal price, BigDecimal excise) {
        super(name, price, BigDecimal.ZERO, excise);
    }
}
