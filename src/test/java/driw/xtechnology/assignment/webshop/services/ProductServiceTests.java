package driw.xtechnology.assignment.webshop.services;

import driw.xtechnology.assignment.webshop.domain.InventoryItem;
import driw.xtechnology.assignment.webshop.domain.Product;
import driw.xtechnology.assignment.webshop.exceptions.NoProductsAvailableInInventoryException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTests {

    @Autowired
    private ProductService productService;

    @Before
    public void beforeTest() {
        productService.emptyInventory();
        productService.populateInventory();
    }

    @Test
    public void canRemoveProductFromInventory() throws NoProductsAvailableInInventoryException {
        Product productToRemove = new Product(
                "PenguinEars",
                new BigDecimal(175),
                20,
                "https://ae01.alicdn.com/kf/HTB1G4DbKFXXXXaKXXXXq6xXFXXX6/Cute-Cartoon-Plush-Animal-Penguin-Fuzzy-Warm-Beanie-Hat-Winter-Adult-Women-Men-s-Children-Kids.jpg",
                "Penguin Ears",
                "They are great to keep you warm");

        Assert.assertEquals(50, getInventoryItem(productToRemove).getNumOfProductsInCategory());
        Assert.assertEquals(2, getInventoryItem(productToRemove).getBoxQuantity());
        productService.remove(productToRemove, 20);
        Assert.assertEquals(30, getInventoryItem(productToRemove).getNumOfProductsInCategory());
        Assert.assertEquals(1, getInventoryItem(productToRemove).getBoxQuantity());
        productService.remove(productToRemove, 30);
        Assert.assertEquals(0, getInventoryItem(productToRemove).getNumOfProductsInCategory());
        Assert.assertEquals(0, getInventoryItem(productToRemove).getBoxQuantity());
    }

    @Test(expected = NoProductsAvailableInInventoryException.class)
    public void tryingToRemoveFromEmptyInveontoryThrowError() throws NoProductsAvailableInInventoryException {
        Product productToRemove = new Product(
                "PenguinEars",
                new BigDecimal(175),
                20,
                "https://ae01.alicdn.com/kf/HTB1G4DbKFXXXXaKXXXXq6xXFXXX6/Cute-Cartoon-Plush-Animal-Penguin-Fuzzy-Warm-Beanie-Hat-Winter-Adult-Women-Men-s-Children-Kids.jpg",
                "Penguin Ears",
                "They are great to keep you warm");
        productService.remove(productToRemove, 50);
        Assert.assertEquals(0, getInventoryItem(productToRemove).getNumOfProductsInCategory());
        Assert.assertEquals(0, getInventoryItem(productToRemove).getBoxQuantity());
        productService.remove(productToRemove, 1);
    }

    @Test
    public void canAddToExistingInventoryItems() {
        Product productToAdd = new Product(
                "PenguinEars",
                new BigDecimal(175),
                20,
                "https://ae01.alicdn.com/kf/HTB1G4DbKFXXXXaKXXXXq6xXFXXX6/Cute-Cartoon-Plush-Animal-Penguin-Fuzzy-Warm-Beanie-Hat-Winter-Adult-Women-Men-s-Children-Kids.jpg",
                "Penguin Ears",
                "They are great to keep you warm");
        Assert.assertEquals(50, getInventoryItem(productToAdd).getNumOfProductsInCategory());
        Assert.assertEquals(2, getInventoryItem(productToAdd).getBoxQuantity());
        productService.add(productToAdd, 5);
        Assert.assertEquals(55, getInventoryItem(productToAdd).getNumOfProductsInCategory());
        Assert.assertEquals(2, getInventoryItem(productToAdd).getBoxQuantity());
        productService.add(productToAdd, 10);
        Assert.assertEquals(65, getInventoryItem(productToAdd).getNumOfProductsInCategory());
        Assert.assertEquals(3, getInventoryItem(productToAdd).getBoxQuantity());
    }

    @Test
    public void canApplyPriceConditionsToInventory() {
        List<InventoryItem> inventory = productService.getInventory();
        Product product = new Product(
                "PenguinEars",
                new BigDecimal(175),
                20,
                "https://ae01.alicdn.com/kf/HTB1G4DbKFXXXXaKXXXXq6xXFXXX6/Cute-Cartoon-Plush-Animal-Penguin-Fuzzy-Warm-Beanie-Hat-Winter-Adult-Women-Men-s-Children-Kids.jpg",
                "Penguin Ears",
                "They are great to keep you warm");
        InventoryItem beforePriceUpdate = getInventoryItem(product);
        Assert.assertEquals(null, beforePriceUpdate.getIndividualItemPrice());
        List<InventoryItem> priceUpdatedInventory = productService.applyPriceConditions(inventory);
        InventoryItem afterPriceUpdated = priceUpdatedInventory.stream().filter(t->t.getCategory().equals(product.getProductName())).findFirst().orElse(null);
        Assert.assertEquals(new BigDecimal(11.70).setScale(2, RoundingMode.HALF_EVEN),
                afterPriceUpdated.getIndividualItemPrice());
    }

    private InventoryItem getInventoryItem(Product productToRemove) {
        return productService.getInventory().stream().filter(t->t.getCategory().equals(productToRemove.getProductName())).findFirst().get();
    }
}
