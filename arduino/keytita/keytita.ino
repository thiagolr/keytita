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
#include <SPI.h>
#include <USB-MIDI.h>

#include <usbh_midi.h>
#include <usbhub.h>

// ###################################################################################################################################
// ###################################################################################################################################

#define TYPE_CLEAR 0
#define TYPE_COLOR 1
#define TYPE_SPEED 2
#define TYPE_BRIGHTNESS 3

#define TYPE_RANDOM_COLOR 11
#define TYPE_RAINBOW_FULL_FIXED 12
#define TYPE_RAINBOW_FULL_MOVING 13
#define TYPE_RAINBOW_SINGLE_SHIFTING 14
#define TYPE_RAINBOW_GRADUAL_MOVING 15
#define TYPE_RAINBOW_NOTE 16
#define TYPE_RAINBOW_OCTAVE 17

#define DEFAULT_BRIGHTNESS 39
#define DEFAULT_SPEED 50

char btData[80];
int btIndex = -1;

bool btDemo = false;
int btType = 0;
int btBrightness = 0;
int btVelocity = DEFAULT_SPEED;
int btColorRed = 0;
int btColorGreen = 0;
int btColorBlue = 0;

// ###################################################################################################################################
// ###################################################################################################################################

#define PIN_LED 2
#define NUM_LEDS 9

CRGB leds[NUM_LEDS];
CRGB colors[NUM_LEDS];

int matrixWhite[12] = { 0, 8, 1, 8, 2, 5, 8, 4, 8, 3, 8, 6 };
int matrixBlack[12] = { 8, 0, 8, 1, 8, 8, 5, 8, 4, 8, 3, 8 };

CRGB COLOR_BLACK = CRGB::Cyan;
CRGB COLOR_WHITE = CRGB::DarkRed;

// ###################################################################################################################################
// ###################################################################################################################################

#define MIDI_LIGHT_CHANNEL 16
#define MIDI_THRU_CHANNEL 15

USB Usb;
USBMIDI_CREATE_DEFAULT_INSTANCE();

USBH_MIDI MIDI_Piano(&Usb);
#define MIDI_Synthesia MIDI

// ###################################################################################################################################
// ###################################################################################################################################

void setup() {       
    pinMode(LED_BUILTIN, OUTPUT);

    Serial.begin(115200);
    while (!Serial) {
        digitalWrite(LED_BUILTIN, HIGH); 
        delay(500);
        digitalWrite(LED_BUILTIN, LOW); 
        delay(500);
    }

    Serial.println("### Initializing LEDs...");
    FastLED.addLeds<WS2811, PIN_LED, RGB>(leds, NUM_LEDS);
    FastLED.setBrightness(DEFAULT_BRIGHTNESS);
    FastLED.show();

    Serial.println("### Initializing MIDI...");
    MIDI_Synthesia.setHandleNoteOn(handleSynthesiaNoteOn);
    MIDI_Synthesia.setHandleNoteOff(handleSynthesiaNoteOff);
    MIDI_Synthesia.begin(MIDI_CHANNEL_OMNI);
    
    Serial.println("### Initializing USB...");
    while (Usb.Init() == -1) {
        digitalWrite(LED_BUILTIN, HIGH); 
        delay(1000);
        digitalWrite(LED_BUILTIN, LOW);
        delay(500);
    }
    
    delay(200);
    digitalWrite(LED_BUILTIN, HIGH); 
}

void loop() {
    MIDI_Synthesia.read();
    Usb.Task();
    
    if (Usb.getUsbTaskState() == USB_STATE_RUNNING) {
        handleUsb();
    }
    
    while (Serial.available() > 0) {
        char data = Serial.read();

        btIndex++;
        btData[btIndex] = data;
    }

    if (btIndex >= 0 && btData[btIndex] == '#') {
        btData[btIndex] = '\0';
        btIndex = -1;
      
        char * data;
        data = strtok(btData, ",");
        int type = atoi(data);

        Serial.print("T:");
        Serial.println(type);

        switch (type) {
            case TYPE_CLEAR:
                btType = type;
                break;   
            case TYPE_COLOR:        
                btType = type;
                data = strtok(NULL, ",");
                btColorRed = atoi(data);
                data = strtok(NULL, ",");
                btColorGreen = atoi(data);
                data = strtok(NULL, ",");
                btColorBlue = atoi(data);
                break;     
            case TYPE_SPEED:     
                data = strtok(NULL, ",");
                btVelocity = atoi(data);    
                break;            
            case TYPE_BRIGHTNESS:
                data = strtok(NULL, ",");
                btBrightness = atoi(data);
                FastLED.setBrightness(btBrightness);
                break;
            default:
                btType = type;
                break;          
        }

        btDemo = true;  
    }

    if (btDemo) {
        handleLedDemo();
    }
}

// ###################################################################################################################################
// ###################################################################################################################################

