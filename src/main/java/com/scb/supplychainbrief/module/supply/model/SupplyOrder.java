package com.scb.supplychainbrief.module.supply.model;

import com.scb.supplychainbrief.common.util.SupplyOrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "supply_order")
@Getter
@Setter
@NoArgsConstructor
public class SupplyOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToMany
    @JoinTable(
            name = "supply_order_material",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "material_id")
    )
    private Set<RawMaterial> materials = new HashSet<>();

    private LocalDate orderDate;

    @Enumerated(EnumType.STRING)
    private SupplyOrderStatus status;
}
