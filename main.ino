#include <FastLED.h>

#define LED_TYPE NEOPIXEL
#define NUM_LED 13
#define DATA_PIN 6

#define OFF off
#define MSG_START "SERIAL"

CRGB leds[NUM_LED];

boolean stringComplete = false;  // whether the received string is complete or not
char currentMode;

void setup() {
  Serial.begin(9600);
  while(!Serial);

  FastLED.addLeds<LED_TYPE, DATA_PIN>(leds, NUM_LED);
  FastLED.clear();
  for (int i = 0; i < NUM_LED; i++)
  {
    leds[i] = CRGB::Black;
  }
  FastLED.show();
}

void loop() {
  switch(currentMode){
    case '1':
      chase();
      break;
    case '0':
    default:
      turnOff();
      break;
  }
//  Serial.print("alp://cevnt/");
//  Serial.print("SERIAL");
//  Serial.print("coucou");
//  Serial.print('\n');
//  Serial.flush();
//
//  delay(1000);
}

void serialEvent() {
  int number = 0;
  String inputString = "";
  
  while (Serial.available()) {
    char inChar = (char)Serial.read();
    
    switch(inChar){
      case '\n':
        stringComplete = true;
        break;
      case '_':
        number++;
        break;
      default:
        inputString += inChar;
        break;
    }
  }

  if(stringComplete)
  {
    int index = inputString.indexOf(MSG_START) + ((String) MSG_START).length();
    String message = inputString.substring(index);

    switch(message.charAt(0)){
      case '1':
        currentMode = '1';
        break;
      case '0':
      default:
        currentMode = '0';
        break;
    }

    sendMessage((String) currentMode);
  }

  stringComplete = false;
}

void sendMessage(String message){
  Serial.print("alp://cevnt/");
  Serial.print("SERIAL");
//  int index = inputString.indexOf(MSG_START) + ((String) MSG_START).length();
//  Serial.print(inputString.substring(index));
  Serial.print(message);
  Serial.print('\n');
  Serial.flush();
}

void chase(){
  for (int i = 0; i < NUM_LED; i++)
  {
    leds[i] = CRGB(0, 25, 25);
    FastLED.show();
    
    leds[i] = CRGB::Black;
    delay(30);
  }
}

void turnOff(){
  for (int i = 0; i < NUM_LED; i++)
  {
    leds[i] = CRGB::Black;
    FastLED.show();
    
    delay(30);
  }
}


