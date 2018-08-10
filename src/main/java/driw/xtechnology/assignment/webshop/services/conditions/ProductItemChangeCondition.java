package driw.xtechnology.assignment.webshop.services.conditions;

import driw.xtechnology.assignment.webshop.domain.CartItem;
import driw.xtechnology.assignment.webshop.domain.PriceChange;
import driw.xtechnology.assignment.webshop.services.IPriceCondition;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductItemChangeCondition implements IPriceCondition {

    @Override
    public CartItem apply(CartItem item) {
        final double CHARGE_RATE = 0.30;
        BigDecimal increment = item.totalItemPrice().multiply(new BigDecimal(CHARGE_RATE));
        item.setItemPriceChange(increment, PriceChange.INCREMENT);
        return item;
    }
}
