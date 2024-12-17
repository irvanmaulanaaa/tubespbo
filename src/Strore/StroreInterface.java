package Strore;

import java.sql.SQLException;

import Product.Product;

public interface StroreInterface {
    public void insertProduct (Product product) throws SQLException;
    public void removeProduct (Product product) throws SQLException;
    public void updateProduct (Product product) throws SQLException;
    public void viewProduct   (Product product) throws SQLException;
    public abstract boolean login (String username, String password);
}
