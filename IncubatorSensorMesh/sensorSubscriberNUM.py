import paho.mqtt.client as mqtt
from ISStreamer.Streamer import Streamer

##User setting MQTT#######
MQTT_BROKER_IP='MQTT_BROKER_IP_ADDRESS'
MQTT_USERNAME='MQTT_USERNAME'
MQTT_PASSWORD='MQTT_PASSWORD'
MQTT_TOPIC='sensors/#/+'##replace the # with the incubator number
#########################

#User settings IS Streamer##########
SENSOR_LOCATION_NAME="sensor #-" ##repalce # with the incubator number
BUCKET_NAME="S#" ##replace # with the incubator number
BUCKET_KEY="BUCKET KEY" # get this from the IS page
ACCESS_KEY="ACCESS KEY" # get this from the IS page
MINUTES_BETWEEN_READS=1
####################################

client = mqtt.client()
client.username_pw_Set(MQTT_USERNAME, MQTT_PASSWORD)
streamer = Streamer(bucket_name = BUCKET_NAME, bucket_key=BUCKET_KEY, access_key = ACCESS_KEY)

def on_connect(client, userdata, flags, rc):
    """The Callback for when the client recieves a CONNACK response from the server"""
    if rc == 0:
        print ("Result Code 0: Connection Successful")
    else:
        print ("Error! Connected with result code " + str(rc))
        print ("| Reference for result code:")
        print ("| 1 - unaccepted protocol version")
        print ("| 2 - identifier rejected")
        print ("| 3 - server unavailable")
        print ("| 4 - bad username or password")
        print ("| 5 - not authorized")
    client.subscribe(MQTT_TOPIC)

def on_message(client, userdata, msg):
    """The callback for when a PUBLISH message is recieved from the server"""
    dataVal = float(msg.payload.decode("utf-8"))
    if msg.topic == "sensors/#/temperature":
        print("Temperature: " + str(dataVal))
        streamer.log(SENSOR_LOCATION_NAME + "Temperature (C)", dataVal)
    if msg.topic == "sensors/#/humidity":
        print("Humidity: " + str(dataVal))
        streamer.log(SENSOR_LOCATION_NAME + "Humidity (%)", dataVal)
    if msg.topic == "sensors/#/pressure":
        print("Pressure: " + str(dataVal))
        streamer.log(SENSOR_LOCATION_NAME + "Pressure (Pa)", dataVal)
    if msg.topic == "sensors/#/Light (lx)":
        print("Temperature: " + str(dataVal))
        streamer.log(SENSOR_LOCATION_NAME + "Temperature (C)", dataVal)


client.on_connect = on_connect
client.on_message = on_message
client.connect(MQTT_BROKER_IP, 1883)
client.loop_forever()
client.disconnect()