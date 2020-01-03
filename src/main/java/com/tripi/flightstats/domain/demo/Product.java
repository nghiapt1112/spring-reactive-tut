package com.tripi.flightstats.domain.demo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Product {
    private String id;
    private String face;
    private Integer price;
    private Integer size;

    public String getId() {
        return id;
    }

    public String getFace() {
        return face;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getSize() {
        return size;
    }

}
