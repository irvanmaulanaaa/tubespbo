package Product;

public class Product {
 
    private String productCode;
    private String productName;
    private double productPrice; 
    private int productStock;    
    private String productDescription;

    public Product(String productCode, String productName, double productPrice, int productStock,
                   String productDescription) {

        this.productCode = productCode;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productStock = productStock;
        this.productDescription = productDescription;
    }
    
    public String getProductCode() {
        return productCode;
    }

    public String getProductName() {
        return productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public int getProductStock() {
        return productStock;
    }

    public String getProductDescription() {
        return productDescription;
    }
}
