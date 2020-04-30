/**
 *  RTI Audio
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
 */

import groovy.json.*

definition(
    name: "RTI Audio",
    namespace: "rtorchia",
    author: "Ralph Torchia",
    description: "Integrate and control RTI audio distribution system",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")
    singleinstance: true

preferences {
//	page(name: "MainSetup")
    section("RTI Audio Setup") {
    	input(name: "deviceIP", type: "text", title: "IP Address", description: "The IP of your RTI audio distribution system (required)", required: true)
        input(name: "maxZones", type: "number", title: "Max Zones", description: "Max number of zones system supports", range: "1...8", required: true)
	}
}

def MainSetup() {
	// todo
}

def installed() {
	log.debug "Installed with settings: ${settings}"
    getcurrentconfig()
    createZoneDevices()
	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
    getcurrentconfig()
    createZoneDevices()
	unsubscribe()
	initialize()
}

def initialize() {
	// todo	
}
// create zones for audio amp
def createZoneDevices() {
   def zoneCounter = 0
    4.times {
    	zoneCounter++
    	def deviceDNI = "RTI Audio Zone ${zoneCounter}"
    	def device = getChildDevice(deviceDNI)
    	log.debug "deviceDNI: ${deviceDNI}"
        if (!device) device = addChildDevice("rtorchia", "RTI Audio Zone", deviceDNI, null, [label: deviceDNI])
    }
}

def getcurrentconfig() {
    log.debug "Trying to access device at ${deviceIP}"
    
    def hubAction = new physicalgraph.device.HubAction(
    	method: "GET",
        path: "/rti_status.cgi",
        headers: ["HOST": "${deviceIP}:80", "Content-Type": "text/xml; charset='utf-8'"],
        null,[callback: parseStatus])
    sendHubCommand(hubAction)
}

def parseStatus(physicalgraph.device.HubResponse hubResponse) {
    def i = 0
    def config = hubResponse.body.replaceAll("<!--#...-->", "")
    def slurper = new JsonSlurper()
    def json = slurper.parseText(config)
    // Setup of response from device is Zones: {Zone: {grp, mut, pwr, src, vol} }
    //log.info "Source: ${json.zones.zone.src}"
    //log.info "Power : ${json.zones.zone.pwr}"
    //log.info "Volume: ${json.zones.zone.vol}"
    //log.info "Mute  : ${json.zones.zone.mut}"
    //log.info "Group : ${json.zones.zone.grp}"
    //for (i = 0; i < maxZones; i++) {
    //	def zsource = json.zones.zone[i].vol
    //    log.info "Volume ${i}: ${zsource}"
    //}
}         
        
def installzones () {
	// todo
}

def sendcmd(rti_attr, rti_zone, rti_status) {
	// send cmd to RTI device to change attribute
    def rti_cmd = ""
	if (rti_attr == "source") {
		// rti_zi.cgi?z= &i=
        rti_cmd = "/rti.zi.cgi?z=${rti_zone}&i=${rti_status}&s=0"
	} else if (rti_attr == "volume") {
    	// rti_zvs.cgi?z= &v=
        rti_cmd = "/rti.zvs.cgi?z=${rti_zone}&v=${rti_status}&s=0"
	} else if (rti_attr == "group") {
    	// rti_zg.cgi?z &g=    
        rti_cmd = "/rti.zg.cgi?z=${rti_zone}&g=${rti_status}&s=0"
    } else if (rti_attr == "power") {
    	// rti_zp0.cgi?z= , rti_zp1.cgi?z=
        if (rti_status == "0") {
        	rti_cmd = "/rti.zp0.cgi?z=${rti_zone}&s=0"
        } else {
        	rti_cmd = "/rti.zp1.cgi?z=${rti_zone}&s=0"
        }
    } else if (rti_attr == "mute") {
    	// rti_zm0.cgi?z= , rti_zm1.cgi?z=
        if (rti_status == "0") {
        	rti_cmd = "/rti.zm0.cgi?z=${rti_zone}&s=0"
        } else {
        	rti_cmd = "/rti.zm1.cgi?z=${rti_zone}&s=0"
        }
    }
	
	log.debug "Trying to access device at ${deviceIP}"
    
    def result = new physicalgraph.device.HubAction(
    	method: "GET",
        path: "${rti_cmd}",
        headers: ["HOST": "${deviceIP}:80", "Content-Type": "text/xml; charset='utf-8'"],
        null,[callback: parseStatus])
    sendHubCommand(result)    
}
