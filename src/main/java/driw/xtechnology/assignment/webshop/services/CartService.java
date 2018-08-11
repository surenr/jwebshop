package driw.xtechnology.assignment.webshop.services;

import driw.xtechnology.assignment.webshop.domain.Cart;
import driw.xtechnology.assignment.webshop.domain.CartItem;
import driw.xtechnology.assignment.webshop.domain.Product;
import driw.xtechnology.assignment.webshop.exceptions.CartEmptyException;
import driw.xtechnology.assignment.webshop.exceptions.InvalidProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {
    private List<Product> cartItems = new ArrayList<>();

    @Autowired
    private List<IPriceCondition> priceConditionServiceList;

    private Cart cart;

    public CartService() {
        this.cart = new Cart(new ArrayList<>());
    }

    public void add(Product product, int count) throws InvalidProductException {
        if(product.getProductName().isEmpty() || product.getPackagePrice().equals(0) || product.getNumItemsInPackage() == 0)
            throw new InvalidProductException();

        CartItem cartItemToAdd = this.cart.items().stream()
                .filter(item -> item.category().equals(product.getProductName())).findFirst().orElse(null);

        int numExitingProducts = cartItemToAdd != null ? cartItemToAdd.getNumberOfItemsInCategory() : 0;
        int newNumExistingProducts = numExitingProducts + count;
        int boxCount = newNumExistingProducts / product.getNumItemsInPackage();
        int itemCount = newNumExistingProducts % product.getNumItemsInPackage();

        if(cartItemToAdd != null) {
            cartItemToAdd.updateCartItem(product, boxCount, itemCount);
        } else {
            cartItemToAdd = new CartItem(product, boxCount, itemCount);
        }
        cartItemToAdd.increase(count);
        this.cart.addOrUpdate(cartItemToAdd);


    }


    public void empty() {
        this.cart.empty();
    }

    public void remove(Product product, int count) throws CartEmptyException {
        if(this.cart.items().size() == 0) throw new CartEmptyException();
        CartItem cartItemToRemove = this.cart.items().stream()
                .filter(item -> item.category().equals(product.getProductName())).findFirst().orElse(null);
        if(cartItemToRemove != null) {
            int numExistingProducts =  cartItemToRemove.getNumberOfItemsInCategory();
            int newItemNumberInCategory = numExistingProducts - count;
            if(newItemNumberInCategory < 0) newItemNumberInCategory = 0;
            if (newItemNumberInCategory == 0) {
                this.cart.removeItem(cartItemToRemove);
            } else {
                int boxCount = newItemNumberInCategory / product.getNumItemsInPackage();
                int itemCount = newItemNumberInCategory % product.getNumItemsInPackage();
                cartItemToRemove.updateCartItem(product, boxCount, itemCount);
                cartItemToRemove.decrease(count);
                this.cart.addOrUpdate(cartItemToRemove);
            }
        }
    }

    public Cart cart() {
//        return new Cart(this.calculatePriceByProductCategory());
        return this.cart;
    }


    public List<CartItem> cartItems() {
        return this.cart.items();
    }

    public Cart applyPriceConditions(Cart cart) {
        for(CartItem item: cart.items()) {
            for(IPriceCondition service: this.priceConditionServiceList)
                item = service.apply(item);
        }
        cart.reTotalCart();
        return cart;
    }

    public int productCount(Product product) {
        CartItem cartItem =  this.cart.items().stream()
                .filter(item -> item.category().equals(product.getProductName())).findFirst().orElse(null);
        return cartItem == null ? 0 : cartItem.getNumberOfItemsInCategory();
    }
}
