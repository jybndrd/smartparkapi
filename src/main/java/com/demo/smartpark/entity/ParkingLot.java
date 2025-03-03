package com.demo.smartpark.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jandrada
 */
    @Data
    @Entity
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Table(name = "\"parking_lot\"")
    public class ParkingLot {

        @Id
        @Column(length = 50)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long parkingLotId;
        @Column(unique = true)
        private String location;
        private int capacity;
        private int occupiedSpaces;
        private double costPerMinute;

    }
