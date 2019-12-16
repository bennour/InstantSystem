package com.salah.instantsystem.controller;

import com.salah.instantsystem.model.Criteria;
import com.salah.instantsystem.exception.BadRequestException;
import com.salah.instantsystem.model.ParkingDetailsVM;
import com.salah.instantsystem.model.ParkingVM;
import com.salah.instantsystem.service.ParkingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

import static com.salah.instantsystem.constant.Constant.SORT_ASC;
import static com.salah.instantsystem.constant.Constant.SORT_DSC;

@RestController
@RequestMapping("/parkings")
@Api("Parking resource API")
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    @GetMapping()
    @ApiOperation(value = "Get parking list")
    public List<ParkingVM> getParkingList(@RequestParam Optional<String> sort,
                                          @RequestParam Optional<String> sortBy,
                                          @RequestParam Optional<Boolean> onlyOpened,
                                          @RequestParam Optional<Integer> minFree,
                                          @RequestParam Optional<Double> maxPrice,
                                          @RequestParam Optional<String> duration) {

        Criteria criteria = buildCriteria(sort, sortBy, onlyOpened, minFree, maxPrice, duration);

        return parkingService.getAll(criteria);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get parking by id")
    public ParkingDetailsVM getParking(@PathVariable String id) {
        return parkingService.getById(id);
    }

    private Criteria buildCriteria(Optional<String> sort, Optional<String> sortBy, Optional<Boolean> onlyOpened, Optional<Integer> minFree, Optional<Double> maxPrice, Optional<String> duration) {
        Criteria criteria = new Criteria();

        if(sort.isPresent() && sortBy.isPresent()) {
            if (SORT_ASC.equalsIgnoreCase(sort.get())) {
                criteria.setSortOrder(SortOrder.ASCENDING);
            }else if (SORT_DSC.equalsIgnoreCase(sort.get())) {
                criteria.setSortOrder(SortOrder.DESCENDING);
            } else {
                throw new BadRequestException(String.format("Invalid sort field value [%s]. Please use : ASC or DSC.", sort.get()));
            }
            criteria.setSortBy(sortBy);
        }

        if(onlyOpened.isPresent()) {
            criteria.setOnlyOpened(onlyOpened);
        }

        if (minFree.isPresent()) {
            criteria.setMinFree(minFree);
        }

        if (maxPrice.isPresent() && duration.isPresent()) {
            criteria.setMaxPrice(maxPrice);
            criteria.setDuration(duration);
        }

        return criteria;
    }
}
