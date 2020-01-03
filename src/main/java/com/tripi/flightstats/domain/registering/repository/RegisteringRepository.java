package com.tripi.flightstats.domain.registering.repository;

import com.tripi.flightstats.domain.registering.FlightMonitored;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisteringRepository extends ReactiveCrudRepository<FlightMonitored, Long> {

}
