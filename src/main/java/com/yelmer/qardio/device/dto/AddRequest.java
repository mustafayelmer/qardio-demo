package com.yelmer.qardio.device.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Add input dto
 * */
@Getter
@Setter
public class AddRequest {
    private String mac;
    private String name;
    private String apiKey;
    private Boolean enabled;
}
