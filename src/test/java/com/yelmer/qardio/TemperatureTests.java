package com.yelmer.qardio;

import com.yelmer.qardio.device.Device;
import com.yelmer.qardio.device.DeviceServiceImpl;
import com.yelmer.qardio.exception.*;
import com.yelmer.qardio.temperature.Temperature;
import com.yelmer.qardio.temperature.TemperatureController;
import com.yelmer.qardio.temperature.TemperatureServiceImpl;
import com.yelmer.qardio.temperature.dto.AggregationResponse;
import com.yelmer.qardio.temperature.dto.SaveRequest;
import com.yelmer.qardio.temperature.enumeration.AggregationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class TemperatureTests {

    @Autowired
    TemperatureController controller;


    @Test
    void controllerShouldBeCreated() {
        final String prefix = "\t\u001B[31mtemperature.controller\u001B[0m > ";
        System.out.println(prefix + "\u001B[35mcomponent\u001B[0m shouldBeCreated");
        assertThat(controller).isNotNull();
    }

    @Test
    void serviceShouldBeCreated() {
        final String prefix = "\t\u001B[31mtemperature.service\u001B[0m > ";
        System.out.println(prefix + "\u001B[35mcomponent\u001B[0m shouldBeCreated");
        assertThat(TemperatureServiceImpl.INSTANCE).isNotNull();
    }

    @Test
    void addShouldNotRaiseError() {
        // fake user
        final Device device = DeviceServiceImpl.INSTANCE.getByMacAddress("39-A5-66-4B-B6-D9");
        DeviceServiceImpl.INSTANCE.setTestApiKey(device.getApiKey());

        final TemperatureServiceImpl service = TemperatureServiceImpl.INSTANCE;
        final SaveRequest dto = new SaveRequest();
        final String prefix = "\t\u001B[31mtemperature.service.save()\u001B[0m ";

        System.out.println(prefix + "\u001B[36mdegree\u001B[0m shouldNotBeEmpty");
        assertThatThrownBy(() -> service.save(dto)).isInstanceOf(EmptyDegreeException.class);

        dto.setDegree(-1000.0);
        System.out.println(prefix + "\u001B[36mdegree\u001B[0m shouldBeInRange \u001B[33m >= -100°\u001B[0m");
        assertThatThrownBy(() -> service.save(dto)).isInstanceOf(OutOfRangeDegreeException.class);

        dto.setDegree(3000.0);
        System.out.println(prefix + "\u001B[36mdegree\u001B[0m shouldBeInRange \u001B[33m <= 1000°\u001B[0m");
        assertThatThrownBy(() -> service.save(dto)).isInstanceOf(OutOfRangeDegreeException.class);

        dto.setDegree(service.randomDegree());
        System.out.println(prefix + "\u001B[34mentity\u001B[0m shouldBeAdded \u001B[33mempty time\u001B[0m");
        assertThat(service.save(dto).size()).isGreaterThanOrEqualTo(1);

        dto.setDegree(service.randomDegree());
        dto.setOccurredAt(service.randomOccurredAt());
        System.out.println(prefix + "\u001B[34mentity\u001B[0m shouldBeAdded \u001B[33mgiven time\u001B[0m");
        assertThat(service.save(dto).size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    void saveAsBulkShouldNotRaiseError() {
        // fake user
        final Device device = DeviceServiceImpl.INSTANCE.getByMacAddress("39-A5-66-4B-B6-D9");
        DeviceServiceImpl.INSTANCE.setTestApiKey(device.getApiKey());

        final TemperatureServiceImpl service = TemperatureServiceImpl.INSTANCE;
        final List<SaveRequest> list = new ArrayList<>();

        String prefix = "\t\u001B[31mtemperature.service.saveAsBulk()\u001B[0m ";

        System.out.println(prefix + "\u001B[34mentities\u001B[0m shouldNotBeEmptyArray");
        assertThatThrownBy(() -> service.saveAsBulk(list)).isInstanceOf(EmptyBulkListException.class);

        for (int i = 0; i < 101; i++) {
            final SaveRequest dto = new SaveRequest();
            dto.setDegree(service.randomDegree());
            if (i == 0) {
                list.add(dto);
                System.out.println(prefix + "\u001B[36moccurredAt\u001B[0m shouldNotBeEmtpy");
                assertThatThrownBy(() -> service.saveAsBulk(list)).isInstanceOf(EmptyTimeInBulkException.class);
                list.clear();
            } else {
                dto.setOccurredAt(service.randomOccurredAt());
                list.add(dto);
            }
        }
        System.out.println(prefix + "\u001B[34mentities\u001B[0m shouldBeAdded \u001B[33msize: " + list.size() + "\u001B[0m");
        assertThat(service.saveAsBulk(list).size()).isEqualTo(list.size());

        prefix = "\t\u001B[31mtemperature.service.list()\u001B[0m ";
        System.out.println(prefix + "\u001B[36mentities\u001B[0m shouldBeFilled");
        assertThat(service.list().size()).isGreaterThanOrEqualTo(list.size());

        prefix = "\t\u001B[31mtemperature.service.aggregate()\u001B[0m ";
        System.out.println(prefix + "\u001B[36msummary\u001B[0m shouldBeFilled");
        assertThat(service.aggregate(AggregationType.DAILY)).isInstanceOf(AggregationResponse.class);

        System.out.println(prefix + "\u001B[36msummary\u001B[0m shouldBeFilled");
        assertThat(service.aggregate(AggregationType.HOURLY)).isInstanceOf(AggregationResponse.class);

    }

    @Test
    void getByIdShouldNotRaiseError() {
        // fake user
        final Device device = DeviceServiceImpl.INSTANCE.getByMacAddress("39-A5-66-4B-B6-D9");
        DeviceServiceImpl.INSTANCE.setTestApiKey(device.getApiKey());

        final TemperatureServiceImpl service = TemperatureServiceImpl.INSTANCE;
        final String prefix = "\t\u001B[31mtemperature.service.getById()\u001B[0m ";

        final SaveRequest dto = new SaveRequest();
        dto.setDegree(service.randomDegree());
        dto.setOccurredAt(service.randomOccurredAt());
        final List<String> ids = service.save(dto);

        final String notFoundUuid = UUID.randomUUID().toString();

        System.out.println(prefix + "\u001B[34mentity\u001B[0m shouldBeExist");
        assertThatThrownBy(() -> service.getById(notFoundUuid)).isInstanceOf(NotFoundException.class);

        System.out.println(prefix + "\u001B[34mentity\u001B[0m shouldBeFound");
        assertThat(service.getById(ids.get(0))).isInstanceOf(Temperature.class);
    }

}
