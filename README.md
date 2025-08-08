# Customer Order System (COS) - Final Project
**CS 2365 - Object-Oriented Programming**  
**Summer 2025**  
**Student:** Samaya Niraula

---

## 1. Project Overview
This project is a comprehensive **Customer Order System (COS)** developed in Java.  
It provides a complete workflow for customer interactions, from account creation and login to product browsing, cart management, and order placement.

The system is available in two forms:
- **Console application** (text-based)
- **Graphical User Interface (GUI)** built with JavaFX

---

## 2. Key Features
The system fully implements the core use cases required for an e-commerce platform:

- **✓ Secure Account Management**  
  Create unique customer accounts, log in with password and security question, log out.  
  Enforces a 3-attempt limit for password entry.

- **✓ Robust Validation**  
  Strong password requirements (length, uppercase, digit, special character)  
  Credit card validation.

- **✓ Product Catalog**  
  Browse available products with descriptions and prices.

- **✓ Shopping Cart**  
  Add, view, and modify items and quantities.

- **✓ Order Placement**  
  Checkout with delivery method choice (mail or in-store pickup), payment processing, and confirmation.

- **✓ Order History**  
  Logged-in users can view their past orders.

---

## 3. How to Compile and Run

### 3.1 Prerequisites
- Java Development Kit (**JDK 17** or higher)
- JavaFX SDK **21** or higher *(required only for GUI application)*
- All source files are located in the `com.example.project` package (adjusted for your structure).

---

### 3.2 Running the Console Application
No JavaFX setup required.

**Compile:**
```bash
javac com/example/project/*.java
```
Run:

```bash
java com.example.project.Main
```
You will be greeted with a console menu — follow on-screen prompts.

### 3.3 Running the GUI Application
Requires JavaFX SDK and VM options.

1. Download JavaFX SDK
   Get it from the Gluon website and unzip it.

2. Compile:

```bash
javac --module-path "/path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml com/example/project/*.java
```
## 3. Run:
```bash
java --module-path "/path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml com.example.project.MainGUI
```
## 4. Project Structure
```bash
src/
 └── main/java/com/example/project
      ├── Main.java              # Console entry point
      ├── MainGUI.java           # JavaFX entry point
      ├── AccountService.java    # Account creation/login/session
      ├── OrderService.java      # Orders, payments, history
      ├── ProductListing.java    # Product catalog
      ├── Customer.java          # Customer entity
      ├── Product.java           # Product entity
      ├── CreditCard.java        # Credit card entity
      ├── Cart.java              # Shopping cart
      ├── CartItem.java          # Cart item
      ├── Order.java             # Order entity
      └── PasswordValidator.java # Password validation utility
```

## 5. Design and Assumptions


**UML Diagram:**
   ![UML Class Diagram](UML%20-%20classes.png)
 **Assumptions:**
- Fixed sales tax rate and $3.00 mail delivery fee.

- Bank is simulated (no real financial institution connection).

- No persistent storage – all data is in-memory and lost on exit.

- Security questions are predefined in AccountService.java.

## 6. Known Limitations
*   No database
*   No unit tests (manual testing only)

## 7. Acknowledgments
     Developed for CS 2365 - Object-Oriented Programming at Texas Tech University.
     All code authored by Samaya Niraula.

