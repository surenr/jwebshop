package driw.xtechnology.assignment.webshop.services;
import driw.xtechnology.assignment.webshop.domain.InventoryItem;

/*
    Helps to implement different price conditions
 */
public interface IPriceCondition {
    InventoryItem apply(InventoryItem item);
}
