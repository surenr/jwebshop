package driw.xtechnology.assignment.webshop.services;

import driw.xtechnology.assignment.webshop.domain.InventoryItem;
import driw.xtechnology.assignment.webshop.domain.Product;
import driw.xtechnology.assignment.webshop.exceptions.NoProductsAvailableInInventoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@SessionScope
public class ProductService {

    @Autowired
    private List<IPriceCondition> priceConditionServiceList;

    private List<InventoryItem> inventory;

    public ProductService() {
        this.populateInventory();
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
        return inventory;
    }

    /*
        Add a product to inventory if it doesn't exist.
        If the product is already in the inventory, it will increase the product item count.
     */
    public void add(Product product, int count) {

        InventoryItem item = inventory.stream().filter(t -> t.getCategory()
                .equals(product.getProductName())).findFirst().orElse(null);

        int numExitingProducts = item != null ? item.getNumOfProductsInCategory() : 0;
        int newNumExistingProducts = numExitingProducts + count;

        if(item != null) {
            item.updateItem(item.getProduct(), newNumExistingProducts);
            inventory.removeIf(t -> t.getCategory().equals(product.getProductName()));
            inventory.add(item);
        }
    }

    /*
       Remove a product from inventory if the product count is 0.
       If the product is already in the inventory, it will decrease the product item count.
    */
    public void remove(Product product, int numItems) throws NoProductsAvailableInInventoryException {

        if(this.inventory.size() == 0) throw new NoProductsAvailableInInventoryException();

        InventoryItem item = inventory.stream().filter(t -> t.getCategory()
                .equals(product.getProductName())).findFirst().orElse(null);

        if(item != null) {
            int numExistingProducts =  item.getNumOfProductsInCategory();
            int newItemNumberInCategory = numExistingProducts - numItems;

            if (newItemNumberInCategory < 0) throw new NoProductsAvailableInInventoryException();

            item.updateItem(item.getProduct(),newItemNumberInCategory);
            inventory.removeIf(t -> t.getCategory().equals(product.getProductName()));
            inventory.add(item);
        }
    }

    public List<InventoryItem> getPriceAppliedInventory() {
        return applyPriceConditions(this.getInventory());
    }

    public void emptyInventory() {
        inventory = new ArrayList<>();
    }

    /*
        TODO: Move the inventory to a database.
        NB: Because of time constraints I have added the inventory in the code itself. In an ideal scenario we would not
        keep the data in the session due to distributed computation concerns, ideally this should come from DB or to a cache
     */
    public void populateInventory() {
        inventory = new ArrayList<>();
        inventory.add(new InventoryItem(new Product(
                "PenguinEars",
                new BigDecimal(175),
                20,
                "https://ae01.alicdn.com/kf/HTB1G4DbKFXXXXaKXXXXq6xXFXXX6/Cute-Cartoon-Plush-Animal-Penguin-Fuzzy-Warm-Beanie-Hat-Winter-Adult-Women-Men-s-Children-Kids.jpg",
                "Penguin Ears",
                "They are great to keep you warm"), 50));

        inventory.add(new InventoryItem(new Product(
                "HorseShoes",
                new BigDecimal(825),
                5,
                "https://images-na.ssl-images-amazon.com/images/I/71iqNBxfV2L._SL1024_.jpg",
                "Horse Shoe",
                "Brings you luck and keep your horses happy"), 50));

        inventory.add(new InventoryItem(new Product(
                "NorwegianTrolls",
                new BigDecimal(400),
                10,
                "https://www.norwaygift.com/2031-home_default/skage-troll-with-shield-figure.jpg",
                "Norwegian Troll",
                "They are the best!!"), 50));

        inventory.add(new InventoryItem(new Product(
                "DecoratedElephant",
                new BigDecimal(300),
                10,
                "http://www.houseofgifts.lk/media/catalog/product/cache/1/thumbnail/480x/17f82f742ffe127f42dca9de82fb58b1/i/m/img_7164.jpg",
                "Decorated Elephant",
                "Feel free to gift away"), 50));
    }
}
