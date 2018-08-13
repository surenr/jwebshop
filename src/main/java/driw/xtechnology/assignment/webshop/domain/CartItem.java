package driw.xtechnology.assignment.webshop.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CartItem extends InventoryItem{

    private BigDecimal totalBoxPrice;
    private BigDecimal totalItemPrice;
    private BigDecimal totalPrice;
    private BigDecimal boxPriceChange;
    private BigDecimal itemPriceChange;

    public CartItem(CartItem cartItem) {

        this.category = new String(cartItem.getCategory());
        this.boxQuantity = cartItem.getBoxQuantity();
        this.itemQuantity = cartItem.getItemQuantity();
        this.numOfProductsInCategory = cartItem.getNumOfProductsInCategory();
        this.totalBoxPrice = cartItem.getTotalBoxPrice().add(BigDecimal.ZERO);
        this.totalItemPrice = cartItem.getTotalItemPrice().add(BigDecimal.ZERO);
        this.totalPrice = cartItem.getTotalPrice().add(BigDecimal.ZERO);
        this.boxPriceChange = cartItem.getBoxPriceChange().add(BigDecimal.ZERO);
        this.itemPriceChange = cartItem.getItemPriceChange().add(BigDecimal.ZERO);
        this.product = new Product(cartItem.getProduct());
    }

    public CartItem(Product product, int numberOfProducts) {
        this.updateItem(product, numberOfProducts);
    }

    @Override
    protected void calculate() {

        super.calculate();
        this.totalBoxPrice = product.getPackagePrice().multiply(new BigDecimal(this.boxQuantity)).setScale(2, RoundingMode.HALF_EVEN);
        this.totalItemPrice = product.getItemPrice().multiply(new BigDecimal(this.itemQuantity)).setScale(2, RoundingMode.HALF_EVEN);
        this.totalPrice = this.totalBoxPrice.add(this.totalItemPrice).setScale(2, RoundingMode.HALF_EVEN);
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

    @Override
    public void updateItem(Product product, int numberOfProducts) {

        super.updateItem(product, numberOfProducts);
        this.boxPriceChange = BigDecimal.ZERO;
        this.itemPriceChange = BigDecimal.ZERO;
        this.calculate();
    }}
