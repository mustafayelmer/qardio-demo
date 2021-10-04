package com.yelmer.qardio.temperature;


import com.yelmer.qardio.device.Device;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "temperature")
@Data
public class Temperature {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id = UUID.randomUUID().toString();

    @Column(name = "occurred_at", updatable = false, nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime occurredAt = LocalDateTime.now();

    @Column(name = "degree", updatable = false, nullable = false)
    @NotNull(message = "Degree can not be null!")
    private Double degree = 0.0;

    @Column(name = "offline", updatable = false, nullable = false)
    private Boolean offline = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private Device device;
}
