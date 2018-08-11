package driw.xtechnology.assignment.webshop.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CartItem {
    private String category;
    private int boxQuantity;
    private int itemQuantity;
    private int numOfItemsInCategory;
    private BigDecimal totalBoxPrice;
    private BigDecimal totalItemPrice;
    private BigDecimal totalPrice;
    private BigDecimal boxPriceChange;
    private BigDecimal itemPriceChange;
    private Product product;

    private void calculate() {
        this.boxQuantity = this.numOfItemsInCategory / product.getNumItemsInPackage();
        this.itemQuantity = this.numOfItemsInCategory % product.getNumItemsInPackage();
        this.totalBoxPrice = product.getPackagePrice().multiply(new BigDecimal(this.boxQuantity)).setScale(2, RoundingMode.HALF_EVEN);
        this.totalItemPrice = product.getItemPrice().multiply(new BigDecimal(this.itemQuantity)).setScale(2, RoundingMode.HALF_EVEN);
        this.totalPrice = this.totalBoxPrice.add(this.totalItemPrice).setScale(2, RoundingMode.HALF_EVEN);
    }

    public CartItem(CartItem cartItem) {
        this.category = new String(cartItem.getCategory());
        this.boxQuantity = cartItem.getBoxQuantity();
        this.itemQuantity = cartItem.getItemQuantity();
        this.numOfItemsInCategory = cartItem.getNumOfItemsInCategory();
        this.totalBoxPrice = cartItem.getTotalBoxPrice().add(BigDecimal.ZERO);
        this.totalItemPrice = cartItem.getTotalItemPrice().add(BigDecimal.ZERO);
        this.totalPrice = cartItem.getTotalPrice().add(BigDecimal.ZERO);
        this.boxPriceChange = cartItem.getBoxPriceChange().add(BigDecimal.ZERO);
        this.itemPriceChange = cartItem.getItemPriceChange().add(BigDecimal.ZERO);
        this.product = new Product(cartItem.getProduct());
    }

    public CartItem(Product product, int numberOfProducts) {
        this.updateCartItem(product, numberOfProducts);
    }

    public void updateCartItem(Product product, int numberOfProducts) {
        this.product = product;
        this.numOfItemsInCategory = numberOfProducts;
        this.category = product.getProductName();
        this.boxPriceChange = BigDecimal.ZERO;
        this.itemPriceChange = BigDecimal.ZERO;
        this.calculate();
    }

    public BigDecimal totalBoxPrice() {
        return this.totalBoxPrice;
    }

    public BigDecimal totalItemPrice() {
        return this.totalItemPrice;
    }

    public void setItemPriceChange(BigDecimal itemPriceChange, PriceChange change) {
        this.itemPriceChange = ((change == PriceChange.INCREMENT) ? itemPriceChange : itemPriceChange.multiply(new BigDecimal(-1))).setScale(2, RoundingMode.HALF_EVEN);
        this.totalItemPrice = this.totalItemPrice.add(this.itemPriceChange).setScale(2, RoundingMode.HALF_EVEN);
        this.totalPrice = this.totalItemPrice.add(this.totalBoxPrice).setScale(2, RoundingMode.HALF_EVEN);
    }

    public void setBoxPriceChange(BigDecimal boxPriceChange, PriceChange change) {
        this.boxPriceChange = ((change == PriceChange.INCREMENT) ? boxPriceChange : boxPriceChange.multiply(new BigDecimal(-1))).setScale(2, RoundingMode.HALF_EVEN);
        this.totalBoxPrice = this.totalBoxPrice.add(this.boxPriceChange).setScale(2, RoundingMode.HALF_EVEN);
        this.totalPrice = this.totalBoxPrice.add(this.totalItemPrice).setScale(2, RoundingMode.HALF_EVEN);
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

    public int getNumOfItemsInCategory() {
        return numOfItemsInCategory;
    }

    public BigDecimal getTotalBoxPrice() {
        return totalBoxPrice;
    }

    public BigDecimal getTotalItemPrice() {
        return totalItemPrice;
    }


    public BigDecimal getTotalPrice() {
        return totalPrice;
    }


    public BigDecimal getBoxPriceChange() {
        return boxPriceChange;
    }

    public BigDecimal getItemPriceChange() {
        return itemPriceChange;
    }

    public Product getProduct() {
        return product;
    }
}
