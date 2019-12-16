package com.salah.instantsystem.service;

import com.salah.instantsystem.model.Criteria;
import com.salah.instantsystem.dao.ParkingDAO;
import com.salah.instantsystem.model.ParkingDetailsVM;
import com.salah.instantsystem.model.ParkingVM;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ParkingService {

    private ParkingDAO parkingDAO;

    /**
     * Instantiates a new Parking service.
     *
     * @param parkingDAO the parking dao
     */
    public ParkingService(ParkingDAO parkingDAO) {
        this.parkingDAO = parkingDAO;
    }

    /**
     * Gets all.
     *
     * @param criteria the criteria
     * @return the all
     */
    public List<ParkingVM> getAll(Criteria criteria) {
        return parkingDAO.getAll(criteria);

    }

    /**
     * Gets by id.
     *
     * @param id the id
     * @return the by id
     */
    public ParkingDetailsVM getById(String id) {
        return parkingDAO.findById(id);
    }

}