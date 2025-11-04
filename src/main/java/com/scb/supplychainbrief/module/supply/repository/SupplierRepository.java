package com.scb.supplychainbrief.module.supply.repository;

import com.scb.supplychainbrief.module.supply.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    // custom queries here for search
}