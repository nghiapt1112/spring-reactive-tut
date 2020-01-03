package com.tripi.flightstats.domain.demo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Airline {
    private String fs;
    private String iata;
    private String icao;
    private String name;
    private String phoneNumber;
    private boolean active;
}
