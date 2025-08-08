package Project;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the product catalog, holding a collection of available products.
 */
public class ProductListing {
    private List<Product> productList;
    /**
     * Constructor - makes a new empty Product list.
     */
    public ProductListing() {
        productList = new ArrayList<>();
    }

    /**
     * Adds a product to the list.
     * @param product The Product object to add.
     */
    public void addProduct(Product product) {
        if (product != null && !productList.contains(product)) {
            productList.add(product);
        }
    }

    /**
     * Retrieves a product from the list by its ID.
     * @param productID The ID of the product to retrieve.
     * @return The Product object if found, or null otherwise.
     */
    public Product getProductByID(String productID) {
        for (Product product : productList) {
            if (product.getProductID().equals(productID)) {
                return product;
            }
        }
        return null; // If product not found
    }
    /**
     * Displays the names, descriptions, regular prices, and sales prices of all products in the list.
     */
    public void displayProductList() {
        if (productList.isEmpty()) {
            System.out.println("There are no products in the list.");
            return;
        }
        System.out.println("PRODUCT LIST:");
        for( int i = 0; i <  productList.size(); i++ ) {
            Product pro = productList.get(i);
            System.out.println((i +1) +  ".  " + pro.toString());
        }

        }
        /**
         * Returns the list of all products in the product list.
         * @return A List of Product objects.
         */
    public List<Product> getAllProducts(){
        return new ArrayList<>(productList); //Returning a copy to prevent external modification
    }
}

