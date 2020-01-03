package com.tripi.flightstats.domain.demo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Appendix {
    private List<Airline> airlines;
    private List<Airport> airports;
    private List<String> equipments;
}
