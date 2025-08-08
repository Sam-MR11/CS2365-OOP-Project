package Project;
/**
 * Utility class for checking password format.
 */
public class PasswordValidator {
    /**
     * Validates if a password meets the specified criteria:
     * - Minimum of six characters.
     * - Includes at least one digit.
     * - Includes at least one special character (@,#,$,%,&,*).
     * - Includes at least one upper case Alphabet.
     *
     * @param password The password string to validate.
     * @return true if the password is valid, false otherwise.
     */
    public static boolean isValid(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }

        boolean containsDigit = false;
        boolean containsUppercase = false;
        boolean containsSpecialCharacters = false;

        if(password.matches(".*\\d.*")) {       // Using regular expression:  \\d for any digits between [0 to 9]
            containsDigit = true;
        }
        if(password.matches(".*[A-Z].*")) {     // [A-Z] for any uppercase letter
            containsUppercase = true;
        }
        if(password.matches(".*[@#$%&*].*")) {  // [!@#$%&*] for any special characters
            containsSpecialCharacters = true;
        }
        return containsDigit && containsUppercase && containsSpecialCharacters;
    }
}
