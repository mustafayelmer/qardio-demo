package com.yelmer.qardio.auth;

import com.yelmer.qardio.device.Device;
import com.yelmer.qardio.device.DeviceServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Handles authenticating api keys against the database.
 */
public class ApiKeyAuthManager implements AuthenticationManager {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String principal = (String) authentication.getPrincipal();
        final Device device = DeviceServiceImpl.INSTANCE.loadByApiKey(principal);
        if (device == null) {
            throw new BadCredentialsException("The API key was not found or not the expected value.");
        } else {
            authentication.setAuthenticated(true);
            return authentication;
        }
    }
}