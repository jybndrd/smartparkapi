package com.demo.smartpark.mapper;

import com.demo.smartpark.dto.VehicleDto;
import com.demo.smartpark.entity.Vehicle;
import org.mapstruct.Mapper;

/**
 * @author jandrada
 */
@Mapper(componentModel = "spring", config = GlobalMapperConfig.class)
public interface VehicleMapper {
    VehicleDto vehicleToVehicleDto (Vehicle vehicle);

    Vehicle vehicleDtoToVehicle (VehicleDto vehicleDto);
}
