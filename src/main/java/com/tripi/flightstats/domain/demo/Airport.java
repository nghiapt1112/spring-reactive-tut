package com.tripi.flightstats.domain.demo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Airport {
    private String fs;
    private String iata;
    private String icao;
    private String faa;
    private String name;
    private String city;
    private String cityCode;
    private String stateCode;
    private String countryCode;
    private String countryName;
    private String regionName;
    private String timeZoneRegionName;
    private String weatherZone;
    private String localTime;
    private Integer utcOffsetHours;
    private Float latitude;
    private Float longitude;
    private Integer elevationFeet;
    private Integer classification;
    private boolean active;
}