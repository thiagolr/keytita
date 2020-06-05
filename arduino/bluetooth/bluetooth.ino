#include <SoftwareSerial.h>

#define PIN_BLUETOOTH_RX 5
#define PIN_BLUETOOTH_TX 4
#define PIN_BLUETOOTH_KEY 6

SoftwareSerial Bluetooth(PIN_BLUETOOTH_RX, PIN_BLUETOOTH_TX);
char data;  

// AT         : Check the connection
// AT+NAME    : See default name
// AT+ADDR    : see default address
// AT+VERSION : See version
// AT+UART    : See baudrate
// AT+ROLE    : See role of bt module(1=master/0=slave)
// AT+RESET   : Reset and exit AT mode
// AT+ORGL    : Restore factory settings
// AT+PSWD    : see default password

void setup() {
    pinMode(PIN_BLUETOOTH_RX, INPUT_PULLUP);  

    pinMode(PIN_BLUETOOTH_KEY, OUTPUT);   
    digitalWrite(PIN_BLUETOOTH_KEY, HIGH); 

    Bluetooth.begin(9600);
    Serial.begin(9600);
}

void loop() {
    Bluetooth.listen();
    while (Bluetooth.available() > 0) {
        data = Bluetooth.read();
        //inData = String(appData);
        Serial.write(data);
    }

    if (Serial.available()) {
        Bluetooth.write(Serial.read());
    }
}
