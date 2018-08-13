package driw.xtechnology.assignment.webshop.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Product {
    private String productName;
    private BigDecimal packagePrice;
    private int numItemsInPackage;
    private String imgUrl;
    private String displayName;
    private String description;

    public Product() {

    }
    public Product(String productName, BigDecimal packagePrice, int numItemsInPackage) {
        this.productName = productName;
        this.packagePrice = packagePrice;
        this.numItemsInPackage = numItemsInPackage;
    }

    public Product(String productName,
                   BigDecimal packagePrice,
                   int numItemsInPackage,
                   String imgUrl,
                   String displayName,
                   String description) {

        this.productName = productName;
        this.packagePrice = packagePrice;
        this.numItemsInPackage = numItemsInPackage;
        this.imgUrl = imgUrl;
        this.displayName = displayName;
        this.description = description;
    }

    public Product(Product product) {
        this.productName = new String(product.getProductName());
        this.packagePrice = product.getPackagePrice().add(BigDecimal.ZERO);
        this.numItemsInPackage = product.getNumItemsInPackage();
        this.imgUrl = product.getImgUrl();
        this.displayName = product.getDisplayName();
        this.description = product.getDescription();
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

    public String getImgUrl() {
        return imgUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
