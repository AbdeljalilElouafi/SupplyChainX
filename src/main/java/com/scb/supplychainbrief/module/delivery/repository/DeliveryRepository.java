package com.scb.supplychainbrief.module.delivery.repository;

import com.scb.supplychainbrief.module.delivery.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

}
