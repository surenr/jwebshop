package driw.xtechnology.assignment.webshop.services;

import driw.xtechnology.assignment.webshop.domain.Cart;
import driw.xtechnology.assignment.webshop.domain.Product;
import driw.xtechnology.assignment.webshop.exceptions.CartEmptyException;
import driw.xtechnology.assignment.webshop.exceptions.InvalidProductException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartServiceTests {
    @Autowired
    private CartService cartService;

    @Test
    public void canInstantiateCartService() {
       Assert.assertNotNull(cartService);
    }

    @Before
    public void beforeTest() {
        cartService.empty();
    }

    @Test
    public void canAddProductToCart() throws InvalidProductException {
        Product product = new Product("Penguinears", new BigDecimal(175), 20);
        cartService.add(product);
        Assert.assertEquals(1, cartService.cartItems().size());
        Assert.assertEquals("Penguinears", cartService.cartItems().get(0).getProductName());
    }

    @Test
    public void canAddMultipleOfSameProductToCart() throws InvalidProductException {
        Product product = new Product("Penguinears", new BigDecimal(175), 20);
        cartService.add(product, 2);
        Assert.assertEquals(2, cartService.cartItems().size());
        Assert.assertEquals("Penguinears", cartService.cartItems().get(0).getProductName());
    }

    @Test(expected = InvalidProductException.class)
    public void addingInvalidProductThrowError() throws InvalidProductException {
        Product product = new Product("", new BigDecimal(0), 0);
        cartService.add(product);
        Assert.fail("InvalidProductException not thrown");
    }

    @Test
    public void canRemoveProductFromCartByIndex() throws InvalidProductException, CartEmptyException {
        Assert.assertEquals(0, cartService.cartItems().size());
        Product product = new Product("Penguinears", new BigDecimal(175), 20);
        cartService.add(product);
        cartService.remove(0);
        Assert.assertEquals(0, cartService.cartItems().size());
    }

    @Test(expected = CartEmptyException.class)
    public void removingFromEmptyCartThrowError() throws CartEmptyException {
        Assert.assertEquals(0, cartService.cartItems().size());
        cartService.remove(0);
    }

    @Test
    public void canCategorizeBySimilarProducts() throws InvalidProductException {
        Product penguinEars = new Product("Penguinears", new BigDecimal(175), 20);
        Product horseShoes = new Product("Horseshoe", new BigDecimal(825), 5);
        cartService.add(penguinEars, 25);
        cartService.add(horseShoes, 15);
        Assert.assertEquals(40, cartService.cartItems().size());
        Map<String, List<Product>> categorizedProducts = cartService.categorize();
        Assert.assertEquals(25, categorizedProducts.get("Penguinears").size());
        Assert.assertEquals(15, categorizedProducts.get("Horseshoe").size());
    }

    @Test
    public void canGetBoxAndItemAmounts() throws InvalidProductException {
        Product penguinEars = new Product("Penguinears", new BigDecimal(175), 20);
        Product horseShoes = new Product("Horseshoe", new BigDecimal(825), 5);
        cartService.add(penguinEars, 25);
        cartService.add(horseShoes, 15);

        BigDecimal expectedPenguinTotalBoxPrice = penguinEars.getPackagePrice().multiply(new BigDecimal(1)).setScale(2, RoundingMode.HALF_EVEN);;
        BigDecimal expectedPenguinTotalItemPrice = penguinEars.getItemPrice().multiply(new BigDecimal(5)).setScale(2, RoundingMode.HALF_EVEN);;
        BigDecimal expectedHorseShoeTotalBoxPrice = horseShoes.getPackagePrice().multiply(new BigDecimal(3)).setScale(2, RoundingMode.HALF_EVEN);;
        BigDecimal expectedHorseShoeTotalItemPrice = horseShoes.getItemPrice().multiply(new BigDecimal(0)).setScale(2, RoundingMode.HALF_EVEN);;
        BigDecimal expectedCartTotal = expectedPenguinTotalBoxPrice.add(expectedPenguinTotalItemPrice
                .add(expectedHorseShoeTotalBoxPrice.add(expectedHorseShoeTotalItemPrice))).setScale(2, RoundingMode.HALF_EVEN);;

        Cart cart = cartService.cart();

        Assert.assertEquals(1, cart.itemByCategory("Penguinears").boxQty());
        Assert.assertEquals(5, cart.itemByCategory("Penguinears").itemQty());

        Assert.assertEquals(expectedPenguinTotalBoxPrice, cart.itemByCategory("Penguinears").totalBoxPrice());
        Assert.assertEquals(expectedPenguinTotalItemPrice, cart.itemByCategory("Penguinears").totalItemPrice());
        Assert.assertEquals(expectedPenguinTotalBoxPrice.add(expectedPenguinTotalItemPrice),cart.itemByCategory("Penguinears").totalPrice());

        Assert.assertEquals(3, cart.itemByCategory("Horseshoe").boxQty());
        Assert.assertEquals(0, cart.itemByCategory("Horseshoe").itemQty());

        Assert.assertEquals(expectedHorseShoeTotalBoxPrice, cart.itemByCategory("Horseshoe").totalBoxPrice());
        Assert.assertEquals(expectedHorseShoeTotalItemPrice, cart.itemByCategory("Horseshoe").totalItemPrice());
        Assert.assertEquals(expectedHorseShoeTotalBoxPrice.add(expectedHorseShoeTotalItemPrice),cart.itemByCategory("Horseshoe").totalPrice());
        Assert.assertEquals(expectedCartTotal, cart.total());

        ;
    }

    @Test
    public void canApplyPriceCondtions() throws InvalidProductException {
        Product penguinEars = new Product("Penguinears", new BigDecimal(175), 20);
        Product horseShoes = new Product("Horseshoe", new BigDecimal(825), 5);
        cartService.add(penguinEars, 25);
        cartService.add(horseShoes, 15);

        BigDecimal expectedPenguinEarsTotalBoxPrice = penguinEars.getPackagePrice().multiply(BigDecimal.ONE).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal penguinEarsTotalItemPrice = penguinEars.getItemPrice().multiply(new BigDecimal(5)).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal penguinEarsItemPriceIncrease = penguinEarsTotalItemPrice.multiply(new BigDecimal(0.30));
        BigDecimal expectedPenguinEarsTotalItemPrice = penguinEarsTotalItemPrice.add(penguinEarsItemPriceIncrease).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal expectedPenguinEarsTotalPrice = expectedPenguinEarsTotalBoxPrice.add(expectedPenguinEarsTotalItemPrice).setScale(2, RoundingMode.HALF_EVEN);

        BigDecimal horseShoeTotalBoxPrice = horseShoes.getPackagePrice().multiply(new BigDecimal(3)).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal horseShoeBoxDiscount = horseShoeTotalBoxPrice.multiply(new BigDecimal(0.1)).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal expectedHorseShoeTotalBoxPrice = horseShoeTotalBoxPrice.subtract(horseShoeBoxDiscount).setScale(2, RoundingMode.HALF_EVEN);

        BigDecimal cartTotal = expectedPenguinEarsTotalPrice.add(expectedHorseShoeTotalBoxPrice).setScale(2, RoundingMode.HALF_EVEN);
        Cart cartWithPriceCondition = cartService.applyPriceConditions(cartService.cart());

        Assert.assertEquals(expectedPenguinEarsTotalBoxPrice, cartWithPriceCondition.itemByCategory("Penguinears").totalBoxPrice());
        Assert.assertEquals(expectedPenguinEarsTotalItemPrice, cartWithPriceCondition.itemByCategory("Penguinears").totalItemPrice());
        Assert.assertEquals(expectedPenguinEarsTotalPrice, cartWithPriceCondition.itemByCategory("Penguinears").totalPrice());

        Assert.assertEquals(expectedHorseShoeTotalBoxPrice, cartWithPriceCondition.itemByCategory("Horseshoe").totalBoxPrice());
        Assert.assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN), cartWithPriceCondition.itemByCategory("Horseshoe").totalItemPrice());
        Assert.assertEquals(expectedHorseShoeTotalBoxPrice, cartWithPriceCondition.itemByCategory("Horseshoe").totalPrice());

        Assert.assertEquals(cartTotal, cartWithPriceCondition.total());
        Assert.assertEquals(horseShoeBoxDiscount.multiply(new BigDecimal(-1)), cartWithPriceCondition.totalDiscount());

    }
}
