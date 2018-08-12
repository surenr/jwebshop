package driw.xtechnology.assignment.webshop.services;

import driw.xtechnology.assignment.webshop.domain.CartItem;
import driw.xtechnology.assignment.webshop.domain.InventoryItem;
import driw.xtechnology.assignment.webshop.domain.Product;
import driw.xtechnology.assignment.webshop.exceptions.NoProductsAvailableInInventoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private List<InventoryItem> inventory;
    @Autowired
    private List<IPriceCondition> priceConditionServiceList;

    public ProductService() {
        this.populateInventory();
    }

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


    public List<InventoryItem> applyPriceConditions(List<InventoryItem> inventory) {
        List<InventoryItem> clone = new ArrayList<>();
        for(InventoryItem item: inventory)
            clone.add(new InventoryItem(item));

        for(InventoryItem item: clone) {
            for(IPriceCondition service: this.priceConditionServiceList)
                item = service.apply(item);
        }
        return clone;
    }

    // TODO: Write a unit test to cover this
    public List<InventoryItem> getInventory() {
        return inventory;
    }

    public void add(Product product, int count) {
        InventoryItem inventoryItemToAdd = inventory.stream().filter(item -> item.getCategory()
                .equals(product.getProductName())).findFirst().orElse(null);
        int numExitingProducts = inventoryItemToAdd != null ? inventoryItemToAdd.getNumOfProductsInCategory() : 0;
        int newNumExistingProducts = numExitingProducts + count;
        if(inventoryItemToAdd != null) {
            inventoryItemToAdd.updateItem(inventoryItemToAdd.getProduct(), newNumExistingProducts);
            inventory.removeIf(item -> item.getCategory().equals(product.getProductName()));
            inventory.add(inventoryItemToAdd);
        }
    }

    public void remove(Product product, int numItems) throws NoProductsAvailableInInventoryException {
        if(this.inventory.size() == 0) throw new NoProductsAvailableInInventoryException();
        InventoryItem inventoryItemToRemove = inventory.stream().filter(item -> item.getCategory()
                .equals(product.getProductName())).findFirst().orElse(null);

        if(inventoryItemToRemove != null) {
            int numExistingProducts =  inventoryItemToRemove.getNumOfProductsInCategory();
            int newItemNumberInCategory = numExistingProducts - numItems;
            if (newItemNumberInCategory < 0) throw new NoProductsAvailableInInventoryException();
            inventoryItemToRemove.updateItem(inventoryItemToRemove.getProduct(),newItemNumberInCategory);
            inventory.removeIf(item -> item.getCategory().equals(product.getProductName()));
            inventory.add(inventoryItemToRemove);

        }
    }

    public void emptyInventory() {
        inventory = new ArrayList<>();
    }
}
