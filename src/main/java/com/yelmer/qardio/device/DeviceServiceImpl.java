package com.yelmer.qardio.device;

import com.yelmer.qardio.device.dto.AddRequest;
import com.yelmer.qardio.device.dto.EditRequest;
import com.yelmer.qardio.exception.*;
import com.yelmer.qardio.temperature.TemperatureServiceImpl;
import com.yelmer.qardio.temperature.dto.SaveRequest;
import com.yelmer.qardio.util.Validations;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Device service implementation
 */
@Service
public class DeviceServiceImpl implements DeviceService {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(DeviceServiceImpl.class);
    /**
     * To use easily in non-dependency-injected classes
     */
    public static DeviceServiceImpl INSTANCE;

    private final DeviceRepository repository;
    private String testApiKey;

    @Autowired
    public DeviceServiceImpl(DeviceRepository repository) {
        this.repository = repository;
        this.testApiKey = null;
        this.mockOnlyDevices();
        INSTANCE = this;
    }


    private Device insertOne(String name, String mac, String apiKey) {
        try {
            final AddRequest dto = new AddRequest();
            dto.setEnabled(true);
            dto.setName(name);
            dto.setMac(mac);
            dto.setApiKey(apiKey);
            return this.add(dto);
        } catch (Exception e) {
            LOG.info(e.getMessage());
            return null;
        }
    }

    @Override
    public Device add(AddRequest dto) {
        LOG.info("device - add");
        final Device device = new Device();
        dto.setName(Validations.toTrimmedString(dto.getName()));
        dto.setMac(Validations.toTrimmedString(dto.getMac()));
        dto.setApiKey(Validations.toTrimmedString(dto.getApiKey()));
        if (!Validations.isValidMacAddress(dto.getMac())) {
            throw new EmptyMacAddressException();
        }
        Device duplicatedMac = this.repository.findByMacAddress(dto.getMac());
        if (duplicatedMac != null) {
            throw new DuplicatedException("macAddress", dto.getMac());
        }
        if (dto.getName() == null) {
            throw new EmptyNameException();
        }
        if (dto.getApiKey() == null) {
            dto.setApiKey(UUID.randomUUID().toString());
        } else if (!Validations.isValidUuid(dto.getApiKey())) {
            throw new InvalidApiKeyException(dto.getApiKey());
        } else {
            final Device duplicatedApiKey = this.repository.findByApiKey(dto.getApiKey());
            if (duplicatedApiKey != null) {
                throw new DuplicatedException("apiKey", dto.getApiKey());
            }
        }
        if (dto.getEnabled() == null) {
            dto.setEnabled(false);
        }
        device.setMac(dto.getMac());
        device.setName(dto.getName());
        device.setApiKey(dto.getApiKey());
        device.setEnabled(dto.getEnabled());
        this.repository.save(device);
        return device;
    }

    @Override
    public Device edit(String id, EditRequest dto) {
        LOG.info("device - edit");
        final Optional<Device> optionalDevice = this.repository.findById(id);
        if (!optionalDevice.isPresent()) {
            throw new NotFoundException("id", id);
        }
        final Device device = optionalDevice.get();
        dto.setName(Validations.toTrimmedString(dto.getName()));
        dto.setApiKey(Validations.toTrimmedString(dto.getApiKey()));
        if (dto.getName() == null) {
            throw new EmptyNameException();
        }
        if (dto.getApiKey() == null) {
            dto.setApiKey(UUID.randomUUID().toString());
        } else if (!Validations.isValidUuid(dto.getApiKey())) {
            throw new InvalidApiKeyException(dto.getApiKey());
        } else {
            final Device duplicated = this.repository.findByApiKey(dto.getApiKey(), id);
            if (duplicated != null) {
                throw new DuplicatedException("apiKey", dto.getApiKey());
            }
        }
        if (dto.getEnabled() == null) {
            dto.setEnabled(false);
        }
        device.setName(dto.getName());
        device.setApiKey(dto.getApiKey());
        device.setEnabled(dto.getEnabled());
        this.repository.save(device);
        return device;
    }

