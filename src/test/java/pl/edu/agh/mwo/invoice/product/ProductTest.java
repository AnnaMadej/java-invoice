package pl.edu.agh.mwo.invoice.product;

import static org.junit.Assert.assertFalse;

import java.math.BigDecimal;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class ProductTest {
   
    @Test
    public void testProductNameIsCorrect() {
        String productName = "buty";
        Product product = new OtherProduct(productName, new BigDecimal("100.0"));
        Assert.assertEquals(productName, product.getName());
    }

    @Test
    public void testProductPriceAndTaxWithDefaultTax() {
        Product product = new OtherProduct("Ogorki", new BigDecimal("100.0"));
        Assert.assertThat(new BigDecimal("100"), Matchers.comparesEqualTo(product.getPrice()));
        Assert.assertThat(new BigDecimal("0.23"),
                Matchers.comparesEqualTo(product.getTaxPercent()));
    }

    @Test
    public void testProductPriceAndTaxWithDairyProduct() {
        Product product = new DairyProduct("Szarlotka", new BigDecimal("100.0"));
        Assert.assertThat(new BigDecimal("100"), Matchers.comparesEqualTo(product.getPrice()));
        Assert.assertThat(new BigDecimal("0.08"),
                Matchers.comparesEqualTo(product.getTaxPercent()));
    }

    @Test
    public void testPriceWithTax() {
        Product product = new DairyProduct("Oscypek", new BigDecimal("100.0"));
        Assert.assertThat(new BigDecimal("108"),
                Matchers.comparesEqualTo(product.getPriceWithTax()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProductWithNullName() {
        new OtherProduct(null, new BigDecimal("100.0"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProductWithEmptyName() {
        new TaxFreeProduct("", new BigDecimal("100.0"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProductWithNullPrice() {
        new DairyProduct("Banany", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProductWithNegativePrice() {
        new TaxFreeProduct("Mandarynki", new BigDecimal("-1.00"));
    }

    @Test
    public void testOtherProductExciseAndTaxPercentWithDefaultExcise() {
        final BigDecimal productPrice = new BigDecimal("50");
        final BigDecimal defaultExciseForOtherProduct = BigDecimal.ZERO;
        final BigDecimal taxForOtherProduct = new BigDecimal("0.23");

        Product product = new OtherProduct("Ogorki", productPrice);
        Assert.assertThat(defaultExciseForOtherProduct,
                Matchers.comparesEqualTo(product.getExcise()));
        Assert.assertThat(taxForOtherProduct, Matchers.comparesEqualTo(product.getTaxPercent()));
    }

    @Test
    public void testTaxFreeProductExciseWithDefaultExcise() {    
        Product product = new TaxFreeProduct("Ogórki", new BigDecimal("50"));
        Assert.assertThat(BigDecimal.ZERO,
                Matchers.comparesEqualTo(product.getExcise()));
        Assert.assertThat(BigDecimal.ZERO, 
                Matchers.comparesEqualTo(product.getTaxPercent()));
    }

    @Test
    public void testAlcoholProductExciseTaxPercentAndPrice() {

        Product product = new AlcoholProduct("Bottle Of Wine",  new BigDecimal("100"));
        Assert.assertThat(new BigDecimal("5.56"), Matchers.comparesEqualTo(product.getExcise()));
        Assert.assertThat(new BigDecimal("0.23"), 
                Matchers.comparesEqualTo(product.getTaxPercent()));
        Assert.assertThat(new BigDecimal("100"), Matchers.comparesEqualTo(product.getPrice()));
    }

    @Test
    public void testLiquidFuelExciseAndTaxPercent() {
        final BigDecimal productPrice = new BigDecimal("100");

        Product product = new LiquidFuelProduct("Fuel Canister", productPrice);
        Assert.assertThat(new BigDecimal("5.56"), Matchers.comparesEqualTo(product.getExcise()));
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(product.getTaxPercent()));
        Assert.assertThat(productPrice, Matchers.comparesEqualTo(product.getPrice()));
    }
    
    @Test
    public void testAlcoholProductPriceWithTax() {
        final BigDecimal priceWithTax = new BigDecimal("100").add(new BigDecimal("100")
                .multiply(new BigDecimal("0.23"))).add(new BigDecimal("5.56"));
      
        Product product = new AlcoholProduct("Bottle of wine", new BigDecimal("100"));
        Assert.assertThat(priceWithTax,
                Matchers.comparesEqualTo(product.getPriceWithTax()));
    }
    
    @Test
    public void testLiquidFuelProductPriceWithTax() {
        final BigDecimal priceWithTax = new BigDecimal("100").add(new BigDecimal("100")
                .multiply(BigDecimal.ZERO)).add(new BigDecimal("5.56"));
    
        Product product = new LiquidFuelProduct("Fuel canister", new BigDecimal("100"));
        Assert.assertThat(priceWithTax,
                Matchers.comparesEqualTo(product.getPriceWithTax()));
    }
    
    @Test
    public void testSameProductsAreEqual() {
        Product product1 = new OtherProduct("produkt", new BigDecimal("100"));
        Product product2 = product1;
        
        Assert.assertTrue(product1.equals(product2));          
    }
    
    @Test
    public void testDifferentObjectsNotEqualToProduct() {
        Product product1 = new OtherProduct("produkt", new BigDecimal("100"));
        Assert.assertFalse(product1.equals("string"));
    }
    
    @Test
    public void testSameProductsWithDifferentPriceAreNotEqual() {
        Product product1 = new OtherProduct("Produkt", new BigDecimal("100"));
        Product product2 = new OtherProduct("Produkt", new BigDecimal("200"));
        
        Assert.assertFalse(product1.equals(product2));
    }
    
    @Test
    public void testSameProductsWithDifferentTaxAreNotEqual() {
        Product product1 = new OtherProduct("Produkt", new BigDecimal("100"));
        Product product2 = new DairyProduct("Produkt", new BigDecimal("100"));
        
        Assert.assertFalse(product1.equals(product2));
    }
    
    @Test
    public void testSameProductsWithDifferentExciseAreNotEqual() {
        Product product1 = new TaxFreeProduct("Produkt", new BigDecimal("100"));
        Product product2 = new LiquidFuelProduct("Produkt", new BigDecimal("100"));
        
        Assert.assertFalse(product1.equals(product2));
    }
    
   
        
}
