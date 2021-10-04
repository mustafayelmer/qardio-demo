package com.yelmer.qardio;

import com.yelmer.qardio.device.Device;
import com.yelmer.qardio.device.DeviceController;
import com.yelmer.qardio.device.DeviceService;
import com.yelmer.qardio.device.DeviceServiceImpl;
import com.yelmer.qardio.device.dto.AddRequest;
import com.yelmer.qardio.device.dto.EditRequest;
import com.yelmer.qardio.exception.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class DeviceTests {

    @Autowired
    DeviceController controller;


    @Test
    void controllerShouldBeCreated() {
        final String prefix = "\t\u001B[31mdevice.controller\u001B[0m > ";
        System.out.println(prefix + "\u001B[35mcomponent\u001B[0m shouldBeCreated");
        assertThat(controller).isNotNull();
    }

    @Test
    void serviceShouldBeCreated() {
        final String prefix = "\t\u001B[31mdevice.service\u001B[0m > ";
        System.out.println(prefix + "\u001B[35mcomponent\u001B[0m shouldBeCreated");
        assertThat(DeviceServiceImpl.INSTANCE).isNotNull();
    }

    @Test
    void addShouldNotRaiseError() {
        final DeviceService service = DeviceServiceImpl.INSTANCE;
        //         this.insertOne("Device #1", "39-A5-66-4B-B6-D9", "187ae038-2fdb-4903-bdaf-1ab699e74c19");
        final AddRequest dto = new AddRequest();
        final String prefix = "\t\u001B[31mdevice.service.add()\u001B[0m ";

        System.out.println(prefix + "\u001B[36mmacAddress\u001B[0m shouldNotBeEmpty");
        assertThatThrownBy(() -> service.add(dto)).isInstanceOf(EmptyMacAddressException.class);

        System.out.println(prefix + "\u001B[36mmacAddress\u001B[0m shouldNotBeDuplicated");
        dto.setMac("39-A5-66-4B-B6-D9");
        assertThatThrownBy(() -> service.add(dto)).isInstanceOf(DuplicatedException.class);

        System.out.println(prefix + "\u001B[36mname\u001B[0m shouldNotBeEmpty");
        dto.setMac("59-A5-66-4B-B6-D9");
        assertThatThrownBy(() -> service.add(dto)).isInstanceOf(EmptyNameException.class);


        System.out.println(prefix + "\u001B[36mapiKey\u001B[0m shouldBeValidUuid");
        dto.setName("Device 99");
        dto.setApiKey("foo-bar");
        assertThatThrownBy(() -> service.add(dto)).isInstanceOf(InvalidApiKeyException.class);

        System.out.println(prefix + "\u001B[36mapiKey\u001B[0m shouldNotBeDuplicated");
        dto.setApiKey("ab15cc12-c0dd-42d9-9b66-25ea2630c94b");
        assertThatThrownBy(() -> service.add(dto)).isInstanceOf(DuplicatedException.class);

        System.out.println(prefix + "\u001B[34mentity\u001B[0m shouldBeAdded \u001B[33mgenerated apiKey\u001B[0m");
        dto.setApiKey(null);
        assertThat(service.add(dto)).isInstanceOf(Device.class);

        System.out.println(prefix + "\u001B[34mentity\u001B[0m shouldBeAdded \u001B[33mgiven apiKey\u001B[0m");
        dto.setMac("99-A5-66-4B-B6-D9");
        dto.setApiKey("287ae038-2fdb-4903-bdaf-1ab699e74c19");
        assertThat(service.add(dto)).isInstanceOf(Device.class);
    }

    @Test
    void editShouldNotRaiseError() {
        final DeviceService service = DeviceServiceImpl.INSTANCE;
        final Device device = service.getByMacAddress("39-A5-66-4B-B6-D9");
        final EditRequest dto = new EditRequest();
        final String prefix = "\t\u001B[31mdevice.service.edit()\u001B[0m > ";
        final String notFoundUuid = UUID.randomUUID().toString();

        System.out.println(prefix + "\u001B[34mentity\u001B[0m shouldBeExist");
        assertThatThrownBy(() -> service.edit(notFoundUuid, dto)).isInstanceOf(NotFoundException.class);

        System.out.println(prefix + "\u001B[36mname\u001B[0m shouldNotBeEmpty");
        assertThatThrownBy(() -> service.edit(device.getId(), dto)).isInstanceOf(EmptyNameException.class);

        System.out.println(prefix + "\u001B[36mapiKey\u001B[0m shouldBeValidUuid");
        dto.setName("Device 99");
        dto.setApiKey("foo-bar");
        assertThatThrownBy(() -> service.edit(device.getId(), dto)).isInstanceOf(InvalidApiKeyException.class);

        System.out.println(prefix + "\u001B[36mapiKey\u001B[0m shouldNotBeDuplicated");
        dto.setApiKey("aad14ba2-b3aa-4412-87a7-1cb31a940f24");
        assertThatThrownBy(() -> service.edit(device.getId(), dto)).isInstanceOf(DuplicatedException.class);

        System.out.println(prefix + "\u001B[34mentity\u001B[0m shouldBeEdited \u001B[33mgenerated apiKey\u001B[0m");
        dto.setApiKey(null);
        assertThat(service.edit(device.getId(), dto)).isInstanceOf(Device.class);

        System.out.println(prefix + "\u001B[34mentity\u001B[0m shouldBeEdited \u001B[33mgiven apiKey\u001B[0m");
        dto.setApiKey("387ae038-2fdb-4903-bdaf-1ab699e74c19");
        assertThat(service.edit(device.getId(), dto)).isInstanceOf(Device.class);

        final Device updated = service.getByMacAddress("39-A5-66-4B-B6-D9");

        System.out.println(prefix + "\u001B[36mapiKey\u001B[0m shouldBeChanged");
        assertThat(device.getApiKey()).isNotEqualTo(updated.getApiKey());

        System.out.println(prefix + "\u001B[36mname\u001B[0m shouldBeChanged");
        assertThat(device.getName()).isNotEqualTo(updated.getName());

    }

    @Test
    void resetShouldNotRaiseError() {
        final DeviceService service = DeviceServiceImpl.INSTANCE;
        final Device device = service.getByMacAddress("39-A5-66-4B-B6-D9");
        final String prefix = "\t\u001B[31mdevice.service.resetApiKey()\u001B[0m > ";
        final String notFoundUuid = UUID.randomUUID().toString();

        System.out.println(prefix + "\u001B[34mentity\u001B[0m shouldBeExist");
        assertThatThrownBy(() -> service.resetApiKey(notFoundUuid)).isInstanceOf(NotFoundException.class);

        System.out.println(prefix + "\u001B[34mentity\u001B[0m shouldBeReset");
        assertThat(service.resetApiKey(device.getId())).isInstanceOf(Device.class);

        final Device updated = service.getByMacAddress("39-A5-66-4B-B6-D9");

        System.out.println(prefix + "\u001B[36mapiKey\u001B[0m shouldBeChanged");
        assertThat(device.getApiKey()).isNotEqualTo(updated.getApiKey());
    }

    @Test
    void getByIdShouldNotRaiseError() {
        final DeviceService service = DeviceServiceImpl.INSTANCE;
        final Device device = service.getByMacAddress("39-A5-66-4B-B6-D9");
        final String prefix = "\t\u001B[31mdevice.service.getById()\u001B[0m > ";
        final String notFoundUuid = UUID.randomUUID().toString();

        System.out.println(prefix + "\u001B[34mentity\u001B[0m shouldBeExist");
        assertThatThrownBy(() -> service.getById(notFoundUuid)).isInstanceOf(NotFoundException.class);

        System.out.println(prefix + "\u001B[34mentity\u001B[0m shouldBeFound");
        assertThat(service.getById(device.getId())).isInstanceOf(Device.class);
    }

    @Test
    void getByApiKeyShouldNotRaiseError() {
        final DeviceService service = DeviceServiceImpl.INSTANCE;
        final Device device = service.getByMacAddress("39-A5-66-4B-B6-D9");
        final String prefix = "\t\u001B[31mdevice.service.getByApiKey()\u001B[0m > ";
        final String notFoundUuid = UUID.randomUUID().toString();

        System.out.println(prefix + "\u001B[34mentity\u001B[0m shouldBeExist");
        assertThatThrownBy(() -> service.getByApiKey(notFoundUuid)).isInstanceOf(NotFoundException.class);

        System.out.println(prefix + "\u001B[34mentity\u001B[0m shouldBeFound");
        assertThat(service.getByApiKey(device.getApiKey())).isInstanceOf(Device.class);
    }

    @Test
    void getByMacAddressShouldNotRaiseError() {
        final DeviceService service = DeviceServiceImpl.INSTANCE;
        final String prefix = "\t\u001B[31mdevice.service.getByMacAddress()\u001B[0m > ";
        final String foundMac = "39-A5-66-4B-B6-D9";
        final String notFoundMac = "39-A5-66-4B-B6-D0";

        System.out.println(prefix + "\u001B[34mentity\u001B[0m shouldBeExist");
        assertThatThrownBy(() -> service.getByMacAddress(notFoundMac)).isInstanceOf(NotFoundException.class);

        System.out.println(prefix + "\u001B[34mentity\u001B[0m shouldBeFound");
        assertThat(service.getByMacAddress(foundMac)).isInstanceOf(Device.class);
    }

    @Test
    void listShouldNotRaiseError() {
        final DeviceService service = DeviceServiceImpl.INSTANCE;
        final String prefix = "\t\u001B[31mdevice.service.list()\u001B[0m > ";

        System.out.println(prefix + "\u001B[34mentity\u001B[0m shouldNotThrow");
        assertThat(service.list()).isInstanceOf(List.class);

        System.out.println(prefix + "\u001B[34mentity\u001B[0m shouldNotBeEmptyList");
        assertThat(service.list().size()).isGreaterThanOrEqualTo(1);

    }

}
