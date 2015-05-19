#!/usr/bin/env python
# -*- coding: utf-8 -*- 
from controllers_objects import Controller_Light
from controllers_objects import Controller_Door
from controllers_objects import Controller_TV

class Voice_Controller:

    def __init__(self):
        self.objects = [];
        self.objects.append({ 
            'name' : 'puerta', 
            'commands' : ['abrir', 'cerrar'],
            'values' : []
            });

        self.objects.append({
            'name' : 'luz',
            'commands' : ['encender', 'apagar'],
            'values' : [u'habitación', 'habitacion', 'sala', 'cocina']
            });

        self.objects.append({
            'name' : 'canal',
            'commands' : ['siguiente', 'anterior'],
            'value' : []
            })

        self.objects.append({
            'name' : 'volumen',
            'commands' : ['subir', 'bajar'],
            'value' : []
            })

        self.objects.append({
            'name' : 'televisor',
            'commands' : ['apagar'],
            'value' : []
            })


    def execute(self, command):
        array_command = self.get_array_command(command)
        if self.is_valid(array_command) is False:
            return False

        controller = self.find_controller(array_command)
        return controller.execute(array_command)


    def get_array_command(self, command):
        normalized_command = ' '.join(command.lower().strip().split());
        return normalized_command.split(' ')


    def is_valid(self, array_command):
        if len(array_command) < 2:
            return False

        object_name = array_command[1]
        object_ = self.get_object(object_name);
        if object_ is None:
            return False

        command_name = array_command[0]
        if self.match_command(object_, command_name) is False:
            return False

        if len(array_command) == 2 or len(object_['values']) == 0:
            return True

        value_name = array_command[2]
        return self.match_value(object_, value_name)


    def match_command(self, object_, command_name):
        for command in object_['commands']:
            if command == command_name:
                return True
        return False

    def match_value(self, object_, value_name):
        for value in object_['values']:
            if value == value_name:
                return True
        return False

    def get_object(self, name):
        for object_ in self.objects:
            if object_['name'] == name:
                return object_
        return None

    def find_controller(self, array_command):
        object_name = array_command[1]
        if object_name == 'puerta':
            return Controller_Door()

        if object_name == 'luz':
            return Controller_Light()

        if object_name == 'volumen' or object_ == 'canal' or object_ == 'televisor':
            return Controller_TV()
