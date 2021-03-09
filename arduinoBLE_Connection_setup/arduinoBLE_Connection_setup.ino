// Implemented from https://developer.ibm.com/technologies/iot/tutorials/build-connected-devices-using-ble-apis/

#include <ArduinoBLE.h>

// create a service to expose our service to BLE Central Devices
BLEService Koneked("AA01");

BLEUnsignedCharCharacteristic motor1("2101", BLERead | BLEWrite | BLENotify);
BLEUnsignedCharCharacteristic motor2("2102", BLERead | BLEWrite | BLENotify);
BLEUnsignedCharCharacteristic motor3("2103", BLERead | BLEWrite | BLENotify);

//BLEUnsignedCharCharacteristic batteryLevelChar("2104", BLERead | BLENotify);

//BLEUnsignedCharCharacteristic scaledValueChar("2105", BLERead | BLENotify);
//BLEDescriptor mydescriptor1("2106", "ABC1");
//BLEDescriptor mydescriptor2("2106", "ABC2");
//BLEDescriptor mydescriptor3("2106", "ABC3");


byte motorValue = 0x00;
int commandIterations = 0;

#define  MOTOR_1_PIN 2
#define  MOTOR_2_PIN 4
#define  MOTOR_3_PIN 6

void setup() {
  Serial.begin(9600);
  while (!Serial);

  pinMode(MOTOR_1_PIN, OUTPUT);
  pinMode(MOTOR_2_PIN, OUTPUT);
  pinMode(MOTOR_3_PIN, OUTPUT);
  pinMode(LED_BUILTIN, OUTPUT);

  if (!BLE.begin())
  {
    Serial.println("starting BLE failed!");
    while (1);
  }

  String address = BLE.address();
  Serial.println("Address is [" + address + "]");

  BLE.setDeviceName("Koneked Device");      // this sets Characteristic 0x2a00 of Service 0x1800
  // Service 0x1800 is the Generic Access Profile
  // Characteristic 0x2a00 is the Device Name
  // Characteristic 0x2a01 is the "Appearance"
  BLE.setAppearance(384);                      // BLE_APPEARANCE_GENERIC_REMOTE_CONTROL

  BLE.setLocalName("BLE Koneked Device");       // this sets the local name for the advertising data

  // Change advertised BLE Characteristics names
  
  // tell the world about us
  BLE.setAdvertisedService(Koneked);
  //Koneked.addCharacteristic(batteryLevelChar);
  Koneked.addCharacteristic(motor1);
  Koneked.addCharacteristic(motor2);
  Koneked.addCharacteristic(motor3);
  //scaledValueChar.addDescriptor(mydescriptor1);
  //scaledValueChar.addDescriptor(mydescriptor2);
  //scaledValueChar.addDescriptor(mydescriptor3);
  //Koneked.addCharacteristic(scaledValueChar);
  BLE.addService(Koneked);

  motor1.writeValue(motorValue);      // start with a zero
  motor2.writeValue(motorValue);      // start with a zero
  motor3.writeValue(motorValue);      // start with a zero

  // advertise to the world so we can be found
  BLE.advertise();

  Serial.println("Bluetooth device active, waiting for connections...");

  // register new connection handler
  BLE.setEventHandler(BLEConnected, blePeripheralConnectHandler);

  // register disconnect handler
  BLE.setEventHandler(BLEDisconnected, blePeripheralDisconnectHandler);

  // handle Characteristic Written Handler
  motor1.setEventHandler(BLEWritten, switchCharacteristicWritten);
  motor2.setEventHandler(BLEWritten, switchCharacteristicWritten);
  motor3.setEventHandler(BLEWritten, switchCharacteristicWritten);
}

void switchCharacteristicWritten(BLEDevice central, BLECharacteristic characteristic) {
  // central wrote new value to characteristic, see if care, etc.
  Serial.print("Characteristic event, written: ");
  Serial.println(characteristic.uuid());
  byte motorValue1, motorValue2, motorValue3; 
  // see if this is the Motor Characteristic
  // Motor 1
  if (characteristic.uuid() == "2101")
  {
    characteristic.readValue(motorValue1);
    Serial.print("new value ");
    Serial.println(motorValue1);
    if (motorValue1 == 0)
    {
      Serial.println("Motor 1 on");
      digitalWrite(MOTOR_1_PIN, HIGH);
      delay(500);
      commandIterations++;
    }
    digitalWrite(MOTOR_1_PIN, LOW);
  }
  // Motor 2
  if (characteristic.uuid() == "2102")
  {
    characteristic.readValue(motorValue2);
    Serial.print("new value ");
    Serial.println(motorValue2);
    if (motorValue2 == 0)
    {
      Serial.println("Motor 2  on");
      digitalWrite(MOTOR_2_PIN, HIGH);
      delay(500);
      commandIterations++;
    }
    digitalWrite(MOTOR_2_PIN, LOW);
  }
  // Motor 3
  if (characteristic.uuid() == "2103")
  {
    characteristic.readValue(motorValue3);
    Serial.print("new value ");
    Serial.println(motorValue3);
    if (motorValue3 == 0)
    {
      Serial.println("Motor 3 on");
      digitalWrite(MOTOR_3_PIN, HIGH);
      delay(500);
      commandIterations++;
    }
    digitalWrite(MOTOR_3_PIN, LOW);
  }
}

void blePeripheralConnectHandler(BLEDevice central) {
  // central connected event handler
  Serial.print("Connected event, central: ");
  Serial.println(central.address());
  digitalWrite(LED_BUILTIN, HIGH);    // indicate that we have a connection
  digitalWrite(MOTOR_1_PIN, LOW);     //  make sure motor is NOT on
  digitalWrite(MOTOR_2_PIN, LOW);     //  make sure motor is NOT on
  digitalWrite(MOTOR_3_PIN, LOW);     //  make sure motor is NOT on
}

void blePeripheralDisconnectHandler(BLEDevice central) {
  // central disconnected event handler
  Serial.print("Disconnected event, central: ");
  Serial.println(central.address());
  digitalWrite(LED_BUILTIN, LOW);     // indicate that we no longer have a connection
  digitalWrite(MOTOR_1_PIN, LOW);     //  make sure motor is NOT on
  digitalWrite(MOTOR_2_PIN, LOW);     //  make sure motor is NOT on
  digitalWrite(MOTOR_3_PIN, LOW);     //  make sure motor is NOT on
}


void loop()
{
  BLEDevice central = BLE.central();

  if (central)
  {
    while (central.connected()) {

      int battery = analogRead(A0);
      int batteryLevel = map(battery, 0, 1023, 0, 100);
      //Serial.print("Battery Level % is now: ");
      //Serial.println(batteryLevel);
      //batteryLevelChar.writeValue(batteryLevel);
      //scaledValueChar.writeValue(something * 5);
      delay(200);
    }
  }
}
