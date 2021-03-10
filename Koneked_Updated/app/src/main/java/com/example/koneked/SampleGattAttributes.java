package com.example.koneked;

import java.util.HashMap;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class SampleGattAttributes {
    private static final HashMap<String, String> attributes = new HashMap();
    public static String MICROPHONE_SERVICE = "0000181a-0000-1000-8000-00805f9b34fb";
    public static String TRANSMITTER_CHARACTERISTIC = "00002a58-0000-1000-8000-00805f9b34fb";
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    static {
        // Sample Services.
        attributes.put("00001800-0000-1000-8000-00805f9b34fb", "Generic Access Profile");
        attributes.put(MICROPHONE_SERVICE, "Microphone Service");
        // Sample Characteristics.
        attributes.put("00002a00-0000-1000-8000-00805f9b34fb", "Device Name");
        attributes.put("00002a01-0000-1000-8000-00805f9b34fb", "Appearance");
        attributes.put("00002a3d-0000-1000-8000-00805f9b34fb", "Receiver Characteristic");
        attributes.put(TRANSMITTER_CHARACTERISTIC, "Transmitter Characteristic");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}