package com.salah.instantsystem.restclient.service;

import com.salah.instantsystem.exception.BadRequestException;
import com.salah.instantsystem.model.Criteria;
import com.salah.instantsystem.dao.ParkingDAO;
import com.salah.instantsystem.exception.ResourceNotFoundException;
import com.salah.instantsystem.exception.UnavailableServiceException;
import com.salah.instantsystem.mapper.ParkingMapper;
import com.salah.instantsystem.model.ParkingDetailsVM;
import com.salah.instantsystem.model.ParkingVM;
import com.salah.instantsystem.restclient.model.Fields;
import com.salah.instantsystem.restclient.model.Record;
import com.salah.instantsystem.restclient.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static com.salah.instantsystem.constant.Constant.*;

@Service
public class ParkingRestClient implements ParkingDAO {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ParkingMapper parkingMapper;


    @Override
    public List<ParkingVM> getAll(Criteria criteria) {

        String uri = buildURI(PARKING_URI, criteria);

        Response response;
        try {
            response = restTemplate.getForObject(uri, Response.class);
        } catch (HttpClientErrorException.BadRequest e) {
            throw new BadRequestException(e.getMessage());
        }

        if (response == null) {
            throw new UnavailableServiceException(SERVICE_NOT_AVAILABLE_MSG);
        }

        List<ParkingVM> parkingVMList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(response.getRecords())) {
            for (Record record : response.getRecords()) {
                parkingVMList.add(parkingMapper.toParkingVM(record.getFields()));
            }
        }

        return parkingVMList;
    }


    @Override
    public ParkingDetailsVM findById(String id) {
        String uri = PARKING_GET_ONE_URI.replace("$", id);

        Response response = restTemplate.getForObject(uri, Response.class);

        if (response == null) {
            throw new UnavailableServiceException(SERVICE_NOT_AVAILABLE_MSG);
        }

        if (CollectionUtils.isEmpty(response.getRecords())) {
            throw new ResourceNotFoundException(String.format(PARKING_NOT_FOUND_MSG, id));
        }

        Fields fields = response.getRecords().get(0).getFields();
        return parkingMapper.toParkingDetailsVM(fields);
    }

    // ----------------
    // PRIVATE METHODS
    // ----------------

    private String buildURI(String uriParkingList, Criteria criteria) {
        return uriParkingList
                + buildQueryParameter(criteria)
                + buildSortParameter(criteria);
    }

    private String buildSortParameter(Criteria criteria) {
        String sortParameter = "";

        if (criteria.getSortBy().isPresent()) {
            sortParameter = "&sort=";
            if (SortOrder.ASCENDING == criteria.getSortOrder()) {
                sortParameter += "-";
            }
            sortParameter += criteria.getSortBy().get();
        }

        return sortParameter;
    }

    private String buildQueryParameter(Criteria criteria) {
        List<String> queryParameter = new ArrayList<>();

        if (criteria.getOnlyOpened().isPresent() && criteria.getOnlyOpened().get()) {
            queryParameter.add("status=" + OPEN_PARKING);
        }

        if (criteria.getMinFree().isPresent()) {
            queryParameter.add("free>=" + criteria.getMinFree().get());
        }

        if (criteria.getMaxPrice().isPresent() && criteria.getDuration().isPresent()) {
            queryParameter.add("tarif_" + criteria.getDuration().get() + "<" + criteria.getMaxPrice().get());
        }

        if (queryParameter.isEmpty()) {
            return "";
        }

        String query = String.join("+", queryParameter);
        return String.format("&q=%s", query);
    }
}