void handleLedDemo() {
    switch (btType) {
        case TYPE_CLEAR:
            colorSingle(CRGB::Black);
            break;   
        case TYPE_COLOR:
            colorSingle(CRGB(btColorRed, btColorGreen, btColorBlue));
            break;              
        case TYPE_BRIGHTNESS:            
            break;
        case TYPE_SPEED:            
            break;            
        case TYPE_RANDOM_COLOR:
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
        case TYPE_RAINBOW_NOTE:
            rainbowNote();
            break;
        case TYPE_RAINBOW_OCTAVE:
            rainbowOctave();
            break;                        
    }
    
    showAllLeds();
    delay(btVelocity);

    if (btType == TYPE_CLEAR || btType == TYPE_BRIGHTNESS) {
        btDemo = false;
    }
}

void handleUsb() {
    uint8_t buffer[3];
    uint8_t size = 0;
  
    do {
        buffer[0] = 0;
        buffer[1] = 0;
        buffer[2] = 0;
    
        size = MIDI_Piano.RecvData(buffer);
        if (size > 0) {
            if (buffer[0] >= 128 && buffer[0] <= 143) {
                handlePianoNoteOff(buffer[0] - 127, buffer[1], buffer[2]);
            } else if (buffer[0] >= 144 && buffer[0] <= 159) {
                handlePianoNoteOn(buffer[0] - 143, buffer[1], buffer[2]);             
            } else if (buffer[0] >= 176 && buffer[0] <= 191) {
                // control change
            } else if (buffer[0] >= 224 && buffer[0] <= 239) {
                // pitch wheel
            } else if (buffer[0] == 254) {
                // active sensing
            } else {
                Serial.print("Unhandled status byte: ");
                Serial.println(buffer[0]);
            }  
        }
    } while (size > 0);
}

void handleSynthesiaNoteOn(byte channel, byte note, byte velocity) {
    Serial.println("note on");
    
    if (channel == MIDI_THRU_CHANNEL) {
        return;      
    }
  
    printNote("NOTE ON  RECV SYNTHESIA", channel, note, velocity);

    if (channel == MIDI_LIGHT_CHANNEL) {       
        handleLed(true, channel, note);    
    } else {
        uint8_t buffer[3];
        buffer[0] = channel + 143;
        buffer[1] = note;
        buffer[2] = velocity;
        MIDI_Piano.SendData(buffer);
        
        printNote("NOTE ON  SEND PIANO    ", buffer[0] - 143, buffer[1], buffer[2]);
    }
}

void handleSynthesiaNoteOff(byte channel, byte note, byte velocity) {
    if (channel == MIDI_THRU_CHANNEL) {
        return;      
    }

    printNote("NOTE OFF RECV SYNTHESIA", channel, note, velocity);

    if (channel == MIDI_LIGHT_CHANNEL) {
        handleLed(false, channel, note);
    } else {
        uint8_t buffer[3];
        buffer[0] = channel + 127;
        buffer[1] = note;
        buffer[2] = 0;
        MIDI_Piano.SendData(buffer);

        printNote("NOTE OFF SEND PIANO    ", buffer[0] - 127, buffer[1], buffer[2]);
    }
}

void handlePianoNoteOn(byte channel, byte note, byte velocity) {
    handleLed(true, channel, note);  
    
    printNote("NOTE ON  RECV PIANO    ", channel, note, velocity);                    
    printNote("NOTE ON  SEND SYNTHESIA", MIDI_THRU_CHANNEL, note, velocity);     
    MIDI_Synthesia.sendNoteOn(note, velocity, MIDI_THRU_CHANNEL);
}

void handlePianoNoteOff(byte channel, byte note, byte velocity) {
    handleLed(false, channel, note);

    printNote("NOTE OFF RECV PIANO    ", channel, note, 0);
    printNote("NOTE OFF SEND SYNTHESIA", MIDI_THRU_CHANNEL, note, 0);
    MIDI_Synthesia.sendNoteOff(note, 0, MIDI_THRU_CHANNEL);
}

void handleLed(bool enable, byte channel, byte note) {
    if (btDemo) {
        btDemo = false;
        colorSingle(CRGB::Black);
        showAllLeds();
    } 

    if (enable) {
        printNote("LED ON                 ", channel, note, 0);
    } else {
        printNote("LED OFF                ", channel, note, 0);
    }

    int led = (note - 24) % 12;
    if (led == 1 || led == 3 || led == 6 || led == 8 || led == 10) {
        leds[matrixBlack[led]] = enable ? COLOR_BLACK : CRGB::Black;
        //leds[8] = enable ? CRGB::Blue : CRGB::Black;
    } else {
        leds[matrixWhite[led]] = enable ? COLOR_WHITE : CRGB::Black;
    }
    
    FastLED.show();
}

void showAllLeds() {
    for (int i = 0; i < NUM_LEDS; i++) {
        leds[i] = colors[i];
    }
    FastLED.show();
}

// ###################################################################################################################################
// ###################################################################################################################################

void printNote(String type, byte channel, byte note, byte velocity) {
    /*Serial.print("[");
    Serial.print(type);
    Serial.print(channel < 10 ? "]  #0" : "]  #");
    Serial.print(channel);
    Serial.print("  note=");
    Serial.print(note);
    if (velocity > 0) {
      Serial.print("  velocity=");
      Serial.print(velocity);
    }
    Serial.println("");*/
}
