package com.fueladvisor.fuelpriceparserservice.repository;

import com.fueladvisor.fuelpriceparserservice.model.entity.GasStationDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GasStationDetailsRepository extends CrudRepository<GasStationDetails, Integer> {
    Optional<GasStationDetails> findByGasStationId(String gasStationId);
}