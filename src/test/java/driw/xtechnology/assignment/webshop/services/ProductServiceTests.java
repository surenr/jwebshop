package driw.xtechnology.assignment.webshop.services;

import driw.xtechnology.assignment.webshop.domain.Product;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTests {

    @Autowired
    private ProductService productService;
    @Test
    public void canInstantiateProductService() {
        Assert.assertNotNull(productService);
    }

    @Test
    public void canAddProductsToInventory() {
        Product penguinEars = new Product("Penguin", new BigDecimal(175), 20);
        productService.addToInventory(penguinEars,1);
    }
}
