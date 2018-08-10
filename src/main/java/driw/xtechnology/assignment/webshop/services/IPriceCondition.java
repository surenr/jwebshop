package driw.xtechnology.assignment.webshop.services;

import driw.xtechnology.assignment.webshop.domain.CartItem;

public interface IPriceCondition {
    CartItem apply(CartItem item);
}
