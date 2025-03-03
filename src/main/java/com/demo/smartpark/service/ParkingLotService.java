package com.demo.smartpark.service;

import com.demo.smartpark.entity.ParkingLot;
import com.demo.smartpark.entity.ParkingLotVehicle;
import com.demo.smartpark.entity.Vehicle;
import com.demo.smartpark.exception.*;
import com.demo.smartpark.repository.ParkingLotRepository;
import com.demo.smartpark.repository.ParkingLotVehicleRepository;
import com.demo.smartpark.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author jandrada
 */
@Service
@RequiredArgsConstructor
public class ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingLotVehicleRepository parkingLotVehicleRepository;


    public ParkingLot registerParkingLot(ParkingLot parkingLot) {
        if (parkingLotRepository.existsByLocation(parkingLot.getLocation()))
            throw new RecordAlreadyExist("Parking Lot already exists!");

        return parkingLotRepository.save(parkingLot);
    }

    public Vehicle registerVehicle(Vehicle vehicle) {
        if (vehicleRepository.existsById(vehicle.getLicensePlate()))
            throw new RecordAlreadyExist("Vehicle already exists!");

        return vehicleRepository.save(vehicle);
    }

    // Get the available space of a parking lot using the id
    public String getLotOccupancy(Long parkingLotId) {
        ParkingLot parkingLot = parkingLotRepository.findById(parkingLotId)
                .orElseThrow(() -> new NotFoundException("Parking lot not found!"));

        return "Occupied spaces: " + parkingLot.getOccupiedSpaces() +
                ", Available spaces: " + (parkingLot.getCapacity() - parkingLot.getOccupiedSpaces());
    }

    // Get the list of vehicles in a given parking lot id
    public List<Vehicle> getParkedVehiclesInLot(Long parkingLotId) {
        ParkingLot parkingLot = parkingLotRepository.findById(parkingLotId)
                .orElseThrow(() -> new NotFoundException("Parking lot not found"));


        // Make sure to fetch only the vehicles that is not checked out
        Optional<List<ParkingLotVehicle>> parkingLotVehicle = parkingLotVehicleRepository.findByParkingLotAndCheckingOutTimeIsNull(parkingLot);

        return parkingLotVehicle.get().stream()
                .map(ParkingLotVehicle::getVehicle)
                .toList();
    }

    // Get all the registered parking lots
    public List<ParkingLot> getAllParkingLots(){
        return parkingLotRepository.findAll();
    }

    // Get the details of a parking lot
    public ParkingLot getParkingLotById(Long parkingLotId){
        return parkingLotRepository
                .findById(parkingLotId).orElseThrow(() -> new NotFoundException("Parking lot not found!"));
    }

    // Get the details of a vehicle
    public Vehicle getVehicleByLicensePlate(String licensePlate){
        return vehicleRepository
                .findById(licensePlate).orElseThrow(() -> new NotFoundException("Vehicle not found!"));
    }

    // Check in a vehicle on a parking lot
    public ParkingLotVehicle checkInVehicle(Long parkingLotId, String licensePlate) {
        ParkingLot parkingLot = parkingLotRepository.findById(parkingLotId)
                .orElseThrow(() -> new NotFoundException("Parking lot not found!"));

        if (parkingLot.getOccupiedSpaces() >= parkingLot.getCapacity())
            throw new ParkingLotFullException();


        Vehicle vehicle = vehicleRepository.findById(licensePlate)
                .orElseThrow(() -> new NotFoundException("Vehicle not found!"));

        // Fetch only the data where the check-out time is null, meaning it is currently not parked
        Optional<ParkingLotVehicle> parkingLotVehicleOptional = parkingLotVehicleRepository.findByVehicleAndCheckingOutTimeIsNull(vehicle);

        if (parkingLotVehicleOptional.isPresent() ) {
            throw new VehicleAlreadyParkedException();
        }

        ParkingLotVehicle newParkingLotVehicle = new ParkingLotVehicle();
        newParkingLotVehicle.setParkingLot(parkingLot);
        newParkingLotVehicle.setCheckingInTime(convertMillisToLocalDateTime(System.currentTimeMillis()));
        newParkingLotVehicle.setVehicle(vehicle);
        parkingLot.setOccupiedSpaces(parkingLot.getOccupiedSpaces() + 1);

        // Save changes for both parking lot and parking lot vehicle entities
        parkingLotRepository.save(parkingLot);
        return parkingLotVehicleRepository.save(newParkingLotVehicle);

    }

    // Check out a vehicle using its license plate
    public String checkOutVehicle(String licensePlate) {

        Vehicle vehicle = vehicleRepository.findById(licensePlate)
                .orElseThrow(() -> new NotFoundException("Vehicle not found!"));

        //find only the record where check out time is null
        Optional<ParkingLotVehicle> parkingLotVehicleOptional = parkingLotVehicleRepository.findByVehicleAndCheckingOutTimeIsNull(vehicle);

        if (parkingLotVehicleOptional.isEmpty())
            throw new VehicleIsNotParkedException();

        ParkingLot parkingLot = parkingLotRepository.findById(parkingLotVehicleOptional.get().getParkingLot().getParkingLotId())
                .orElseThrow(() -> new NotFoundException("Parking lot not found!"));

        LocalDateTime checkInTime = parkingLotVehicleOptional.get().getCheckingInTime();
        LocalDateTime currentTime = convertMillisToLocalDateTime(System.currentTimeMillis());
        long durationMinutes = Duration.between(checkInTime, currentTime).toMinutes();
        double cost = durationMinutes * parkingLot.getCostPerMinute();

        // No grace period applied, minimum fee is given whenever parked.
        if (cost == 0)
            cost = parkingLot.getCostPerMinute();

        parkingLotVehicleOptional.get().setCheckingOutTime(currentTime);
        parkingLot.setOccupiedSpaces(parkingLot.getOccupiedSpaces() - 1);

        parkingLotRepository.save(parkingLot);
        parkingLotVehicleRepository.save(parkingLotVehicleOptional.get());

        return "Parking Cost: " + cost;
    }

    // Get park records
    public List<ParkingLotVehicle> getParkingLotVehicleRecords() {
        return parkingLotVehicleRepository.findAll();
    }

    private LocalDateTime convertMillisToLocalDateTime(long millis) {
        // Create an Instant from the milliseconds
        Instant instant = Instant.ofEpochMilli(millis);

        // Convert the Instant to LocalDateTime using the system default time zone
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
