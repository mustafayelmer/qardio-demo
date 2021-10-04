package com.yelmer.qardio.temperature.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SaveRequest {
    private Double degree;
    private LocalDateTime occurredAt;

    @Override
    public String toString() {
        return "TemperatureSaveRequest{" +
                "degree=" + degree +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
