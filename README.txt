===============================================================================
                    Customer Order System (COS) - Final Project
                        CS 2365 - Object-Oriented Programming
                                  Summer 2025
                             Student: Samaya Niraula
===============================================================================

1. PROJECT OVERVIEW
===============================================================================
This project is a comprehensive Customer Order System (COS) developed in Java. 
It provides a complete workflow for customer interactions, from account creation 
and login to product Browse, cart management, and order placement. The system is 
available in two forms: a text-based console application and a user-friendly 
Graphical User Interface (GUI) built with JavaFX.

2. KEY FEATURES
===============================================================================
The system fully implements the core use cases required for an e-commerce 
platform:

✓ Secure Account Management: Create unique customer accounts, log in with a 
  password and security question, and log out. The system enforces a 3-attempt 
  limit for password entry.

✓ Robust Validation: Enforces strong password requirements (length, uppercase, 
  digit, special character) and validates credit card information.

✓ Product Catalog: Browse a list of available products with descriptions and 
  prices.

✓ Shopping Cart: Add, view, and modify items and quantities in a personal 
  shopping cart.

✓ Order Placement: A complete checkout process with a choice of delivery methods 
  (mail or in-store pickup), payment processing, and order confirmation.

✓ Order History: Logged-in users can view a list of their past orders.

3. HOW TO COMPILE AND RUN
===============================================================================
This project includes both a console application and a GUI application. Please 
follow the instructions carefully for the version you wish to run.

3.1. PREREQUISITES
- Java Development Kit (JDK) 17 or higher.
- JavaFX SDK 21 or higher (required only for the GUI application).
- All project source files are located in the Project1P3 package.

3.2. RUNNING THE CONSOLE APPLICATION
The console application does not require the JavaFX SDK.

Compile the Code: 
Open a terminal or command prompt, navigate to the project's root directory 
(the one containing the Project1P3 folder), and run:

    javac Project1P3/*.java

Run the Application: 
Execute the Main class to start the program:

    java Project1P3.Main

You will be greeted with a menu in the console. Follow the on-screen prompts to 
interact with the system.

3.3. RUNNING THE GUI APPLICATION
The GUI application requires the JavaFX SDK and specific VM options to run.

Set Up JavaFX: 
Download the JavaFX SDK from the Gluon website and unzip it to a known location 
on your computer.

Compile the Code: 
From the project's root directory, compile all files while providing the path 
to your JavaFX library. Replace /path/to/your/javafx-sdk/lib with the actual 
path on your machine.

    javac --module-path "/path/to/your/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml Project1P3/*.java

Run the Application: 
Run the MainGUI class using the same VM options. Remember to replace the path.

    java --module-path "/path/to/your/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml Project1P3.MainGUI

The GUI window will launch, starting with the login screen.

4. PROJECT STRUCTURE
===============================================================================
The project is organized into logical packages and classes.

UI Layer:
- Main.java: The entry point and controller for the console application.
- MainGUI.java: The entry point and controller for the JavaFX GUI application.

Core Services:
- AccountService.java: Handles all logic related to customer accounts, including 
  creation, login, and session management.
- OrderService.java: Manages order creation, payment processing, and history 
  retrieval.
- ProductListing.java: Acts as the product catalog.

Data/Entity Classes:
- Customer.java: Represents a customer.
- Product.java: Represents a single product.
- CreditCard.java: Represents a credit card.
- Cart.java & CartItem.java: Represent the shopping cart and its contents.
- Order.java: Represents a completed customer order.

Utility Classes:
- PasswordValidator.java: Validates password strength requirements including minimum length, uppercase letters, digits, and special characters.

5. DESIGN AND ASSUMPTIONS
===============================================================================
UML Diagram: A detailed UML class diagram is included with this submission, 
illustrating the relationships and dependencies between all classes.

Assumptions Made:
- The system uses a fixed sales tax rate and a fixed fee for mail delivery 
  ($3.00).
- The "bank" is a simulation (BankService.java) and does not connect to a live 
  financial institution. Its approval/denial logic is based on a simplified 
  model.
- The application does not persist data. All customer accounts, carts, and 
  orders are stored in memory and are lost when the program terminates.
- The list of security questions is predefined and hardcoded in 
  AccountService.java.

6. KNOWN LIMITATIONS
===============================================================================
No Database: The system lacks persistent storage. 

No Unit Tests: The application was tested manually.

7. ACKNOWLEDGMENTS
===============================================================================
This project was developed for the CS 2365 Object-Oriented Programming course 
at Texas Tech University.
All code is original and authored by Samaya Niraula.
===============================================================================
                                 END OF README
===============================================================================
