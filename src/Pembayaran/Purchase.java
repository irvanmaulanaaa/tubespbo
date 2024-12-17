package Pembayaran;

public interface Purchase {

    public void processPayment(String productCode, int quantityToBuy);
    public boolean payWithCOD(double totalAmount);
    
}
