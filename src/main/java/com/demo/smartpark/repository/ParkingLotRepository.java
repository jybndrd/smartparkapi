package com.demo.smartpark.repository;

import com.demo.smartpark.entity.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author jandrada
 */
@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {
    boolean existsByLocation(String location);
}
