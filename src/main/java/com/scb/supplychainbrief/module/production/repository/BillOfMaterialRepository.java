package com.scb.supplychainbrief.module.production.repository;

import com.scb.supplychainbrief.module.production.model.BillOfMaterial;
import com.scb.supplychainbrief.module.production.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillOfMaterialRepository extends JpaRepository<BillOfMaterial, Long> {
    List<BillOfMaterial> findByProduct(Product product);
}
