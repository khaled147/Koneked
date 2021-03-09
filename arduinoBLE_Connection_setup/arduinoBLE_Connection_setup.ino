// Implemented from https://developer.ibm.com/technologies/iot/tutorials/build-connected-devices-using-ble-apis/

#include <ArduinoBLE.h>
#include <PDM.h>

#define NAME_OF_PERIPHERAL "BLE Koneked Device"

// Create a service to expose our service to BLE Central Devices
BLEService Koneked("181A");

// Setup the incoming data characteristic (RX).
const int RX_BUFFER_SIZE = 256;
bool RX_BUFFER_FIXED_LENGTH = false;

// RX / TX Characteristics (Receive/Transmit Data)
BLECharacteristic rxChar("2A3D", BLEWriteWithoutResponse | BLEWrite, RX_BUFFER_SIZE, RX_BUFFER_FIXED_LENGTH);
BLEByteCharacteristic txChar("2A58", BLERead | BLENotify | BLEBroadcast);
BLEDescriptor txDescriptor("2902", "ABC1");

// Buffer to read samples into, each sample is 16-bits
short sampleBuffer[256];

// Number of samples read
volatile int samplesRead;

#define  MOTOR_1_PIN 2
#define  MOTOR_2_PIN 4
#define  MOTOR_3_PIN 6

void setup() {
  // Start serial
  Serial.begin(9600);
  //Ensure serial port is ready
  while (!Serial);

  // Prepare LED pins
  pinMode(LED_BUILTIN, OUTPUT);
  pinMode(LEDR, OUTPUT);
  pinMode(LEDG, OUTPUT);

  // Prepare Motor pins
  pinMode(MOTOR_1_PIN, OUTPUT);
  pinMode(MOTOR_2_PIN, OUTPUT);
  pinMode(MOTOR_3_PIN, OUTPUT);

  // Configure the audio data receive callback
  PDM.onReceive(onPDMdata);

  // Start PDM
  startPDM();

  // Start BLE.
  startBLE();

  // Create BLE service and characteristics
  // Change advertised BLE Characteristics names
  BLE.setLocalName(NAME_OF_PERIPHERAL);
  BLE.setDeviceName(NAME_OF_PERIPHERAL);          // this sets Characteristic 0x2a00 of Service 0x1800
  // Service 0x1800 is the Generic Access Profile
  // Characteristic 0x2a00 is the Device Name
  // Characteristic 0x2a01 is the "Appearance"
  BLE.setAppearance(192);                       // BLE_APPEARANCE_GENERIC_WATCH
  // Appearance can be determined using the following: https://infocenter.nordicsemi.com/index.jsp?topic=%2Fcom.nordic.infocenter.s132.api.v6.0.0%2Fgroup___b_l_e___a_p_p_e_a_r_a_n_c_e_s.html
  BLE.setAdvertisedService(Koneked);
  txChar.addDescriptor(txDescriptor);
  Koneked.addCharacteristic(rxChar);
  Koneked.addCharacteristic(txChar);
  BLE.addService(Koneked);

  // Bluetooth LE connection handlers
  BLE.setEventHandler(BLEConnected, onBLEConnected);
  BLE.setEventHandler(BLEDisconnected, onBLEDisconnected);

  // Event driven reads.
  rxChar.setEventHandler(BLEWritten, onRxCharValueUpdate);

  // Print out full UUID and MAC address.
  Serial.println("Peripheral advertising info: ");
  Serial.print("Name: ");
  Serial.println(NAME_OF_PERIPHERAL);
  Serial.print("MAC: ");
  Serial.println(BLE.address());
  Serial.print("Service UUID: ");
  Serial.println(Koneked.uuid());
  Serial.print("rxCharacteristic UUID: ");
  Serial.println(rxChar.uuid());
  Serial.print("txCharacteristics UUID: ");
  Serial.println(txChar.uuid());

  Serial.println("Bluetooth device active, waiting for connections...");;
  
  // Advertise to the world so device can be found
  BLE.advertise();
}

void loop()
{
  BLEDevice central = BLE.central();
  if (central)
  {
    // Only send data if we are connected to a central device.
    while (central.connected()) {
      connectedLight();

      // Send the microphone values to the central device.
      if (samplesRead) {
        // print samples to the serial monitor or plotter
        for (int i = 0; i < samplesRead; i++) {
          txChar.writeValue(sampleBuffer[i]);
          Serial.println(sampleBuffer[i]);
        }
        // Clear the read count
        samplesRead = 0;
      }
    }
    disconnectedLight();
  } else {
    disconnectedLight();
  }
}

