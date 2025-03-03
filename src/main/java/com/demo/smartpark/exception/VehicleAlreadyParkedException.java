package com.demo.smartpark.exception;

public class VehicleAlreadyParkedException extends ApplicationException {
    public VehicleAlreadyParkedException() {
        super("Vehicle is already parked");
    }

    public VehicleAlreadyParkedException(String message) {
        super(message);
    }
}
