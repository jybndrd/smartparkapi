package com.demo.smartpark.mapper;

import com.demo.smartpark.dto.ParkingLotVehicleDto;
import com.demo.smartpark.entity.ParkingLotVehicle;
import org.mapstruct.Mapper;

/**
 * @author jandrada
 */
@Mapper(componentModel = "spring", config = GlobalMapperConfig.class)
public interface ParkingLotVehicleMapper {
    ParkingLotVehicleDto parkingLotVehicleToParkingLotVehicleDto (ParkingLotVehicle parkingLotVehicle);

    ParkingLotVehicle parkingLotVehicleDtoToParkingLotVehicle (ParkingLotVehicleDto parkingLotVehicleDto);
}
