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
@Table(name = "\"vehicle\"")
public class Vehicle {
    @Id
    @Column(nullable = false, unique = true)
    private String licensePlate;
    //TODO: Enum implemenation for car, truck, and motorcycle.
    private String type;
    private String ownerName;
}
