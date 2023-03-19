import java.util.ArrayList;
import java.util.List;

public class App {
    private static List<Order> orders = new ArrayList<>();

    public static void main(String[] args) {
        populateOrders();
        Restaurant restaurant = new Restaurant(orders);
    }

    public static void populateOrders() {
        orders.add(new Order("Fuagra"));
        orders.add(new Order("Guakamole"));
        orders.add(new Order("Guinness"));
        orders.add(new Order("Wurst"));
        orders.add(new Order("Khinkali kalakuri"));
        orders.add(new Order("Cherry"));
        orders.add(new Order("Kholodec"));
    }
}