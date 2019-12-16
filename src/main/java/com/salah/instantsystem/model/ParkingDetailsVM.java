package com.salah.instantsystem.model;

import lombok.Data;

@Data
public class ParkingDetailsVM extends ParkingVM {

    private String orgahoraires;
    private double tarif15;
    private double tarif30;
    private double tarif1h;
    private double tarif1h30;
    private double tarif2h;
    private double tarif3h;
    private double tarif4h;
}