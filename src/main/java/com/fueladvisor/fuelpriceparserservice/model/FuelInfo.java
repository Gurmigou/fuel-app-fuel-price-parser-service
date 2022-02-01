package com.fueladvisor.fuelpriceparserservice.model;

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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private GasStation gasStation;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Region region;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private FuelType fuelType;

    private Double price;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime date;
}
