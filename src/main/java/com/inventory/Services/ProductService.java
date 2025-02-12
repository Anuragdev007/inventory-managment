//package com.inventory.Services;
//
//import com.inventory.Entites.Product;
//import com.inventory.Entites.Vendor;
//import com.inventory.Repo.ProductRepository;
//import com.inventory.Repo.VendorRepository;
//import com.opencsv.CSVReader;
//import com.opencsv.exceptions.CsvValidationException;
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.io.InputStreamReader;
//
//@Service
//public class ProductService {
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private VendorRepository vendorRepository;
//
//    @Autowired
//    private JavaMailSender mailSender;
//
//    public void importProducts(MultipartFile file) throws IOException, MessagingException, CsvValidationException {
//        String fileName = file.getOriginalFilename();
//        if (fileName.endsWith(".xlsx") || fileName.endsWith(".xls")) {
//            importFromExcel(file);
//        } else if (fileName.endsWith(".csv")) {
//            importFromCsv(file);
//        } else {
//            throw new IOException("Invalid file format. Please upload Excel or CSV file.");
//        }
//    }
//
//    private void importFromExcel(MultipartFile file) throws IOException, MessagingException {
//        Workbook workbook = new XSSFWorkbook(file.getInputStream());
//        Sheet sheet = workbook.getSheetAt(0); // Get the first sheet
//
//        for (Row row : sheet) {
//            if (row.getRowNum() == 0) continue; // Skip header row
//
//            // Read product details from the Excel row
//            String productName = getCellStringValue(row.getCell(0));
//            int quantity = (int) getCellNumericValue(row.getCell(1));
//            String productSku = getCellStringValue(row.getCell(2));
//            String productImage = getCellStringValue(row.getCell(3));
//            int threshold = (int) getCellNumericValue(row.getCell(4));
//
//            // Read vendor details
//            String vendorName = getCellStringValue(row.getCell(5));
//            String vendorEmail = getCellStringValue(row.getCell(6));
//            String vendorPhone = getCellStringValue(row.getCell(7));
//
//            // Check if the vendor already exists in the database
//            Vendor vendor = vendorRepository.findByName(vendorName);
//            if (vendor == null) {
//                // Create new vendor if it doesn't exist
//                vendor = new Vendor();
//                vendor.setName(vendorName);
//                vendor.setEmail(vendorEmail);
//                vendor.setPhoneNumber(vendorPhone);
//                vendorRepository.save(vendor);
//            }
//
//            // Create and save the product
//            Product product = new Product();
//            product.setName(productName);
//            product.setQuantity(quantity);
//            product.setSku(productSku);
//            product.setImage(productImage);
//            product.setThreshold(threshold);
//            product.setVendor(vendor);
//
//            productRepository.save(product);
//
//            // Check if the product's quantity is less than the threshold and trigger an email
//            if (quantity < threshold) {
//                sendLowStockEmail(vendor, product);
//            }
//        }
//        workbook.close();
//    }
//
//    // Helper method to get string value from a cell
//    private String getCellStringValue(Cell cell) {
//        if (cell.getCellType() == CellType.STRING) {
//            return cell.getStringCellValue();
//        } else if (cell.getCellType() == CellType.NUMERIC) {
//            return String.valueOf((int) cell.getNumericCellValue());
//        }
//        return "";
//    }
//
//    // Helper method to get numeric value from a cell
//    private double getCellNumericValue(Cell cell) {
//        if (cell.getCellType() == CellType.NUMERIC) {
//            return cell.getNumericCellValue();
//        } else if (cell.getCellType() == CellType.STRING) {
//            return Double.parseDouble(cell.getStringCellValue());
//        }
//        return 0;
//    }
//
//
//    private void importFromCsv(MultipartFile file) throws IOException, CsvValidationException, MessagingException {
//        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
//            String[] values;
//            csvReader.readNext(); // Skip header
//
//            while ((values = csvReader.readNext()) != null) {
//                String productName = values[0];
//                int quantity = Integer.parseInt(values[1]);
//                String productSku = values[2];
//                String productImage = values[3];
//                int threshold = Integer.parseInt(values[4]);
//
//                String vendorName = values[5];
//                String vendorEmail = values[6];
//                String vendorPhone = values[7];
//
//                // Check if vendor exists
//                Vendor vendor = vendorRepository.findByName(vendorName);
//                if (vendor == null) {
//                    vendor = new Vendor();
//                    vendor.setName(vendorName);
//                    vendor.setEmail(vendorEmail);
//                    vendor.setPhoneNumber(vendorPhone);
//                    vendorRepository.save(vendor);
//                }
//
//                // Save product
//                Product product = new Product();
//                product.setName(productName);
//                product.setQuantity(quantity);
//                product.setSku(productSku);
//                product.setImage(productImage);
//                product.setThreshold(threshold);
//                product.setVendor(vendor);
//
//                productRepository.save(product);
//
//                // Check threshold and send email
//                if (quantity < threshold) {
//                    sendLowStockEmail(vendor, product);
//                }
//            }
//        }
//    }
//
//    private void sendLowStockEmail(Vendor vendor, Product product) throws MessagingException {
//        String subject = "Low Stock Alert: " + product.getName();
//        String message = "Dear " + vendor.getName() + ",\n\n" +
//                "The product " + product.getName() + " (SKU: " + product.getSku() + ") has fallen below the threshold level.\n" +
//                "Current quantity: " + product.getQuantity() + "\n" +
//                "Threshold: " + product.getThreshold() + "\n\n" +
//                "Please restock the product.\n\n" +
//                "Regards,\nInventory Management System";
//
//        MimeMessage mimeMessage = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
//        helper.setTo(vendor.getEmail());
//        helper.setSubject(subject);
//        helper.setText(message);
//
//        mailSender.send(mimeMessage);
//    }
//}


