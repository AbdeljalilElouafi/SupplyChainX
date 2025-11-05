package com.scb.supplychainbrief.module.supply.repository;

import com.scb.supplychainbrief.common.util.SupplyOrderStatus;
import com.scb.supplychainbrief.module.supply.model.SupplyOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplyOrderRepository extends JpaRepository<SupplyOrder, Long> {


    long countBySupplierIdSupplierAndStatusNot(Long supplierId, SupplyOrderStatus status);
}
