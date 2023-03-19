public class Order {
    private final String description;

    public Order(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
