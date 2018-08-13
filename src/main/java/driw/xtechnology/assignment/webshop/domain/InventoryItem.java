package driw.xtechnology.assignment.webshop.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class InventoryItem {
    protected String category;
    protected int boxQuantity;
    protected int itemQuantity;
    protected int numOfProductsInCategory;
    protected BigDecimal individualItemPrice;
    protected Product product;


    public InventoryItem () {

    }

    public InventoryItem(Product product, int numberOfProducts) {
        this.updateItem(product, numberOfProducts);
    }

    public InventoryItem(InventoryItem inventoryItem) {

        this.category = new String(inventoryItem.getCategory());
        this.boxQuantity = inventoryItem.getBoxQuantity();
        this.itemQuantity = inventoryItem.getItemQuantity();
        this.numOfProductsInCategory = inventoryItem.getNumOfProductsInCategory();
        this.product = new Product(inventoryItem.getProduct());
        this.individualItemPrice = inventoryItem.getIndividualItemPrice();
    }

    public String getCategory() {
        return category;
    }


    public int getBoxQuantity() {
        return boxQuantity;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public int getNumOfProductsInCategory() {
        return numOfProductsInCategory;
    }

    public Product getProduct() {
        return product;
    }



    public BigDecimal getIndividualItemPrice() {
        return individualItemPrice;
    }

    public void setIndividualItemPrice(BigDecimal individualItemPrice) {
        this.individualItemPrice = individualItemPrice.setScale(2, RoundingMode.HALF_EVEN);;
    }

    public void updateItem(Product product, int numberOfProducts) {

        this.product = product;
        this.numOfProductsInCategory = numberOfProducts;
        this.category = product.getProductName();
        this.calculate();
    }

    protected void calculate() {

        this.boxQuantity = this.numOfProductsInCategory / product.getNumItemsInPackage();
        this.itemQuantity = this.numOfProductsInCategory % product.getNumItemsInPackage();
    }

}
