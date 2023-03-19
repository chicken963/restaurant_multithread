import java.util.List;

public class Waiter extends Thread {
    private List<Order> orders;
    private Chef chef;

    public Waiter(Chef chef, List<Order> orders) {
        this.chef = chef;
        this.orders = orders;
    }

    @Override
    public void run() {
        while (!orders.isEmpty()) {
            synchronized (orders) {
                Order currentOrder = orders.get(0);
                System.out.printf("%s - Bringing order %s to chef\n", Thread.currentThread().getName(), currentOrder);
                synchronized (currentOrder) {
                    if (orders.size() == 1) {
                        chef.setLastOrder(true);
                    }
                    chef.setOrder(currentOrder);
                    try {
                        currentOrder.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.printf("%s - Bringing order %s to customer\n\n", Thread.currentThread().getName(), currentOrder);
                orders.remove(0);
                orders.notify();
            }
            try {
                sleep(15);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}