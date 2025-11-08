package com.scb.supplychainbrief.module.production.model;

import com.scb.supplychainbrief.module.delivery.model.Order; // <-- IMPORT THIS
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
@EqualsAndHashCode(of = "idProduct")
@ToString(exclude = {"bomEntries", "productionOrders", "clientOrders"}) // <-- UPDATE THIS
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduct;

    private String name;
    private Integer productionTime;
    private Double cost;
    private Integer stock;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BillOfMaterial> bomEntries = new HashSet<>();

    @OneToMany(mappedBy = "product")
    private Set<ProductionOrder> productionOrders = new HashSet<>();


    @OneToMany(mappedBy = "product")
    private Set<Order> clientOrders = new HashSet<>();


    public void addBomEntry(BillOfMaterial entry) {
        bomEntries.add(entry);
        entry.setProduct(this);
    }
}
