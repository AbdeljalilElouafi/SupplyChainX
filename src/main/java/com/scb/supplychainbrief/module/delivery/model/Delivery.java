package com.scb.supplychainbrief.module.delivery.model;

import com.scb.supplychainbrief.common.util.DeliveryStatus;
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
@EqualsAndHashCode(of = "idDelivery")
@ToString(exclude = "order")
@Entity
@Table(name = "delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDelivery;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    private String vehicle;
    private String driver;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    private LocalDate deliveryDate;
    private Double cost;
}
