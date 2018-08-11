package driw.xtechnology.assignment.webshop.controllers;
import driw.xtechnology.assignment.webshop.domain.APIResponse;
import driw.xtechnology.assignment.webshop.domain.Cart;
import driw.xtechnology.assignment.webshop.domain.Product;
import driw.xtechnology.assignment.webshop.exceptions.CartEmptyException;
import driw.xtechnology.assignment.webshop.exceptions.InvalidProductCountException;
import driw.xtechnology.assignment.webshop.exceptions.InvalidProductException;
import driw.xtechnology.assignment.webshop.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @RequestMapping("/cart")
    public Cart getCart() {
        return this.cartService.applyPriceConditions(this.cartService.cart());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/cart/product")
    public APIResponse addProductToCart(@RequestBody Product product) throws InvalidProductException, InvalidProductCountException {
        this.cartService.add(product, 1);
        return new APIResponse(HttpStatus.OK, "done");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/cart/product")
    public APIResponse removeProductFromCart(@RequestBody Product product) throws CartEmptyException {
        this.cartService.remove(product, 1);
        return new APIResponse(HttpStatus.OK, "done");
    }
}
