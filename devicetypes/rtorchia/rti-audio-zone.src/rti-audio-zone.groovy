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
 */
metadata {
	definition (
        name:          "RTI Audio Zone", 
        namespace:     "rtorchia", 
        author:        "Ralph Torchia",
        ocfDeviceType: "oic.d.receiver",
        vid:           "e0e2d839-53a3-332d-9c2d-655e9502ab38",
        mnmn:          "SmartThingsCommunity"
	)
    
    {
		capability "Switch"
		capability "Audio Mute"
        capability "pizzafiber16443.audioVolume"
        capability "pizzafiber16443.audioSources"
        capability "Refresh"

		attribute "power", "string"
        attribute "source", "string"
        attribute "1", "string"
        attribute "2", "string"
        attribute "3", "string"
        attribute "4", "string"
        
 		command "setAudioVolume"
        command "setAudioSources"
        command "powerOn"
        command "powerOff"
        command "muteOn"
        command "muteOff"
    	command "source1"
        command "source2"
        command "source3"
        command "source4"
    }
        
	tiles(scale: 2) {
     	multiAttributeTile(name:"status", type:"generic", width:6, height:4) {
        	tileAttribute ("device.power", key:"PRIMARY_CONTROL") {
            	attributeState ("powerOn",  label: "On",  action: "powerOff", icon: "st.Electronics.electronics16", backgroundColor: "#79b821")
        		attributeState ("powerOff", label: "Off", action: "powerOn",  icon: "st.Electronics.electronics16", backgroundColor: "#ffffff")
            }
      		tileAttribute ("device.source", key: "SECONDARY_CONTROL") {
        		attributeState ("source", label:'${currentValue}')
      		}
        }
        valueTile ("volumeLabel", "device.volumeLabel", decoration: "flat", height: 1, width: 2) {
      		state ("volumeLabel", label: "Volume :")
    	}
        controlTile ("audioVolume", "device.audioVolume", "slider", height: 1, width: 4, range: "(0..100)") {
      		state ("audioVolume", label: "Volume", action: "setAudioVolume", unit: "%", backgroundColor: "#00a0dc")
    	}
        standardTile ("audioMute", "device.mute", decoration: "flat", width: 2, height: 2) {
      		state ("unmuted", label:"Unmuted", action: "muteOn", icon: "https://raw.githubusercontent.com/rtorchia/rti_audio/master/resources/images/mute-off.png", backgroundColor: "#ffffff")
      		state ("muted", label:"Muted", action: "muteOff", icon: "https://raw.githubusercontent.com/rtorchia/rti_audio/master/resources/images/mute-on.png", backgroundColor: "#ffffff")
    	}
    	standardTile ("1", "device.source1", decoration: "flat", width: 2, height: 2) {
      		state ("off", label: "Source 1", action: "source1", icon: "https://raw.githubusercontent.com/rtorchia/rti_audio/master/resources/images/indicator-dot-gray.png", backgroundColor: "#ffffff")
      		state ("on", label: "Source 1", action: "source1", icon: "https://raw.githubusercontent.com/rtorchia/rti_audio/master/resources/images/indicator-dot-green.png", backgroundColor: "#ffffff")
    	}
    	standardTile ("2", "device.source2", decoration: "flat", width: 2, height: 2) {
      		state ("off", label: "Source 2", action:"source2", icon: "https://raw.githubusercontent.com/rtorchia/rti_audio/master/resources/images/indicator-dot-gray.png", backgroundColor: "#ffffff")
      		state ("on", label: "Source 2", action:"source2", icon: "https://raw.githubusercontent.com/rtorchia/rti_audio/master/resources/images/indicator-dot-green.png", backgroundColor: "#ffffff")
    	}
    	standardTile ("3", "device.source3", decoration: "flat", width: 2, height: 2) {
      		state ("off", label: "Source 3", action: "source3", icon: "https://raw.githubusercontent.com/rtorchia/rti_audio/master/resources/images/indicator-dot-gray.png", backgroundColor: "#ffffff")
    	  	state ("on", label: "Source 3", action: "source3", icon: "https://raw.githubusercontent.com/rtorchia/rti_audio/master/resources/images/indicator-dot-green.png", backgroundColor: "#ffffff")
    	}
    	standardTile ("4", "device.source4", decoration: "flat", width: 2, height: 2) {
      		state ("off", label: "Source 4", action: "source4", icon: "https://raw.githubusercontent.com/rtorchia/rti_audio/master/resources/images/indicator-dot-gray.png", backgroundColor: "#ffffff")
    	  	state ("on", label: "Source 4", action: "source4", icon: "https://raw.githubusercontent.com/rtorchia/rti_audio/master/resources/images/indicator-dot-green.png", backgroundColor: "#ffffff")
    	}
		standardTile("refresh", "device.refresh", width: 2, height: 2, decoration: "flat") {
        	state "default", label:"Refresh", action:"refresh.refresh", icon:"st.secondary.refresh-icon"
        }
		main "status"
  		details (["status", "volumeLabel", "audioVolume", "audioMute", "1", "2", "3", "4", "refresh"])
	}
}

// map metadata to action calls
def on() {
	powerOn()
}
def off() {
	powerOff()
}

def mute() {
	muteOn()
}
def unmute() {
	muteOff()
}

def powerOn() {
    setZoneSettings(["pwr": "1"], null)
	sendCommand(["power": "1"])
    sendEvent(name: "switch", value: "on")
}
def powerOff() {
    setZoneSettings(["pwr": "0"], null)
	sendCommand(["power": "0"])
    sendEvent(name: "switch", value: "off")
}
def source1() {
	sendCommand(["source": "1"])
    setZoneSettings(["src": "1"], parent.getSourceName("1"))
}
def source2() {
	sendCommand(["source": "2"])
    setZoneSettings(["src": "2"], parent.getSourceName("2"))
}
def source3() {
	sendCommand(["source": "3"])
    setZoneSettings(["src": "3"], parent.getSourceName("3"))
}
def source4() {
	sendCommand(["source": "4"])
    setZoneSettings(["src": "4"], parent.getSourceName("4"))
}
def setAudioVolume(value) {
	sendCommand(["volume": "${value}"])
    //setZoneSettings(["vol": "${value}"], null)
    sendEvent(name: "audioVolume", value: value)
}
def setAudioSources(String value) {
    def sourceName = parent.getSourceName("${value}")
	sendCommand(["source": value])
	setZoneSettings(["src": "${value}"], sourceName)
    sendEvent(name: "audioSources", value: sourceName)	
}

def muteOn() {
    sendCommand(["mute": "1"])
    sendEvent(name: "mute", value: "on")
    setZoneSettings(["mut": "1"], null)
}
def muteOff() {
	sendCommand(["mute":"0"])
    sendEvent(name: "mute", value: "off")
    setZoneSettings(["mut": "0"], null)
}

def refresh() {
	log.debug "Retrieving status update"
    parent.getCurrentConfig()
}

def setZoneSettings(evt, name) {
    log.debug "Received update config: ${evt}, ${name}"
    
    if (evt.containsKey("pwr")) {
		sendEvent(name: "power", value: ((evt.pwr == "1") ? "powerOn" : "powerOff"))
    }
	if (evt.containsKey("vol")) {
        def vol = Math.round((1-(evt.vol.toInteger()/75))*100)
        sendEvent(name: "audioVolume", value: vol)
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

def sendCommand(data) {
	def zone = device.id
    
	log.debug "Sending command(${data}, for ${zone})"
	parent.sendCommand(data, zone)
}
