import java.util.List;

public class Restaurant {
    private List<Order> orders;
    private final Chef chef;
    private final Waiter waiter;

    public Restaurant(List<Order> orders) {
        this.orders = orders;
        this.chef = new Chef();
        this.waiter = new Waiter(chef, orders);
        System.out.printf("%s - Starting processing orders\n", Thread.currentThread().getName());
        waiter.start();
        chef.start();
    }
}