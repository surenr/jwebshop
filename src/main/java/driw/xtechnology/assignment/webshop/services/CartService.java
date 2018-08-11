package driw.xtechnology.assignment.webshop.services;

import driw.xtechnology.assignment.webshop.domain.Cart;
import driw.xtechnology.assignment.webshop.domain.CartItem;
import driw.xtechnology.assignment.webshop.domain.Product;
import driw.xtechnology.assignment.webshop.exceptions.CartEmptyException;
import driw.xtechnology.assignment.webshop.exceptions.InvalidProductCountException;
import driw.xtechnology.assignment.webshop.exceptions.InvalidProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {

    @Autowired
    private List<IPriceCondition> priceConditionServiceList;

    private Cart cart;

    public CartService() {
        this.cart = new Cart(new ArrayList<>());
    }

    public void add(Product product, int count) throws InvalidProductException, InvalidProductCountException {
        validateAddProduct(product, count);
        CartItem cartItemToAdd = findCartItemByProductName(product);
        int numExitingProducts = cartItemToAdd != null ? cartItemToAdd.getNumberOfItemsInCategory() : 0;
        int newNumExistingProducts = numExitingProducts + count;
        if(cartItemToAdd != null) {
            cartItemToAdd.updateCartItem(product, newNumExistingProducts);
        } else {
            cartItemToAdd = new CartItem(product, newNumExistingProducts);
        }
        this.cart.addOrUpdate(cartItemToAdd);
    }

    public void empty() {
        this.cart.empty();
    }

    public void remove(Product product, int count) throws CartEmptyException {
        if(this.cart.items().size() == 0) throw new CartEmptyException();
        CartItem cartItemToRemove = findCartItemByProductName(product);
        if(cartItemToRemove != null) {
            int numExistingProducts =  cartItemToRemove.getNumberOfItemsInCategory();
            int newItemNumberInCategory = numExistingProducts - count;
            if (newItemNumberInCategory < 0) newItemNumberInCategory = 0;
            if (newItemNumberInCategory == 0) {
                this.cart.removeItem(cartItemToRemove);
            } else {
                cartItemToRemove.updateCartItem(product,newItemNumberInCategory);
                this.cart.addOrUpdate(cartItemToRemove);
            }
        }
    }

    public Cart cart() {
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
        CartItem cartItem = findCartItemByProductName(product);
        return cartItem == null ? 0 : cartItem.getNumberOfItemsInCategory();
    }


    private CartItem findCartItemByProductName(Product product) {
        return this.cart.items().stream()
                .filter(item -> item.category().equals(product.getProductName())).findFirst().orElse(null);
    }

    private void validateAddProduct(Product product, int count) throws InvalidProductCountException,
            InvalidProductException {
        if(count == 0) throw new InvalidProductCountException();
        if(product.getProductName().isEmpty() || product.getPackagePrice().equals(0) ||
                product.getNumItemsInPackage() == 0)
            throw new InvalidProductException();
    }
}
