package com.demo.smartpark.mapper;

import com.demo.smartpark.dto.ParkingLotDto;
import com.demo.smartpark.entity.ParkingLot;
import org.mapstruct.Mapper;

/**
 * @author jandrada
 */
@Mapper(componentModel = "spring", config = GlobalMapperConfig.class)
public interface ParkingLotMapper {

    ParkingLotDto parkingLotToParkingLotDto(ParkingLot parkingLot);

    ParkingLot parkingLotDtoToParkingLot (ParkingLotDto parkingLotDto);
}
