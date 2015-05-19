# Home-Automation

##How does it work?

It's a Raspberry Pi "test". This project consists of a "brain" that is in charge of managing the house. The "brain" is a python server that runs on a Raspberry Pi, and receives orders through a REST API.

The orders are sent by a smartphone (specifically Android) via voice command. The Android app processes voice commands and sends a http request to the "brain". In the moment that the Android app start, a background thread scans the local network for finding the "brain" (loop from 0 to 254), whether the brain is not found, the scanning can be launched again.

When the "brain" receives orders, it parses and executes them. Some examples about kind of orders could be : "abrir puerta" (open door), "encender luz habitación" (turn on bedroom light), "apagar luz" (turn off lights), "encender televisor" (turn on TV), "siguiente canal" (next channel), "subir volumen" (volume up).


##Project Structure

* Android app => It's in the AndoidClient folder
* Home Server (brain) => It's in the home_server folder (A simple Python server)
