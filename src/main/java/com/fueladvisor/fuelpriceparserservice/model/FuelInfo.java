package com.fueladvisor.fuelpriceparserservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FuelInfo {
    @Id
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
