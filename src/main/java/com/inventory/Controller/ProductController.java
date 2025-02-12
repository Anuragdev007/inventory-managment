package com.inventory.Controller;


import com.inventory.Entites.Product;
import com.inventory.Entites.Vendor;
import com.inventory.Repo.ProductRepository;
import com.inventory.Repo.VendorRepository;
import com.inventory.Services.EmailService;
import com.inventory.Services.InventoryService;
import com.inventory.Services.ProductService;
import com.inventory.dto.ProductDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
//
//@Controller
//public class ProductController {
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private VendorRepository vendorRepository;
//
//    @Autowired
//    private InventoryService inventoryService;
//
//    @Autowired
//    private ProductService productService;
//
//
//    @Autowired
//    private EmailService emailService;
//
//
//
//
//    // Load the home page with products and vendors
//    @GetMapping("/")
//    public String getHomePage(Model model) {
//        List<Product> products = productRepository.findAll();
//        List<Vendor> vendors = vendorRepository.findAll();
//
//        // Add both products and vendors to the model
//        model.addAttribute("products", products);
//        model.addAttribute("vendors", vendors);
//
//        return "index"; // Thymeleaf template
//    }
//
//
//
//    @GetMapping("/products/new")
//    public String showAddProductForm(Model model) {
//        List<Product> products = productRepository.findAll();
//        List<Vendor> vendors = vendorRepository.findAll();
//
//        // Add both products and vendors to the model
//        model.addAttribute("products", products);
//        model.addAttribute("vendors", vendors);
//        return "newproduct"; // This should match the filename of your HTML template
//    }
//
//
//    // Add a new product
//    @PostMapping("/inventory/add")
//    public String addProduct(@RequestParam("name") String name,
//                             @RequestParam("quantity") int quantity,
//                             @RequestParam("threshold") int threshold,
//                             @RequestParam("sku") String sku,
//                             @RequestParam("image") MultipartFile imageFile,
//                             @RequestParam("vendorId") Long vendorId) {
//
//        // Find the vendor by ID
//        Vendor vendor = vendorRepository.findById(vendorId)
//                .orElseThrow(() -> new RuntimeException("Vendor not found"));
//
//        // Create the product and link it with the vendor
//        Product product = new Product();
//        product.setName(name);
//        product.setQuantity(quantity);
//        product.setThreshold(threshold);
//        product.setSku(sku);  // Set the SKU
//        product.setVendor(vendor);
//        // Save the image file
//        if (!imageFile.isEmpty()) {
//            String imageName = imageFile.getOriginalFilename();
//            Path imagePath = Paths.get("uploads/images/" + imageName);
//            try {
//                Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
//                product.setImage(imageName);  // Set the image file name in the product
//            } catch (IOException e) {
//                e.printStackTrace();
//                // Handle image upload error
//            }
//        }
//
//        // Save the product to the repository
//        productRepository.save(product);
//
//        // Call the method to check inventory and send an email if needed
//        inventoryService.checkAndSendInventoryAlert(product);
//
//        // Redirect to home page
//        return "redirect:/";
//    }
//
//
//
//
//
//    // Delete product
//    @DeleteMapping("/inventory/delete/{id}")
//    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
//        Optional<Product> existingProduct = productRepository.findById(id);
//        if (existingProduct.isPresent()) {
//            productRepository.deleteById(id);
//            return ResponseEntity.ok("Product deleted successfully");
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
//    }
//
//    @Transactional
//    @PutMapping("/inventory/update/{id}")
//    public ResponseEntity<?> updateProduct(@PathVariable("id") Long productId,
//                                           @RequestBody ProductDTO updatedProductDTO) {
//        Optional<Product> existingProductOpt = productRepository.findById(productId);
//
//        if (!existingProductOpt.isPresent()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
//        }
//
//        Product existingProduct = existingProductOpt.get();
//
//        // Update product fields only if they are provided (non-null)
//        if (updatedProductDTO.getName() != null) {
//            existingProduct.setName(updatedProductDTO.getName());
//        }
//        if (updatedProductDTO.getSku() != null) {
//            existingProduct.setSku(updatedProductDTO.getSku());
//        }
//        if (updatedProductDTO.getQuantity() != null) {
//            existingProduct.setQuantity(updatedProductDTO.getQuantity());
//        }
//        if (updatedProductDTO.getThreshold() != null) {
//            existingProduct.setThreshold(updatedProductDTO.getThreshold());
//        }
//        if (updatedProductDTO.getVendorId() != null) {
//            Vendor vendor = vendorRepository.findById(updatedProductDTO.getVendorId())
//                    .orElseThrow(() -> new RuntimeException("Vendor not found"));
//            existingProduct.setVendor(vendor);
//        }
//
//        // Save the updated product to the database
//        productRepository.save(existingProduct);
//
//        // Check if quantity is below threshold
//        if (existingProduct.getQuantity() < existingProduct.getThreshold()) {
//            emailService.sendLowStockEmail(existingProduct);
//        }
//
//        return ResponseEntity.ok("Product updated successfully!");
//    }
//
//    @PostMapping("/import")
//    public String importProducts(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
//        try {
//            if (file.isEmpty()) {
//                redirectAttributes.addFlashAttribute("errorMessage", "File is empty");
//                return "redirect:/"; // Redirect to homepage with error
//            }
//
//            productService.importProducts(file);
//            redirectAttributes.addFlashAttribute("successMessage", "Products imported successfully");
//            return "redirect:/"; // Redirect to homepage after successful import
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Error importing products: " + e.getMessage());
//            return "redirect:/"; // Redirect to homepage with error
//        }
//    }
//
//
//
//}




