/**
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  RTI Audio Zone
 *
 *  Copyright 2020 Ralph Torchia
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Date: 2020-10-13
 */

metadata {
	definition (
        name:          "RTI Audio Zone", 
        namespace:     "rtorchia", 
        author:        "Ralph Torchia",
        ocfDeviceType: "oic.d.networkaudio",
        vid:           "6f089406-ab70-3814-8294-3928dda5c1ca",
        mnmn:          "SmartThingsCommunity"
	)
    
    {
		capability "Switch"
		capability "Audio Mute"
        capability "Audio Volume"
        capability "pizzafiber16443.audioSources"
        capability "Refresh"
    }
        
	tiles {}
}

// map to capability calls
def on() {
	setZoneSettings(["pwr": "1"], null)
	sendCommand(["power": "1"])
    sendEvent(name: "switch", value: "on")
}

def off() {
    setZoneSettings(["pwr": "0"], null)
	sendCommand(["power": "0"])
    sendEvent(name: "switch", value: "off")
}

def mute() {
    sendCommand(["mute": "1"])
    sendEvent(name: "mute", value: "on")
    setZoneSettings(["mut": "1"], null)
}

def unmute() {
	sendCommand(["mute":"0"])
    sendEvent(name: "mute", value: "off")
    setZoneSettings(["mut": "0"], null)
}

def setVolume(value) {
	sendCommand(["volume": "${value}"])
    sendEvent(name: "volume", value: value)
}

def setAudioSources(String value) {
    def sourceName = parent.getSourceName("${value}")
	sendCommand(["source": value])
	setZoneSettings(["src": "${value}"], sourceName)
    sendEvent(name: "audioSources", value: sourceName)	
}

def updated() {
	refresh()
}

def refresh() {
	log.debug "Retrieving status update"
    parent.getCurrentConfig()
}

// set ST device to RTI device settings
def setZoneSettings(evt, name) {
    log.debug "Received update config: ${evt}, ${name}"
    
    if (evt.containsKey("pwr")) {
		sendEvent(name: "power", value: ((evt.pwr == "1") ? "powerOn" : "powerOff"))
    }
	if (evt.containsKey("vol")) {
        def vol = Math.round((1-(evt.vol.toInteger()/75))*100)
        sendEvent(name: "audioVolume", value: vol)
        sendEvent(name: "volume", value: vol)
    }
    if (evt.containsKey("mut")) {
        log.debug "sendEvent mut == ${evt.mut}"
    	sendEvent(name:"mute", value: ((evt.mut == "1") ? "muted":"unmuted"))
    }
    if (evt.containsKey("src")) {
        for (def i = 1; i < 5; i++) {
            if (i == evt.src.toInteger()) {
   	        	state.source = i
                state.sourceName = name
                sendEvent(name: "source${i}", value: "on")
            	sendEvent(name: "source", value: "Source ${i}: ${name}")
                sendEvent(name: "audioSources", value: "${name}", descriptionState: "Source ${i}: ${name}")
            }
            else {
            	sendEvent(name: "source${i}", value: "off")
            }
        }
    }
}

//send new settings to RTI device
def sendCommand(data) {
	def zone = device.id
    
	log.debug "Sending command(${data}, for ${zone})"
	parent.sendCommand(data, zone)
}
