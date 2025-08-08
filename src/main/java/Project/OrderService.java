package Project;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Service class responsible for handling order-related operations(placing new orders, processing payments, and retrieving customer order history) in the Customer Order System.
 */
public class OrderService {
    private static List<Order> orders = new ArrayList<>();

    /**
     * Places a new order for a customer based on their current cart contents.
     * This method orchestrates the "Make Order" use case, including validation,
     * delivery method selection, payment processing, and order storage.
     *
     * @param customer       The Customer object placing the order.
     * @param cart           The Cart object containing the items to be ordered.
     * @param deliveryMethod The chosen delivery method ("mail" or "pickup").
     * @return A String message indicating the success or failure of the order placement,
     * along with order details if successful.
     */
    public static String placeOrder(Customer customer, Cart cart, String deliveryMethod) {
        // Validate parameters
        if (!AccountService.isLoggedIn(customer.getCustomerID())) {
            return "Error: You must be logged in to place an order";
        }

        if (cart.isEmpty()) {
            return "Error: Your cart is empty";
        }

        if (!deliveryMethod.equals("mail") && !deliveryMethod.equals("pickup")) {
            return "Error: Invalid delivery method";
        }

        // Calculate total with delivery fee
        double total = cart.getTotal();
        if (deliveryMethod.equals("mail")) {
            total += 3.00; // Add mailing fee
        }

        // Display order summary
        System.out.println("\n=== Order Summary ===");
        cart.displayCartDetails();
        System.out.println("Delivery Method: " + deliveryMethod +
                (deliveryMethod.equals("mail") ? " ($3.00 fee)" : " (Free pickup)"));
        System.out.println("Total with delivery: $" + String.format("%.2f", total));

        // Process payment
        CreditCard card = customer.getCreditCard();
        String authNumber = processPayment(total, card);

        if (authNumber == null) {
            System.out.println("Payment failed with current card.");
            return "Error: Payment could not be processed";
        }

        // Create and store order
        String orderId = "ORD" + System.currentTimeMillis();
        Order newOrder = new Order(orderId, customer, cart.getItems(), deliveryMethod, authNumber);
        orders.add(newOrder);

        // Clear cart
        cart.clearCart();

        // Return confirmation
        return "Order placed successfully!\n" + newOrder.getOrderDetails();
    }

    /**
     * Retrieves all orders for a specific customer
     *
     * @param customerID The ID of the customer to find orders for
     * @return List of orders belonging to the customer
     */
    public static List<Order> getCustomerOrders(String customerID) {
        List<Order> customerOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getCustomer().getCustomerID().equals(customerID)) {
                customerOrders.add(order);
            }
        }
        return customerOrders;
    }

    /**
     * Simulates processing a credit card payment for a given amount.
     * Handles credit card validation, simulates bank approval/denial,
     * and allows for up to 3 attempts to enter a new credit card if the initial one fails.
     *
     * @param amount The total amount to be charged to the credit card.
     * @param originalCard   The initial CreditCard object to attempt charging. This object is updated
     *               internally if the user enters a new card.
     * @return A four-digit authorization number as a String if payment is approved, or {@code null} if
     * payment is declined after all attempts or the user chooses to exit.
     */
    private static String processPayment(double amount, CreditCard originalCard) {
        Scanner scanner = new Scanner(System.in);
        int attempts = 0;
        CreditCard currentCard = originalCard;
        boolean cardChanged = false;

        while (attempts < 3) {
            // First, check card validity before bank approval
            if (!currentCard.isValid()) {
                System.out.println("Stored card is invalid.");
            } else {
                // Simulate realistic bank approval
                System.out.println("Processing payment of $" + String.format("%.2f", amount) +
                        " with card ending in " + currentCard.getNumber().substring(12));

                if (simulateBankApproval(currentCard, amount)) {
                    String authNumber = String.format("%04d", (int) (Math.random() * 10000));
                    System.out.println("Payment approved. Auth#: " + authNumber);

                    // If customer entered a new card, save it to their account
                    if (cardChanged) {
                        originalCard.setNumber(currentCard.getNumber());
                        originalCard.setHolderName(currentCard.getHolderName());
                        originalCard.setExpirationDate(currentCard.getExpirationDate());
                        originalCard.setCvv(currentCard.getCvv());
                        originalCard.setBalance(currentCard.getBalance());
                    }

                    return authNumber; // success
                } else {
                    System.out.println("Payment declined by bank.");
                }
            }

            attempts++;

            // Only prompt for a new card if we still have attempts left
            if (attempts < 3) {
                System.out.println("Would you like to try another card? (yes/no)");
                String response = scanner.nextLine();
                if (!response.equalsIgnoreCase("yes")) break;
                currentCard = readNewCard(scanner);
                cardChanged = true;
            }
        }

        return null; // All attempts failed
    }


    private static boolean simulateBankApproval(CreditCard card, double amount) {
        if (card.isExpired()) {
            System.out.println("Bank declined: Card expired.");
            return false;
        }

        if (card.getBalance() < amount) {
            System.out.println("Bank declined: Insufficient balance.");
            return false;
        }

        card.setBalance(card.getBalance() - amount);
        return true;
    }


    // Helper to read new card input
    private static CreditCard readNewCard(Scanner scanner) {
        System.out.print("Enter new card number: ");
        String number = scanner.nextLine();

        System.out.print("Enter card holder name: ");
        String name = scanner.nextLine();

        System.out.print("Enter expiration date (MM/YY): ");
        String exp = scanner.nextLine();

        System.out.print("Enter CVV: ");
        String cvv = scanner.nextLine();

        return new CreditCard(number, name, exp, cvv);
    }
}
