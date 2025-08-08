package Project;

import java.util.Objects;
    /**
    * Represents a customer in the Customer Order System (COS).
    * It stores customer's personal details, account and credit card credentials.
    */
public class Customer {
    private String customerID;
    private String customerPassword;
    private String name;
    private String address;
    private CreditCard creditCard;
    private String securityQuestion;
    private String securityAnswer;
    private int loginAttempts;


    /**
     * Constructor for creating a new Customer object.
     *
     * @param ID                ID for the customer.
     * @param password          Password for the customer's account.
     * @param customerName      Full name of the customer.
     * @param customerAddress   Physical address of the customer.
     * @param creditCardNo      Credit card associated with the customer's account.
     * @param securityQ         Security question chosen by the customer.
     * @param securityAns       The answer to the security question.
     */

    public Customer(String ID, String password, String customerName, String customerAddress, CreditCard creditCardNo, String securityQ, String securityAns) {
        customerID = ID;
        customerPassword = password;
        name = customerName;
        address = customerAddress;
        creditCard = creditCardNo;
        securityQuestion = securityQ;
        securityAnswer = securityAns;
        loginAttempts = 0;
    }
    // Getters
    public String getCustomerID(){
        return customerID;
    }
    public String getName(){
        return name;
    }
    public CreditCard getCreditCard(){
        return creditCard;
    }
    public String getSecurityQuestion(){
        return securityQuestion;
    }
    public int getLoginAttempts() {
            return loginAttempts;
    }

    // Setters
    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }
    /**
     * Checks if the provided password matches the stored password.
     * @param password The password to validate.
     * @return true if the passwords match, false otherwise.
     */
    public boolean checkPassword(String password){
        return password.equals(customerPassword);
    }
    /**
     * Validates if the provided security answer matches the stored security answer.
     * @param securityAns The answer to validate.
     * @return true if the answers match, false otherwise.
     */
    public boolean checkSecurityAnswer(String securityAns){
        return securityAnswer.equals(securityAns);
    }
    public void incrementLoginAttempts() {
            loginAttempts++;
    }

    public void resetLoginAttempts() {
            loginAttempts = 0;
    }

    // Overriding the default equals method for objects
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Customer customer = (Customer) o;
        return Objects.equals(customerID, customer.customerID);
    }
    // Method to generate Hash Code based on customerID
    @Override
    public int hashCode() {
        return Objects.hash(customerID);
    }

}
