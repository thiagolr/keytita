#include <FastLED.h>
#include <MIDI.h>
#include <SoftwareSerial.h>

#define PIN_LED 2
#define PIN_BLUETOOTH_RX 5
#define PIN_BLUETOOTH_TX 4

#define MIDI_CHANNEL 16

#define NUM_LEDS 9



CRGB leds[NUM_LEDS];
int matrixWhite[12] = { 0, 8, 1, 8, 2, 5, 8, 4, 8, 3, 8, 6 };
int matrixBlack[12] = { 8, 0, 8, 1, 8, 8, 5, 8, 4, 8, 3, 8 };

MIDI_CREATE_DEFAULT_INSTANCE();

SoftwareSerial Bluetooth(PIN_BLUETOOTH_RX, PIN_BLUETOOTH_TX);
char data;  

CRGB COLOR_BLACK = CRGB::Blue;
CRGB COLOR_WHITE = CRGB::Red;

void setup() {
    pinMode(PIN_BLUETOOTH_RX, INPUT_PULLUP);  
  
    FastLED.addLeds<WS2811, PIN_LED, RGB>(leds, NUM_LEDS);
    FastLED.show();
  
    MIDI.setHandleNoteOn(handleNoteOn);
    MIDI.setHandleNoteOff(handleNoteOff);
    MIDI.begin(MIDI_CHANNEL_OMNI);

    Bluetooth.begin(9600);
}

void loop() {
    MIDI.read();

    Bluetooth.listen();
    while (Bluetooth.available() > 0) {
        data = Bluetooth.read();
    
        if (data == 'p') {
            COLOR_WHITE = CRGB::Purple;
        } else if (data == 'y') {
            COLOR_WHITE = CRGB::Yellow;      
        }
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
