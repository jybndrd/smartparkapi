package com.demo.smartpark.dto;

import com.demo.smartpark.entity.ParkingLot;
import com.demo.smartpark.entity.Vehicle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author jandrada
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingLotVehicleDto {

    private Long id;
    private ParkingLot parkingLot;
    private Vehicle vehicle;
    private LocalDateTime checkingInTime;
    private LocalDateTime checkingOutTime;
}
