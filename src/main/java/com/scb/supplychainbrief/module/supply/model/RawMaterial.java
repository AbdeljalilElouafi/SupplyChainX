package com.scb.supplychainbrief.module.supply.model;

import com.scb.supplychainbrief.module.production.model.BillOfMaterial;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "raw_material")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "idMaterial")
@ToString(exclude = {"suppliers", "supplyOrders", "bomEntries"})
public class RawMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMaterial;

    private String name;
    private Integer stock;
    private Integer stockMin;
    private String unit;

    @ManyToMany
    @JoinTable(
            name = "raw_material_supplier",
            joinColumns = @JoinColumn(name = "material_id"),
            inverseJoinColumns = @JoinColumn(name = "supplier_id")
    )
    private Set<Supplier> suppliers = new HashSet<>();

    @ManyToMany(mappedBy = "materials")
    private Set<SupplyOrder> supplyOrders = new HashSet<>();


    @OneToMany(mappedBy = "material")
    private Set<BillOfMaterial> bomEntries = new HashSet<>();
}
