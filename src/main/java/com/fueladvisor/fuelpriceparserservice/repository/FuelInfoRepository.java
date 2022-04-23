package com.fueladvisor.fuelpriceparserservice.repository;

import com.fueladvisor.fuelpriceparserservice.model.entity.FuelInfo;
import com.fueladvisor.fuelpriceparserservice.model.entity.FuelType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuelInfoRepository extends CrudRepository<FuelInfo, Integer> {
    @Query("FROM FuelInfo fi " +
           "INNER JOIN FETCH fi.gasStation gs " +
           "INNER JOIN FETCH fi.region r " +
           "WHERE r.latinName = :regionLatinName " +
           "ORDER BY gs.id, fi.fuelType"
    )
    List<FuelInfo> getFuelInfosByRegionLatinName(@Param("regionLatinName") String regionLatinName);

    @Query("FROM FuelInfo fi " +
           "INNER JOIN FETCH fi.gasStation gs " +
           "INNER JOIN FETCH fi.region r " +
           "WHERE gs.id = :gasStationId " +
           "ORDER BY r.id, fi.fuelType"
    )
    List<FuelInfo> getFuelInfosByGasStationId(@Param("gasStationId") String gasStationId);

    @Query("FROM FuelInfo fi " +
           "INNER JOIN FETCH fi.gasStation gs " +
           "INNER JOIN FETCH fi.region r " +
           "WHERE r.latinName = :regionLatin AND gs.id = :gasStationId " +
           "ORDER BY fi.fuelType"
    )
    List<FuelInfo> getFuelInfosByRegionLatinNameAndGasStationId(@Param("regionLatin") String regionLatin,
                                                                @Param("gasStationId") String gasStationId);

//    @Query("SELECT AVG(fi.price) FROM FuelInfo fi " +
//           "INNER JOIN fi.gasStation gs " +
//           "WHERE gs.id = :gasStationId")
//    Double getAverageAllTypesFuelPriceByGasStationId(@Param("gasStationId") String gasStationId);

    @Query("SELECT AVG(fi.price) FROM FuelInfo fi " +
           "INNER JOIN fi.gasStation gs " +
           "WHERE gs.id = ?1 AND fi.fuelType = ?2")
    Optional<Double> getAverageFuelPriceByFuelTypeAndGasStationId(String gasStationId, FuelType fuelType);
}