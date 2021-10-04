package com.yelmer.qardio.device;

import com.yelmer.qardio.device.dto.AddRequest;
import com.yelmer.qardio.device.dto.EditRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Device controller
 * look security later
 * */
@SuppressWarnings("unused")
@RestController
@RequestMapping("/v1/devices")
public class DeviceController {

    private final DeviceService service;

    @Autowired
    public DeviceController(DeviceService service) {
        this.service = service;
    }

    @PostMapping(name = "add device")
    @ResponseBody
    public Device add(@RequestBody AddRequest dto) {
        return service.add(dto);
    }

    @PutMapping(value = "/{id}", name = "edit device")
    @ResponseBody
    public Device edit(@PathVariable String id, @RequestBody EditRequest dto) {
        return service.edit(id, dto);
    }

    @PatchMapping(value = "/{id}", name = "reset/refresh api-key")
    @ResponseBody
    public Device resetApiKey(@PathVariable String id) {
        return service.resetApiKey(id);
    }

    @GetMapping(value = "/{id}", name = "fetch device by id")
    @ResponseBody
    public Device getById(@PathVariable String id) {
        return service.getById(id);
    }

    @GetMapping(value = "/{apiKey}/api-key", name = "fetch device by api-key")
    @ResponseBody
    public Device getByApiKey(@PathVariable String apiKey) {
        return service.getByApiKey(apiKey);
    }

    @GetMapping(value = "/{macAddress}/mac-address", name = "fetch device by mac-address")
    @ResponseBody
    public Device getByMacAddress(@PathVariable String macAddress) {
        return service.getByMacAddress(macAddress);
    }

    @GetMapping(name = "list devices")
    @ResponseBody
    public List<Device> list() {
        return service.list();
    }

    @GetMapping(value = "/mock", name = "inject mock data to test easily")
    @ResponseBody
    public Map<String, String> mock() {
        return service.mock();
    }
}
