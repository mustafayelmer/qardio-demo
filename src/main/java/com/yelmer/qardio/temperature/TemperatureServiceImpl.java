package com.yelmer.qardio.temperature;

import com.yelmer.qardio.device.Device;
import com.yelmer.qardio.device.DeviceServiceImpl;
import com.yelmer.qardio.exception.*;
import com.yelmer.qardio.temperature.dto.AggregationResponse;
import com.yelmer.qardio.temperature.dto.SaveRequest;
import com.yelmer.qardio.temperature.enumeration.AggregationType;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class TemperatureServiceImpl implements TemperatureService {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(TemperatureServiceImpl.class);
    /**
     * To use easily in non-dependency-injected classes
     * */
    public static TemperatureServiceImpl INSTANCE;

    private final TemperatureRepository repository;

    @Autowired
    public TemperatureServiceImpl(TemperatureRepository repository) {
        this.repository = repository;
        INSTANCE = this;
    }
    /**
     * Add entity
     * */
    private String addOne(SaveRequest dto, boolean offline) {
        final Temperature temperature = new Temperature();
        // degree should not be empty
        if (dto.getDegree() == null) {
            throw new EmptyDegreeException();
        }
        // degree should be in range
        if (dto.getDegree() < -100 || dto.getDegree() > 1000) {
            throw new OutOfRangeDegreeException(dto.getDegree());
        }
        temperature.setDegree(dto.getDegree());
        if (dto.getOccurredAt() == null) {
            // for offline usage, time is required
            if (offline) {
                throw new EmptyTimeInBulkException();
            }
            // if occured-time is null then, is also now
            dto.setOccurredAt(LocalDateTime.now());
        }
        temperature.setOccurredAt(dto.getOccurredAt());
        temperature.setDevice(DeviceServiceImpl.INSTANCE.loadByApiKey());
        temperature.setOffline(offline);
        this.repository.save(temperature);
        return temperature.getId();
    }
    public Double randomDegree() {
        return (double) ThreadLocalRandom.current().nextLong(-100, 1000);
    }

    public LocalDateTime randomOccurredAt() {
        final LocalDateTime now = LocalDateTime.now();
        final long max = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), now.getMinute(), now.getSecond(), 0).toEpochSecond(ZoneOffset.UTC);
        final long min = max - (3 * 60 * 60 * 24);
        final long randomDay = ThreadLocalRandom.current().nextLong(min, max);
        return LocalDateTime.ofEpochSecond(randomDay, 0, ZoneOffset.UTC);
        // final LocalDateTime result = LocalDateTime.ofEpochSecond(randomDay, 0, ZoneOffset.UTC);
        // System.out.println(result);
        // return result;
    }

    @Override
    public Temperature getById(String id) {
        LOG.info("temperature - get by id");
        final Optional<Temperature> optionalTemperature = this.repository.findById(id);
        if (!optionalTemperature.isPresent()) {
            throw new NotFoundException("id", id);
        }
        return optionalTemperature.get();
    }

    @Override
    public List<String> save(SaveRequest dto) {
        LOG.info("temperature - save");
        final List<String> ids = new ArrayList<>();
        ids.add(this.addOne(dto, false));
        return ids;
    }

    @Override
    public List<String> saveAsBulk(List<SaveRequest> list) {
        LOG.info("temperature - save bulk");
        if (list.size() < 1) {
            throw new EmptyBulkListException();
        }
        final List<String> ids = new ArrayList<>();
        for (SaveRequest dto : list) {
            ids.add(this.addOne(dto, true));
        }
        return ids;
    }

    @Override
    public AggregationResponse aggregate(AggregationType type) {
        LOG.info("temperature - aggregate - " + type);
        final LocalDateTime now = LocalDateTime.now();
        LocalDateTime latest;
        if (type.equals(AggregationType.DAILY)) {
            latest = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0, 0);
        } else {
            latest = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), 0, 0, 0);
        }
        final AggregationResponse result = this.repository.listAggregation(DeviceServiceImpl.INSTANCE.loadByApiKey(), latest);
        System.out.println(result);
        return result;
    }

    @Override
    public List<Temperature> list() {
        LOG.info("temperature - list");
        return this.repository.listByDevice(DeviceServiceImpl.INSTANCE.loadByApiKey());
    }
}
