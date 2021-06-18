#include <SPI.h>
#include "nRF24L01.h"
#include "RF24.h"

RF24 radio(7, 8);
const byte address[6] = "00001";
String income = "";

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  radio.begin();
  radio.openReadingPipe(0, address);
  radio.setPALevel(RF24_PA_MIN);
  radio.startListening();
}

void loop() {
  // put your main code here, to run repeatedly:
  if(radio.available()){
    char message[20] = "";
    radio.read(&message, sizeof(message));
    String str = String(message);
    int index = str.indexOf("+");
    String id = str.substring(0, index);
    String data = str.substring(index+1);
    /*Serial.print(data);
    Serial.print(" from ");
    Serial.println(id);*/

    Serial.println(str);
    }
  }
