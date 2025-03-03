package com.demo.smartpark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jandrada
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingLotDto {
    private String parkingLotId;
    private String location;
    private int capacity;
    private int occupiedSpaces;
    private double costPerMinute;
}
