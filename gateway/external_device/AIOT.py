import time
from datetime import datetime

import serial.tools.list_ports

import adafruit.Adafruit as devices


class AIOT:

    def __init__(self, client):
        self.client = client
        self.ser = serial.Serial(port=self.getPort(), baudrate=115200)

    def getPort(self):
        ports = serial.tools.list_ports.comports()
        N = len(ports)
        commPort = "None"
        for i in range(0, N):
            port = ports[i]
            strPort = str(port)
            if "USB-SERIAL CH340" in strPort:
                commPort = str(port.device)

        return commPort

    def processData(self, data):
        data = data.replace("!", "")
        data = data.replace("#", "")
        return data.split(":")

    # def readSerial(self):
    #     value = ''
    #     while True:
    #         bytesToRead = self.ser.read().decode("UTF-8")
    #         if (len(bytesToRead) > 0):
    #             value = value + bytesToRead
    #             if ("#" in value) and ("!" in value):
    #                 start = value.find("!")
    #                 end = value.find("#")
    #                 return self.processData(value[start:end + 1])

    def readSerial(self):
        value = ''
        while True:
            bytesToRead = self.ser.read().decode("UTF-8")
            if (len(bytesToRead) > 0):
                value = value + bytesToRead
                if ("#" in value) and ("!" in value):
                    start = value.find("!")
                    end = value.find("#")
                    return self.processData(value[start:end + 1])

    def measureTemperatureAndHumidity(self):
        m_data = {}
        while True:
            data = self.readSerial()
            m_data[data[1]] = data[2]
            if len(m_data) >= 2:
                print("m_data", m_data)
                # if (data[1] == "T"):
                self.client.publish(devices.get('sensor_1'), m_data['T'])
                # if (data[1] == "H"):
                self.client.publish(devices.get('sensor_2'), m_data['H'])
                print("send success", datetime.now())
                m_data = {}
                time.sleep(1)