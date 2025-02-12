package com.inventory.Services;


import com.inventory.Entites.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;



@Service
public class EmailService {


    @Autowired
    private JavaMailSender mailSender;

    public void sendInventoryAlert(String toEmail, String productName, int quantity, int threshold,String size) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Inventory Alert: Low Stock for " + productName + size );
            message.setText("Dear Vendor,\n\nThe inventory for product '" + productName + "(" + size + ")"+"' is below the threshold."
                    + "\nCurrent Quantity: " + quantity
                    + "\nThreshold: " + threshold
                    + "\nPlease restock as soon as possible.\n\nBest regards,\nYour Inventory Management System");

            mailSender.send(message);
            System.out.println("Low stock email sent to " + toEmail);
        } catch (MailException e) {
            System.out.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendLowStockEmail(Product product) {
        String subject = "Low Stock Alert for " + product.getName() + product.getSize();
        String message = "Dear Vendor,\n\nThe product '" + product.getName() +product.getSize()+
                "' (SKU: " + product.getSku() + ") has a low stock level. " +
                "The current quantity is " + product.getQuantity() +
                ", which is below the threshold of " + product.getThreshold() + ".\n\n" +
                "Please restock the item as soon as possible.\n\nThank you!";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(product.getVendor().getEmail());  // Assuming the vendor email is set
        email.setSubject(subject);
        email.setText(message);

        mailSender.send(email);  // Send the email
    }

    public void sendThresholdAlert(Product product) {
        String subject = "Low Stock Alert for " + product.getName() + product.getSize();
        String message = "Dear Vendor,\n\nThe product '" + product.getName() +product.getSize()+
                "' (SKU: " + product.getSku() + ") has a low stock level. " +
                "The current quantity is " + product.getQuantity() +
                ", which is below the threshold of " + product.getThreshold() + ".\n\n" +
                "Please restock the item as soon as possible.\n\nThank you!";

        // Construct the email
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(product.getVendor().getEmail());  // Assuming the vendor's email is set
        email.setSubject(subject);
        email.setText(message);

        // Send the email
        mailSender.send(email);
    }



}


