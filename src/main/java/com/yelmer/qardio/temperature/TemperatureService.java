package com.yelmer.qardio.temperature;

import com.yelmer.qardio.temperature.dto.AggregationResponse;
import com.yelmer.qardio.temperature.dto.SaveRequest;
import com.yelmer.qardio.temperature.enumeration.AggregationType;

import java.util.List;

public interface TemperatureService {
    List<String> save(SaveRequest dto);

    List<String> saveAsBulk(List<SaveRequest> list);

    AggregationResponse aggregate(AggregationType type);

    Temperature getById(String id);

    List<Temperature> list();
}
