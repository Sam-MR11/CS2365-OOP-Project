package Project;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
/**
 * Represents a customer's credit card information.
 */
public class CreditCard {
    private String number;
    private String holderName;
    private String expirationDate;
    private String cvv;
    private double balance;


    /**
     * Constructor for CreditCard with default balance of $1000.
     * @param ccNumber   16-digit credit card number.
     * @param cardHolder Name of the cardholder.
     * @param exDate     Expiration date of the Credit Card (e.g., "12/25").
     * @param CVV        The Card Verification Value (3 or 4 digits).
     *
     */
    public CreditCard(String ccNumber, String cardHolder, String exDate, String CVV) {
        this.number = ccNumber;
        this.holderName = cardHolder;
        this.expirationDate = exDate;
        this.cvv = CVV;
        this.balance = 1000.0;
    }

    /**
     * Constructor for CreditCard with custom balance.
     *
     * @param ccNumber   16-digit credit card number.
     * @param cardHolder Name of the cardholder.
     * @param exDate     Expiration date of the Credit Card (e.g., "12/25").
     * @param CVV        The Card Verification Value (3 or 4 digits).
     * @param balance    Balance in the card.
     */
    public CreditCard(String ccNumber, String cardHolder, String exDate, String CVV, double balance) {
        this.number = ccNumber;
        this.holderName = cardHolder;
        this.expirationDate = exDate;
        this.cvv = CVV;
        this.balance = balance;
    }

    // Getters
    public String getNumber() {
        return number;
    }

    public String getHolderName() {
        return holderName;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getCvv() {
        return cvv;
    }

    public double getBalance() {
        return balance;
    }

    // Setters for updating card details
    public void setNumber(String number) {
        this.number = number;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Checks if the credit card number is valid (16 digits).
     *
     * @return true if valid, false otherwise.
     */
    public boolean isValid() {
        return number != null && number.matches("\\d{16}");
    }

    /**
     * Checks if the credit card is expired.
     *
     * @return true if expired, false otherwise.
     */
    public boolean isExpired() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth expiry = YearMonth.parse(expirationDate, formatter);
            YearMonth current = YearMonth.now();
            return expiry.isBefore(current);
        } catch (DateTimeParseException e) {
            return true; // Treat as expired if format is invalid
        }
    }
}

