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

    public void add(Product product) throws InvalidProductException {
        if(product.getProductName().isEmpty() || product.getPackagePrice().equals(0) || product.getNumItemsInPackage() == 0)
            throw new InvalidProductException();
        this.cartItems.add(product);
    }

    public void add(Product product, int count) throws InvalidProductException {
        for(int i=0; i<count; i++)
            this.add(product);
    }


    public void empty() {
        this.cartItems = new ArrayList<>();
    }

    public void remove(int  index) throws CartEmptyException {
        if(this.cartItems.size() == 0) throw new CartEmptyException();
        this.cartItems.remove(index);
    }

    public Cart cart() {
        return new Cart(this.calculatePriceByProductCategory());
    }


    public List<Product> cartItems() {
        return this.cartItems;
    }

    public Map<String, List<Product>> categorize() {
        Map<String, List<Product>> tempMap = new HashMap<>();
        for(Product item : this.cartItems) {
            if(tempMap.get(item.getProductName()) == null) {
                List<Product> itemList= new ArrayList<>();
                itemList.add(item);
                tempMap.put(item.getProductName(), itemList);
                continue;
            }
            tempMap.get(item.getProductName()).add(item);
        }
       return tempMap;
    }

    public List<CartItem> calculatePriceByProductCategory() {
        Map<String, List<Product>> categoryMap = this.categorize();
        List<CartItem> cartItemList = new ArrayList<>();
        Iterator<Map.Entry<String, List<Product>>> iterator = categoryMap.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<String, List<Product>> productCategory = iterator.next();
            Product productDetail = productCategory.getValue().get(0);
            int boxCount = productCategory.getValue().size() / productDetail.getNumItemsInPackage();
            int itemCount =productCategory.getValue().size() % productDetail.getNumItemsInPackage();
            CartItem tempCartItem = new CartItem(productDetail, boxCount, itemCount);
            cartItemList.add(tempCartItem);
        }
        return cartItemList;
    }

    public Cart applyPriceConditions(Cart cart) {
        for(CartItem item: cart.items()) {
            for(IPriceCondition service: this.priceConditionServiceList)
                item = service.apply(item);
        }
        cart.reTotalCart();
        return cart;
    }
}
