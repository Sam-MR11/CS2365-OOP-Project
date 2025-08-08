package Project;

/**
 * Represents a single item (product and quantity) within a shopping cart.
 */
public class CartItem {
    private Product product;
    private int quantity;

    /**
     * Constructs a new CartItem.
     * @param item The Product object being added to the cart.
     * @param number The quantity of this product.
     */
    public CartItem(Product item, int number) {
        product = item;
        quantity = number;
    }
    // Getters
    public Product getProduct(){
        return product;
    }

    public int getQuantity(){
        return quantity;
    }

    // Setters(to modify quantity)
    public void setQuantity(int quantity){
        if (quantity < 0){
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        this.quantity = quantity;
    }
    /**
     * Calculates the total price for the cart item (product price * quantity).
     * @return - returns The total price for this cart item.
     */
    public double getTotalPrice(){
        return product.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return quantity + " x " + product.getProductName() + " (@ $" + String.format("%.2f", product.getPrice()) + " each) = $" + String.format("%.2f", getTotalPrice()) ;
    }
}

