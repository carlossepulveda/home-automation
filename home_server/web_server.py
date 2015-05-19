#!/usr/bin/env python
# -*- coding: latin1 -*- 
import sys

from BaseHTTPServer import HTTPServer
from BaseHTTPServer import BaseHTTPRequestHandler
import json
import urlparse
from voice_controller import Voice_Controller
 
class Web_Server (BaseHTTPRequestHandler) :

    def do_GET(self) :
        if self.path == "/home-automation/ping"
            self.send_response(200)
            self.wfile.close()

    def do_POST(self) :
        if self.path == "/control/voice/command" :
            body = self.get_body()
            command = body['command']
            voice_controller = Voice_Controller()
            if voice_controller.execute(unicode(command)):
                self.send_response(200)
            else:
                self.send_response(404)

    def get_body(self):
        length = int(self.headers['Content-Length'])
        body_str = self.rfile.read(length).decode('latin1')
        body = json.loads(body_str)

        return body