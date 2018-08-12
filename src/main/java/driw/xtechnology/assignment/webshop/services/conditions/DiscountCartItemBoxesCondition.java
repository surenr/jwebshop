package driw.xtechnology.assignment.webshop.services.conditions;

import driw.xtechnology.assignment.webshop.domain.CartItem;
import driw.xtechnology.assignment.webshop.domain.InventoryItem;
import driw.xtechnology.assignment.webshop.domain.PriceChange;
import driw.xtechnology.assignment.webshop.services.IPriceCondition;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DiscountCartItemBoxesCondition implements IPriceCondition {
    @Override
    public InventoryItem apply(InventoryItem item) {
        final double DISCOUNT_RATE = 0.1;
        final int MIN_BOX_NUMBER = 3;
        if(item instanceof CartItem) {
            CartItem cartItem = (CartItem)item;
            BigDecimal discount = cartItem.getBoxQuantity() >= MIN_BOX_NUMBER ? cartItem.totalBoxPrice()
                    .multiply(new BigDecimal(DISCOUNT_RATE)) : BigDecimal.ZERO;
            cartItem.setBoxPriceChange(discount, PriceChange.DECREMENT);
        }
        return item;
    }
}
