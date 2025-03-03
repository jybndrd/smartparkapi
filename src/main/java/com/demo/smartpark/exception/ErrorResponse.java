package com.demo.smartpark.exception;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder
public class ErrorResponse {

    private Date timestamp;
    private Integer status;
    private String path;
    private String message;

}
