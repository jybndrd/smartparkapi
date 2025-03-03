package com.demo.smartpark.exception;

public class ParkingLotFullException  extends ApplicationException {
    public ParkingLotFullException() {
        super("Parking Lot is full! Please try again later.");
    }

    public ParkingLotFullException(String message) {
        super(message);
    }
}
