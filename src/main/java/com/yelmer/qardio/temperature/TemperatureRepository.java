package com.yelmer.qardio.temperature;

import com.yelmer.qardio.device.Device;
import com.yelmer.qardio.temperature.dto.AggregationResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TemperatureRepository extends JpaRepository<Temperature, String> {

    @Query("SELECT new com.yelmer.qardio.temperature.dto.AggregationResponse(AVG(t.degree), COUNT(t.degree)) "
            + "FROM Temperature t WHERE t.device = ?1 AND t.occurredAt >= ?2")
    AggregationResponse listAggregation(Device device, LocalDateTime lastTime);

    @Query("SELECT t FROM Temperature t WHERE t.device = ?1 ORDER BY t.occurredAt")
    List<Temperature> listByDevice(Device device);
}
