/*
  Copyright (c) 2020 Thiago Lopes Rosa

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
 
#include <FastLED.h>
#include <MIDI.h>
#include <SoftwareSerial.h>

// ###################################################################
// ###################################################################

#define PIN_BLUETOOTH_RX 5
#define PIN_BLUETOOTH_TX 4

#define TYPE_CLEAR 0
#define TYPE_BRIGHTNESS 1
#define TYPE_COLOR_SINGLE 11
#define TYPE_COLOR_RANDOM 12
#define TYPE_RAINBOW_FULL_FIXED 13
#define TYPE_RAINBOW_FULL_MOVING 14
#define TYPE_RAINBOW_SINGLE_SHIFTING 15
#define TYPE_RAINBOW_GRADUAL_MOVING 16

SoftwareSerial Bluetooth(PIN_BLUETOOTH_RX, PIN_BLUETOOTH_TX);

char btData[80];
int btIndex = -1;

bool btDemo = false;
int btType = 0;
int btBrightness = 0;
int btColorRed = 0;
int btColorGreen = 0;
int btColorBlue = 0;
int btVelocity = 0;

// ###################################################################
// ###################################################################

#define PIN_LED 2
#define NUM_LEDS 9

CRGB leds[NUM_LEDS];
CRGB colors[NUM_LEDS];

int matrixWhite[12] = { 0, 8, 1, 8, 2, 5, 8, 4, 8, 3, 8, 6 };
int matrixBlack[12] = { 8, 0, 8, 1, 8, 8, 5, 8, 4, 8, 3, 8 };


CRGB COLOR_BLACK = CRGB::Cyan;
CRGB COLOR_WHITE = CRGB::DarkRed;

// ###################################################################
// ###################################################################

#define MIDI_CHANNEL 16

MIDI_CREATE_DEFAULT_INSTANCE();

// ###################################################################
// ###################################################################

void setup() {
    pinMode(PIN_BLUETOOTH_RX, INPUT_PULLUP);  
  
    FastLED.addLeds<WS2811, PIN_LED, RGB>(leds, NUM_LEDS);
    FastLED.show();
  
    MIDI.setHandleNoteOn(handleNoteOn);
    MIDI.setHandleNoteOff(handleNoteOff);
    MIDI.begin(MIDI_CHANNEL_OMNI);

    Serial.begin(9600);

    Bluetooth.begin(9600);
}

void loop() {
    MIDI.read();
    
    while (Bluetooth.available() > 0) {
        char data = Bluetooth.read();

        btIndex++;
        btData[btIndex] = data;
    }

    if (btIndex >= 0 && btData[btIndex] == '#') {
        btData[btIndex] = '\0';
        btIndex = -1;
      
        char * data;
        data = strtok (btData, ",");
        int type = atoi(data);
        if (type == TYPE_CLEAR) {
            
        } else if (type == TYPE_BRIGHTNESS) {
            data = strtok (NULL, ",");
            btBrightness = atoi(data);
            FastLED.setBrightness(btBrightness);
        } else {
            btType = type;
            data = strtok (NULL, ",");
            btColorRed = atoi(data);
            data = strtok (NULL, ",");
            btColorGreen = atoi(data);
            data = strtok (NULL, ",");
            btColorBlue = atoi(data);
            data = strtok (NULL, ",");
            btVelocity = atoi(data);
        }

        btDemo = true;  
    }

    if (btDemo) {
        handleLedDemo();
    }
}

void handleLedDemo() {
    switch (btType) {
        case TYPE_CLEAR:
            colorSingle(CRGB::Black);
            break;   
        case TYPE_BRIGHTNESS:
            
            break;
        case TYPE_COLOR_SINGLE:
            colorSingle(CRGB(btColorRed, btColorGreen, btColorBlue));
            break;  
        case TYPE_COLOR_RANDOM:
            colorRandom();
            break;  
        case TYPE_RAINBOW_FULL_FIXED:
            rainbowCompleteStatic();
            break;   
        case TYPE_RAINBOW_FULL_MOVING:
            rainbowCompleteMoving();
            break;   
        case TYPE_RAINBOW_SINGLE_SHIFTING:
            rainbowSingleColorShifting();
            break;   
        case TYPE_RAINBOW_GRADUAL_MOVING:
            rainbowGradualMoving();
            break;
    }
    
    showAllLeds();
    delay(btVelocity);

    if (btType == TYPE_CLEAR || btType == TYPE_BRIGHTNESS) {
        btDemo = false;
    }
}

void handleNoteOn(byte channel, byte pitch, byte velocity) {
    //printNote("ON ", channel, pitch, velocity);
    handleLed(true, channel, pitch);    
}

void handleNoteOff(byte channel, byte pitch, byte velocity) {
    //printNote("OFF", channel, pitch, velocity);
    handleLed(false, channel, pitch);   
}

void handleLed(bool enable, byte channel, byte note) {
    if (btDemo) {
        btDemo = false;
        colorSingle(CRGB::Black);
        showAllLeds();
    } 

    //if (channel == MIDI_CHANNEL) {
        int led = (note - 24) % 12;
        if (led == 1 || led == 3 || led == 6 || led == 8 || led == 10) {
            leds[matrixBlack[led]] = enable ? COLOR_BLACK : CRGB::Black;
            //leds[8] = enable ? CRGB::Blue : CRGB::Black;
        } else {
            leds[matrixWhite[led]] = enable ? COLOR_WHITE : CRGB::Black;
        }
        
        FastLED.show();
    //}
}

void showAllLeds() {
    for (int i = 0; i < NUM_LEDS; i++) {
        leds[i] = colors[i];
    }
    FastLED.show();
}

void printNote(String type, byte channel, byte pitch, byte velocity) {
    Serial.print("[");
    Serial.print(type);
    Serial.print("][");
    Serial.print(channel);
    Serial.print("][");
    Serial.print(pitch);
    Serial.print("][");
    Serial.print(velocity);
    Serial.println("]");
}
