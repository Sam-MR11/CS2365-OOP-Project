package Project;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

/**
 * JavaFX GUI for the Customer Order System (COS).
 *
 */
public class MainGUI extends Application {

    private Stage primaryStage;
    private ProductListing catalog;
    private Cart cart;
    private OrderService orderService;
    private Customer currentCustomer;

    private int loginAttemptsLeft = 3;

    // UI root panes
    private BorderPane root;
    private Scene scene;

    // central nodes reused
    private TableView<Product> productTable;
    private TableView<CartItem> cartTable;
    private TableView<Order> ordersTable;
    /**
     * The main entry point for the Java application.
     *
     *
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
    /**
     * The main entry point for the JavaFX application. This method is called after
     * the application has been launched. It sets up the primary stage, initializes
     * the application state, and displays the initial login view.
     *
     * @param primaryStage The primary stage for this application, onto which the
     * application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.catalog = new ProductListing();
        this.cart = new Cart();

        // Seed products (same as console Main)
        seedProducts();

        root = new BorderPane();
        scene = new Scene(root, 900, 600);

        primaryStage.setTitle("Customer Order System (COS) - GUI");
        showLoginView();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    /**
     * Clears the main application pane and displays the login view. This is the
     * default view when the application starts or after a user logs out.
     */
    private void showLoginView() {
        root.setTop(null);
        root.setCenter(null);
        root.setLeft(null);
        root.setRight(null);
        root.setBottom(null);

        VBox loginBox = new VBox(10);
        loginBox.setPadding(new Insets(20));
        loginBox.setAlignment(Pos.CENTER);

        Label title = new Label("Customer Order System");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField idField = new TextField();
        idField.setPromptText("Customer ID");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginBtn = new Button("Login");
        Button createAccountBtn = new Button("Create Account");
        HBox buttons = new HBox(10, loginBtn, createAccountBtn);
        buttons.setAlignment(Pos.CENTER);

        loginBox.getChildren().addAll(title, idField, passwordField, buttons);

        // Login action
        loginBtn.setOnAction(e -> {
            String id = idField.getText().trim();
            String pw = passwordField.getText();

            if (!AccountService.customerIDTaken(id)) {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "No account found with that ID. Create an account first.");
                return;
            }

            Customer candidate = AccountService.loginCustomer(id, pw);
            if (candidate == null) {
                loginAttemptsLeft--;
                if (loginAttemptsLeft > 0) {
                    showAlert(Alert.AlertType.ERROR, "Login Failed",
                            "Incorrect password or account locked.\nAttempts remaining: " + loginAttemptsLeft);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Login Failed",
                            "No login attempts remaining. Login locked until restart.");
                    loginBtn.setDisable(true);  // Lock login button after 3 failed attempts
                }
                return;
            }

            // Reset attempts on successful login
            loginAttemptsLeft = 3;

            // Ask security question
            TextInputDialog secDialog = new TextInputDialog();
            secDialog.setTitle("Security Question");
            secDialog.setHeaderText("Security Question:");
            secDialog.setContentText(candidate.getSecurityQuestion());

            Optional<String> answer = secDialog.showAndWait();
            if (answer.isPresent() && AccountService.checkSecurityAnswer(candidate, answer.get())) {
                // Successful login
                AccountService.loginCustomer(id);
                currentCustomer = candidate;
                showMainAppView();
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Incorrect security answer.");
            }
        });

        createAccountBtn.setOnAction(e -> showCreateAccountView());

        root.setCenter(loginBox);
    }
    /**
     * Displays the view for creating a new customer account. It presents a form for
     * the user to enter their details.
     */
    private void showCreateAccountView() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        Label header = new Label("Create Account");
        header.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        grid.add(header, 0, 0, 2, 1);

        TextField idField = new TextField();
        PasswordField pwField = new PasswordField();
        TextField nameField = new TextField();
        TextField addressField = new TextField();
        TextField ccNumberField = new TextField();
        TextField ccNameField = new TextField();
        TextField ccExpField = new TextField();
        TextField ccCvvField = new TextField();
        TextField ccBalanceField = new TextField();

        ComboBox<String> secQuestions = new ComboBox<>();
        secQuestions.getItems().addAll(AccountService.getSecurityQuestions());
        secQuestions.getSelectionModel().selectFirst();

        TextField secAnswer = new TextField();

        grid.add(new Label("Customer ID:"), 0, 1);
        grid.add(idField, 1, 1);
        grid.add(new Label("Password:"), 0, 2);
        grid.add(pwField, 1, 2);
        grid.add(new Label("Name:"), 0, 3);
        grid.add(nameField, 1, 3);
        grid.add(new Label("Address:"), 0, 4);
        grid.add(addressField, 1, 4);

        grid.add(new Label("Credit Card Number (16):"), 0, 5);
        grid.add(ccNumberField, 1, 5);
        grid.add(new Label("Card Holder Name:"), 0, 6);
        grid.add(ccNameField, 1, 6);
        grid.add(new Label("Expiration (MM/YY):"), 0, 7);
        grid.add(ccExpField, 1, 7);
        grid.add(new Label("CVV:"), 0, 8);
        grid.add(ccCvvField, 1, 8);
        grid.add(new Label("Initial Card Balance ($):"), 0, 9);
        ccBalanceField.setPromptText("1000.0");
        grid.add(ccBalanceField, 1, 9);

        grid.add(new Label("Security Question:"), 0, 10);
        grid.add(secQuestions, 1, 10);
        grid.add(new Label("Answer:"), 0, 11);
        grid.add(secAnswer, 1, 11);

        Button createBtn = new Button("Create");
        Button backBtn = new Button("Back");
        HBox buttons = new HBox(10, createBtn, backBtn);
        grid.add(buttons, 1, 12);

        // ====== ENFORCE PASSWORD VALIDATION BEFORE CREATE ======
        createBtn.setOnAction(e -> {
            String id = idField.getText().trim();
            String pw = pwField.getText();
            String name = nameField.getText().trim();
            String address = addressField.getText().trim();

            if (!validatePassword(pw)) {
                showAlert(Alert.AlertType.ERROR, "Invalid Password", "Password must be at least 6 characters, " +
                        "contain at least one digit, one uppercase letter, and one special character (!@#$%^&*()_+ etc.)");
                return;
            }

            String ccNum = ccNumberField.getText().trim();
            String ccHolder = ccNameField.getText().trim();
            String ccExp = ccExpField.getText().trim();
            String ccCvv = ccCvvField.getText().trim();

            double balance = 1000.0;
            if (!ccBalanceField.getText().trim().isEmpty()) {
                try { balance = Double.parseDouble(ccBalanceField.getText().trim()); }
                catch (NumberFormatException ex) { showAlert(Alert.AlertType.ERROR, "Invalid Input", "Card balance must be a number."); return; }
            }

            int qIndex = secQuestions.getSelectionModel().getSelectedIndex();
            String answer = secAnswer.getText().trim();

            CreditCard card = new CreditCard(ccNum, ccHolder, ccExp, ccCvv, balance);
            String result = AccountService.createAccount(id, pw, name, address, card, qIndex, answer);

            if (result.toLowerCase().startsWith("error")) {
                showAlert(Alert.AlertType.ERROR, "Account Creation Failed", result);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Success", result);
                showLoginView();
            }
        });

        backBtn.setOnAction(e -> showLoginView());

        root.setCenter(grid);
    }

    /**
     * Validates a password based on the system's security requirements.
     * <p>
     * The criteria are:
     * <ul>
     * <li>At least 6 characters long.</li>
     * <li>Contains at least one digit.</li>
     * <li>Contains at least one uppercase letter.</li>
     * <li>Contains at least one special character.</li>
     * </ul>
     *
     * @param pw The password string to validate.
     * @return {@code true} if the password meets all criteria, {@code false} otherwise.
     */
    private boolean validatePassword(String pw) {
        if (pw == null || pw.length() < 6) return false;
        if (!pw.matches(".*\\d.*")) return false;                 // digit
        if (!pw.matches(".*[A-Z].*")) return false;               // uppercase
        if (!pw.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) return false; // special char
        return true;
    }
    /**
     * Displays the main application view for a logged-in user. This view includes
     * the product catalog, cart management, and order history functionalities.
     */
    private void showMainAppView() {
        // Top menu (welcome + logout)
        HBox topBar = new HBox(10);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER_LEFT);

        Label welcome = new Label("Welcome, " + currentCustomer.getName());
        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> {
            AccountService.logoutCustomer(currentCustomer.getCustomerID());
            currentCustomer = null;
            cart.clearCart();
            showLoginView();
        });

        topBar.getChildren().addAll(welcome, new Region(), logoutBtn);
        HBox.setHgrow(topBar.getChildren().get(1), Priority.ALWAYS);

        // Left menu buttons
        VBox leftMenu = new VBox(10);
        leftMenu.setPadding(new Insets(10));
        leftMenu.setPrefWidth(180);

        Button browseBtn = new Button("Browse Products");
        browseBtn.setMaxWidth(Double.MAX_VALUE);
        Button viewCartBtn = new Button("View Cart");
        viewCartBtn.setMaxWidth(Double.MAX_VALUE);
        Button checkoutBtn = new Button("Checkout");
        checkoutBtn.setMaxWidth(Double.MAX_VALUE);
        Button viewOrdersBtn = new Button("View Orders");
        viewOrdersBtn.setMaxWidth(Double.MAX_VALUE);

        leftMenu.getChildren().addAll(browseBtn, viewCartBtn, checkoutBtn, viewOrdersBtn);

        // Center area - default browse products
        VBox centerBox = new VBox(10);
        centerBox.setPadding(new Insets(10));
        productTable = createProductTable();

        centerBox.getChildren().addAll(new Label("Product Catalog:"), productTable);

        // Actions
        browseBtn.setOnAction(e -> {
            centerBox.getChildren().clear();
            centerBox.getChildren().addAll(new Label("Product Catalog:"), productTable);
        });

        viewCartBtn.setOnAction(e -> {
            centerBox.getChildren().clear();
            cartTable = createCartTable();
            centerBox.getChildren().addAll(new Label("Your Cart:"), cartTable);
        });

        viewOrdersBtn.setOnAction(e -> {
            centerBox.getChildren().clear();
            ordersTable = createOrdersTable();
            centerBox.getChildren().addAll(new Label("Your Orders:"), ordersTable);
        });

        checkoutBtn.setOnAction(e -> showCheckoutDialog());

        // Right side quick actions (Add to cart control)
        VBox rightBox = new VBox(10);
        rightBox.setPadding(new Insets(10));
        rightBox.setPrefWidth(220);

        Label quickTitle = new Label("Add to Cart");
        TextField productIdField = new TextField();
        productIdField.setPromptText("Product ID (e.g. P1)");
        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity (e.g. 1)");
        Button addToCartBtn = new Button("Add");

        addToCartBtn.setOnAction(e -> {
            String pid = productIdField.getText().trim();
            if (pid.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Enter a product ID.");
                return;
            }
            Product p = catalog.getProductByID(pid);
            if (p == null) {
                showAlert(Alert.AlertType.ERROR, "Not Found", "Product not found for ID: " + pid);
                return;
            }
            int qty = 1;
            try {
                qty = Integer.parseInt(quantityField.getText().trim());
            } catch (Exception ex) { qty = 1; }
            if (qty <= 0) { showAlert(Alert.AlertType.ERROR, "Input Error", "Quantity must be positive."); return; }

            cart.addItem(p, qty);
            showAlert(Alert.AlertType.INFORMATION, "Added", p.getProductName() + " x" + qty + " added to cart.");
            productIdField.clear();
            quantityField.clear();
        });

        rightBox.getChildren().addAll(quickTitle, productIdField, quantityField, addToCartBtn);

        // Assemble layout
        root.setTop(topBar);
        root.setLeft(leftMenu);
        root.setCenter(centerBox);
        root.setRight(rightBox);
    }

    /**
     * Creates and configures the TableView for displaying products from the catalog.
     * Includes a double-click event listener to add items to the cart.
     *
     * @return A fully configured {@link TableView} of {@link Product} objects.
     */
    private TableView<Product> createProductTable() {
        TableView<Product> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Product, String> idCol = new TableColumn<>("ProductID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("ProductID"));

        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));

        TableColumn<Product, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("productDescription"));

        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        table.getColumns().addAll(idCol, nameCol, descCol, priceCol);

        ObservableList<Product> products = FXCollections.observableArrayList(catalog.getAllProducts());
        table.setItems(products);

        // double-click add dialog
        table.setRowFactory(tv -> {
            TableRow<Product> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Product rowData = row.getItem();
                    TextInputDialog dialog = new TextInputDialog("1");
                    dialog.setTitle("Add to Cart");
                    dialog.setHeaderText("Add " + rowData.getProductName() + " to cart");
                    dialog.setContentText("Quantity:");
                    Optional<String> res = dialog.showAndWait();
                    res.ifPresent(qs -> {
                        try {
                            int q = Integer.parseInt(qs.trim());
                            if (q <= 0) throw new NumberFormatException();
                            cart.addItem(rowData, q);
                            showAlert(Alert.AlertType.INFORMATION, "Added", rowData.getProductName() + " x" + q + " added to cart.");
                        } catch (NumberFormatException ex) {
                            showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Enter a positive integer.");
                        }
                    });
                }
            });
            return row;
        });

        return table;
    }
    /**
     * Creates and configures the TableView for displaying the contents of the shopping cart.
     * Includes a double-click event listener to modify the quantity of items.
     *
     * @return A fully configured {@link TableView} of {@link CartItem} objects.
     */
    private TableView<CartItem> createCartTable() {
        TableView<CartItem> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Assuming cartTable is your TableView<CartItem>
        TableColumn<CartItem, String> idColumn = new TableColumn<>("Product ID");
        idColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProduct().getProductID()));

        TableColumn<CartItem, String> nameColumn = new TableColumn<>("Product Name");
        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProduct().getProductName()));

        TableColumn<CartItem, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<CartItem, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getProduct().getPrice()).asObject());

        TableColumn<CartItem, Double> totalColumn = new TableColumn<>("Total");
        totalColumn.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(
                        cellData.getValue().getProduct().getPrice() * cellData.getValue().getQuantity()
                ).asObject());

        table.getColumns().setAll(idColumn, nameColumn, quantityColumn, priceColumn, totalColumn);

        // get list from cart assumed method getItems()
        List<CartItem> items = cart.getItems();
        ObservableList<CartItem> obs = FXCollections.observableArrayList(items);
        table.setItems(obs);

        // Context menu to remove or change quantity
        table.setRowFactory(tv -> {
            TableRow<CartItem> row = new TableRow<>();
            row.setOnMouseClicked(ev -> {
                if (ev.getClickCount() == 2 && (!row.isEmpty())) {
                    CartItem item = row.getItem();
                    TextInputDialog dialog = new TextInputDialog(Integer.toString(item.getQuantity()));
                    dialog.setTitle("Modify Cart Item");
                    dialog.setHeaderText("Modify quantity for " + item.getProduct().getProductName());
                    dialog.setContentText("New quantity (0 to remove):");
                    Optional<String> res = dialog.showAndWait();
                    res.ifPresent(qs -> {
                        try {
                            int q = Integer.parseInt(qs.trim());
                            if (q < 0) throw new NumberFormatException();
                            if (q == 0) {
                                cart.removeItem(item.getProduct().getProductID(), item.getQuantity());
                            } else {
                                // remove all then re-add with q (assuming Cart supports set via remove/add)
                                cart.removeItem(item.getProduct().getProductID(), item.getQuantity());
                                cart.addItem(item.getProduct(), q);
                            }
                            // refresh table
                            table.setItems(FXCollections.observableArrayList(cart.getItems()));
                        } catch (NumberFormatException ex) {
                            showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Enter a non-negative integer.");
                        }
                    });
                }
            });
            return row;
        });

        return table;
    }
    /**
     * Creates and configures the TableView for displaying the customer's past orders.
     * Includes a double-click event listener to show detailed information about an order.
     *
     * @return A fully configured {@link TableView} of {@link Order} objects.
     */
    private TableView<Order> createOrdersTable() {
        TableView<Order> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Order, String> idC = new TableColumn<>("Order ID");
        idC.setCellValueFactory(new PropertyValueFactory<>("orderId"));

        TableColumn<Order, String> dateC = new TableColumn<>("Date");
        dateC.setCellValueFactory(param -> javafx.beans.property.SimpleStringProperty.stringExpression(
                new javafx.beans.binding.StringBinding() {
                    { bind(); }
                    @Override
                    protected String computeValue() {
                        return param.getValue().getOrderDate().toString();
                    }
                }
        ));

        TableColumn<Order, Double> totalC = new TableColumn<>("Total");
        totalC.setCellValueFactory(new PropertyValueFactory<>("total"));

        table.getColumns().addAll(idC, dateC, totalC);

        List<Order> orders = OrderService.getCustomerOrders(currentCustomer.getCustomerID());
        table.setItems(FXCollections.observableArrayList(orders));

        // double click shows details
        table.setRowFactory(tv -> {
            TableRow<Order> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && (!row.isEmpty())) {
                    Order ord = row.getItem();
                    showAlert(Alert.AlertType.INFORMATION, "Order Details", ord.getOrderDetails());
                }
            });
            return row;
        });

        return table;
    }

    /**
     * Initiates the checkout process. It first confirms the cart contents and delivery
     * method, then proceeds with payment processing. This method handles complex logic
     * for card validation and re-attempts.
     */
    private void showCheckoutDialog() {
        if (cart.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Empty Cart", "Your cart is empty.");
            return;
        }

        // display cart summary in dialog
        Alert summary = new Alert(Alert.AlertType.CONFIRMATION);
        summary.setTitle("Checkout");
        summary.setHeaderText("Cart Summary");
        StringBuilder sb = new StringBuilder();
        cart.displayCartDetails(); // (console) keep that behavior as well
        for (CartItem item : cart.getItems()) {
            sb.append(item.toString()).append("\n");
        }
        double total = cart.getTotal();
        sb.append("\nSubtotal: $").append(String.format("%.2f", total));
        sb.append("\nTax (8%): $").append(String.format("%.2f", total * 0.08));
        summary.setContentText(sb.toString());

        ButtonType mail = new ButtonType("Mail ($3 fee)");
        ButtonType pickup = new ButtonType("Pickup (Free)");
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        summary.getButtonTypes().setAll(mail, pickup, cancel);

        Optional<ButtonType> res = summary.showAndWait();
        if (!res.isPresent() || res.get() == cancel) return;

        String delivery = (res.get() == mail) ? "mail" : "pickup";
        // Now process payment using customer's card (use same flow as console)
        CreditCard card = currentCustomer.getCreditCard();
        double totalPrice = cart.getTotal();
        if (delivery.equals("mail")) totalPrice += 3.00;

        // If card expired or insufficient, prompt user to enter new card or add funds
        while (true) {
            if (card.isExpired()) {
                CreditCard newCard = askForNewCard();
                if (newCard == null) {
                    showAlert(Alert.AlertType.INFORMATION, "Cancelled", "Checkout cancelled.");
                    return;
                }
                card = newCard;
                currentCustomer.setCreditCard(card);
            }

            if (card.getBalance() >= totalPrice) {
                // place order
                String result = OrderService.placeOrder(currentCustomer, cart, delivery);
                showAlert(Alert.AlertType.INFORMATION, "Order Result", result);
                return;
            } else {
                // insufficient
                Alert insufficient = new Alert(Alert.AlertType.CONFIRMATION);
                insufficient.setTitle("Insufficient Balance");
                insufficient.setHeaderText(String.format("Card balance: $%.2f, needed: $%.2f", card.getBalance(), totalPrice));
                ButtonType enterNew = new ButtonType("Enter New Card");
                ButtonType addFunds = new ButtonType("Add Funds");
                ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                insufficient.getButtonTypes().setAll(enterNew, addFunds, cancelBtn);

                Optional<ButtonType> choice = insufficient.showAndWait();
                if (!choice.isPresent() || choice.get() == cancelBtn) {
                    showAlert(Alert.AlertType.INFORMATION, "Cancelled", "Checkout cancelled.");
                    return;
                } else if (choice.get() == enterNew) {
                    CreditCard newCard = askForNewCard();
                    if (newCard == null) {
                        showAlert(Alert.AlertType.INFORMATION, "Cancelled", "Checkout cancelled.");
                        return;
                    }
                    card = newCard;
                    currentCustomer.setCreditCard(card);
                } else {
                    // add funds dialog
                    TextInputDialog fundDialog = new TextInputDialog("100.0");
                    fundDialog.setTitle("Add Funds");
                    fundDialog.setHeaderText("Add funds to card");
                    fundDialog.setContentText("Amount to add:");
                    Optional<String> fundRes = fundDialog.showAndWait();
                    if (fundRes.isPresent()) {
                        try {
                            double add = Double.parseDouble(fundRes.get().trim());
                            if (add <= 0) throw new NumberFormatException();
                            card.setBalance(card.getBalance() + add);
                            showAlert(Alert.AlertType.INFORMATION, "Funds Added", String.format("New balance: $%.2f", card.getBalance()));
                        } catch (NumberFormatException ex) {
                            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Amount must be a positive number.");
                        }
                    } else {
                        showAlert(Alert.AlertType.INFORMATION, "Cancelled", "Checkout cancelled.");
                        return;
                    }
                }
            }
        }
    }
    /**
     * Displays a dialog to prompt the user for new credit card details.
     * <p>
     * This method creates a custom dialog with fields for all necessary credit card information.
     * It validates the entered data before creating and returning a new {@link CreditCard} object.
     *
     * @return A new, validated {@link CreditCard} object if the user enters valid information
     * and clicks OK; otherwise, returns {@code null}.
     */
    private CreditCard askForNewCard() {
        Dialog<CreditCard> dialog = new Dialog<>();
        dialog.setTitle("New Credit Card");
        dialog.setHeaderText("Enter new credit card details");

        ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setVgap(8);
        grid.setHgap(10);
        grid.setPadding(new Insets(10));

        TextField ccNum = new TextField();
        TextField ccHolder = new TextField();
        TextField ccExp = new TextField();
        TextField ccCvv = new TextField();
        TextField balance = new TextField("1000.0");

        grid.add(new Label("Number (16):"), 0, 0);
        grid.add(ccNum, 1, 0);
        grid.add(new Label("Holder:"), 0, 1);
        grid.add(ccHolder, 1, 1);
        grid.add(new Label("Exp (MM/YY):"), 0, 2);
        grid.add(ccExp, 1, 2);
        grid.add(new Label("CVV:"), 0, 3);
        grid.add(ccCvv, 1, 3);
        grid.add(new Label("Balance:"), 0, 4);
        grid.add(balance, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(btn -> {
            if (btn == ok) {
                try {
                    double bal = Double.parseDouble(balance.getText().trim());
                    CreditCard c = new CreditCard(ccNum.getText().trim(), ccHolder.getText().trim(), ccExp.getText().trim(), ccCvv.getText().trim(), bal);
                    if (!c.isValid()) {
                        showAlert(Alert.AlertType.ERROR, "Invalid Card", "Card number must be 16 digits.");
                        return null;
                    }
                    if (c.isExpired()) {
                        showAlert(Alert.AlertType.ERROR, "Invalid Card", "Card is expired or date format invalid.");
                        return null;
                    }
                    return c;
                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Balance must be a valid number.");
                    return null;
                }
            }
            return null;
        });

        Optional<CreditCard> result = dialog.showAndWait();
        return result.orElse(null);
    }

    /**
     * A utility method to display a standardized alert dialog to the user.
     *
     * @param type    The type of alert (e.g., Alert.AlertType.ERROR).
     * @param title   The title for the alert window.
     * @param message The main content message to display in the alert.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
    /**
     * Populates the product catalog with sample data for demonstration purposes.
     */
    private void seedProducts() {
        catalog.addProduct(new Product("P1", "Laptop", "High-performance laptop", 999.99, 899.99));
        catalog.addProduct(new Product("P2", "Phone", "Latest smartphone", 699.99, 649.99));
        catalog.addProduct(new Product("P3", "Headphones", "Noise-cancelling", 199.99, 0));
        catalog.addProduct(new Product("P4", "Tablet", "10-inch display with stylus", 449.99, 399.99));
        catalog.addProduct(new Product("P5", "Smart Watch", "Fitness tracking and notifications", 299.99, 249.99));
        catalog.addProduct(new Product("P6", "Wireless Earbuds", "True wireless with charging case", 149.99, 0));
        catalog.addProduct(new Product("P7", "Gaming Console", "Next-gen gaming system", 499.99, 449.99));
    }
}

