package com.fueladvisor.fuelpriceparserservice.model.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FuelInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.MERGE)
    private GasStation gasStation;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Region region;

    @Enumerated(value = EnumType.ORDINAL)
    @Column(nullable = false)
    private FuelType fuelType;

    private Double price;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime date;
}
