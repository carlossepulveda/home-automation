#!/usr/bin/env python
# -*- coding: utf-8 -*- 

import RPi.GPIO as GPIO

class Controller_Light:

    def __init__(self):
        self.objects = ['habitacion', 'sala', 'cocina']
        self.LIVINGROOM_LIGHTS = 11
        self.BEDROOM_LIGHTS = 7
        self.KITCHEN_LIGHTS = 16

    def execute(self, array_command):
        command = array_command[0]
        if len(array_command) == 2:
            return self.manage_all(command)

        object_ = array_command[2]
        if object_ == 'habitacion' or object_ == u'habitación':
            return self.manage_bed_room(command)
        
        if object_ == 'sala':
            return self.manage_living_room(command)

        if object_ == 'cocina':
            return self.manage_kitchen_room(command)

    def manage_all(self, command):
        return self.manage_bed_room(command) and self.manage_kitchen_room(command) and self.manage_living_room(command)

    def manage_bed_room(self, command):
        if command == 'encender':
            print('ON BEDROOM LIGHTS')
            GPIO.output(self.BEDROOM_LIGHTS, True)
            return True
        else:
            print('OFF BEDGROOM LIGHTS')
            GPIO.output(self.BEDROOM_LIGHTS, False)
            return True

    def manage_living_room(self, command):
        if command == 'encender':
            print('ON LIVING LIGHTS')
            GPIO.output(self.LIVINGROOM_LIGHTS, True)
            return True
        else:
            print('OFF LIVING LIGHTS')
            GPIO.output(self.LIVINGROOM_LIGHTS, False)
            return True

    def manage_kitchen_room(self, command):
        if command == 'encender':
            print('ON KITCHEN LIGHTS')
            GPIO.output(self.KITCHEN_LIGHTS, True)
            return True
        else:
            print('OFF KITCHEN LIGHTS')
            GPIO.output(self.KITCHEN_LIGHTS, False)
            return True