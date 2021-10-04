package com.yelmer.qardio.temperature;

import com.yelmer.qardio.temperature.dto.AggregationResponse;
import com.yelmer.qardio.temperature.dto.SaveRequest;
import com.yelmer.qardio.temperature.enumeration.AggregationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/v1/temperatures")
public class TemperatureController {

    private final TemperatureService service;

    @Autowired
    public TemperatureController(TemperatureService service) {
        this.service = service;
    }

    @PostMapping()
    @ResponseBody
    public List<String> save(@RequestBody SaveRequest dto) {
        return service.save(dto);
    }

    @PostMapping(value = "/bulk")
    @ResponseBody
    public List<String> saveBulk(@RequestBody List<SaveRequest> list) {
        return service.saveAsBulk(list);
    }

    @GetMapping(value = "/{id}", name = "fetch temperature by id")
    @ResponseBody
    public Temperature getById(@PathVariable String id) {
        return service.getById(id);
    }

    @GetMapping(value = "/hourly", name = "aggregate temperatures by hourly")
    @ResponseBody
    public AggregationResponse aggregateHourly() {
        return service.aggregate(AggregationType.HOURLY);
    }

    @GetMapping(value = "/daily", name = "aggregate temperatures by daily")
    @ResponseBody
    public AggregationResponse aggregateDaily() {
        return service.aggregate(AggregationType.DAILY);
    }

    @GetMapping(name = "list temperatures")
    @ResponseBody
    public List<Temperature> list() {
        return service.list();
    }


}
