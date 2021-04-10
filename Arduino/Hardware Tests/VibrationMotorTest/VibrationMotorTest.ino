// Vibration motor tests
// It seems like digital pins do not provide enough current to power the vibration motors
// TODO: connect a digital switch

#define  MOTOR_1_PIN 12
#define  MOTOR_2_PIN 3
#define  MOTOR_3_PIN 2

void setup() {
  // Prepare Motor pins
  pinMode(MOTOR_1_PIN, OUTPUT);
  pinMode(MOTOR_2_PIN, OUTPUT);
  pinMode(MOTOR_3_PIN, OUTPUT);

  digitalWrite(MOTOR_1_PIN, HIGH);
  digitalWrite(MOTOR_2_PIN, HIGH);
  digitalWrite(MOTOR_3_PIN, HIGH);
}

void loop() {
  // put your main code here, to run repeatedly:

}
