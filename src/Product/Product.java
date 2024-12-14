package Product;

public class Product {

    private String productCode;
    private String productName;
    private String productPrice;
    private String productStock;
    private String productDescription;

    public Product(String productCode, String productName, String productPrice, String productStock,
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

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductStock() {
        return productStock;
    }

    public String getProductDescription() {
        return productDescription;
    }
}
