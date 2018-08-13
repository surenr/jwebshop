package driw.xtechnology.assignment.webshop.services;
import driw.xtechnology.assignment.webshop.domain.InventoryItem;

public interface IPriceCondition {
    InventoryItem apply(InventoryItem item);
}
