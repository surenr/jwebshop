package driw.xtechnology.assignment.webshop.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Product {
    String productName;
    BigDecimal packagePrice;
    int numItemsInPackage;

    public Product() {

    }
    public Product(String productName, BigDecimal packagePrice, int numItemsInPackage) {
        this.productName = productName;
        this.packagePrice = packagePrice;
        this.numItemsInPackage = numItemsInPackage;
    }

    public Product(Product product) {
        this.productName = new String(product.getProductName());
        this.packagePrice = product.getPackagePrice().add(BigDecimal.ZERO);
        this.numItemsInPackage = product.getNumItemsInPackage();
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getPackagePrice() {
        return packagePrice;
    }

    public int getNumItemsInPackage() {
        return numItemsInPackage;
    }

    public BigDecimal getItemPrice() {
        return this.packagePrice.divide(new BigDecimal(this.numItemsInPackage), RoundingMode.HALF_EVEN);
    }
}
