package com.inventory.Repo;


import com.inventory.Entites.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Custom query to fetch products ordered by createdAt descending (newest first)
    Page<Product> findAllByOrderByCreatedAtDesc(Pageable pageable);
    // Custom query to find products below threshold
    List<Product> findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(String name, String sku);

//    @Transactional
//    @Modifying
//    @Query("UPDATE Product p SET p.quantity = p.quantity - 1 WHERE p.trackingId = ?1 AND p.quantity > 0")
//    void decreaseStock(String trackingId);
//
//    @Transactional
//    @Modifying
//    @Query("UPDATE Product p SET p.quantity = p.quantity + 1 WHERE p.trackingId = ?1")
//    void increaseStock(String trackingId);


}