package driw.xtechnology.assignment.webshop.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> cartItems;
    private BigDecimal total = new BigDecimal(0);
    private BigDecimal totalDiscount = new BigDecimal(0);

    public Cart(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        this.reTotalCart();
    }

    public void reTotalCart() {
        this.total = BigDecimal.ZERO;
        this.totalDiscount = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;
        this.totalDiscount = BigDecimal.ZERO;
        for(CartItem cartItem: this.cartItems) {
            this.total = this.total.add(cartItem.totalPrice());
            if(cartItem.boxPriceChanges().compareTo(BigDecimal.ZERO) < 0)
                this.totalDiscount = this.totalDiscount.add(cartItem.boxPriceChanges()).setScale(2, RoundingMode.HALF_EVEN);
            if(cartItem.itemPriceChanges().compareTo(BigDecimal.ZERO) < 0)
                this.totalDiscount = this.totalDiscount.add(cartItem.itemPriceChanges()).setScale(2, RoundingMode.HALF_EVEN);
        }
    }

    public CartItem itemByCategory(String name) {
        return this.cartItems.stream()
                .filter(cartItem -> cartItem.category().equals(name))
                .findFirst()
                .get();
    }

    public void addOrUpdate(CartItem item) {
        this.cartItems.removeIf(listItem -> listItem.category() == item.category());
        this.cartItems.add(item);
        this.reTotalCart();
    }

    public List<CartItem> items() {
        return this.cartItems;
    }

    public BigDecimal total() {
        return this.total;
    }

    public BigDecimal totalDiscount() {
        return this.totalDiscount;
    }

    public void removeItem(CartItem cartItemToRemove) {
        this.cartItems.removeIf(listItem -> listItem.category() == cartItemToRemove.category());
        this.reTotalCart();
    }

    public void empty() {
        this.cartItems = new ArrayList<>();
        this.reTotalCart();
    }
}
