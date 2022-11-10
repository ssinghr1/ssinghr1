#include <ESP8266WiFi.h>
#include <Wire.h>
#include <BH1750.h>
#include <Adafruit_BME280.h>
#include "PubSubClient.h"

BH1750 lightMeter(0x23);
Adafruit_BME280 bme;

extern "C" {
#include "user_interface.h"
#include "wpa2_enterprise.h"
#include "c_types.h"
}

//Wifi
const char* ssid = "YOUR WIFI HERE";
const char* password = "YOUR PASS HERE";
//NOTE: this only works for WPA2. 
//Will not support WPA2 Enterprise, due to constrains of ESP8266

//MQTT
const char* mqtt_server = "IP";
const char* humidity_topic = "sensors/1/humidity";
const char* temperature_topic = "sensors/1/temperature";
const char* pressure_topic = "sensors/1/pressure";
const char* light_topic = "sensors/1/light";
const char* mqtt_username = "MQTT_USERNAME"; // MQTT username
const char* mqtt_password = "MQTT_PASSWORD"; // MQTT password
const char* clientID = "client_1"; // MQTT client ID


// Initialise the WiFi and MQTT Client objects
WiFiClient wifiClient;
// 1883 is the listener port for the Broker
PubSubClient client(mqtt_server, 1883, wifiClient); 


// Custom function to connet to the MQTT broker via WiFi
void connect_MQTT(){
  Serial.print("Connecting to ");
  Serial.println(ssid);

  // Connect to the WiFi
  WiFi.begin(ssid, password);

  // Wait until the connection has been confirmed before continuing
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  // Debugging - Output the IP Address of the ESP8266
  Serial.println("WiFi connected");
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());

  // Connect to MQTT Broker
  // client.connect returns a boolean value to let us know if the connection was successful.
  // If the connection is failing, make sure you are using the correct MQTT Username and Password (Setup Earlier in the Instructable)
  if (client.connect(clientID, mqtt_username, mqtt_password)) {
    Serial.println("Connected to MQTT Broker!");
  }
  else {
    Serial.println("Connection to MQTT Broker failed...");
  }
}

void setup(){
  Serial.begin(9600);

  // Initialize the I2C bus (BH1750 library doesn't do this automatically)
  Wire.begin();
  // On esp8266 you can select SCL and SDA pins using Wire.begin(D4, D3);

  lightMeter.begin();
  bme.begin(0x76);  

  Serial.println(("BH1750 Test begin"));
}

void loop() {
  connect_MQTT();
  Serial.setTimeout(2000);
  float l = lightMeter.readLightLevel();
  float t = bme.readTemperature();
  float p = bme.readPressure();
  float h = bme.readHumidity();
  
  Serial.print("Light: ");
  Serial.print(l);
  Serial.println(" lx");
  Serial.print("Temperature = ");
  Serial.print(t);
  Serial.println(" Celsius");
  Serial.print("Pressure = ");
  Serial.print(p);
  Serial.println("Pascal");
  Serial.print("Humidity = ");
  Serial.print(h);
  Serial.println("%"); 

  String light = "Light: " + String((float)l) + " lx";
  String temp = "Temperature:" + String((float)t)+ " Celcius";
  String pressure = "Pressure: " + String((float)p)+ "Pascals";
  String humidity = "Humidity: " + String((float)h)+"%";

  // PUBLISH to the MQTT Broker (topic = Temperature, defined at the beginning)
  if (client.publish(temperature_topic, String(t).c_str())) {
    Serial.println("Temperature sent!");
  }
  // Again, client.publish will return a boolean value depending on whether it succeded or not.
  // If the message failed to send, we will try again, as the connection may have broken.
  else {
    Serial.println("Temperature failed to send. Reconnecting to MQTT Broker and trying again");
    client.connect(clientID, mqtt_username, mqtt_password);
    delay(10); // This delay ensures that client.publish doesn't clash with the client.connect call
    client.publish(temperature_topic, String(t).c_str());
  }

    // PUBLISH to the MQTT Broker (topic = Humidity, defined at the beginning)
  if (client.publish(humidity_topic, String(h).c_str())) {
    Serial.println("Humidity sent!");
  }
  // Again, client.publish will return a boolean value depending on whether it succeded or not.
  // If the message failed to send, we will try again, as the connection may have broken.
  else {
    Serial.println("Humidity failed to send. Reconnecting to MQTT Broker and trying again");
    client.connect(clientID, mqtt_username, mqtt_password);
    delay(10); // This delay ensures that client.publish doesn't clash with the client.connect call
    client.publish(humidity_topic, String(h).c_str());
  }

     // PUBLISH to the MQTT Broker (topic = pressure, defined at the beginning)
  if (client.publish(pressure_topic, String(p).c_str())) {
    Serial.println("Pressure sent!");
  }
  // Again, client.publish will return a boolean value depending on whether it succeded or not.
  // If the message failed to send, we will try again, as the connection may have broken.
  else {
    Serial.println("Pressure failed to send. Reconnecting to MQTT Broker and trying again");
    client.connect(clientID, mqtt_username, mqtt_password);
    delay(10); // This delay ensures that client.publish doesn't clash with the client.connect call
    client.publish(pressure_topic, String(p).c_str());
  }

  // PUBLISH to the MQTT Broker (topic = light, defined at the beginning)
  if (client.publish(light_topic, String(l).c_str())) {
    Serial.println("light sent!");
  }
  // Again, client.publish will return a boolean value depending on whether it succeded or not.
  // If the message failed to send, we will try again, as the connection may have broken.
  else {
    Serial.println("light failed to send. Reconnecting to MQTT Broker and trying again");
    client.connect(clientID, mqtt_username, mqtt_password);
    delay(10); // This delay ensures that client.publish doesn't clash with the client.connect call
    client.publish(light_topic, String(l).c_str());
  }
  client.disconnect();
  
  delay(1000*60);
}