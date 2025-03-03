package com.demo.smartpark.exception;

public class RecordAlreadyExist extends ApplicationException {

    public RecordAlreadyExist() {
        super("Record already exist");
    }

    public RecordAlreadyExist(String message) {
        super(message);
    }
}