/*
 *  BLUETOOTH
 */
void startBLE() {
  if (!BLE.begin())
  {
    Serial.println("starting BLE failed!");
    while (1);
  }
}

void onRxCharValueUpdate(BLEDevice central, BLECharacteristic characteristic) {
  // central wrote new value to characteristic, update LED
  Serial.print("Characteristic event, read: ");
  byte tmp[256];
  int dataLength = rxChar.readValue(tmp, 256);

  for(int i = 0; i < dataLength; i++) {
    Serial.print((char)tmp[i]);
  }
  Serial.println();
  Serial.print("Value length = ");
  Serial.println(rxChar.valueLength());

  //TODO: Think of a different logic
//  // Central wrote new value to characteristic, see if care, etc.
//  Serial.print("Characteristic event, written: ");
//  Serial.println(characteristic.uuid());
//  byte motorValue1, motorValue2, motorValue3;
//  // see if this is the Motor Characteristic
//  // Motor 1
//  if (characteristic.uuid() == "2101")
//  {
//    characteristic.readValue(motorValue1);
//    Serial.print("new value ");
//    Serial.println(motorValue1);
//    if (motorValue1 == 0x55)
//    {
//      Serial.println("Motor 1 on");
//      digitalWrite(MOTOR_1_PIN, HIGH);
//      delay(500);
//    }
//    digitalWrite(MOTOR_1_PIN, LOW);
//  }
//  // Motor 2
//  if (characteristic.uuid() == "2102")
//  {
//    characteristic.readValue(motorValue2);
//    Serial.print("new value ");
//    Serial.println(motorValue2);
//    if (motorValue2 == 0x55)
//    {
//      Serial.println("Motor 2  on");
//      digitalWrite(MOTOR_2_PIN, HIGH);
//      delay(500);
//    }
//    digitalWrite(MOTOR_2_PIN, LOW);
//  }
//  // Motor 3
//  if (characteristic.uuid() == "2103")
//  {
//    characteristic.readValue(motorValue3);
//    Serial.print("new value ");
//    Serial.println(motorValue3);
//    if (motorValue3 == 0x55)
//    {
//      Serial.println("Motor 3 on");
//      digitalWrite(MOTOR_3_PIN, HIGH);
//      delay(500);
//    }
//    digitalWrite(MOTOR_3_PIN, LOW);
//  }
}

void onBLEConnected(BLEDevice central) {
  Serial.print("Connected event, central: ");
  Serial.println(central.address());
  connectedLight();
  digitalWrite(MOTOR_1_PIN, LOW);     //  make sure motor is NOT on
  digitalWrite(MOTOR_2_PIN, LOW);     //  make sure motor is NOT on
  digitalWrite(MOTOR_3_PIN, LOW);     //  make sure motor is NOT on
}

void onBLEDisconnected(BLEDevice central) {
  Serial.print("Disconnected event, central: ");
  Serial.println(central.address());
  disconnectedLight();
  digitalWrite(MOTOR_1_PIN, LOW);     //  make sure motor is NOT on
  digitalWrite(MOTOR_2_PIN, LOW);     //  make sure motor is NOT on
  digitalWrite(MOTOR_3_PIN, LOW);     //  make sure motor is NOT on
}

/*
 * LEDS
 */
void connectedLight() {
  digitalWrite(LEDR, LOW);
  digitalWrite(LEDG, HIGH);
}


void disconnectedLight() {
  digitalWrite(LEDR, HIGH);
  digitalWrite(LEDG, LOW);
}

/*
 *  MICROPHONE
 */
void startPDM() {
  // initialize PDM with:
  // - one channel (mono mode)
  // - a 16 kHz sample rate
  if (!PDM.begin(1, 16000)) {
    Serial.println("Failed to start PDM!");
    while (1);
  }
}

void onPDMdata() {
  // Query the number of bytes available
  int bytesAvailable = PDM.available();

  // Read into the sample buffer
  int bytesRead = PDM.read(sampleBuffer, bytesAvailable);

  // 16-bit, 2 bytes per sample
  samplesRead = bytesRead / 2;
}
