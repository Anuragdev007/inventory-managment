package com.inventory.Controller;

import com.inventory.Entites.Vendor;
import com.inventory.Repo.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class VendorController {
    @Autowired
    private VendorRepository vendorRepository;

    // Serve the vendor management page
    @GetMapping("/vendors")
    public String getVendorPage(Model model) {
        model.addAttribute("vendors", vendorRepository.findAll());
        return "vendor";
    }

    @PostMapping("/vendor/add")
    public String addVendor(@RequestParam("name") String name,
                            @RequestParam("email") String email,
                            @RequestParam("phone") String phone) {

        Vendor vendor = new Vendor();
        vendor.setName(name);
        vendor.setEmail(email);
        vendor.setPhoneNumber(phone);

        vendorRepository.save(vendor);
        return "redirect:/vendors";
    }

    @PostMapping("/vendor/delete/{id}")
    public ResponseEntity<?> deleteVendor(@PathVariable Long id, @RequestParam("_method") String method) {
        if ("delete".equalsIgnoreCase(method)) {
            Optional<Vendor> optionalVendor = vendorRepository.findById(id);
            if (!optionalVendor.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vendor not found");
            }
            vendorRepository.deleteById(id);
            return ResponseEntity.ok("Vendor deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Method Not Allowed");
    }


    @PostMapping("/vendor/update")
    public String updateVendor(@RequestParam("id") Long id,
                               @RequestParam("name") String name,
                               @RequestParam("email") String email,
                               @RequestParam("phone") String phone) {
        Vendor existingVendor = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        existingVendor.setName(name);
        existingVendor.setEmail(email);
        existingVendor.setPhoneNumber(phone);

        vendorRepository.save(existingVendor);
        return "redirect:/vendors";
    }
}
