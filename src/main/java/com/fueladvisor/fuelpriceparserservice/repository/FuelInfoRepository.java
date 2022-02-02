package com.fueladvisor.fuelpriceparserservice.repository;

import com.fueladvisor.fuelpriceparserservice.model.entity.FuelInfo;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuelInfoRepository extends CrudRepository<FuelInfo, Integer> {
    @Query("""
        FROM FuelInfo fi
        INNER JOIN FETCH fi.gasStation gs
        INNER JOIN FETCH fi.region r
        WHERE r.name = :region
    """)
    List<FuelInfo> getFuelInfosByRegionName(@Param("region") String region);
}
