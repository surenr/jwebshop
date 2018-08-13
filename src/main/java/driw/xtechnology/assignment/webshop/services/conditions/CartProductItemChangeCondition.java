package driw.xtechnology.assignment.webshop.services.conditions;

import driw.xtechnology.assignment.webshop.domain.CartItem;
import driw.xtechnology.assignment.webshop.domain.InventoryItem;
import driw.xtechnology.assignment.webshop.domain.PriceChange;
import driw.xtechnology.assignment.webshop.services.IPriceCondition;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/*
    Apply the 30% charge (when buying individual items) price condition
 */
@Service
public class CartProductItemChangeCondition implements IPriceCondition {

    @Override
    public InventoryItem apply(InventoryItem item) {

        final double CHARGE_RATE = 0.30;

        if(item instanceof CartItem) {
            CartItem cartItem = (CartItem)item;
            BigDecimal increment = cartItem.totalItemPrice().multiply(new BigDecimal(CHARGE_RATE));
            cartItem.setItemPriceChange(increment, PriceChange.INCREMENT);
        }

        return item;
    }
}
