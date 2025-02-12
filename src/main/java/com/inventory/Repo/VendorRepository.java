package com.inventory.Repo;

import com.inventory.Entites.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

    Vendor findByName(String name);
}