import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private EmailService emailService;

    // Load the home page with paginated products and all vendors
    @GetMapping("/")
    public String getHomePage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "name") String sortBy, // Field to sort by
            @RequestParam(defaultValue = "asc") String sortDir, // Sort direction: asc or desc
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.by(sortBy).with(Sort.Direction.fromString(sortDir))));

        Page<Product> productPage = productRepository.findAll(pageable);

        // Fetch all vendors (not paginated)
        List<Vendor> vendors = vendorRepository.findAll();

        // Add paginated products and vendors to the model
        model.addAttribute("productPage", productPage);
        model.addAttribute("vendors", vendors);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("sortBy", sortBy);  // Add sorting field to model
        model.addAttribute("sortDir", sortDir);  // Add sorting direction to model

        return "index"; // Thymeleaf template
    }





    @GetMapping("/products/new")
    public String showAddProductForm(Model model) {
        List<Vendor> vendors = vendorRepository.findAll();
        model.addAttribute("vendors", vendors);
        return "newproduct"; // This should match the filename of your HTML template
    }

    // Add a new product
    @PostMapping("/inventory/add")
    public String addProduct(@RequestParam("name") String name,
                             @RequestParam("size") String size,
                             @RequestParam("quantity") int quantity,
                             @RequestParam("threshold") int threshold,
                             @RequestParam("sku") String sku,
                             @RequestParam("image") MultipartFile imageFile,
                             @RequestParam("vendorId") Long vendorId) {

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        Product product = new Product();
        product.setName(name);
        product.setSize(size);
        product.setQuantity(quantity);
        product.setThreshold(threshold);
        product.setSku(sku);
        product.setVendor(vendor);

        if (!imageFile.isEmpty()) {
            String imageName = imageFile.getOriginalFilename();
            Path imagePath = Paths.get("uploads/images/" + imageName);
            try {
                Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                product.setImage(imageName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        productRepository.save(product);
        inventoryService.checkAndSendInventoryAlert(product);

        return "redirect:/";
    }

    // Delete product
    @DeleteMapping("/inventory/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            productRepository.deleteById(id);
            return ResponseEntity.ok("Product deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
    }


    @Transactional
    @PutMapping("/inventory/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long productId,
                                           @RequestBody ProductDTO updatedProductDTO) {
        Optional<Product> existingProductOpt = productRepository.findById(productId);

        if (!existingProductOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        Product existingProduct = existingProductOpt.get();

        if (updatedProductDTO.getName() != null) {
            existingProduct.setName(updatedProductDTO.getName());
        }

        if (updatedProductDTO.getSku() != null) {
            existingProduct.setSku(updatedProductDTO.getSku());
        }
        if (updatedProductDTO.getQuantity() != null) {
            existingProduct.setQuantity(updatedProductDTO.getQuantity());
        }
        if (updatedProductDTO.getThreshold() != null) {
            existingProduct.setThreshold(updatedProductDTO.getThreshold());
        }
        if (updatedProductDTO.getVendorId() != null) {
            Vendor vendor = vendorRepository.findById(updatedProductDTO.getVendorId())
                    .orElseThrow(() -> new RuntimeException("Vendor not found"));
            existingProduct.setVendor(vendor);
        }

        productRepository.save(existingProduct);

        if (existingProduct.getQuantity() < existingProduct.getThreshold()) {
            emailService.sendLowStockEmail(existingProduct);
        }

        return ResponseEntity.ok("Product updated successfully!");
    }

    @PostMapping("/import")
    public String importProducts(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "File is empty");
                return "redirect:/";
            }

            productService.importProducts(file);
            redirectAttributes.addFlashAttribute("successMessage", "Products imported successfully");
            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error importing products: " + e.getMessage());
            return "redirect:/";
        }
    }

    @PostMapping("/inventory/bulk-delete")
    @ResponseBody
    public ResponseEntity<String> bulkDeleteProducts(@RequestBody List<Long> productIds) {
        try {
            productRepository.deleteAllById(productIds);
            return ResponseEntity.ok("Products deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting products: " + e.getMessage());
        }
    }

}


