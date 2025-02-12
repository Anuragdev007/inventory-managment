package com.inventory.Controller;

import com.inventory.Entites.Product;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
    private  final JavaMailSender mailSender;

    public EmailController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    @RequestMapping("/send-email")
    public String sendEmail(){
        try {
            SimpleMailMessage message =new SimpleMailMessage();
            message.setFrom("bhardwajanurag546@gmail.com");
            message.setTo("anuragbhardwaj1333@gmail.com");
            message.setSubject("test email");
            message.setText("hi this is test email");
            mailSender.send(message);
            return "sucess";

        }catch (Exception e){
            return e.getMessage();
        }


    }
    public void sendLowInventoryAlert(Product product) {
        String subject = "Low Inventory Alert";
        String message = "The inventory for product " + product.getName() + " (SKU: " + product.getSku() +
                ") is below the threshold. Current quantity: " + product.getQuantity() +
                ". Please reorder soon.";

        // Email to vendor or admin
        String recipientEmail = product.getVendor().getEmail();  // Assuming vendor has email
        sendEmail(recipientEmail, subject, message);
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);  // Send email
    }
}
