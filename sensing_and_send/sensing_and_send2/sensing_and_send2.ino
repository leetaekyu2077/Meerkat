#include <SPI.h>
#include "RF24.h"
#include "nRF24L01.h"

RF24 radio(7, 8);
const byte address[6] = "00001";
const String id = "sensor2";
void setup() {
  // put your setup code here, to run once:
  radio.begin();
  radio.openWritingPipe(address);
  radio.setPALevel(RF24_PA_MIN); //RF24_PA_MIN / RF24_PA_LOW / RF24_PA_HIGH / RF24_PA_MAX
  radio.stopListening();
  pinMode(A0, INPUT);
  pinMode(LED_BUILTIN, OUTPUT);
  Serial.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:
  String message = id;
  message += "+";
  char result[20] = "";
  message.concat(String(analogRead(A0)));
  message.toCharArray(result, 20);
  digitalWrite(LED_BUILTIN, HIGH);
  radio.write(&result, sizeof(result));
  delay(1000);
  digitalWrite(LED_BUILTIN, LOW);  
}
