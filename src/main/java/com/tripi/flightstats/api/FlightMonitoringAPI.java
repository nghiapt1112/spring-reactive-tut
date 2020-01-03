package com.tripi.flightstats.api;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flight")
public class FlightMonitoringAPI {
    @PostMapping
    public void register(@RequestBody Object object) {

    }


    @PostMapping("/noti-receiver")
    public void receiverNotification(@RequestBody Object object) {

    }

    @GetMapping
    public void findAll(
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) String maHangKhong,
            @RequestParam(required = false) String maCHuyenBay,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end
    ){

    }

}
