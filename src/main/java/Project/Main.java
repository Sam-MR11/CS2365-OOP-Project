package Project;

import java.util.List;
import java.util.Scanner;
/**
 * Starting point of Customer Order System application.
 * Allows users to:
 *     <li>Create a customer account</li>
 *     <li>Log in with credentials and a security question</li>
 *     <li>Browse products and add items to cart</li>
 *     <li>View and manage cart contents</li>
 *     <li>Checkout with delivery options</li>
 *     <li>View order history</li>
 *     <li>Logout securely</li>
 * </ul>
 * <p>
 * Uses supporting services and classes such as AccountService, ProductListing,
 * Cart,OrderService, and Customer.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProductListing catalog = new ProductListing();
        Cart cart = new Cart();
        Customer currentCustomer = null;

        // Sample products
        catalog.addProduct(new Product("P1", "Laptop", "High-performance laptop", 999.99, 899.99));
        catalog.addProduct(new Product("P2", "Phone", "Latest smartphone", 699.99, 649.99));
        catalog.addProduct(new Product("P3", "Headphones", "Noise-cancelling", 199.99, 0));
        catalog.addProduct(new Product("P4", "Tablet", "10-inch display with stylus", 449.99, 399.99));
        catalog.addProduct(new Product("P5", "Smart Watch", "Fitness tracking and notifications", 299.99, 249.99));
        catalog.addProduct(new Product("P6", "Wireless Earbuds", "True wireless with charging case", 149.99, 0));
        catalog.addProduct(new Product("P7", "Gaming Console", "Next-gen gaming system", 499.99, 449.99));

        while (true) {
            if (currentCustomer == null) {
                displayMainMenu();
                int choice = readInt(scanner);
                switch (choice) {
                    case 1:
                        createAccount(scanner);
                        break;
                    case 2:
                        currentCustomer = login(scanner);
                        break;
                    case 0:
                        exitApp();
                        break;
                    default:
                        System.out.println("Invalid choice!");
                        break;
                }
            } else {
                displayLoggedInMenu(currentCustomer);
                int choice = readInt(scanner);
                switch (choice) {
                    case 3:
                        browseProducts(scanner, catalog, cart);
                        break;
                    case 4:
                        viewCart(scanner, cart);
                        break;
                    case 5:
                        checkout(scanner, currentCustomer, cart);
                        break;
                    case 6:
                        viewOrders(currentCustomer);
                        break;
                    case 7 : {
                        AccountService.logoutCustomer(currentCustomer.getCustomerID());
                        currentCustomer = null;
                        cart.clearCart();
                        System.out.println("Logged out successfully.");
                        break;
                    }
                    case 0:
                        exitApp();
                        break;
                    default:
                        System.out.println("Invalid choice!");
                        break;
                }
            }
        }
    }
    /**
     * Displays the main menu for unauthenticated users.
     * Options: Create account, login, or exit.
     */
    private static void displayMainMenu() {
        System.out.println("\n=== Customer Order System ===");
        System.out.println("1. Create Account");
        System.out.println("2. Login");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }
    /**
     * Displays the menu for logged-in customers.
     *
     * @param currentCustomer The currently logged-in customer.
     */
    private static void displayLoggedInMenu(Customer currentCustomer) {
        System.out.println("\n=== Welcome, " + currentCustomer.getName() + " ===");
        System.out.println("3. Browse Products");
        System.out.println("4. View Cart");
        System.out.println("5. Checkout");
        System.out.println("6. View Orders");
        System.out.println("7. Logout");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }
    /**
     * Reads and returns a valid integer input from the user.
     * Also handles invalid input loops.
     *
     * @param scanner Scanner for user input.
     * @return A valid integer entered by the user.
     */
    private static int readInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Invalid input. Enter a number: ");
        }
        int val = scanner.nextInt();
        scanner.nextLine();
        return val;
    }
    /**
     * Exits the application with a farewell message.
     */
    private static void exitApp() {
        System.out.println("Exiting... Thank you!");
        System.exit(0);
    }
    /**
     * Handles customer account creation process.
     *
     * @param scanner Scanner for reading user input.
     */
    private static void createAccount(Scanner scanner) {
        System.out.println("\n=== Create Account ===");
        System.out.print("Enter customer ID: ");
        String customerID = scanner.nextLine();

        if (AccountService.customerIDTaken(customerID)) {
            System.out.println("Customer ID already exists. Please try a different ID.");
            return;
        }

        System.out.print("Enter password (at least 6 characters): ");
        String password = scanner.nextLine();
        if (password.length() < 6) {
            System.out.println("Password too short! Account creation cancelled.");
            return;
        }

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter your address: ");
        String address = scanner.nextLine();

        System.out.print("Enter credit card number: ");
        String ccNumber = scanner.nextLine();

        System.out.print("Enter card holder name: ");
        String ccName = scanner.nextLine();

        System.out.print("Enter expiration date (MM/YY): ");
        String expDate = scanner.nextLine();

        System.out.print("Enter CVV: ");
        String cvv = scanner.nextLine();

        CreditCard card = new CreditCard(ccNumber, ccName, expDate, cvv);

        if (!card.isValid()) {
            System.out.println("Credit card declined. Please try a different card.");
            return;
        }

        System.out.println("\nSelect a security question:");
        String[] questions = AccountService.getSecurityQuestions();
        for (int i = 0; i < questions.length; i++) {
            System.out.println((i + 1) + ". " + questions[i]);
        }
        System.out.print("Enter question number: ");
        int qNum = readInt(scanner);

        System.out.print("Enter your answer: ");
        String answer = scanner.nextLine();

        String result = AccountService.createAccount(customerID, password, name, address, card, qNum - 1, answer);
        System.out.println(result);
    }
    /**
     * Handles customer login by verifying ID, password, and security answer.
     *
     * @param scanner Scanner for user input.
     * @return The logged-in {@code Customer} if successful; otherwise {@code null}.
     */
    private static Customer login(Scanner scanner) {
        System.out.println("\n=== Login ===");
        System.out.print("Enter customer ID: ");
        String customerID = scanner.nextLine();

        if (!AccountService.customerIDTaken(customerID)) {
            System.out.println("Customer ID does not exist. Please create an account first.");
            return null;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Customer customer = AccountService.loginCustomer(customerID, password);
        if (customer == null) {
            System.out.println("Login failed. Incorrect password.");
            return null;
        }

        System.out.println("Security Question: " + customer.getSecurityQuestion());
        System.out.print("Enter your answer: ");
        String answer = scanner.nextLine();

        if (AccountService.checkSecurityAnswer(customer, answer)) {
            AccountService.loginCustomer(customerID);
            System.out.println("Login successful! Welcome, " + customer.getName());
            return customer;
        } else {
            System.out.println("Incorrect security answer.");
            return null;
        }
    }
    /**
     * Allows the logged-in customer to browse available products and add them to the cart.
     *
     * @param scanner Scanner for user input.
     * @param catalog Product listing object.
     * @param cart    Customer's cart.
     */
    private static void browseProducts(Scanner scanner, ProductListing catalog, Cart cart) {
        System.out.println("\n=== Product Catalog ===");
        catalog.displayProductList();

        System.out.print("\nEnter product ID to add to cart (or 0 to go back): ");
        String productID = scanner.nextLine();

        if (!productID.equals("0")) {
            Product product = catalog.getProductByID(productID);
            if (product != null) {
                System.out.print("Enter quantity: ");
                int quantity = readInt(scanner);
                cart.addItem(product, quantity);
                System.out.println(product.getProductName() + " added to cart.");
            } else {
                System.out.println("Invalid product ID!");
            }
        }
    }
    /**
     * Displays the contents of the customer's cart and allows optional removal of items.
     *
     * @param scanner Scanner for user input.
     * @param cart    Customer's cart.
     */
    private static void viewCart(Scanner scanner, Cart cart) {
        System.out.println("\n=== Your Cart ===");
        cart.displayCartDetails();
        if (!cart.isEmpty()) {
            System.out.print("Do you want to remove an item? (yes/no): ");
            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("yes")) {
                System.out.print("Enter product ID to remove: ");
                String removeID = scanner.nextLine();
                System.out.print("Enter quantity: ");
                int quantity = readInt(scanner);
                cart.removeItem(removeID, quantity);
                System.out.println("Item removed if it existed.");
            }
        }
    }
    /**
     * Handles the checkout process. Customer can choose between mail or in-store pickup.
     * Finalizes the order through {@code OrderService}.
     * If the credit card is expired or has insufficient balance, user can enter new card
     * or add funds. The new card now correctly processes the order upon valid input.
     *
     * @param scanner  Scanner for user input.
     * @param customer The logged-in customer.
     * @param cart     The customer's cart with items ready for checkout.
     */
    private static void checkout(Scanner scanner, Customer customer, Cart cart) {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty!");
            return;
        }

        cart.displayCartDetails();

        System.out.println("\nChoose delivery method:");
        System.out.println("1. Mail ($3.00 fee)");
        System.out.println("2. In-store pickup (free)");
        System.out.println("0. Cancel order");
        System.out.print("Enter choice: ");

        int deliveryChoice = readInt(scanner);

        if (deliveryChoice == 0) {
            System.out.println("Order cancelled.");
            return;
        }

        if (deliveryChoice != 1 && deliveryChoice != 2) {
            System.out.println("Invalid choice. Order cancelled.");
            return;
        }

        String deliveryMethod = (deliveryChoice == 1) ? "mail" : "pickup";

        // Get total price with delivery fee (if any)
        double totalPrice = cart.getTotal();
        if (deliveryMethod.equals("mail")) {
            totalPrice += 3.00;
        }
        System.out.printf("Total to pay (including delivery): $%.2f%n", totalPrice);

        // Start with the customer's registered credit card
        CreditCard card = customer.getCreditCard();

        while (true) {
            if (card.isExpired()) {
                System.out.println("Your credit card is expired. Please enter a new card.");
                card = promptForNewCard(scanner);
                continue;
            }

            if (card.getBalance() >= totalPrice) {
                // Enough balance - place the order
                String result = OrderService.placeOrder(customer, cart, deliveryMethod);
                System.out.println("\n" + result);
                break;
            } else {
                System.out.printf("❌ Insufficient balance: $%.2f%n", card.getBalance());
                System.out.println("Choose an option:");
                System.out.println("1. Enter a new credit card");
                System.out.println("2. Add funds to the existing card");
                System.out.println("0. Cancel order");
                System.out.print("Enter your choice: ");

                int choice = readInt(scanner);

                if (choice == 1) {
                    card = promptForNewCard(scanner);
                    customer.setCreditCard(card);
                } else if (choice == 2) {
                    System.out.print("Enter amount to add: ");
                    double amountToAdd = scanner.nextDouble();
                    scanner.nextLine(); // consume newline
                    card.setBalance(card.getBalance() + amountToAdd);
                    System.out.printf("✅ New card balance: $%.2f%n", card.getBalance());
                } else if (choice == 0) {
                    System.out.println("Order cancelled.");
                    break;
                } else {
                    System.out.println("Invalid choice, please try again.");
                }
            }
        }
    }

    private static CreditCard promptForNewCard(Scanner scanner) {
        System.out.println("\n=== Enter New Credit Card Details ===");

        System.out.print("Enter credit card number (16 digits): ");
        String ccNumber = scanner.nextLine();

        System.out.print("Enter card holder name: ");
        String ccName = scanner.nextLine();

        System.out.print("Enter expiration date (MM/YY): ");
        String expDate = scanner.nextLine();

        System.out.print("Enter CVV: ");
        String cvv = scanner.nextLine();

        System.out.print("Enter card balance: ");
        double balance = 0;
        while (true) {
            try {
                balance = Double.parseDouble(scanner.nextLine());
                if (balance < 0) {
                    System.out.print("Balance cannot be negative. Enter again: ");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Enter card balance: ");
            }
        }

        CreditCard newCard = new CreditCard(ccNumber, ccName, expDate, cvv);
        newCard.setBalance(balance);

        if (!newCard.isValid()) {
            System.out.println("Invalid credit card number. Please try again.");
            return promptForNewCard(scanner);
        }
        if (newCard.isExpired()) {
            System.out.println("Credit card is expired. Please try again.");
            return promptForNewCard(scanner);
        }

        System.out.println("New card accepted.");
        return newCard;
    }

    /**
     * Displays the customer's past orders, if any.
     *
     * @param customer The logged-in customer.
     */
    private static void viewOrders(Customer customer) {
        System.out.println("\n=== Your Orders ===");
        List<Order> orders = OrderService.getCustomerOrders(customer.getCustomerID());

        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        for (Order order : orders) {
            System.out.println(order.getOrderDetails());
            System.out.println("----------------------");
        }
        System.out.println("Total Orders: " + orders.size());
    }
}
