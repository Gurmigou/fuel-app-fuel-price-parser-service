package com.fueladvisor.fuelpriceparserservice.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class GasStation {
    @Id
    @Column(length = 20)
    private String id;
    private String name;

    @OneToOne(mappedBy = "gasStation", fetch = FetchType.LAZY)
    private GasStationDetails gasStationDetails;

    public GasStation(String id, String name) {
        this.id = id;
        this.name = name;
    }
}