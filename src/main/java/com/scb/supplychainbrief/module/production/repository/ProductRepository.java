package com.scb.supplychainbrief.module.production.repository;

import com.scb.supplychainbrief.module.production.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Add findByName searches
    Product findByName(String name);
}
