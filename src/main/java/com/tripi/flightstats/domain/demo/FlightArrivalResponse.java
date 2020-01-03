package com.tripi.flightstats.domain.demo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightArrivalResponse {
    private FlightStatsRequest request;
    private Rule rule;
    private Appendix appendix;
}
