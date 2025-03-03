package com.demo.smartpark.repository;

import com.demo.smartpark.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author jandrada
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
}
