package com.scb.supplychainbrief.module.delivery.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "idCustomer")
@ToString(exclude = "orders")
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCustomer;

    private String name;
    private String address;
    private String city;

    @OneToMany(mappedBy = "customer")
    private Set<Order> orders = new HashSet<>();
}
