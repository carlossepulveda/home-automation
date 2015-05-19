import RPi.GPIO as GPIO
import time

@Singleton
class Raspberry_Controller:

    def __init__(self):
        self.GPIO

        MOTOR_FORWARD = 13
        MOTOR_BACKWARD = 15
        LIVINGROOM_LIGHTS = 11
        BEDROOM_LIGHTS = 7

        GPIO.setmode(GPIO.BOARD)

        #bed lights
        GPIO.setup(BED_PORT, GPIO.OUT)
        #living lights
        GPIO.setup(LIVING_PORT, GPIO.OUT)
        #motor
        GPIO.setup(MOTOR_FORWARD, GPIO.OUT)
        GPIO.setup(MOTOR_BACKWARD, GPIO.OUT)

    def move_motor(mov):
        if mov == 1:
            GPIO.output(MOTOR_BACKWARD, False)
            GPIO.output(MOTOR_FORWARD, True)
            time.sleep(0.5)
            GPIO.output(MOTOR_FORWARD, False)
        else:
            GPIO.output(MOTOR_FORWARD, False)
            GPIO.output(MOTOR_BACKWARD, True)
            time.sleep(0.5)
            GPIO.output(MOTOR_BACKWARD, False)






