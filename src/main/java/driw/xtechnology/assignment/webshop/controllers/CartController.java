package driw.xtechnology.assignment.webshop.controllers;
import driw.xtechnology.assignment.webshop.domain.APIResponse;
import driw.xtechnology.assignment.webshop.domain.Cart;
import driw.xtechnology.assignment.webshop.domain.CartItem;
import driw.xtechnology.assignment.webshop.domain.ProductRequest;
import driw.xtechnology.assignment.webshop.exceptions.CartEmptyException;
import driw.xtechnology.assignment.webshop.exceptions.InvalidProductCountException;
import driw.xtechnology.assignment.webshop.exceptions.InvalidProductException;
import driw.xtechnology.assignment.webshop.exceptions.NoProductsAvailableInInventoryException;
import driw.xtechnology.assignment.webshop.services.CartService;
import driw.xtechnology.assignment.webshop.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @RequestMapping("/cart")
    public Cart getCart() {

        return this.cartService.applyPriceConditions(this.cartService.cart());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/cart/product")
    public APIResponse addProductToCart(@RequestBody ProductRequest productRequest) throws InvalidProductException, InvalidProductCountException, NoProductsAvailableInInventoryException {

        this.productService.remove(productRequest.getProduct(), productRequest.getNumItems());
        this.cartService.add(productRequest.getProduct(), productRequest.getNumItems());
        return new APIResponse(HttpStatus.OK, "done");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/cart/product")
    public APIResponse removeProductFromCart(@RequestBody ProductRequest productRequest) throws CartEmptyException {

        this.cartService.remove(productRequest.getProduct(), productRequest.getNumItems());
        this.productService.add(productRequest.getProduct(), productRequest.getNumItems());
        return new APIResponse(HttpStatus.OK, "done");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/cart/category/{name}")
    public APIResponse removeProductFromCart(@PathVariable String name) {
        CartItem itemToAd = this.cartService.findCartItemByCategory(name);
        this.productService.add(itemToAd.getProduct(), itemToAd.getNumOfProductsInCategory());
        this.cartService.removeCategory(name);
        return new APIResponse(HttpStatus.OK, "done");
    }
}
