package com.scb.supplychainbrief.module.supply.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "supplier")
@Getter
@Setter
@NoArgsConstructor
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSupplier;

    private String name;
    private String contact;
    private Double rating;
    private Integer leadTime;

    @OneToMany(mappedBy = "supplier")
    private Set<SupplyOrder> orders = new HashSet<>();

    @ManyToMany(mappedBy = "suppliers")
    private Set<RawMaterial> rawMaterials = new HashSet<>();
}