package com.inventory.Services;

import com.inventory.Entites.Product;
import com.inventory.Entites.Vendor;
import com.inventory.Repo.ProductRepository;
import com.inventory.Repo.VendorRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private JavaMailSender mailSender;

    private static final int PAGE_SIZE = 10; // Define the size of each page (chunk)



    private void sendLowStockEmail(Vendor vendor, Product product) throws MessagingException {
        String subject = "Low Stock Alert: " + product.getName();
        String message = "Dear " + vendor.getName() + ",\n\n" +
                "The product " + product.getName() + " (SKU: " + product.getSku() + ") has fallen below the threshold level.\n" +
                "Current quantity: " + product.getQuantity() + "\n" +
                "Threshold: " + product.getThreshold() + "\n\n" +
                "Please restock the product.\n\n" +
                "Regards,\nInventory Management System";

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(vendor.getEmail());
        helper.setSubject(subject);
        helper.setText(message);

        mailSender.send(mimeMessage);
    }

    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAllByOrderByCreatedAtDesc(pageable);
    }


    public void importProducts(MultipartFile file) throws IOException {
        String fileName = Optional.ofNullable(file.getOriginalFilename())
                .orElseThrow(() -> new IOException("File name cannot be null."));

        // Determine file type and process accordingly
        if (fileName.endsWith(".xlsx") || fileName.endsWith(".xls")) {
            processExcelFile(file.getInputStream());
        } else if (fileName.endsWith(".csv")) {
            processCsvFile(file.getInputStream());
        } else {
            throw new IOException("Invalid file format. Please upload an Excel or CSV file.");
        }
    }

    // Process Excel file
    private void processExcelFile(InputStream inputStream) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0); // Read the first sheet
            List<Product> products = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip the header row
                products.add(processRow(row));
            }

            // Save all products
            productRepository.saveAll(products);
        }
    }

    // Process CSV file
    private void processCsvFile(InputStream inputStream) throws IOException {
        List<Product> products = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                if (isHeader) { // Skip the header row
                    isHeader = false;
                    continue;
                }

                String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (fields.length < 9) {
                    System.err.println("Skipping row due to insufficient fields: " + line);
                    continue; // Ensure sufficient columns
                }

                products.add(processCsvFields(fields));
            }
        } catch (Exception e) {
            throw new IOException("Error processing CSV file: " + e.getMessage(), e);
        }

        // Save all products
        productRepository.saveAll(products);
    }

    // Process a single Excel row into a Product
    private Product processRow(Row row) {
        String productName = getCellStringValue(row.getCell(0));
        int quantity = (int) getCellNumericValue(row.getCell(1));
        String productSku = getCellStringValue(row.getCell(2));
        String productImage = getCellStringValue(row.getCell(3));
        int threshold = (int) getCellNumericValue(row.getCell(4));
        String size = getCellStringValue(row.getCell(5));

        String vendorName = getCellStringValue(row.getCell(6));
        String vendorEmail = getCellStringValue(row.getCell(7));
        String vendorPhone = getCellStringValue(row.getCell(8));

        return mapProduct(productName, quantity, productSku, productImage, threshold, size, vendorName, vendorEmail, vendorPhone);
    }

    // Process CSV fields into a Product
    private Product processCsvFields(String[] fields) {
        String productName = fields[0].trim().replaceAll("^\"|\"$", "");
        int quantity = parseInteger(fields[1].trim());
        String productSku = fields[2].trim().replaceAll("^\"|\"$", "");
        String productImage = fields[3].trim().replaceAll("^\"|\"$", "");
        int threshold = parseInteger(fields[4].trim());
        String size = fields[5].trim().replaceAll("^\"|\"$", "");

        String vendorName = fields[6].trim().replaceAll("^\"|\"$", "");
        String vendorEmail = fields[7].trim().replaceAll("^\"|\"$", "");
        String vendorPhone = fields[8].trim().replaceAll("^\"|\"$", "");

        return mapProduct(productName, quantity, productSku, productImage, threshold, size, vendorName, vendorEmail, vendorPhone);
    }

    // Map product and vendor details
    private Product mapProduct(String productName, int quantity, String productSku,
                               String productImage, int threshold, String size,
                               String vendorName, String vendorEmail, String vendorPhone) {

        Vendor vendor = Optional.ofNullable(vendorRepository.findByName(vendorName))
                .orElseGet(() -> createVendor(vendorName, vendorEmail, vendorPhone));

        Product product = new Product();
        product.setName(productName);
        product.setQuantity(quantity);
        product.setSku(productSku);
        product.setImage(productImage);
        product.setThreshold(threshold);
        product.setSize(size);
        product.setVendor(vendor);

        return product;
    }

    // Create a new Vendor and save it
    private Vendor createVendor(String name, String email, String phone) {
        Vendor vendor = new Vendor();
        vendor.setName(name);
        vendor.setEmail(email);
        vendor.setPhoneNumber(phone);

        return vendorRepository.save(vendor);
    }

    // Helper methods to handle cell value parsing
    private String getCellStringValue(Cell cell) {
        return cell != null && cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : "";
    }

    private double getCellNumericValue(Cell cell) {
        return cell != null && cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0;
    }

    private int parseInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format for value: " + value);
            return 0; // or handle as needed
        }
    }




}
