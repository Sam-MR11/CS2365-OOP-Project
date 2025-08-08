package Project;

import java.util.Date;
import java.util.List;

/**
 * Represents a customer order
 */
public class Order {
    private String orderId;
    private Customer customer;
    private Date orderDate;
    private List<CartItem> items;
    private String deliveryMethod;
    private double total;
    private String authorizationNumber;

    /**
     * Constructor for order creating a new Order instance.
     *
     * @param orderId A unique identifier for this order.
     * @param customer The {@link Customer} object who placed this order.
     * @param items A {@link List} of {@link CartItem} objects representing the products and quantities in the order.
     * @param deliveryMethod The method chosen for delivery (e.g., "mail", "pickup").
     * @param authorizationNumber The authorization number received from the bank after successful payment.
     */
    public Order(String orderId, Customer customer, List<CartItem> items,
                 String deliveryMethod, String authorizationNumber) {
        this.orderId = orderId;
        this.customer = customer;
        this.orderDate = new Date();
        this.items = items;
        this.deliveryMethod = deliveryMethod;
        this.authorizationNumber = authorizationNumber;
        this.total = calculateTotal();
    }
    //Getters
    public Customer getCustomer() {
        return customer;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public String getAuthorizationNumber() {
        return authorizationNumber;
    }
    public String getOrderId() {
        return orderId;
    }
    public Date getOrderDate() {
        return orderDate;
    }
    public double getTotal() {
        return total;
    }

    /**
     * Calculates the total cost of the order, including item subtotals, sales tax, and delivery fees.
     * Called internally when the Order object is constructed.
     *
     * @return The calculated total cost of the order.
     */
    private double calculateTotal() {
        double subtotal = 0;
        for (CartItem item : items) {
            subtotal += item.getTotalPrice();
        }

        double tax = subtotal * 0.08; // 8% tax
        double shipping = deliveryMethod.equals("mail") ? 3.00 : 0.00;

        return subtotal + tax + shipping;
    }

    /**
     * Generates a formatted string containing all relevant details of the order.
     * This includes Order ID, Date, Delivery Method, a list of items, the total cost,
     * and the payment authorization number.
     *
     * @return A formatted String containing the complete order details.
     */
    public String getOrderDetails() {
        String details = "Order ID: " + orderId + "\n";
        details += "Date: " + orderDate + "\n";
        details += "Delivery: " + deliveryMethod;
        if (deliveryMethod.equals("mail")) {
            details += " ($3.00 fee)";
        }
        details += "\nItems:\n";

        for (CartItem item : items) {
            details += "  " + item.toString() + "\n";
        }

        details += "Total: $" + String.format("%.2f", total) + "\n";
        details += "Auth#: " + authorizationNumber;

        return details;
    }
}
