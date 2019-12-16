package com.salah.instantsystem.model;

import lombok.Data;

@Data
public class ParkingVM {

    private String id;
    private String key;
    private int max;
    private int free;
    private boolean isOpen;
    private double latitude;
    private double longitude;
}