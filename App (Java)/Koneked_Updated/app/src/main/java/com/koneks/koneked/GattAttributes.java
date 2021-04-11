/**
 * @author Khaled Elmalawany
 * @version 1.3
 * @since 1.0
 */


package com.koneks.koneked;

import java.util.HashMap;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class GattAttributes {
    private static final HashMap<String, String> attributes = new HashMap();
    public static String TRANSMITTER_AND_RECEIVER_SERVICE = "0000181a-0000-1000-8000-00805f9b34fb";
    public static String TRANSMITTER_CHARACTERISTIC = "00002a58-0000-1000-8000-00805f9b34fb";
    public static String RECEIVER_CHARACTERISTIC = "00002a3d-0000-1000-8000-00805f9b34fb";
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    static {
        // Available Services.
        attributes.put("00001800-0000-1000-8000-00805f9b34fb", "Generic Access Profile");
        attributes.put("00001801-0000-1000-8000-00805f9b34fb", "Generic Attribute Service");
        attributes.put(TRANSMITTER_AND_RECEIVER_SERVICE, "Transmitter and Receiver Service");
        // Available Characteristics.
        attributes.put("00002a00-0000-1000-8000-00805f9b34fb", "Device Name");
        attributes.put("00002a01-0000-1000-8000-00805f9b34fb", "Appearance");
        attributes.put("00002a05-0000-1000-8000-00805f9b34fb", "Service Changed");
        attributes.put(RECEIVER_CHARACTERISTIC, "Receiver Characteristic");
        attributes.put(TRANSMITTER_CHARACTERISTIC, "Transmitter Characteristic");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}