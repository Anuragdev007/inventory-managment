package com.inventory.Controller;

import com.inventory.Entites.Product;
import com.inventory.Services.InventoryService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {

    private final InventoryService inventoryService;

    public SearchController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/search/suggest")
    @ResponseBody
    public List<Product> searchProducts(@RequestParam String query) {
        // Call the service layer to search products by query
        return inventoryService.searchProducts(query);
    }

}
