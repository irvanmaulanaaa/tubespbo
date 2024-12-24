package interfaces;

import models.Product;
public interface CRUD {
    void insertProduct(Product product);
    void updateProduct(Product product);
    void removeProduct(Product product);
    void viewProduct();
}
