package com.demo.smartpark.repository;

import com.demo.smartpark.entity.ParkingLot;
import com.demo.smartpark.entity.Vehicle;
import com.demo.smartpark.entity.ParkingLotVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

/**
 * @author jandrada
 */
@Repository
public interface ParkingLotVehicleRepository extends JpaRepository<ParkingLotVehicle, Long> {

    Optional<List<ParkingLotVehicle>> findByParkingLotAndCheckingOutTimeIsNull(ParkingLot parkingLot);

    Optional<ParkingLotVehicle> findByVehicleAndCheckingOutTimeIsNull(Vehicle vehicle);

    Optional<List<ParkingLotVehicle>> findByCheckingOutTimeIsNull();
}
