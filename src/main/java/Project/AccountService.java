package Project;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages customer accounts (Creation, retrieval, and validation).
 * Acts as a repository for Customer objects.
 */
public class AccountService {
    private static List<Customer> customers = new ArrayList<>();
    private static String[] loggedInUsers = new String[100];
    private static int loggedInCount = 0;

    private static final String[] securityQuestions = {
            "What city were you born in?",
            "What is your favorite color?",
            "What was your first car?",
            "What was the name of your first pet?",
            "What is your favorite childhood movie?"
    };

    /**
     * To create a new customer account.
     * This method encapsulates the "Create Account" use case logic.
     *
     * @param customerID            Desired customer ID.
     * @param customerPassword      Desired password.
     * @param name                  Customer's name.
     * @param address               Customer's address.
     * @param creditCard            Customer's credit card.
     * @param securityQuestionIndex The index of the selected security question.
     * @param securityAnswer        The answer to the security question.
     * @return - returns a message(string) indicating the result of the account creation (success or specific error).
     */
    public static String createAccount(String customerID, String customerPassword, String name,
                                       String address, CreditCard creditCard, int securityQuestionIndex, String securityAnswer) {
        // Check if customer ID already exists
        if (customerIDTaken(customerID)) {
            return "Error: Customer ID '" + customerID + "' is already taken! Please choose a different ID.";
        }

        // Check password validity
        if (!PasswordValidator.isValid(customerPassword)) {
            return "Password is not valid! It must be at least 6 characters long, " +
                    "contain one special character (@, #, $, %, &, *), and one uppercase letter.";
        }

        // Check if name, address and credit card are valid
        if (name == null || name.isEmpty() ||
                address == null || address.isEmpty() ||
                creditCard == null || !creditCard.isValid()) {
            return "Error: Name, address, and valid credit card cannot be empty or invalid!";
        }

        // Check security answer validity
        if (securityAnswer == null || securityAnswer.isEmpty()) {
            return "Error: Security Answer cannot be empty!";
        }

        String selectedSecurityQuestion = securityQuestions[securityQuestionIndex];

        // Create new customer
        Customer customer = new Customer(customerID, customerPassword, name, address,
                creditCard, selectedSecurityQuestion, securityAnswer);
        customers.add(customer);

        return "Account created successfully for customer ID: " + customerID;
    }

    /**
     * Checks if a customer ID is already registered in the system.
     *
     * @param customerID The ID to check.
     * @return true if the ID is taken, false otherwise.
     */
    public static boolean customerIDTaken(String customerID) {
        for (Customer customer : customers) {
            if (customer.getCustomerID().equals(customerID)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves a customer by their ID.
     *
     * @param customerID The ID of the customer to retrieve.
     * @return The Customer object if found, or null otherwise.
     */
    public static Customer getCustomerByID(String customerID) {
        for (Customer customer : customers) {
            if (customer.getCustomerID().equals(customerID)) {
                return customer;
            }
        }
        return null;
    }

    /**
     * Provides the list of available security questions.
     *
     * @return An array of security question strings.
     */
    public static String[] getSecurityQuestions() {
        return securityQuestions;
    }

    /**
     * Attempts to log in a customer.
     * Implements account locking after 3 failed attempts.
     *
     * @param customerID The customer's ID.
     * @param password   The customer's password.
     * @return The logged-in Customer object if successful, or null otherwise.
     */
    public static Customer loginCustomer(String customerID, String password) {
        Customer customer = getCustomerByID(customerID);
        if (customer == null) {
            System.out.println("No account found with that ID.");
            return null;
        }

        if (customer.getLoginAttempts() >= 3) {
            System.out.println("Account locked due to too many failed login attempts.");
            return null;
        }

        if (!customer.checkPassword(password)) {
            customer.incrementLoginAttempts();
            System.out.println("Incorrect password. Attempts remaining: " + (3 - customer.getLoginAttempts()));
            return null;
        }

        // Reset attempts on successful login
        customer.resetLoginAttempts();
        return customer;
    }

    /**
     * Validates the security answer for a given customer.
     *
     * @param customer       The customer object.
     * @param securityAnswer The provided security answer.
     * @return true if the answer is correct, false otherwise.
     */
    public static boolean checkSecurityAnswer(Customer customer, String securityAnswer) {
        if (customer == null) {
            return false;
        }
        return customer.checkSecurityAnswer(securityAnswer);
    }

    /**
     * Checks if a customer ID exists in the system.
     *
     * @param customerID The ID to check.
     * @return true if the ID exists, false otherwise.
     */
    public static boolean doesCustomerIDExist(String customerID) {
        return getCustomerByID(customerID) != null;
    }
    /**
     * Checks whether a customer with the given ID is currently logged in.
     *
     * @param customerID The ID to check.
     * @return -returns true if the customer is logged in; otherwise false.
     */
    public static boolean isLoggedIn(String customerID) {
        for (int i = 0; i < loggedInCount; i++) {
            if (loggedInUsers[i].equals(customerID)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Adds a customer ID to the list of currently logged-in users, if not already present.
     *
     * @param customerID The ID of the customer to log in.
     */
    public static void loginCustomer(String customerID) {
        if (!isLoggedIn(customerID)) {
            loggedInUsers[loggedInCount++] = customerID;
        }
    }
    /**
     * Logs out the customer by removing their ID from the logged-in users list.
     *
     * @param customerID The ID of the customer to log out.
     */
    public static void logoutCustomer(String customerID) {
        for (int i = 0; i < loggedInCount; i++) {
            if (loggedInUsers[i].equals(customerID)) {
                for (int j = i; j < loggedInCount - 1; j++) {
                    loggedInUsers[j] = loggedInUsers[j + 1];
                }
                loggedInCount--;
                break;
            }
        }
    }
}
