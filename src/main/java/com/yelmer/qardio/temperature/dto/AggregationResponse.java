package com.yelmer.qardio.temperature.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AggregationResponse {
    private Double degree;
    private Integer times;

    public AggregationResponse(Double degree, Long times) {
        this.degree = degree;
        this.times = times.intValue();
    }

    @Override
    public String toString() {
        return "AggregationResponse{" +
                "degree=" + degree +
                ", times=" + times +
                '}';
    }
}
