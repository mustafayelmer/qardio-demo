package com.yelmer.qardio.util;

import java.util.UUID;
import java.util.regex.Pattern;

public class Validations {
    public static String toTrimmedString(String value) {
        if (value == null) {
            return null;
        }
        value = value.trim();
        return (value.equals("")) ? null : value;
    }

    public static boolean isValidMacAddress(String value) {
        // If the string is null, return false
        if (value == null) {
            return false;
        }
        // Compile the ReGex
        String REGEX = "^([0-9A-Fa-f]{2}[:-])"
                + "{5}([0-9A-Fa-f]{2})|"
                + "([0-9a-fA-F]{4}\\."
                + "[0-9a-fA-F]{4}\\."
                + "[0-9a-fA-F]{4})$";
        Pattern p = Pattern.compile(REGEX);
        // Find match between given string and regular expression
        return p.matcher(value).matches();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isValidUuid(String value) {
        try {
            //noinspection ResultOfMethodCallIgnored
            UUID.fromString(value);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

}
