package Project;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer's shopping cart, holding selected products and quantities.
 */
public class Cart {
    private List<CartItem> items;
    private static final double SALES_TAX  = 0.08; //Randomly taken (8%)

    /**
     * Contructor to create a new empty shopping cart.
     */
    public Cart(){
        items = new ArrayList<>();
    }

    /**
     * Adds a product to the cart or updates its quantity if already present.
     * @param product The product to add.
     * @param quantity The quantity to add.
     */
    public void addItem(Product product, int quantity){
        if (product == null || quantity <= 0){
            System.out.println("Product or quantity is negative and cannot be added to cart.");
            return;
        }

        for  (CartItem item : items) {
            if (item.getProduct().getProductName().equals(product.getProductName())){
                item.setQuantity(item.getQuantity() + quantity); // Update the quantity
                System.out.println("Updated quantity for " + product.getProductName()+ " to" + item.getQuantity());
                return;
            }
        }
        // If not found, add as a new item
        items.add(new CartItem(product, quantity));
        System.out.println("Added " + quantity + " x " + product.getProductName() + " to cart.");
    }

    /**
     * Removes a product from the cart, or reduces its quantity.
     * @param productID The ID of the product to remove.
     * @param quantityToRemove The quantity to remove. If 0 or less, removes all of this product.
     * @return true if the item was removed/quantity updated, false if product not found.
     */
    public boolean removeItem(String productID, int quantityToRemove) {
        // **Added validation for null or empty productID**
        if (productID == null || productID.trim().isEmpty()) {
            System.out.println("Error: Product ID cannot be null or empty.");
            return false;
        }

        CartItem itemToRemove = null;
        for (CartItem item : items) {
            if (item.getProduct().getProductID().equals(productID)) {
                itemToRemove = item;
                break;
            }
        }

        if (itemToRemove == null) {
            System.out.println("Product with ID " + productID + " not found in cart.");
            return false;
        }

        if (quantityToRemove <= 0 || quantityToRemove >= itemToRemove.getQuantity()) {
            items.remove(itemToRemove); // Remove all of this item
            System.out.println("Removed all " + itemToRemove.getProduct().getProductName() + " from cart.");
        } else {
            itemToRemove.setQuantity(itemToRemove.getQuantity() - quantityToRemove); // Reduce quantity
            System.out.println("Removed " + quantityToRemove + " of " + itemToRemove.getProduct().getProductName() + ". Remaining: " + itemToRemove.getQuantity());
        }
        return true;
    }
    /**
     * Calculates the subtotal of all items in the cart before taxes.
     * @return The subtotal amount.
     */
    public double getSubTotal(){
        double subTotal = 0;
        for (CartItem item : items) {
            subTotal += item.getQuantity() * item.getTotalPrice();
        }
        return subTotal;
    }
    /**
     * Calculates the sales tax for the current cart total.
     * @return The calculated tax amount.
     */
    public double getTax() {
        return getSubTotal() * SALES_TAX;
    }

    /**
     * Calculates the grand total of the carts, including subtotal and tax.
     * @return The total amount.
     */
    public double getTotal() {
        return getSubTotal() + getTax();
    }

    /**
     * Displays all selected products, their quantities, taxes, and the total price.
     */
    public void displayCartDetails() {
        if (items.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }
        System.out.println("\n YOUR SHOPPING CART DETAILS:");
        for (CartItem item : items) {
            System.out.println(item.toString());
        }
        System.out.println("--------------------------");
        System.out.printf("Subtotal: $%.2f%n", getSubTotal());
        System.out.printf("Tax (%.0f%%): $%.2f%n", SALES_TAX * 100, getTax());
        System.out.printf("Total:    $%.2f%n", getTotal());
        System.out.println("--------------------------");
    }

    /**
     * Clears all items from the cart.
     */
    public void clearCart() {
        items.clear();
        System.out.println("Cart has been cleared.");
    }

    /**
     * Returns true if the cart is empty, false otherwise.
     * @return boolean indicating if the cart is empty.
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Returns the list of cart items.
     * @return A List of CartItem objects.
     */
    public List<CartItem> getItems() {
        return new ArrayList<>(items); // Return a copy
    }
    /**
     * Completes the checkout process for the current cart contents.
     *
     * @param customer The Customer object placing the order (must be logged in)
     * @param deliveryMethod The delivery option selected ("mail" or "pickup")
     * @return A String containing either:
     *         - The order confirmation details if successful, or
     *         - An error message if checkout fails
     */
    public String checkout(Customer customer, String deliveryMethod) {
        return OrderService.placeOrder(customer, this, deliveryMethod);
    }
}

