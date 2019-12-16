package com.salah.instantsystem.mapper;

import com.salah.instantsystem.model.ParkingDetailsVM;
import com.salah.instantsystem.model.ParkingVM;
import com.salah.instantsystem.restclient.model.Fields;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.util.CollectionUtils;

import static com.salah.instantsystem.constant.Constant.OPEN_PARKING;

@Mapper(componentModel = "spring")
public interface ParkingMapper {

    @Mapping(source = "fields", target = "latitude", qualifiedByName = "getLatitude")
    @Mapping(source = "fields", target = "longitude", qualifiedByName = "getLongitude")
    @Mapping(source = "fields", target = "open", qualifiedByName = "isOpened")
    ParkingDetailsVM toParkingDetailsVM(Fields fields);

    @Mapping(source = "fields", target = "latitude", qualifiedByName = "getLatitude")
    @Mapping(source = "fields", target = "longitude", qualifiedByName = "getLongitude")
    @Mapping(source = "fields", target = "open", qualifiedByName = "isOpened")
    ParkingVM toParkingVM(Fields fields);

    // ----------------
    // NAMED METHODS
    // ----------------

    @Named("getLatitude")
    default double getLatitude(Fields fields) {
        if (fields == null || CollectionUtils.isEmpty(fields.getGeo())) {
            return -1;
        }
        return fields.getGeo().get(0);
    }

    @Named("getLongitude")
    default double getLongitude(Fields fields) {
        if (fields == null || CollectionUtils.isEmpty(fields.getGeo()) || fields.getGeo().size() < 2) {
            return -1;
        }
        return fields.getGeo().get(1);
    }

    @Named("isOpened")
    default boolean isOpened(Fields fields) {
        return fields != null && OPEN_PARKING.equalsIgnoreCase(fields.getStatus());
    }
}