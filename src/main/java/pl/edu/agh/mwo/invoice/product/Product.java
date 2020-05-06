package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;
import java.util.Objects;

public abstract class Product {
    private final String name;

    private final BigDecimal price;

    private final BigDecimal taxPercent;

    private final BigDecimal excise;

    protected Product(String name, BigDecimal price, BigDecimal tax) {
        this(name, price, tax, BigDecimal.ZERO);
    }

    protected Product(String name, BigDecimal price, BigDecimal tax, BigDecimal excise) {
        if (name == null 
                || name.equals("") 
                || price == null 
                || tax == null
                || tax.compareTo(new BigDecimal(0)) < 0 
                || price.compareTo(new BigDecimal(0)) < 0
                || excise == null
                || excise.compareTo(new BigDecimal(0)) < 0) {
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.price = price;
        this.taxPercent = tax;
        this.excise = excise;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getTaxPercent() {
        return taxPercent;
    }

    public BigDecimal getPriceWithTax() {
        return price.multiply(taxPercent).add(price).add(excise);
    }

    @Override
    public String toString() {
        return this.name;
    }
    
    
    
    public BigDecimal getExcise() {
        return this.excise;
    }

    @Override
    public int hashCode() {
        return Objects.hash(excise, name, price, taxPercent);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Product)) {
            return false;
        }
        Product other = (Product) obj;
        return Objects.equals(name, other.name) 
                && Objects.equals(price, other.price)
                && Objects.equals(taxPercent, other.taxPercent)
                && Objects.equals(excise, other.excise);
              
    
    
    
    
    }
}
