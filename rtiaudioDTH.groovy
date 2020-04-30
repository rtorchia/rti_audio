/**
 *  Copyright 2015 SmartThings
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
 *  Sonos Player
 *
 *  Author: SmartThings/**
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
		capability "Switch"
        capability "Switch Level"
		capability "Refresh"
		capability "Music Player"
        
        command "powerOff"
        command "powerOn"
        command "muter"
        command "zone1on"
        command "zone2on"
        command "zone3on"
        command "zone4on"
	}
        
	simulator {
		// testing code
	}

	tiles(scale: 2) {
		multiAttributeTile(name: "powerTile", type: "generic", width:6, height:4) {
			tileAttribute("device.switch", key: "PRIMARY_CONTROL") {
				attributeState "powerOn", label:"On", icon: "st.Entertainment.entertainment11", action: "powerOff", nextState: "off"
				attributeState "powerOff", label:"Off", icon: "st.Entertainment.entertainment11", action: "powerOn", nextState: "on"
			}
        }
		multiAttributeTile(name: "volumeTile", type: "generic", width:6, height:4) {
            tileAttribute("device.switch", key: "PRIMARY_CONTROL") {
				attributeState "on", label:"On", nextState: "off"
				attributeState "off", label:"Off", nextState: "on"
            }
            tileAttribute("device.level", key: "SECONDARY_CONTROL") {
            	attributeState "level", icon: "st.Weather.weather1", action: "muter"
            }
            tileAttribute("device.level", key: "SLIDER_CONTROL") {
				attributeState "level", action:"setLevel"
			}
			tileAttribute("device.mute", key: "MEDIA_MUTED") {
				attributeState "unmuted", icon: "st.custom.sonos.unmuted", action: "unmuted", nextState: "muted"
				attributeState "muted", icon: "st.custom.sonos.muted", action: "muted", nextState: "unmuted"
			}
        }
        standardTile("zone1", "device.switch", width: 1, height: 2) {
    		state "off", label: "off", icon: "st.unknown.thing.thing-circle", backgroundColor: "#ffffff", action: "zone1on"
    		state "on", label: "on", icon: "st.unknown.thing.thing-circle", backgroundColor: "#00a0dc", action: "zone1on"
        }
        standardTile("zone2", "device.switch", width: 1, height: 2) {
    		state "off", label: "off", icon: "st.unknown.thing.thing-circle", backgroundColor: "#ffffff", action: "zone2on"
    		state "on", label: "on", icon: "st.unknown.thing.thing-circle", backgroundColor: "#00a0dc", action: "zone2on"
        }
        standardTile("zone3", "device.switch", width: 1, height: 2) {
    		state "off", label: "off", icon: "st.unknown.thing.thing-circle", backgroundColor: "#ffffff", action: "zone3on"
    		state "on", label: "on", icon: "st.switches.switch.on", backgroundColor: "#00a0dc", action: "zone3on"
        }
        standardTile("zone4", "device.switch", width: 1, height: 2) {
    		state "off", label: "off", icon: "st.unknown.thing.thing-circle", backgroundColor: "#ffffff", action: "zone4on"
    		state "on", label: "on", icon: "st.unknown.thing.thing-circle", backgroundColor: "#00a0dc", action: "zone4on"
        }
        
		main(["powerTile"])
  		details(["powerTile", "volumeTile", "zone1","zone2","zone3","zone4"])
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
	// TODO: handle 'activities' attribute
	// TODO: handle 'currentActivity' attribute
	// TODO: handle 'inputSource' attribute
	// TODO: handle 'supportedInputSources' attribute
	// TODO: handle 'playbackStatus' attribute
	// TODO: handle 'supportedPlaybackCommands' attribute
}

def installed() {
	sendEvent(name: "level", value: 50)
	sendEvent(name: "mute", value: "unmuted")
	sendEvent(name: "status", value: "on")
    zoneAlloff()
    sendEvent(name: "zone1", value: "on")
}

// handle commands
def muted() {
	log.debug "Muted"
	sendEvent(name: "mute", value: "muted")
}

def unmuted() {
	log.debug "Unmuted"
	sendEvent(name: "mute", value: "unmuted")
}

def setLevel(level) {
	int vol
    vol = level/1.33
    log.debug "Volume: (-${vol}) ${level}%"
    sendEvent(name: "level", value: level)
}

def powerOn() {
	log.debug "Zone On"
	sendEvent(name: "power", value="on")
}

def powerOff() {
	log.debug "Zone Off"
	sendEvent(name: "power", value="off")
}

def zone1on() {
	zoneAlloff()
    log.debug "Zone 1 On"
	sendEvent(name: "zone1", value="on")
}
def zone2on() {
	zoneAlloff()
	log.debug "Zone 2 On"
    sendEvent(name: "zone2", value="on")
}
def zone3on() {
	zoneAlloff()
	log.debug "Zone 3 On"
    sendEvent(name: "zone3", value="on")
}
def zone4on() {
	zoneAlloff()
	log.debug "Zone 4 On"
	sendEvent(name: "zone4", value="on")
}

def zoneAlloff() {
  	sendEvent(name: "zone1", value="off")
    sendEvent(name: "zone2", value="off")
    sendEvent(name: "zone3", value="off")
    sendEvent(name: "zone4", value="off")
}
