import base64
import os
import time

import cv2  # Install opencv-python
import numpy as np
from keras.models import load_model  # TensorFlow is required for Keras to work

import adafruit.Adafruit as adafruit
import adafruit.Adafruit as devices


class RecognizeFace:

    def __init__(self, client):
        self.client = client

    def __open_camera__(self, cam_pos):
        camera = cv2.VideoCapture(cam_pos)
        return camera

    def __predict_score_image__(self, model, image):
        prediction = model.predict(image)
        index = np.argmax(prediction)
        return prediction[0][index]

    def __predict_class_image__(self, model, class_names, image):
        prediction = model.predict(image)
        index = np.argmax(prediction)
        return class_names[index]

    def __normalize_image__(self, image, width, height):
        # Resize the raw image into (224-height,224-width) pixels
        image = cv2.resize(image, (height, width))

        # Make the image a numpy array and reshape it to the models input shape.
        image = np.asarray(image, dtype=np.uint8).reshape(1, height, width, 3)
        # Normalize the image array
        return (image / 127.5) - 1
        # return image

    def detect_wear_mark(self):
        # Disable scientific notation for clarity
        np.set_printoptions(suppress=True)

        # Load the model
        dirname = os.path.dirname(__file__)
        fileTrainModel = os.path.join(dirname, "./train_model/keras_model.h5")
        model = load_model(filepath=fileTrainModel, compile=False)

        # Load the labels
        fileLabel = os.path.join(dirname, "./train_model/labels.txt")
        class_names = open(fileLabel, "r").readlines()

        # CAMERA can be 0 or 1 based on default camera of your computer
        camera = self.__open_camera__(0)

        try:
            count = 0
            previousTime = round(time.time() * 1000)
            while True:
                # Grab the webcamera's image.
                ret, image = camera.read()
                cv2.imshow("Webcam Image", image)


                # normalize image
                image = self.__normalize_image__(image, 224, 224)

                # Predicts the model
                class_name = self.__predict_class_image__(model, class_names, image)
                confidence_score = self.__predict_score_image__(model, image)

                # strImage = np.fromstring(model.predict(image), dtype=np.uint8)
                # _, frame = cv2.imencode('.jpg', strImage)
                # encode_base64_image = base64.b64encode(strImage)

                # Print prediction and confidence score

                curTime = round(time.time() * 1000)
                if curTime - previousTime >= 5000:
                    if "HAS MARK".__eq__(class_name[2:].strip().upper()):
                        if count % 2 == 0:
                            self.client.publish(devices.get('do_something'), 1)
                        count = count + 1
                        # print()
                    else:
                        self.client.publish(devices.get('do_something'), 0)
                        # print()
                    print("Class:", class_name[2:], end="")
                    print("Confidence Score:", str(np.round(confidence_score * 100))[:-2], "%")
                    previousTime = round(time.time() * 1000)

                # Listen to the keyboard for presses.
                keyboard_input = cv2.waitKey(1)

                # 27 is the ASCII for the esc key on your keyboard.
                if keyboard_input == 27:
                    break

                # time.sleep(5)
        finally:
            camera.release()
            cv2.destroyAllWindows()