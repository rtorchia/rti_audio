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
	definition (name: "RTI Audio Zone", namespace: "rtorchia", author: "Ralph Torchia") {
        capability "Actuator"
        capability "Music Player"
		capability "Switch"
        capability "Switch Level"
        capability "Sensor"
//		capability "Polling"
//		capability "Refresh"
	    
 		command "muteOn"
        command "muteOff"
    	command "source1"
        command "source2"
        command "source3"
        command "source4"
        command "getStatus"
        command "setZoneSettings"
    }
        
	tiles(scale: 2) {
     	multiAttributeTile(name:"power", type:"generic", width:6, height:4) {
        	tileAttribute("device.switch", key:"PRIMARY_CONTROL") {
            	attributeState ("on",  label: "On",  action: "switch.off", icon: "st.Electronics.electronics16", backgroundColor: "#79b821")
        		attributeState ("off", label: "Off", action: "switch.on",  icon: "st.Electronics.electronics16", backgroundColor: "#ffffff")
            }
      		tileAttribute ("source", key: "SECONDARY_CONTROL") {
        		attributeState "source", label:'${currentValue}'
      		}
		}        
    	controlTile ("volume", "device.volume", "slider", height: 1, width: 6, range: "(0..100)") {
      		state ("volume", label: "Volume", action: "music Player.setLevel", backgroundColor: "#00a0dc")
    	}
        standardTile ("mute", "device.mute", decoration: "flat", width: 2, height: 2) {
      		state ("off", label:"Unmuted", action: "muteOn", icon: "https://raw.githubusercontent.com/rtorchia/rti_audio/master/resources/images/mute-off.png", backgroundColor: "#ffffff")
      		state ("on", label:"Muted", action: "muteOff", icon: "https://raw.githubusercontent.com/rtorchia/rti_audio/master/resources/images/mute-on.png", backgroundColor: "#ffffff")
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
        standardTile ("refresh", "device.getStatus", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
      		state ("default", label: "Refresh", action: "getStatus", icon: "st.secondary.refresh-icon", backgroundColor: "#ffffff")
    	}

		main "power"
  		details (["power", "volume", "mute", "1", "2", "3", "4", "refresh"])
	}
}

// map metadata to action calls
def on() {
	sendCommand(["power": "1"])
    setZoneSettings(["pwr":"1"], null)
}
def off() {
	sendCommand(["power": "0"])
    setZoneSettings(["pwr":"0"], null)
}
def source1() {
	sendCommand(["source": "1"])
    setZoneSettings(["src":"1"], parent.getSourceName("1"))
}
def source2() {
	sendCommand(["source": "2"])
    setZoneSettings(["src":"2"], parent.getSourceName("2"))
}
def source3() {
	sendCommand(["source": "3"])
    setZoneSettings(["src":"3"], parent.getSourceName("3"))
}
def source4() {
	sendCommand(["source": "4"])
    setZoneSettings(["src":"4"], parent.getSourceName("4"))
}
def setLevel(value) {
	sendCommand(["volume": "${value}"])
	sendEvent(name: "volume", value: value)
}
def muteOn() {
	sendCommand(["mute": "1"])
	sendEvent(name:"mute", value: "on")
}
def muteOff() {
	sendCommand(["mute":"0"])
   	sendEvent(name:"mute", value: "off")
}

// Refresh tile
def getStatus() {
	log.debug "Retrieving status update"
    parent.getCurrentConfig()
}

def setZoneSettings(evt, name) {
    log.debug "Received update config: ${evt}, ${name}"
    
    if (evt.containsKey("pwr")) {
		sendEvent(name: "switch", value: ((evt.pwr == "1") ? "on" : "off"))
    }
	if (evt.containsKey("vol")) {
        def vol = Math.round(((evt.vol.toInteger()*1.33)-100)*-1)
        sendEvent(name: "volume", value: vol)
    }
    if (evt.containsKey("mut")) {
    	sendEvent(name:"mute", value: ((evt.mut == "1") ? "on":"off"))
    }
    if (evt.containsKey("src")) {
        for (def i = 1; i < 5; i++) {
            if (i == evt.src.toInteger()) {
   	        	state.source = i
                state.sourceName = name
                sendEvent(name: "source${i}", value: "on")
            	sendEvent(name: "source", value: "Source: ${i}: ${name}")
            }
            else {
            	sendEvent(name: "source${i}", value: "off")
            }
        }
    }
}

private def sendCommand(data) {
	def zone = device.id
    
	log.debug "Sending command(${data}, ${zone})"
	parent.sendCommand(data, zone)
}
