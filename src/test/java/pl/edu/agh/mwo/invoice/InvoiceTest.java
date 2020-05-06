package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.mwo.invoice.product.AlcoholProduct;
import pl.edu.agh.mwo.invoice.product.DairyProduct;
import pl.edu.agh.mwo.invoice.product.OtherProduct;
import pl.edu.agh.mwo.invoice.product.Product;
import pl.edu.agh.mwo.invoice.product.TaxFreeProduct;

public class InvoiceTest {
    private Invoice invoice;

    @Before
    public void createEmptyInvoiceForTheTest() {
        invoice = new Invoice();
    }

    @Test
    public void testEmptyInvoiceHasEmptySubtotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTaxAmount() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasTheSameSubtotalAndTotalIfTaxIsZero() {
        Product taxFreeProduct = new TaxFreeProduct("Warzywa", new BigDecimal("199.99"));
        invoice.addProduct(taxFreeProduct);
        Assert.assertThat(invoice.getNetTotal(), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasProperSubtotalForManyProducts() {
        invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        invoice.addProduct(new OtherProduct("Wino", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("310"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasProperTaxValueForManyProduct() {
        // tax: 0
        invoice.addProduct(new TaxFreeProduct("Pampersy", new BigDecimal("200")));
        // tax: 8
        invoice.addProduct(new DairyProduct("Kefir", new BigDecimal("100")));
        // tax: 2.30
        invoice.addProduct(new OtherProduct("Piwko", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("10.30"), Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testInvoiceHasProperTotalValueForManyProduct() {
        // price with tax: 200
        invoice.addProduct(new TaxFreeProduct("Maskotki", new BigDecimal("200")));
        // price with tax: 108
        invoice.addProduct(new DairyProduct("Maslo", new BigDecimal("100")));
        // price with tax: 12.30
        invoice.addProduct(new OtherProduct("Chipsy", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("320.30"),
                Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasPropoerSubtotalWithQuantityMoreThanOne() {
        // 2x kubek - price: 10
        invoice.addProduct(new TaxFreeProduct("Kubek", new BigDecimal("5")), 2);
        // 3x kozi serek - price: 30
        final int dairyProductQuantity = 3;
        invoice.addProduct(new DairyProduct("Kozi Serek", new BigDecimal("10")), 
                dairyProductQuantity);
        // 1000x pinezka - price: 10
        final int otherProductQuantity = 1000;
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 
                otherProductQuantity);
        Assert.assertThat(new BigDecimal("50"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasPropoerTotalWithQuantityMoreThanOne() {
        // 2x kubek - price: 10
        invoice.addProduct(new TaxFreeProduct("Kubek", new BigDecimal("5")), 2);
        // 3x kozi serek - price: 30
        final int dairyProductQuantity = 3;
        invoice.addProduct(new DairyProduct("Kozi Serek", new BigDecimal("10")), 
                dairyProductQuantity);
        // 1000x pinezka - price: 10
        final int otherProductQuantity = 1000;
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 
                otherProductQuantity);
        Assert.assertThat(new BigDecimal("54.70"),
                Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithZeroQuantity() {
        invoice.addProduct(new TaxFreeProduct("Tablet", new BigDecimal("1678")), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithNegativeQuantity() {
        invoice.addProduct(new DairyProduct("Zsiadle mleko", new BigDecimal("5.55")), -1);
    }

    @Test
    public void testInvoiceHasNumberGreaterThan0() {
        int number = invoice.getNumber();
        Assert.assertThat(number, Matchers.greaterThan(0));
    }

    @Test
    public void testTwoInvoicesHaveDifferentNumbers() {
        int number1 = new Invoice().getNumber();
        int number2 = new Invoice().getNumber();
        Assert.assertNotEquals(number1, number2);
    }

    @Test
    public void testInvoiceDoesNotChangeItsNumber() {
        Assert.assertEquals(invoice.getNumber(), invoice.getNumber());
    }

    @Test
    public void testTheFirstInvoiceNumberIsLowerThanTheSecond() {
        int number1 = new Invoice().getNumber();
        int number2 = new Invoice().getNumber();
        Assert.assertThat(number1, Matchers.lessThan(number2));
    }

    @Test
    public void invoicesHaveConsequentNumbers() {
        int number1 = new Invoice().getNumber();
        int number2 = new Invoice().getNumber();
        Assert.assertEquals(number1, number2 - 1);
    }

    @Test
    public void testinvoiceCanBeConvertedToString() {
        String invoiceString = invoice.toString();
        Assert.assertTrue(invoiceString != null);
    }

    @Test
    public void testinvoiceStringStartsWithInvoiceNumber() {
        String invoiceString = invoice.toString();
        Assert.assertTrue(invoiceString.startsWith(Integer.toString(invoice.getNumber())));
    }

    @Test
    public void testinvoiceStringEndsWithNumberOfAllProducts() {
        invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        invoice.addProduct(new OtherProduct("Wino", new BigDecimal("10")));

        String invoiceString = invoice.toString();
        String lastLine = invoiceString.substring(invoiceString.lastIndexOf("\n") + 1);
        String expectedLine = "Liczba pozycji: " + invoice.getProducts().size();

        Assert.assertEquals(expectedLine, lastLine);
    }

    @Test
    public void testinvoiceStringHasProperAmountOfLines() {
        invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        invoice.addProduct(new OtherProduct("Wino", new BigDecimal("10")));
        String invoiceString = invoice.toString();
        String onlyProductsString = invoiceString.substring(invoiceString.indexOf("\n") + 1,
                invoiceString.lastIndexOf("\n") + 1);
        int numberOfLines = onlyProductsString.split("\r\n|\r|\n").length;
        Assert.assertEquals(numberOfLines, invoice.getProducts().size());
    }

    @Test
    public void testinvoiceStringHasProperContentOfLines() {
        invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        invoice.addProduct(new OtherProduct("Wino", new BigDecimal("10")));

        String invoiceString = invoice.toString();
        String onlyProductsString = invoiceString.substring(invoiceString.indexOf("\n") + 1,
                invoiceString.lastIndexOf("\n") + 1);

        Iterator<Map.Entry<Product, Integer>> productIterator = invoice.getProducts().entrySet()
                .iterator();

        Scanner sc = new Scanner(onlyProductsString);
        String productLine;
        String productLineFromString;

        while (sc.hasNextLine()) {
            Map.Entry<Product, Integer> currentProduct = productIterator.next();
            productLine = currentProduct.getKey() + ", " + currentProduct.getValue() + ", "
                    + currentProduct.getKey().getPrice();
            productLineFromString = sc.nextLine();
            Assert.assertEquals(productLine, productLineFromString);
        }
        sc.close();

        Assert.assertTrue(!productIterator.hasNext());
    }
    

    @Test
    public void testproductQuantityChangesWhenAddedMultipleTimesOneAtTime() {
        Product product = new TaxFreeProduct("Owoce", new BigDecimal("100"));  
        final Integer times = 5;
        for (int i = 0; i < times; i++) {
            invoice.addProduct(product);
        }
        Assert.assertTrue(invoice.getProducts().containsKey(product));
        Assert.assertEquals(times, invoice.getProducts().get(product));      
    }
    
    @Test
    public void testproductQuantityChangesWhenAddedMultipleTimesMoreThanOneAtTime() {
        Product product = new TaxFreeProduct("Owoce", new BigDecimal("100"));  
        
        final Integer firstTimeQuantity = 1;
        final Integer secondTimeQuantity = 2;
        invoice.addProduct(product, firstTimeQuantity);
        invoice.addProduct(product, secondTimeQuantity);
        
        Assert.assertTrue(invoice.getProducts().containsKey(product));
        Assert.assertEquals(Integer.valueOf(firstTimeQuantity + secondTimeQuantity), 
                invoice.getProducts().get(product));      
    }
    
    @Test
    public void testproductQuantityChangesWhenAddedMultipleTimesAsNewObject() {
        Product product = null;
        final Integer times = 12;
        for (int i = 0; i < times; i++) {
            product = new TaxFreeProduct("Owoce", new BigDecimal("100"));  
            invoice.addProduct(product);
        }
        
        Assert.assertTrue(invoice.getProducts().containsKey(product));
        Assert.assertEquals(times, invoice.getProducts().get(product));      
    
    }
    
    @Test
    public void testproductAppearsMoreThanOnceWhenAddedWithDifferentPriceOneAtTime() {
        Product product1 =   new DairyProduct("Maslanka", new BigDecimal("100"));
        Product product2 =   new DairyProduct("Maslanka", new BigDecimal("200"));
        
        invoice.addProduct(product1);
        invoice.addProduct(product2);
     
        Assert.assertTrue(invoice.getProducts().containsKey(product1));
        Assert.assertTrue(invoice.getProducts().containsKey(product2));
        Assert.assertEquals(2, invoice.getProducts().size());
        Assert.assertEquals(Integer.valueOf(1), invoice.getProducts().get(product1));      
        Assert.assertEquals(Integer.valueOf(1), invoice.getProducts().get(product2)); 
        
    }
    
    @Test
    public void testproductAppearsMoreThanOnceWhenAddedWithDifferentPriceMoreThanOneAtTime() {
        Product product1 = new OtherProduct("Wino", new BigDecimal("10"));
        Product product2 = new OtherProduct("Wino", new BigDecimal("15"));
        
        final Integer product1Quantity = 3;
        final Integer product2Quantity = 4;
        
        invoice.addProduct(product1, product1Quantity);
        invoice.addProduct(product2, product2Quantity);
     
        Assert.assertTrue(invoice.getProducts().containsKey(product1));
        Assert.assertTrue(invoice.getProducts().containsKey(product2));
        Assert.assertEquals(2, invoice.getProducts().size());
        Assert.assertEquals(product1Quantity, invoice.getProducts().get(product1));    
        Assert.assertEquals(product2Quantity, invoice.getProducts().get(product2));   
        
    }
    
    @Test
    public void testproductAppearsMoreThanOnceWhenAddedWithDifferentTaxOneAtTime() {
        Product product1 =   new DairyProduct("Maslanka", new BigDecimal("100"));
        Product product2 =   new OtherProduct("Maslanka", new BigDecimal("100"));
        
        invoice.addProduct(product1);
        invoice.addProduct(product2);
     
        Assert.assertTrue(invoice.getProducts().containsKey(product1));
        Assert.assertTrue(invoice.getProducts().containsKey(product2));
        Assert.assertEquals(2, invoice.getProducts().size());
        Assert.assertEquals(Integer.valueOf(1), invoice.getProducts().get(product1));      
        Assert.assertEquals(Integer.valueOf(1), invoice.getProducts().get(product2)); 
        
    }
    
    @Test
    public void testproductAppearsMoreThanOnceWhenAddedWithDifferentTaxMoreThanOneAtTime() {
        Product product1 =   new DairyProduct("Maslanka", new BigDecimal("100"));
        Product product2 =   new OtherProduct("Maslanka", new BigDecimal("100"));
        
        final Integer product1Quantity = 3;
        final Integer product2Quantity = 4;
        
        invoice.addProduct(product1, product1Quantity);
        invoice.addProduct(product2, product2Quantity);
     
        Assert.assertTrue(invoice.getProducts().containsKey(product1));
        Assert.assertTrue(invoice.getProducts().containsKey(product2));
        Assert.assertEquals(2, invoice.getProducts().size());
        Assert.assertEquals(product1Quantity, invoice.getProducts().get(product1));    
        Assert.assertEquals(product2Quantity, invoice.getProducts().get(product2));  
        
    }
    
    @Test
    public void testproductAppearsMoreThanOnceWhenAddedWithDifferentExciseOneAtTime() {
        Product product1 =   new AlcoholProduct("Wino", new BigDecimal("100"));
        Product product2 =   new OtherProduct("Wino", new BigDecimal("100"));
        
        invoice.addProduct(product1);
        invoice.addProduct(product2);
     
        Assert.assertTrue(invoice.getProducts().containsKey(product1));
        Assert.assertTrue(invoice.getProducts().containsKey(product2));
        Assert.assertEquals(2, invoice.getProducts().size());
        Assert.assertEquals(Integer.valueOf(1), invoice.getProducts().get(product1));      
        Assert.assertEquals(Integer.valueOf(1), invoice.getProducts().get(product2));         
    }
    
    @Test
    public void testproductAppearsMoreThanOnceWhenAddedWithDifferentExciseMoreThanOneAtTime() {
        Product product1 =   new AlcoholProduct("Wino", new BigDecimal("100"));
        Product product2 =   new OtherProduct("Wino", new BigDecimal("100"));
        
        final Integer product1Quantity = 3;
        final Integer product2Quantity = 4;
        
        invoice.addProduct(product1, product1Quantity);
        invoice.addProduct(product2, product2Quantity);
     
        Assert.assertTrue(invoice.getProducts().containsKey(product1));
        Assert.assertTrue(invoice.getProducts().containsKey(product2));
        Assert.assertEquals(2, invoice.getProducts().size());
        Assert.assertEquals(product1Quantity, invoice.getProducts().get(product1));    
        Assert.assertEquals(product2Quantity, invoice.getProducts().get(product2));          
    }
    
  
    
    
    

}
