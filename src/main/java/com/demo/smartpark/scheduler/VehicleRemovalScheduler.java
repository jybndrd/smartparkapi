package com.demo.smartpark.scheduler;

import com.demo.smartpark.entity.ParkingLot;
import com.demo.smartpark.entity.ParkingLotVehicle;
import com.demo.smartpark.repository.ParkingLotRepository;
import com.demo.smartpark.repository.ParkingLotVehicleRepository;
import com.demo.smartpark.service.ParkingLotService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VehicleRemovalScheduler {

    // Create a logger for this class since It's scheduled to run on the background
    // We could also add loggers on the other class if its necessary
    private static final Logger logger = LoggerFactory.getLogger(VehicleRemovalScheduler.class);
    private static final int SCHEDULED_TASK_DURATION = 60000; // runs every 1 minute for testing
    private static final Duration MAX_DURATION_IN_MINUTES = Duration.ofMinutes(5);

    private final ParkingLotVehicleRepository parkingLotVehicleRepository;
    private final ParkingLotService parkingLotService;


    @Scheduled(fixedRate = SCHEDULED_TASK_DURATION)
    public void removeLongParkedVehicles() {
        logger.info("Running scheduled task to remove long-parked vehicles.");

        LocalDateTime now = LocalDateTime.now();

        Optional<List<ParkingLotVehicle>> parkedVehicles = parkingLotVehicleRepository.findByCheckingOutTimeIsNull(); // Get all parked vehicles

        if (parkedVehicles.isEmpty()) {
            logger.info("No vehicles to checkout. Scheduled task finished.");
            return;
        }

        for (ParkingLotVehicle parkingLotVehicle : parkedVehicles.get()) {
            LocalDateTime checkInTime = parkingLotVehicle.getCheckingInTime();
            if (checkInTime != null && Duration.between(checkInTime, now).compareTo(MAX_DURATION_IN_MINUTES) > 0) {

                String cost = parkingLotService.checkOutVehicle(parkingLotVehicle.getVehicle().getLicensePlate());

                Long parkingLotId = parkingLotVehicle.getParkingLot().getParkingLotId();

                logger.info("Removing vehicle {} from parking lot {} due to exceeding 15 minutes. {}",
                        parkingLotVehicle.getVehicle().getLicensePlate(), parkingLotId, cost);
            }
        }
        logger.info("Scheduled task finished.");
    }
}