#!/usr/bin/env python
# -*- coding: latin1 -*- 
import sys

from BaseHTTPServer import HTTPServer
from voice_controller import Voice_Controller
from web_server import Web_Server
import RPi.GPIO as GPIO

def initialize_raspberry_pins() :
    MOTOR_FORWARD = 13
    MOTOR_BACKWARD = 15
    LIVINGROOM_LIGHTS = 11
    BEDROOM_LIGHTS = 7
    KITCHEN_LIGHTS = 16

    GPIO.setmode(GPIO.BOARD)

    #bed lights
    GPIO.setup(BEDROOM_LIGHTS, GPIO.OUT)
    #living lights
    GPIO.setup(LIVINGROOM_LIGHTS, GPIO.OUT)
    #kitchen lights
    GPIO.setup(KITCHEN_LIGHTS, GPIO.OUT)
    #motor
    GPIO.setup(MOTOR_FORWARD, GPIO.OUT)
    GPIO.setup(MOTOR_BACKWARD, GPIO.OUT)

def start_web_server(port) :
    server = HTTPServer(("0.0.0.0", port), Web_Server)
    print("Running on port : " + str(port))
    server.serve_forever()


initialize_raspberry_pins()
start_web_server(80)