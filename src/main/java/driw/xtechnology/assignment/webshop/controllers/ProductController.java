package driw.xtechnology.assignment.webshop.controllers;

import driw.xtechnology.assignment.webshop.domain.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @RequestMapping("/")
    public APIResponse home() {
        return new APIResponse(HttpStatus.OK, "Welcome to the world of JAVA");
    }

    @RequestMapping("/greeting")
    public APIResponse greeting() {
        return new APIResponse(HttpStatus.OK, "Hey, Hey Hey, Looks like proxy is working fine");
    }
}
