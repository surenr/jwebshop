package driw.xtechnology.assignment.webshop.services.conditions;

import driw.xtechnology.assignment.webshop.domain.CartItem;
import driw.xtechnology.assignment.webshop.domain.PriceChange;
import driw.xtechnology.assignment.webshop.services.IPriceCondition;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BoxDiscountCondition implements IPriceCondition {
    @Override
    public CartItem apply(CartItem item) {
        final double DISCOUNT_RATE = 0.1;
        final int MIN_BOX_NUMBER = 3;
        BigDecimal discount = item.boxQty() >= MIN_BOX_NUMBER ? item.totalBoxPrice()
                .multiply(new BigDecimal(DISCOUNT_RATE)) : BigDecimal.ZERO;
        item.setBoxPriceChange(discount, PriceChange.DECREMENT);
        return item;
    }
}
