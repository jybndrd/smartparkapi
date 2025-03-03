package com.demo.smartpark.controller;

import com.demo.smartpark.dto.ParkingLotDto;
import com.demo.smartpark.dto.ParkingLotVehicleDto;
import com.demo.smartpark.dto.VehicleDto;
import com.demo.smartpark.entity.ParkingLotVehicle;
import com.demo.smartpark.entity.Vehicle;
import com.demo.smartpark.mapper.ParkingLotMapper;
import com.demo.smartpark.mapper.ParkingLotVehicleMapper;
import com.demo.smartpark.mapper.VehicleMapper;
import com.demo.smartpark.service.ParkingLotService;
import com.demo.smartpark.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author jandrada
 */
@RestController
@RequestMapping("/api/smartparking")
@RequiredArgsConstructor
public class SmartParkingController {

    private final ParkingLotService parkingLotService;
    private final ParkingLotMapper parkingLotMapper;
    private final VehicleMapper vehicleMapper;
    private final ParkingLotVehicleMapper parkingLotVehicleMapper;

    // Sanity check for api connectivity
    @GetMapping("/sanityCheck")
    public ResponseEntity<String> sanityCheck() {
        return ResponseEntity.ok("Welcome to Smart Parking!");
    }

    // Registering a parking lot
    @PostMapping("/registerParkingLot")
    public ResponseEntity<ParkingLotDto> registerParkingLot(@RequestBody ParkingLotDto parkingLotDTO) {

        return Optional.of(parkingLotDTO)
                .map(parkingLotMapper::parkingLotDtoToParkingLot)
                .map(parkingLotService::registerParkingLot)
                .map(parkingLotMapper::parkingLotToParkingLotDto)
                .map(ResponseEntity::ok)
                .orElseThrow(ApplicationException::new);
    }

    // Registering a vehicle
    @PostMapping("/registerVehicle")
    public ResponseEntity<VehicleDto> registerVehicle(@RequestBody VehicleDto vehicleDto) {
        return Optional.of(vehicleDto)
                .map(vehicleMapper::vehicleDtoToVehicle)
                .map(parkingLotService::registerVehicle)
                .map(vehicleMapper::vehicleToVehicleDto)
                .map(ResponseEntity::ok)
                .orElseThrow(ApplicationException::new);
    }

    @GetMapping("/getAllParkingLots")
    public ResponseEntity<List<ParkingLotDto>> getAllParkingLots() {
        return ResponseEntity.ok(parkingLotService.getAllParkingLots().stream()
                .map(parkingLotMapper::parkingLotToParkingLotDto)
                .toList());
    }

    @GetMapping("/getVehicle/{licensePlate}")
    public ResponseEntity<VehicleDto> getVehicle(@PathVariable String licensePlate) {
        return Optional.of(licensePlate)
                .map(parkingLotService::getVehicleByLicensePlate)
                .map(vehicleMapper::vehicleToVehicleDto)
                .map(ResponseEntity::ok)
                .orElseThrow(ApplicationException::new);
    }

    @GetMapping("/getParkingLot/{parkingLotId}")
    public ResponseEntity<ParkingLotDto> getParkingLot(@PathVariable Long parkingLotId) {
        return Optional.of(parkingLotId)
                .map(parkingLotService::getParkingLotById)
                .map(parkingLotMapper::parkingLotToParkingLotDto)
                .map(ResponseEntity::ok)
                .orElseThrow(ApplicationException::new);
    }

    // Viewing current occupancy and availability of a parking lot
    @GetMapping("/parkingLotOccupancy/{parkingLotId}")
    public ResponseEntity<String> getLotsOccupancy(@PathVariable Long parkingLotId) {
        return ResponseEntity.ok(parkingLotService.getLotOccupancy(parkingLotId));
    }

    // Viewing all vehicles currently parked in a lot
    @GetMapping("/parkingLotVehicles/{parkingLotId}")
    public ResponseEntity<List<VehicleDto>> getParkedVehiclesInLot(@PathVariable Long parkingLotId) {
        List<Vehicle> vehicles = parkingLotService.getParkedVehiclesInLot(parkingLotId);

        if (vehicles == null)
            return ResponseEntity.notFound().build();

        if (vehicles.isEmpty())
            return ResponseEntity.noContent().build();

      return ResponseEntity.ok(vehicles.stream()
                .map(vehicleMapper::vehicleToVehicleDto)
                .toList());
    }

    // Check in a vehicle license plate using parking lot id
    @PostMapping("/checkInVehicle/{parkingLotId}/{licensePlate}")
    public ResponseEntity<ParkingLotVehicleDto> checkInVehicle(@PathVariable Long parkingLotId, @PathVariable String licensePlate) {

        return Optional.of(parkingLotService.checkInVehicle(parkingLotId, licensePlate))
                .map(parkingLotVehicleMapper::parkingLotVehicleToParkingLotVehicleDto)
                .map(ResponseEntity::ok)
                .orElseThrow(ApplicationException::new);
    }

    // Check out a vehicle using license plate
    @PostMapping("/checkOutVehicle/{licensePlate}")
    public ResponseEntity<String> checkInVehicle(@PathVariable String licensePlate) {

        return Optional.of(licensePlate)
                .map(parkingLotService::checkOutVehicle)
                .map(ResponseEntity::ok)
                .orElseThrow(ApplicationException::new);
    }

    @GetMapping("/getParkRecords")
    public ResponseEntity<List<ParkingLotVehicleDto>> getParkRecords() {
        List<ParkingLotVehicle> parkingLotVehicle = parkingLotService.getParkingLotVehicleRecords();

        if (parkingLotVehicle == null)
            return ResponseEntity.notFound().build();

        if (parkingLotVehicle.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(parkingLotVehicle.stream()
                .map(parkingLotVehicleMapper::parkingLotVehicleToParkingLotVehicleDto)
                .toList());
    }
}
