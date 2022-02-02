package com.fueladvisor.fuelpriceparserservice.repository;

import com.fueladvisor.fuelpriceparserservice.model.entity.GasStation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GasStationRepository extends CrudRepository<GasStation, Integer> {
    Optional<GasStation> findByName(String name);
}
