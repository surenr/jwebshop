package driw.xtechnology.assignment.webshop.services;
import driw.xtechnology.assignment.webshop.domain.InventoryItem;
import driw.xtechnology.assignment.webshop.domain.Product;
import driw.xtechnology.assignment.webshop.exceptions.NoProductsAvailableInInventoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private List<IPriceCondition> priceConditionServiceList;

    @Autowired
    private DataService dataService;

    public ProductService() {

    }

    /*
        Apply all price conditions to all inventory items
     */
    public List<InventoryItem> applyPriceConditions(List<InventoryItem> inventory) {
        List<InventoryItem> clone = new ArrayList<>();

        for(InventoryItem item: inventory) {
            InventoryItem forPriceConditions = new InventoryItem(item);
            for(IPriceCondition service: this.priceConditionServiceList) {
                forPriceConditions = service.apply(forPriceConditions);
            }
            clone.add(forPriceConditions);
        }

        return clone;
    }

    public List<InventoryItem> getInventory() {
        return dataService.getInventory();
    }

    /*
        Add a product to inventory if it doesn't exist.
        If the product is already in the inventory, it will increase the product item count.
     */
    public void add(Product product, int count) {
        InventoryItem item = getInventory().stream().filter(t -> t.getCategory()
                .equals(product.getProductName())).findFirst().orElse(null);

        int numExitingProducts = item != null ? item.getNumOfProductsInCategory() : 0;
        int newNumExistingProducts = numExitingProducts + count;

        if(item != null) {
            item.updateItem(item.getProduct(), newNumExistingProducts);
            getInventory().removeIf(t -> t.getCategory().equals(product.getProductName()));
            getInventory().add(item);
        }
    }

    /*
       Remove a product from inventory if the product count is 0.
       If the product is already in the inventory, it will decrease the product item count.
    */
    public void remove(Product product, int numItems) throws NoProductsAvailableInInventoryException {
        if(this.getInventory().size() == 0) throw new NoProductsAvailableInInventoryException();

        InventoryItem item = getInventory().stream().filter(t -> t.getCategory()
                .equals(product.getProductName())).findFirst().orElse(null);

        if(item != null) {
            int numExistingProducts =  item.getNumOfProductsInCategory();
            int newItemNumberInCategory = numExistingProducts - numItems;

            if (newItemNumberInCategory < 0) throw new NoProductsAvailableInInventoryException();

            item.updateItem(item.getProduct(),newItemNumberInCategory);
            getInventory().removeIf(t -> t.getCategory().equals(product.getProductName()));
            getInventory().add(item);
        }
    }

    public List<InventoryItem> getPriceAppliedInventory() {
        return applyPriceConditions(this.getInventory());
    }

    public void emptyInventory() {
        this.dataService.emptyInventory();
    }


    public void populateInventory() {
        this.dataService.resetInventory();
    }

}
