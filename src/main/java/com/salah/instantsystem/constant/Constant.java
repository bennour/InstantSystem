package com.salah.instantsystem.constant;

public class Constant {

    public static String OPEN_PARKING = "OUVERT";

    public static String SORT_DSC = "DSC";
    public static String SORT_ASC = "ASC";

    public final static String PARKING_URI = "https://data.rennesmetropole.fr/api/records/1.0/search/?dataset=export-api-parking-citedia";
    public final static String PARKING_GET_ONE_URI = "https://data.rennesmetropole.fr/api/records/1.0/search/?dataset=export-api-parking-citedia&q=id:$";

    public static String PARKING_NOT_FOUND_MSG = "Parking id [%s] not found";
    public static String SERVICE_NOT_AVAILABLE_MSG = "Service temporarily unavailable";

}
