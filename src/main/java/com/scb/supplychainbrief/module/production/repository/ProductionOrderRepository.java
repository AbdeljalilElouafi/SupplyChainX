package com.scb.supplychainbrief.module.production.repository;

import com.scb.supplychainbrief.module.production.model.Product;
import com.scb.supplychainbrief.module.production.model.ProductionOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionOrderRepository extends JpaRepository<ProductionOrder, Long> {

    long countByProduct(Product product);

}
