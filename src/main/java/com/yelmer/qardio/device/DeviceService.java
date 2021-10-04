package com.yelmer.qardio.device;


import com.yelmer.qardio.device.dto.AddRequest;
import com.yelmer.qardio.device.dto.EditRequest;

import java.util.List;
import java.util.Map;

/**
 * Device service interface
 * */
public interface DeviceService {
    Device add(AddRequest dto);

    Device edit(String id, EditRequest dto);

    Device resetApiKey(String id);

    Device getById(String id);

    Device getByApiKey(String apiKey);

    Device getByMacAddress(String macAddress);

    List<Device> list();

    Map<String, String> mock();
}
