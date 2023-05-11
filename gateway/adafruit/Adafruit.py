import sys

from Adafruit_IO import MQTTClient


DEVICES = {
    "sensor_1": "sensor.sensor-1",
    "sensor_2": "sensor.sensor-2",
    "detect_face": "detect-face",
    "do_something": "do-something"
}

AIO_FEED_ID = DEVICES.values()
AIO_USERNAME = "hieplh"
AIO_KEY = "aio_DePt82n5ijnuAg6f0IySeNpCrqle"


def get(key):
    return DEVICES.get(key)


class Adafruit:

    def __init__(self,
                 username=AIO_USERNAME,
                 key=AIO_KEY,
                 feed_id=AIO_FEED_ID):
        self.username = username
        self.key = key
        self.feed_id = feed_id
        self.client = MQTTClient(self.username, self.key)
        self.connect()

    def connect(self):
        self.client.connect()
        self.client.on_disconnect = self.disconnected
        self.client.on_message = self.message
        self.client.on_subscribe = self.subscribe
        self.client.loop_background()
        print("Ket noi thanh cong ...")

    def subscribe(self):
        print("Subscribe thanh cong ...")

    def disconnected(self):
        print("Ngat ket noi ...")
        sys.exit(1)

    def message(self, feed_id, payload):
        print("device:", feed_id, "- Nhan du lieu:", payload)
        self.client.publish(feed_id, payload)
