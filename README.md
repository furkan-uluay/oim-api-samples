# ORACLE IDENTITY MANAGER API SAMPLES
__Version: 12.2.1.4__ 


This project includes common OIM API's and how they can be used on client machine or server (OIM Platform)

Project contains following operations:

* Access Policy Operations
* Application Instance Operations
* Entitlement Operations
* IT Resource Operations
* Lookup Operations
* Organization Operations
* Platform Utils Service Operations
* Process Form Operations
* Reconciliation Operations
* Role Operations
* Scheduler Job Operations
* User Operations


## Required Jars


* commons-logging : 1.2
* spring-core : 5.3.9
* spring-context : 5.3.9
* spring-tx : 5.3.9  


Following jars are located under OIM installation folder
* oimclient.jar
* OIMServer.jar
* idmdf-common.jar
* wlthint3client.jar
* jrf-api.jar
* event-recording-client.jar
* eclipselink.jar

## Configuration
To call OIM API's from client, IP adress, username(xelsysadm), password and authwl.conf path must be set in 
`ExternalOimClient.class.setOimClient()` method according to your OIM environment.

      serverURL = "<ip_adress>:14000";
      username = "xelsysadm";
      password = "<password>".toCharArray();

`String authwlPath = "C:\\design_console\\config\\authwl.conf";`



## Usage
To call OIM API's from client, OIMClient instance must be created before API's calls.  

    ExternalOimClient externalOimClient = new ExternalOimClient();
    OIMClient oimClient = externalOimClient.setOimClient(ExternalOimClient.Environment.DEV);