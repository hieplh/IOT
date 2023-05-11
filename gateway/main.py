import threading

import adafruit.Adafruit as adafruit
import ai.features.recognize_face.RecognizeFace as recognize
import external_device.AIOT as aiot

adafruit = adafruit.Adafruit()
client = adafruit.__getattribute__("client")
recognizeObj = recognize.RecognizeFace(client)
aiotObj = aiot.AIOT(client)


def MyThread1():
    recognizeObj.detect_wear_mark()
    pass


def MyThread2():
    # aiotObj.measureTemperatureAndHumidity()
    pass


t1 = threading.Thread(target=MyThread1, args=[])
t2 = threading.Thread(target=MyThread2, args=[])
t1.start()
t2.start()
