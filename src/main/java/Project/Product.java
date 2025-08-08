package Project;

public class Product {
    private String productID;
    private String productName;
    private String productDescription;
    private double regularPrice;
    private double salesPrice;
    /**
     * Constructs a new Product with the given details.
     * @param ID A unique identifier for the product.
     * @param name The name of the product.
     * @param description A brief description of the product.
     * @param price The standard price of the product.
     * @param discountedPrice The discounted price of the product, if applicable.
     */
    public Product(String ID, String name, String description, double price,double discountedPrice) {
        productID = ID;
        productName = name;
        productDescription = description;
        regularPrice = price;
        salesPrice = discountedPrice;
    }
    // Getters
    public String getProductID() {
        return productID;
    }
    public String getProductName() {
        return productName;
    }
    public String getProductDescription() {
        return productDescription;
    }
    public double getRegularPrice() {
        return regularPrice;
    }
    public double getSalesPrice() {
        return salesPrice;
    }
    /**
     * Returns the effective price of the product (sales price if available and lower, otherwise regular price).
     * @return The price to be used for calculations.
     */
    public double getPrice(){
        if (salesPrice > 0 && salesPrice < regularPrice){
            return salesPrice;
        }
        return regularPrice;
    }
    @Override
    public String toString() {
        return "ID: " + productID + ", Name: "+ productName + ", Description: " + productDescription + ", Reg. Price: $ " + String.format("%.2f", regularPrice) + (salesPrice > 0 && salesPrice < regularPrice ? ", Sale Price: $" + String.format("%.2f", salesPrice) : "");
    }
}

