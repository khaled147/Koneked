// Read microphone data

float startTime;

#define MIC_PIN A1
void setup() {
  // Initialise Serial communication
  Serial.begin(9600);

  // Delay for 2 seconds to give time to open serial monitor
  delay(2000);

  // Assign the start time
  startTime = millis();
}

void loop() {
  // Read the microphone data for 3 seconds
//  while(millis() - startTime < 3000) {
    Serial.println(analogRead(MIC_PIN) - 550);
//  }
}
