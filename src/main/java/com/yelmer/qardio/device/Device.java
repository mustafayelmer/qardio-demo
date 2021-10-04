package com.yelmer.qardio.device;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Device entity
 * */
@Entity
@Table(name = "device")
@Data
public class Device {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id = UUID.randomUUID().toString();

    @Column(name = "created_at", updatable = false, nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "mac_address", nullable = false)
    @NotNull(message = "Mac address can not be null!")
    private String mac;

    @Column(name = "name", nullable = false)
    @NotNull(message = "Device name can not be null!")
    private String name;

    @Column(name = "api_key", nullable = false)
    @NotNull(message = "Api key can not be null!")
    private String apiKey;

    @Column(name = "is_enabled", nullable = false)
    private Boolean enabled = false;
}
