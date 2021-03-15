//TODO: Implement a method to not require manual toggling of location permissions
//TODO: Implement activity overlay instead of browser overlay
//TODO: Check out https://github.com/Fakher-Hakim/android-BluetoothLeGatt

package com.example.koneked;

import android.Manifest;
import android.bluetooth.*;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.*;
import android.util.Log;
import android.webkit.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    private Handler mHandler = null;
    private WebView browser = null;
    private BluetoothAdapter bluetoothAdapter;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    // Current state variables
    private final int NOTCONNECTED = 0;
    private final int SEARCHING = 1;
    private final int FOUND = 2;
    private final int CONNECTED = 3;
    private final int DISCOVERING = 4;
    private final int COMMUNICATING = 5;
    private final int TESTING_MOTOR = 6;
    private final int DISCONNECTING = 7;
    private final int INTERROGATE = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new Handler() {
            public void handleMessage(Message inputMessage) {
                switch (inputMessage.what) {
                    case NOTCONNECTED:
                        browser.loadUrl("javascript:setStatus('Not Connected');");
                        browser.loadUrl("javascript:setClassOnArduino('notconnected');");
                        break;
                    case SEARCHING:
                        browser.loadUrl("javascript:setStatus('Searching');");
                        browser.loadUrl("javascript:setClassOnArduino('searching');");
                        break;
                    case FOUND:
                        browser.loadUrl("javascript:setStatus('Found');");
                        break;
                    case CONNECTED:
                        browser.loadUrl("javascript:setStatus('Connected');");
                        browser.loadUrl("javascript:setClassOnArduino('discovering');");
                        break;
                    case TESTING_MOTOR:
                        browser.loadUrl("javascript:setStatus('Discovering');");
                        browser.loadUrl("javascript:setClassOnArduino('discovering');");
                        break;
                    case COMMUNICATING:
                        browser.loadUrl("javascript:setStatus('Communicating');");
                        browser.loadUrl("javascript:setClassOnArduino('communicating');");
                        break;
                    case DISCONNECTING:
                        browser.loadUrl("javascript:setStatus('Disconnecting');");
                        break;
                }
            }
        };

        browser = (WebView) this.findViewById(R.id.browser);

        // Set a webview client to override the default functionality
        browser.setWebViewClient(new wvClient());

        // Get settings to configure our WebView instance
        WebSettings settings = browser.getSettings();

        // Include JavaScript
        settings.setJavaScriptEnabled(true);

        // clear cache
        browser.clearCache(true);

        // this is necessary for "alert()" to work
        browser.setWebChromeClient(new WebChromeClient());

        // Add custom functionality to the javascript environment
        browser.addJavascriptInterface(new BLEUIHandler(), "bleui");

        // Load a page to get things started
        browser.loadUrl("file:///android_asset/index.html");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
        }

        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Log.i("BLEUIHandler", getString(R.string.bluetoot_connection_error));
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }

    }

    final class wvClient extends WebViewClient {
        public void onPageFinished(WebView view, String url) {
            // When web page is loaded, call a function that is contained within the page.
            // Functionally equivalent to placing an onload attribute in the <body> tag
            // whenever the loadUrl method is used
            //  --> essentially "injecting" code into the page when it is prefixed with "javascript:"
            browser.loadUrl("javascript:startup()");
        }
    }

    // Javascript handler
    final class BLEUIHandler {
        @JavascriptInterface
        public void connect() {
            Log.i("BLEUIHandler", "Connection the Arduino");
            mHandler.sendEmptyMessage(SEARCHING);
            bluetoothAdapter.getBluetoothLeScanner().startScan(new BLEFoundDevice(INTERROGATE));
        }

        @JavascriptInterface
        public void toggleMotor() {
            Log.i("BLEUIHandler", "Testing the motors");
            mHandler.sendEmptyMessage(SEARCHING);
            bluetoothAdapter.getBluetoothLeScanner().startScan(new BLEFoundDevice(TESTING_MOTOR));
        }

        @JavascriptInterface
        public void disconnect() {
            Log.i("BLEUIHandler", "Disconnecting");
            mHandler.sendEmptyMessage(DISCONNECTING);
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice("24:08:78:9b:35:b4");
        }
    }


    final class BLERemoteDevice extends BluetoothGattCallback {
        private final String tag = "BLEDEVICE";
        UUID serviceWeWant = new UUID(0x0000AA0100001000L,0x800000805f9b34fbL);
        UUID toggleMotor1UUID = new UUID(0x0000210100001000L ,0x800000805f9b34fbL);
        UUID toggleMotor2UUID = new UUID(0x0000210200001000L ,0x800000805f9b34fbL);
        UUID toggleMotor3UUID = new UUID(0x0000210300001000L ,0x800000805f9b34fbL);

        byte toggleMotorValue[] = {0x55};
        Queue<BLEQueueItem> taskQ = new LinkedList<BLEQueueItem>();
        private int mode = INTERROGATE;

        BLERemoteDevice(int mode) {
            this.mode = mode;
        }

        private void doNextThing(BluetoothGatt gatt) {
            Log.i(tag,"doNextThing");
            try {
                BLEQueueItem thisTask = taskQ.poll();
                if (thisTask != null) {
                    Log.i(tag,"processing " + thisTask.toString());
                    switch (thisTask.getAction()) {
                        case BLEQueueItem.READCHARACTERISTIC:
                            gatt.readCharacteristic((BluetoothGattCharacteristic) thisTask.getObject());
                            break;
                        case BLEQueueItem.WRITECHARACTERISTIC:
                            Log.i(tag,"Write out this Characteristic");
                            mHandler.sendEmptyMessage(TESTING_MOTOR);
                            BluetoothGattCharacteristic c = (BluetoothGattCharacteristic) thisTask.getObject();
                            Log.i(tag,"Value to be written is [" + c.getStringValue(0) + "]");
                            // c.setValue("U");
                            gatt.writeCharacteristic(c);
                            break;
                        case BLEQueueItem.READDESCRIPTOR:
                            gatt.readDescriptor((BluetoothGattDescriptor) thisTask.getObject());
                            break;
                        case BLEQueueItem.DISCONNECT:
                            mHandler.sendEmptyMessage(DISCONNECTING);
                            gatt.disconnect();
                            break;
                    }
                } else {
                    Log.i(tag,"Mo more tasks");
                }
            }
            catch (Exception e) {
                Log.i(tag,"Error in doNextThing " + e.getMessage());
            }
        }

        @Override
        public void onConnectionStateChange (BluetoothGatt gatt, int status, int newState) {
            Log.i(tag,"onConnectionStatChange [" + status + "][" + newState  + "]");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothGatt.STATE_CONNECTED) {
                    Log.i(tag,"Connected to [" + gatt.toString() + "]");
                    mHandler.sendEmptyMessage(DISCOVERING);
                    gatt.discoverServices();
                } else if (status == BluetoothGatt.STATE_DISCONNECTED) {
                    mHandler.sendEmptyMessage((NOTCONNECTED));
                }
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.i(tag,"OnServiceDiscovered ["+ status + "] " + gatt.toString());
            if (mode == INTERROGATE) {
                List<BluetoothGattService> services = gatt.getServices();
                for (int i = 0; i < services.size(); i++) {
                    Log.i(tag, "service [" + i + "] is [" + services.get(i).getUuid().toString() + "]");
                    if (serviceWeWant.equals(services.get(i).getUuid())) {
                        Log.i(tag, "************COOL, found it!!!");
                    }
                    UUID serviceUUID = services.get(i).getUuid();
                    List<BluetoothGattCharacteristic> schars = services.get(i).getCharacteristics();
                    for (int j = 0; j < schars.size(); j++) {
                        Log.i(tag, "characteristic [" + j + "] [" + schars.get(j).getUuid() + "] properties [" + schars.get(j).getProperties() + "]");
                        if ((schars.get(j).getProperties() & 2) == 2) {
                            taskQ.add(new BLEQueueItem(BLEQueueItem.READCHARACTERISTIC, schars.get(j).getUuid(), "Read Characteristic of Available Service", schars.get(j)));
                        } else {
                            Log.i(tag, "This Characteristic cannot be Read");
                        }
                        List<BluetoothGattDescriptor> scdesc = schars.get(j).getDescriptors();
                        for (int k = 0; k < scdesc.size(); k++) {
                            Log.i(tag, "Descriptor [" + k + "] [" + scdesc.get(k).toString() + "]");
                            Log.i(tag, "Descriptor UUID [" + scdesc.get(k).getUuid() + "]");
                            Log.i(tag, "Descriptor Permissions [" + scdesc.get(k).getPermissions() + "]");
                            //Log.i(tag,"Attempting to read this Descriptor");
                            taskQ.add(new BLEQueueItem(BLEQueueItem.READDESCRIPTOR, scdesc.get(k).getUuid(), "Read Descriptor of Characteristic", scdesc.get(k)));
                        }
                    }
                }
            }
            else if (mode == TESTING_MOTOR) {
                BluetoothGattService testMotors = gatt.getService(serviceWeWant);
                if (testMotors != null) {
                    Log.i(tag, "Got it, woo hoo!!!");
                    BluetoothGattCharacteristic motor1 = testMotors.getCharacteristic(toggleMotor1UUID);
                    BluetoothGattCharacteristic motor2 = testMotors.getCharacteristic(toggleMotor2UUID);
                    BluetoothGattCharacteristic motor3 = testMotors.getCharacteristic(toggleMotor3UUID);
                    if (motor1 != null) {
                        Log.i(tag, "Got the motor");
                        Log.i(tag, "value is [" + motor1.getStringValue(0) + "]");
                        motor1.setValue(toggleMotorValue);
                        Log.i(tag, "value is [" + motor1.getStringValue(0) + "]");
                        taskQ.add(new BLEQueueItem(BLEQueueItem.WRITECHARACTERISTIC, motor1.getUuid(), "Write Characteristic to Toggle Motor", motor1));
                        //gatt.writeCharacteristic(toggleDoor);
                    } else {
                        Log.i(tag, "No motor");
                    }
                    if (motor2 != null) {
                        Log.i(tag, "Got the motor");
                        Log.i(tag, "value is [" + motor2.getStringValue(0) + "]");
                        motor2.setValue(toggleMotorValue);
                        Log.i(tag, "value is [" + motor2.getStringValue(0) + "]");
                        taskQ.add(new BLEQueueItem(BLEQueueItem.WRITECHARACTERISTIC, motor2.getUuid(), "Write Characteristic to Toggle Motor", motor2));
                        //gatt.writeCharacteristic(toggleDoor);
                    } else {
                        Log.i(tag, "No motor");
                    }
                    if (motor3 != null) {
                        Log.i(tag, "Got the motor");
                        Log.i(tag, "value is [" + motor3.getStringValue(0) + "]");
                        motor3.setValue(toggleMotorValue);
                        Log.i(tag, "value is [" + motor3.getStringValue(0) + "]");
                        taskQ.add(new BLEQueueItem(BLEQueueItem.WRITECHARACTERISTIC, motor3.getUuid(), "Write Characteristic to Toggle Motor", motor3));
                        //gatt.writeCharacteristic(toggleDoor);
                    } else {
                        Log.i(tag, "No motor");
                    }
                } else {
                    Log.i(tag, "No Service");
                }
            }
            Log.i(tag, "OK, let's go^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
//            taskQ.add(new BLEQueueItem(BLEQueueItem.DISCONNECT, new UUID(0, 0), "Disconnect", null));
            mHandler.sendEmptyMessage(COMMUNICATING);
            doNextThing(gatt);
        }

        @Override
        public void onCharacteristicWrite (BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.i(tag,"characteristic written [" + status + "]");
            if (characteristic.getUuid().equals(toggleMotor1UUID)) {
                Log.i(tag,"value is [" + characteristic.getStringValue(0) + "]");
                if (characteristic.getStringValue(0).equals(("U"))) {
                    Log.i(tag,"We're done here!");
                }
            }
            if (characteristic.getUuid().equals(toggleMotor2UUID)) {
                Log.i(tag,"value is [" + characteristic.getStringValue(0) + "]");
                if (characteristic.getStringValue(0).equals(("U"))) {
                    Log.i(tag,"We're done here!");
                }
            }
            if (characteristic.getUuid().equals(toggleMotor3UUID)) {
                Log.i(tag,"value is [" + characteristic.getStringValue(0) + "]");
                if (characteristic.getStringValue(0).equals(("U"))) {
                    Log.i(tag,"We're done here!");
                }
            }
            doNextThing(gatt);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Log.i(tag,"onCharacteristicChanged " + characteristic.getUuid());
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.i(tag,"characteristic read [" + characteristic.getUuid() + "] [" + characteristic.getStringValue(0) + "]");
            doNextThing(gatt);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            try {
                Log.i(tag,"onDescriptorRead status is [" + status + "]");
                Log.i(tag, "descriptor read [" + descriptor.getCharacteristic().getUuid() + "]");
                Log.i(tag, "descriptor value is [" + new String(descriptor.getValue(), StandardCharsets.UTF_8) + "]");
                doNextThing(gatt);
            } catch (Exception e) {
                Log.e(tag,"Error reading descriptor " + Arrays.toString(e.getStackTrace()));
                doNextThing(gatt);
            }
        }
    }

    final class BLEFoundDevice extends ScanCallback {
        private final String tag = "BLEDEVICE";
        private int mode = INTERROGATE;
        BLEFoundDevice(int mode) {
            this.mode = mode;
        }
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.i(tag,"Found a device ==> " + result.toString());
            ScanRecord sr = result.getScanRecord();
            if (sr!= null) {
                if (sr.getDeviceName() != null) {
                    if (sr.getDeviceName().equals("BLE Koneked Device")) {
                        bluetoothAdapter.getBluetoothLeScanner().stopScan(this);
                        mHandler.sendEmptyMessage(FOUND);
                        Log.i(tag, "Found our Koneked Device!");
                        BluetoothDevice remoteDevice = result.getDevice();
                        if (remoteDevice != null) {
                            String nameOfDevice = result.getDevice().getName();
                            if (nameOfDevice != null) {
                                Log.i(tag, "device is [" + nameOfDevice + "]");
                            }
                        }
                        Log.i(tag,"Advertise Flags [" + sr.getAdvertiseFlags() + "]");
                        List<ParcelUuid> solicitationInfo = sr.getServiceUuids();
                        for(int i=0;i<solicitationInfo.size();i++) {
                            ParcelUuid thisone = solicitationInfo.get(i);
                            Log.i(tag,"solicitationinfo [" + i + "] uuid [" + thisone.getUuid() + "]");
                        }
                        ParcelUuid [] services = remoteDevice.getUuids();
                        if (services != null)
                        {
                            Log.i(tag,"length of services is [" + services.length + "]");
                        }
                        // attempt to connect here
                        remoteDevice.connectGatt(getApplicationContext(),true,new BLERemoteDevice(mode));
                        Log.i(tag,"after connect GATT");
                    } else {
                        Log.i(tag, "Not for us [" + sr.getDeviceName() + "]");
                    }
                }
            } else {
                Log.i(tag,"Null ScanRecord??");
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e(tag, "Error Scanning [" + errorCode + "]");
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            Log.i(tag,"onBatchScanResults " + results.size());
            for (int i=0; i < results.size(); i++) {
                Log.i(tag,"Result [" + i + "]" + results.get(i).toString());
            }
        }
    }
}