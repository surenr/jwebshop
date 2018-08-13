package driw.xtechnology.assignment.webshop.services;

import driw.xtechnology.assignment.webshop.domain.Cart;
import driw.xtechnology.assignment.webshop.domain.InventoryItem;
import driw.xtechnology.assignment.webshop.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@SessionScope // Since we are not using a database, make sure data is instantiated for every session
public class DataService {

    private Cart cart;
    private List<InventoryItem> inventory;

    public DataService() {
        cart = new Cart(new ArrayList<>());
        inventory = new ArrayList<>();
        populateInventory();
    }

    public Cart getCart() {
        return cart;
    }

    public List<InventoryItem> getInventory() {
        return inventory;
    }

    /*
        TODO: Move the inventory to a database.
        NB: Because of time constraints I have added the inventory in the code itself. In an ideal scenario we would not
        keep the data in the session due to distributed computation concerns, ideally this should come from DB or to a cache
     */
    private void populateInventory() {
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

    public void emptyInventory() {
        inventory = new ArrayList<>();
    }

    public void resetInventory() {
        this.populateInventory();
    }
}
