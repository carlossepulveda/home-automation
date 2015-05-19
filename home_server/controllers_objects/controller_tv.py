import httplib
import json
import xml.etree.ElementTree as ET

class Singleton(object):
    _instance = None
    def __new__(class_, *args, **kwargs):
        if not isinstance(class_._instance, class_):
            class_._instance = object.__new__(class_, *args, **kwargs)
        return class_._instance

class Controller_TV(Singleton):
    pass

    def __init__(self):
        self.pair_code = '343067'
        self.connection = httplib.HTTPConnection('192.168.1.4:8080')
        self.headers = {'Content-type': 'application/atom+xml'}
        self.UP_CHANNEL_CODE = '27'
        self.DOWN_CHANNEL_CODE = '28'
        self.UP_VOLUME_CODE = '24'
        self.DOWN_VOLUME_CODE = '25'
        self.SHUTDOWN_TV = '1'

        request_session_command = '<?xml version="1.0" encoding="utf-8"?><auth><type>AuthReq</type><value>' + self.pair_code + '</value></auth>'
        self.connection.request('POST', '/roap/api/auth', request_session_command, self.headers)

        response = self.connection.getresponse().read().decode()
        print(response)
        root = ET.fromstring(response)
        self.session_id = root.find('session').text

    def execute(self, array_command):
        command = array_command[0]
        if command == 'siguiente':
            return self.next_channel()
        if command == 'anterior':
            return self.back_channel()
        if command == 'subir':
            return self.up_volume()
        if command == 'bajar':
            return self.down_volume()
        if command == 'apagar':
            return self.shutdown_tv()

    def next_channel(self):
        body = '<?xml version="1.0" encoding="utf-8"?><command><session>' + self.session_id + '</session><type>HandleKeyInput</type><value>' + self.UP_CHANNEL_CODE + '</value></command>'
        self.connection.request('POST', '/roap/api/command', body, self.headers)
        print(self.connection.getresponse())
        return True

    def back_channel(self):
        body = '<?xml version="1.0" encoding="utf-8"?><command><session>' + self.session_id + '</session><type>HandleKeyInput</type><value>' + self.DOWN_CHANNEL_CODE+ '</value></command>'
        self.connection.request('POST', '/roap/api/command', body, self.headers)
        print(self.connection.getresponse())
        return True

    def up_volume(self):
        body = '<?xml version="1.0" encoding="utf-8"?><command><session>' + self.session_id + '</session><type>HandleKeyInput</type><value>' + self.UP_VOLUME_CODE+ '</value></command>'
        self.connection.request('POST', '/roap/api/command', body, self.headers)
        print(self.connection.getresponse())
        return True

    def down_volume(self):
        body = '<?xml version="1.0" encoding="utf-8"?><command><session>' + self.session_id + '</session><type>HandleKeyInput</type><value>' + self.DOWN_VOLUME_CODE+ '</value></command>'
        self.connection.request('POST', '/roap/api/command', body, self.headers)
        print(self.connection.getresponse())
        return True

    def shutdown_tv(self):
        body = '<?xml version="1.0" encoding="utf-8"?><command><session>' + self.session_id + '</session><type>HandleKeyInput</type><value>' + self.SHUTDOWN_TV + '</value></command>'
        self.connection.request('POST', '/roap/api/command', body, self.headers)
        print(self.connection.getresponse())
        return True

