package driw.xtechnology.assignment.webshop.controllers;

import driw.xtechnology.assignment.webshop.domain.APIResponse;
import driw.xtechnology.assignment.webshop.domain.InventoryItem;
import driw.xtechnology.assignment.webshop.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @RequestMapping("/")
    public APIResponse home() {
        return new APIResponse(HttpStatus.OK, "Welcome to the world of JAVA");
    }

    @RequestMapping("/greeting")
    public APIResponse greeting() {
        return new APIResponse(HttpStatus.OK, "Hey, Hey Hey, Looks like proxy is working fine");
    }

    @RequestMapping("/products")
    public List<InventoryItem> getProducts() {
       return productService.applyPriceConditions(productService.getInventory());
    }
}
