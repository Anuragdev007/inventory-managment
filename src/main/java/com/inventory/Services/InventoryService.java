//package com.inventory.Services;
//
//import com.inventory.Entites.OrderTrackingResponse;
//import com.inventory.Entites.Product;
//import com.inventory.Entites.Vendor;
//import com.inventory.Repo.ProductRepository;
//import com.inventory.Repo.VendorRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.List;
//
//
//@Service
//public class InventoryService {
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private EmailService emailService;
//
//    @Autowired
//    private VendorRepository vendorRepository;
//
//    public void checkAndSendInventoryAlert(Product product) {
//        System.out.println("Checking inventory for product: " + product.getName());
//        System.out.println("Current quantity: " + product.getQuantity());
//        System.out.println("Threshold: " + product.getThreshold());
//
//        if (product.getQuantity() < product.getThreshold()) {
//            System.out.println("Quantity is below threshold. Sending email...");
//            emailService.sendInventoryAlert(product.getVendor().getEmail(),
//                    product.getName(),
//                    product.getQuantity(),
//                    product.getThreshold());
//        } else {
//            System.out.println("Quantity is above threshold. No email sent.");
//        }
//    }
//
//
//
//    // Add product method that also checks the inventory status
//    public Product addProduct(String name, int quantity, int threshold, Long vendorId) {
//        // Find the vendor by ID
//        Vendor vendor = vendorRepository.findById(vendorId)
//                .orElseThrow(() -> new RuntimeException("Vendor not found"));
//
//        // Create a new product
//        Product product = new Product();
//        product.setName(name);
//        product.setQuantity(quantity);
//        product.setThreshold(threshold);
//        product.setVendor(vendor);
//
//        // Save the product
//        productRepository.save(product);
//
//        // Check and send inventory alert if needed
//        checkAndSendInventoryAlert(product);
//
//        return product;
//    }
//
//
//    // Delete product method
//    public void deleteProduct(Long productId) {
//        productRepository.deleteById(productId);
//    }
//
//
//    public List<Product> searchProducts(String query) {
//        return productRepository.findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(query, query);
//    }
//
//    public InventoryService(ProductRepository productRepository) {
//        this.productRepository = productRepository;
//    }
//
//    public void updateInventory(String trackingId) {
//        DelhiveryService delhiveryService = new DelhiveryService(new RestTemplate());
//        OrderTrackingResponse trackingResponse = delhiveryService.trackOrder(trackingId);
//
//        for (OrderTrackingResponse.OrderTrackingDetails details : trackingResponse.getTrackingDetails()) {
//            if ("Delivered".equalsIgnoreCase(details.getStatus())) {
//                // Adjust inventory logic here
//                // Example: Reduce the stock for the delivered product
//                productRepository.decreaseStock(trackingId);
//            } else if ("Returned".equalsIgnoreCase(details.getStatus())) {
//                // Example: Restock returned items
//                productRepository.increaseStock(trackingId);
//            }
//        }
//    }
//}



package com.inventory.Services;

import com.inventory.Entites.Product;
import com.inventory.Entites.Vendor;
import com.inventory.Repo.ProductRepository;
import com.inventory.Repo.VendorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final ProductRepository productRepository;
    private final EmailService emailService;
    private final VendorRepository vendorRepository;


    @Autowired
    public InventoryService(ProductRepository productRepository, EmailService emailService, VendorRepository vendorRepository) {
        this.productRepository = productRepository;
        this.emailService = emailService;
        this.vendorRepository = vendorRepository;
    }

    public void checkAndSendInventoryAlert(Product product) {
        System.out.println("Checking inventory for product: " + product.getName());
        System.out.println("Current quantity: " + product.getQuantity());
        System.out.println("Threshold: " + product.getThreshold());

        if (product.getQuantity() < product.getThreshold()) {
            System.out.println("Quantity is below threshold. Sending email...");
            emailService.sendInventoryAlert(
                    product.getVendor().getEmail(),
                    product.getName(),

                    product.getQuantity(),
                    product.getThreshold(),
                    product.getSize()
            );
        } else {
            System.out.println("Quantity is above threshold. No email sent.");
        }
    }

    public Product addProduct(String size,String name, int quantity, int threshold, Long vendorId) {
        // Find the vendor by ID
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        // Create and configure a new product
        Product product = new Product();
        product.setSize(size);
        product.setName(name);
        product.setQuantity(quantity);
        product.setThreshold(threshold);
        product.setVendor(vendor);

        // Save the product
        Product savedProduct = productRepository.save(product);

        // Check and send inventory alert if needed
        checkAndSendInventoryAlert(savedProduct);

        return savedProduct;
    }

    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(productId);
    }
    public List<Product> searchProducts(String query) {
        // Ensure query is not null and trimmed
        query = query != null ? query.trim() : "";

        // If the query is empty, return all products
        if (query.isEmpty()) {
            return productRepository.findAll();
        }

        // Search for products by name or SKU
        return productRepository.findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(query, query);
    }





    public Page<Product> findPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable);
    }




}