    @Override
    public Device resetApiKey(String id) {
        LOG.info("device - reset api key");
        final Optional<Device> optionalDevice = this.repository.findById(id);
        if (!optionalDevice.isPresent()) {
            throw new NotFoundException("id", id);
        }
        final Device device = optionalDevice.get();
        device.setApiKey(UUID.randomUUID().toString());
        this.repository.save(device);
        return device;
    }

    @Override
    public Device getById(String id) {
        LOG.info("device - get by id");
        final Optional<Device> optionalDevice = this.repository.findById(id);
        if (!optionalDevice.isPresent()) {
            throw new NotFoundException("id", id);
        }
        return optionalDevice.get();
    }

    @Override
    public Device getByApiKey(String apiKey) {
        LOG.info("device - get by api key");
        final Device device = this.repository.findByApiKey(apiKey);
        if (device == null) {
            throw new NotFoundException("apiKey", apiKey);
        }
        return device;
    }

    @Override
    public Device getByMacAddress(String macAddress) {
        LOG.info("device - get by mac address");
        final Device device = this.repository.findByMacAddress(macAddress);
        if (device == null) {
            throw new NotFoundException("macAddress", macAddress);
        }
        return device;

    }

    @Override
    public List<Device> list() {
        LOG.info("device - list");
        return this.repository.findAll();
    }

    public String getEffectiveApiKey() {
        return (this.testApiKey == null) ? SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString() : this.testApiKey;
    }

    public Device loadByApiKey(String apiKey) {
        return this.repository.findByApiKey(apiKey);
    }

    public Device loadByApiKey() {
        return this.repository.findByApiKey(getEffectiveApiKey());
    }

    public void setTestApiKey(String apiKey) {
        LOG.info("device - set api key");
        this.testApiKey = apiKey;
    }

    public String mockOnlyDevices() {
        LOG.info("temperature - mockOnlyDevices");
        Device device;
        String apiKey = null;
        device = this.insertOne("Device #1", "39-A5-66-4B-B6-D9", "187ae038-2fdb-4903-bdaf-1ab699e74c19");
        if (device != null) {
            apiKey = device.getApiKey();
        }
        device = this.insertOne("Device #2", "39-A5-66-5B-B6-D9", "aad14ba2-b3aa-4412-87a7-1cb31a940f24");
        if (device != null && apiKey == null) {
            apiKey = device.getApiKey();
        }
        device = this.insertOne("Device #3", "39-A5-66-3B-B6-D9", "9aecd20d-04c9-46fa-a7c6-cb758ad2faf9");
        if (device != null && apiKey == null) {
            apiKey = device.getApiKey();
        }
        device = this.insertOne("Device #4", "B9-02-F4-D6-CC-87", "79caeda7-06e1-4213-92e0-a7f53c3feb05");
        if (device != null && apiKey == null) {
            apiKey = device.getApiKey();
        }
        device = this.insertOne("Device #5", "B9-02-A4-D6-CC-87", "b83af1f3-ba07-4a5b-854e-32539bd02ef1");
        if (device != null && apiKey == null) {
            apiKey = device.getApiKey();
        }
        device = this.insertOne("Device #6", "C9-02-A4-D6-CC-87", "ab15cc12-c0dd-42d9-9b66-25ea2630c94b");
        if (device != null && apiKey == null) {
            apiKey = device.getApiKey();
        }
        return apiKey;
    }
    @Override
    public Map<String, String> mock() {
        final Map<String, String> result = new HashMap<>();
        LOG.info("temperature - mock");
        setTestApiKey(this.mockOnlyDevices());
        final List<SaveRequest> list = new ArrayList<>();
        try {
            final TemperatureServiceImpl temperatureService = TemperatureServiceImpl.INSTANCE;
            for (int i = 0; i < 100; i++) {
                final SaveRequest dto = new SaveRequest();
                dto.setDegree(temperatureService.randomDegree());
                dto.setOccurredAt(temperatureService.randomOccurredAt());
                list.add(dto);
            }
            List<String> ids = temperatureService.saveAsBulk(list);
            for (String id : ids) {
                result.put(id, "temperature");
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        setTestApiKey(null);
        return result;
    }

}
