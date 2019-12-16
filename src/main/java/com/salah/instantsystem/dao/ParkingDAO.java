package com.salah.instantsystem.dao;

import com.salah.instantsystem.model.Criteria;
import com.salah.instantsystem.model.ParkingDetailsVM;
import com.salah.instantsystem.model.ParkingVM;

import java.util.List;


/**
 * The interface Parking dao.
 */
public interface ParkingDAO {


    /**
     * Gets all parking vm.
     *
     * @return list of parking vm
     */
    List<ParkingVM> getAll(Criteria criteria);

    /**
     * Find by id parking details vm.
     *
     * @param id the id
     * @return the parking details vm
     */
    ParkingDetailsVM findById(String id);
}
