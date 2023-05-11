import random
import time

import adafruit.Adafruit as Adafruit
from adafruit.feeds.model.Sensor import Sensor


class Device:

    def __init__(self, client):
        self.client = client

    def process(self, source, destination, value):
        self.client.publish(source.key, value)
        source.state = False
        destination.state = True

    def test_post_value(self):
        const_count = 3
        count = const_count
        sensor_1 = Sensor(Adafruit.get("sensor_1"), True)
        sensor_2 = Sensor(Adafruit.get("sensor_2"), False)
        while True:
            count -= 1
            if count <= 0:
                if sensor_1.state:
                    self.process(sensor_1, sensor_2, random.uniform(50.0, 120.0))
                elif sensor_2.state:
                    self.process(sensor_2, sensor_1, random.uniform(0.0, 100.0))
                count = const_count
            time.sleep(1)