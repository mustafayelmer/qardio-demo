package com.yelmer.qardio.device;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Device repository & dao
 * */
@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {
    @Query(value = "SELECT d FROM Device d WHERE d.apiKey = ?1")
    Device findByApiKey(String apiKey);

    @Query(value = "SELECT d FROM Device d WHERE d.apiKey = ?1 AND d.id != ?2")
    Device findByApiKey(String apiKey, String id);

    @Query(value = "SELECT d FROM Device d WHERE d.mac = ?1")
    Device findByMacAddress(String macAddress);
}
