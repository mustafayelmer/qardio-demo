package com.yelmer.qardio.device.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Edit input dto
 * */
@Getter
@Setter
public class EditRequest {
    private String name;
    private String apiKey;
    private Boolean enabled;
}
