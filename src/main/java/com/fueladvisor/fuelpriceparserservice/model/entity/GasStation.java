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
}