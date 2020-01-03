package com.tripi.flightstats.domain.demo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightStatsRequest {
    private AirlineCode airlineCode;
//    private Object flightNumber;
//    private Object name;
//    private Object type;
    private DeliverTo deliverTo;
    private String url;
//    private Object date;

}
