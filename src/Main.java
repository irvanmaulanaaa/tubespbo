import models.*;

import java.io.Console;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Owner currentOwner = null;
        Customer currentCustomer = null;

        Console console = System.console();
        if (console == null) {
            System.out.println("Console tidak tersedia.");
            return;
        }

        boolean isRunning = true;
        while (isRunning) {
            System.out.println("\n==== Selamat Datang di Toko Madura! ====");
            System.out.println("Silakan pilih opsi:");
            System.out.println("1. Login");
            System.out.println("2. Register (Customer)");
            System.out.println("3. Tampilkan Produk");
            System.out.println("4. Keluar");
            System.out.print("Pilihan: ");
            int userChoice = scanner.nextInt();
            scanner.nextLine();

            switch (userChoice) {
                case 1:

                    System.out.println("==== Login ====");
                    System.out.print("Username: ");
                    String loginUsername = scanner.nextLine();
                    char[] loginPasswordArray = console.readPassword("Password: ");
                    String loginPassword = new String(loginPasswordArray);

                    if (loginUsername.equalsIgnoreCase("owner")) {
                        // Login sebagai Owner) 
                        currentOwner = new Owner(loginUsername, loginPassword);
                        if (currentOwner.login(loginUsername, loginPassword)) {
                            System.out.println("\nLogin berhasil sebagai Owner!");
                            ownerMenu(currentOwner, scanner); 
                        } else {
                            System.out.println("Login gagal! Cek username dan password.");
                        }
                    } else {
                        // Login sebagai Customer
                        currentCustomer = new Customer(loginUsername, loginPassword, null);
                        if (currentCustomer.login(loginUsername, loginPassword)) {
                            System.out.println("\nLogin berhasil sebagai Customer!");
                            customerMenu(currentCustomer, scanner);
                        } else {
                            System.out.println("Login gagal! Cek username dan password.");
                        }
                    }
                    break;

                case 2:
                    System.out.println("==== Registrasi Customer ====");
                    System.out.print("Customer ID: ");
                    String registerId = scanner.nextLine();
                    System.out.print("Username: ");
                    String registerUsername = scanner.nextLine();
                    char[] registerPasswordArray = console.readPassword("Password: ");
                    String registerPassword = new String(registerPasswordArray);

                    Customer.register(registerUsername, registerPassword, registerId);
                    System.out.println("Silakan login untuk menggunakan aplikasi!.");
                    break;

                case 3:
                    Owner owner = new Owner("defaultUsername", "defaultPassword");
                    owner.viewProduct();
                    break;

                case 4:                   
                    System.out.println("Terima kasih telah menggunakan Toko Madura. Sampai jumpa!");
                    isRunning = false;
                    break;

                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
                    break;
            }
        }
    }

    private static void ownerMenu(Owner owner, Scanner scanner) {
        int choice;
        do {
            System.out.println("\n==== Menu Owner ====");
            System.out.println("1. Lihat Produk");
            System.out.println("2. Tambah Produk");
            System.out.println("3. Update Produk");
            System.out.println("4. Hapus Produk");
            System.out.println("5. Keluar");
            System.out.print("Pilihan: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    owner.viewProduct();
                    break;

                case 2:
                    System.out.print("Masukkan Kode Produk: ");
                    String code = scanner.nextLine();

                    System.out.print("Masukkan Nama Produk: ");
                    String name = scanner.nextLine();

                    System.out.print("Masukkan Harga Produk: ");
                    double price = scanner.nextDouble();
                    scanner.nextLine();
                    if (price < 0)
                        System.out.println("Harga tidak boleh negatif.");

                    System.out.print("Masukkan Stok Produk: ");
                    int stock = scanner.nextInt();
                    scanner.nextLine();
                    if (stock < 0)
                        System.out.println("Stok tidak boleh negatif.");

                    System.out.print("Masukkan Deskripsi Produk: ");
                    String description = scanner.nextLine();
                    if (description.trim().isEmpty()) {
                        System.out.println("Deskripsi tidak boleh kosong. Produk tidak ditambahkan.");
                        break;
                    }
                    owner.insertProduct(new Product(name, price, stock, description, code));
                    break;

                case 3:
                    System.out.print("Masukkan Kode Produk yang ingin diupdate: ");
                    String updateCode = scanner.nextLine();
                    Product productToUpdate = Product.getProductByCode(updateCode);

                    System.out.println(productToUpdate);

                    System.out.print("Masukkan Nama Produk Baru (-1 untuk tidak diubah): ");
                    String newName = scanner.nextLine();
                    if (newName.equals("-1")) {
                        newName = productToUpdate.getProductName();
                    }

                    System.out.print("Masukkan Harga Produk Baru (-1 untuk tidak diubah): ");
                    double newPrice = scanner.nextDouble();
                    if (newPrice == -1) {
                        newPrice = productToUpdate.getProductPrice();
                    }

                    scanner.nextLine();

                    System.out.print("Masukkan Stok Produk (-1 untuk tidak diubah): ");
                    int newStock = scanner.nextInt();
                    if (newStock < 0) {
                        if (newStock == -1) {
                            newStock = productToUpdate.getProductStock();
                            break;
                        } 

                        System.out.println("stok tidak boleh negatif");

                        newPrice = scanner.nextInt();
                    }

                    scanner.nextLine();

                    System.out.print("Masukkan Deskripsi Baru (-1 untuk tidak diubah): ");
                    String newDescription = scanner.nextLine();
                    if (newDescription.equals("-1")) {
                        newDescription = productToUpdate.getProductDescription();
                    }

                    productToUpdate.setProductName(newName);
                    productToUpdate.setProductPrice(newPrice);
                    productToUpdate.setProductStock(newStock);
                    productToUpdate.setProductDescription(newDescription);

                    owner.updateProduct(productToUpdate);
                    break;

                case 4:
                    System.out.print("Masukkan Kode Produk yang ingin dihapus: ");
                    String deleteCode = scanner.nextLine();
                    Product deleteProduct = Product.getProductByCode(deleteCode);
                    owner.removeProduct(deleteProduct);
                    break;

                case 5:
                    System.out.println("Berhasil keluar dari menu Owner!.");
                    break;

                default:
                    System.out.println("Pilihan tidak valid.");
            }
        } while (choice != 5);
    }

    // Menu untuk Customer
    private static void customerMenu(Customer customer, Scanner scanner) {
        int choice;
        System.out.println("\nSelamat datang di Toko Madura, " + customer.getUsername() + "!");
        do {
            System.out.println("\n==== Menu Customer ====");
            System.out.println("1. Lihat Produk");
            System.out.println("2. Lihat Keranjang Belanja");
            System.out.println("3. Tambah Produk ke Keranjang");
            System.out.println("4. Hapus Produk dari Keranjang");
            System.out.println("5. Keluar");
            System.out.print("Pilihan: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    Owner owner = new Owner("defaultUsername", "defaultPassword");
                    owner.viewProduct();
                    break;

                case 2:
                    customer.viewCart();
                    break;

                case 3:
                    System.out.print("Masukkan Kode Produk: ");
                    String productCode = scanner.nextLine();
                    System.out.print("Masukkan Jumlah Produk: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine();

                    Product product = Product.getProductByCode(productCode);
                    if (product != null) {
                        customer.addOrder(product, quantity);
                    } else {
                        System.out.println("Produk tidak ditemukan.");
                    }
                    break;

                case 4:
                    System.out.print("Masukkan Kode Produk yang ingin dihapus dari Keranjang: ");
                    String removeCode = scanner.nextLine();
                    Product productToRemove = Product.getProductByCode(removeCode);
                    if (productToRemove != null) {
                        customer.removeOrder(productToRemove);
                    } else {
                        System.out.println("Produk tidak ditemukan di keranjang.");
                    }
                    break;

                case 5:
                    System.out.println("Berhasil keluar dari menu Customer!.");
                    break;

                default:
                    System.out.println("Pilihan tidak valid.");
            }
        } while (choice != 5);
    }
    
}
