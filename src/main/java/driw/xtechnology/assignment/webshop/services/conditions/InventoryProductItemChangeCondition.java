package driw.xtechnology.assignment.webshop.services.conditions;

import driw.xtechnology.assignment.webshop.domain.InventoryItem;
import driw.xtechnology.assignment.webshop.services.IPriceCondition;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class InventoryProductItemChangeCondition implements IPriceCondition {

    @Override
    public InventoryItem apply(InventoryItem item) {

        final double CHARGE_RATE = 0.30;
        item.setIndividualItemPrice(item.getProduct().getItemPrice()
                    .add(item.getProduct().getItemPrice().multiply(new BigDecimal(CHARGE_RATE))));

        return item;
    }
}


