package com.scb.supplychainbrief.module.delivery.repository;

import com.scb.supplychainbrief.module.delivery.model.Customer;
import com.scb.supplychainbrief.module.delivery.model.Order;
import com.scb.supplychainbrief.module.production.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    long countByCustomer(Customer customer);


    long countByProduct(Product product);
}
