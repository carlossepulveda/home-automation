#!/usr/bin/env python
# -*- coding: utf-8 -*- 

import RPi.GPIO as GPIO
import time

class Controller_Door:

    def __init__(self):
        self.MOTOR_FORWARD = 13
        self.MOTOR_BACKWARD = 15

    def execute(self, array_command):
        command = array_command[0]
        if command == 'abrir':
            print('OPEN DOOR')
            self.open_door()
            return True
        else:
            print('CLOSE DOOR')
            self.close_door()
            return True

    def open_door(self):
        GPIO.output(self.MOTOR_BACKWARD, False)
        GPIO.output(self.MOTOR_FORWARD, True)
        time.sleep(0.5)
        GPIO.output(self.MOTOR_FORWARD, False)

    def close_door(self):
        GPIO.output(self.MOTOR_FORWARD, False)
        GPIO.output(self.MOTOR_BACKWARD, True)
        time.sleep(0.5)
        GPIO.output(self.MOTOR_BACKWARD, False)