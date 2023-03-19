public class Chef extends Thread {

    private static final int COOKING_TIME_IN_MILLIS = 1000;
    private boolean isLastOrder = false;

    private Order order;
    private Order lastCookedOrder;

    @Override
    public void run() {
        while (true) {
            if (order != lastCookedOrder) {
                System.out.printf("%s - Start cooking %s\n", Thread.currentThread().getName(), order);
                synchronized (order) {
                    try {
                        Thread.sleep(COOKING_TIME_IN_MILLIS);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.printf("%s - Finished cooking %s\n", Thread.currentThread().getName(), order);
                    lastCookedOrder = order;
                    order.notify();
                    if (isLastOrder) {
                        break;
                    }
                }
            } else {
                sleep(5);
            }

        }
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setLastOrder(boolean lastOrder) {
        isLastOrder = lastOrder;
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}