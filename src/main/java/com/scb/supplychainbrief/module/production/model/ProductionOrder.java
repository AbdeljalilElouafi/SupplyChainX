package com.scb.supplychainbrief.module.production.model;

import com.scb.supplychainbrief.common.util.ProductionOrderStatus;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "idOrder")
@ToString(exclude = {"product"})
@Entity
@Table(name = "production_order")
public class ProductionOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private ProductionOrderStatus status;

    private LocalDate startDate;
    private LocalDate endDate;
}
