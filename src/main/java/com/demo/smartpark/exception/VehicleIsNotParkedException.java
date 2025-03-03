package com.demo.smartpark.exception;

public class VehicleIsNotParkedException extends ApplicationException {

    public VehicleIsNotParkedException() {
        super("Vehicle is not parked.");
    }

    public VehicleIsNotParkedException(String message) {
        super(message);
    }
}
