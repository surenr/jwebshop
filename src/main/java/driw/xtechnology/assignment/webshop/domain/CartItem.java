package driw.xtechnology.assignment.webshop.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CartItem {
    private String category;
    private int boxQuantity;
    private int itemQuantity;
    private BigDecimal totalBoxPrice;
    private BigDecimal totalItemPrice;
    private BigDecimal totalPrice;
    private BigDecimal boxPriceChange;
    private BigDecimal itemPriceChange;

    public CartItem(Product product, int boxQuantity, int itemQuantity) {
        this.category = product.getProductName();
        this.boxQuantity = boxQuantity;
        this.itemQuantity = itemQuantity;
        this.totalBoxPrice = product.getPackagePrice().multiply(new BigDecimal(this.boxQuantity)).setScale(2, RoundingMode.HALF_EVEN);
        this.totalItemPrice = product.getItemPrice().multiply(new BigDecimal(this.itemQuantity)).setScale(2, RoundingMode.HALF_EVEN);
        this.totalPrice = this.totalBoxPrice.add(this.totalItemPrice).setScale(2, RoundingMode.HALF_EVEN);
        this.boxPriceChange = BigDecimal.ZERO;
        this.itemPriceChange = BigDecimal.ZERO;
    }

    public String category() {
        return this.category;
    }

    public int boxQty() {
        return this.boxQuantity;
    }

    public int itemQty() {
        return this.itemQuantity;
    }

    public BigDecimal totalBoxPrice() {
        return this.totalBoxPrice;
    }

    public BigDecimal totalItemPrice() {
        return this.totalItemPrice;
    }

    public BigDecimal boxPriceChanges() { return this.boxPriceChange; }

    public BigDecimal itemPriceChanges() { return this.itemPriceChange; }

    public BigDecimal totalPrice() {
        return this.totalPrice;
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
}
