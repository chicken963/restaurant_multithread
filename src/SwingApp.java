import net.miginfocom.swing.MigLayout;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SwingApp {

    private static List<JComboBox<Order>> orderComboBoxes = new ArrayList<>();
    private static List<JLabel> successLabels = new ArrayList<>();
    private static final int NUMBER_IN_ROW = 3;
    private static final AtomicInteger indexToInsertNewItem = new AtomicInteger(NUMBER_IN_ROW + 1);
    private static JPanel orderComposePanel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Restaurant Application");
        renderMainMenu(frame);

    }

    private static void renderMainMenu(JFrame frame) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new MigLayout());

        orderComposePanel = new JPanel();
        orderComposePanel.setLayout(new MigLayout());

        JLabel composeMessage = new JLabel("Please choose the dishes:");
        orderComposePanel.add(composeMessage, "wrap");

        JComboBox<Order> availableOrders = new JComboBox<>(prepareAvailableOrders());
        JButton removeItemButton = prepareRemoveItemButton(availableOrders);

        orderComposePanel.add(availableOrders);
        orderComposePanel.add(removeItemButton);
        orderComposePanel.add(prepareSuccessLabel(), "wrap");

        orderComboBoxes.add(availableOrders);

        JButton addItem = prepareAddItemButton();
        orderComposePanel.add(addItem);

        orderComposePanel.setSize(800, 400);
        mainPanel.add(orderComposePanel, "wrap");
        mainPanel.add(prepareCookButton());


        frame.getContentPane().add(mainPanel);
        frame.setSize(1000, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    private static JLabel prepareSuccessLabel() {
        JLabel successLabel = new JLabel("Dish is prepared");
        successLabel.setForeground(Color.getHSBColor(150, 255, 200));
        successLabel.setVisible(false);
        successLabels.add(successLabel);
        return successLabel;
    }

    private static JButton prepareAddItemButton() {
        JButton addItem = new JButton("Add order");
        addItem.addActionListener((e) -> {
            JComboBox<Order> availableOrders = new JComboBox<>(prepareAvailableOrders());
            JButton removeItemButton = prepareRemoveItemButton(availableOrders);
            orderComposePanel.add(availableOrders, indexToInsertNewItem.getAndIncrement());
            orderComposePanel.add(removeItemButton, indexToInsertNewItem.getAndIncrement());
            orderComposePanel.add(prepareSuccessLabel(), "wrap", indexToInsertNewItem.getAndIncrement());
            orderComposePanel.revalidate();

            orderComboBoxes.add(availableOrders);
        });
        return addItem;
    }

    private static JButton prepareRemoveItemButton(JComboBox<Order> availableOrders) {
        JButton removeItemButton = new JButton("-");
        removeItemButton.addActionListener((e) -> {
            int successLabelIndex = orderComposePanel.getComponentZOrder(removeItemButton) + 1;
            successLabels.remove(orderComboBoxes.indexOf(availableOrders));
            orderComposePanel.remove(successLabelIndex);
            orderComposePanel.remove(availableOrders);
            orderComposePanel.remove(removeItemButton);
            indexToInsertNewItem.getAndAdd(-NUMBER_IN_ROW);
            orderComposePanel.revalidate();

            orderComposePanel.remove(availableOrders);
        });
        return removeItemButton;
    }

    private static JButton prepareCookButton() {
        JButton cookButton = new JButton("Let's cook");
        cookButton.addActionListener(e -> {
            for (JLabel successLabel: successLabels) {
                successLabel.setVisible(false);
            }
            List<Order> orderList = new ArrayList<>();
            for (JComboBox<Order> comboBox : orderComboBoxes) {
                orderList.add((Order) comboBox.getSelectedItem());
            }
            int orderListSize = orderList.size();
            Thread thread = new Thread(() -> {
                for (int i = 0; i < orderListSize; i++) {
                    synchronized (orderList) {
                        try {
                            orderList.wait();
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        JLabel successLabel = (JLabel) orderComposePanel.getComponent((i + 1) * NUMBER_IN_ROW);
                        SwingUtilities.invokeLater(() -> successLabel.setVisible(true));
                        if (orderList.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "All the food is ready!");
                        }
                    }
                }
            });
            thread.start();
            Restaurant restaurant = new Restaurant(orderList);

        });
        return cookButton;
    }

    private static Order[] prepareAvailableOrders() {
        return new Order[]{
                new Order("Fuagra"),
                new Order("French fries"),
                new Order("Guinness"),
                new Order("Wurst"),
                new Order("Khinkali kalakuri"),
                new Order("Salmon"),
                new Order("Kholodec")
        };
    }
}
