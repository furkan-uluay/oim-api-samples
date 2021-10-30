package com.furkanuluay.oim;


import oracle.iam.platform.OIMClient;

import java.util.Hashtable;

public class ExternalOimClient {


  public static OIMClient oimClient;


  public enum Environment {
    DEV, PROD
  }


  public OIMClient setOimClient(Environment environment) throws Exception {
    System.out.println("Creating client....");
    System.out.println("Environment: " + environment);
    String ctxFactory;
    String serverURL;
    String username;
    char[] password;
    if (environment.toString().equalsIgnoreCase("DEV")) {

      ctxFactory = "weblogic.jndi.WLInitialContextFactory";
      serverURL = "<ip_adress>:14000";
      username = "xelsysadm";
      password = "<password>".toCharArray();
    } else if (environment.toString().equalsIgnoreCase("PROD")) {

      ctxFactory = "weblogic.jndi.WLInitialContextFactory";
      serverURL = "<ip_adress>:14000";
      username = "xelsysadm";
      password = "<password>".toCharArray();
    } else {
      throw new Exception("Environment Error");
    }
    System.out.println("OimClient from: " +  environment + " -- URL:" + serverURL);
    String authwlPath = "C:\\design_console\\config\\authwl.conf";
    Hashtable<String, String> env = new Hashtable<String, String>();
    env.put(OIMClient.JAVA_NAMING_FACTORY_INITIAL, ctxFactory);
    env.put(OIMClient.JAVA_NAMING_PROVIDER_URL, serverURL);
    env.put("weblogic.MaxMessageSize", "300000000");
    System.setProperty("weblogic.MaxMessageSize", "300000000");

    System.setProperty("java.security.auth.login.config", authwlPath);
    System.setProperty("APPSERVER_TYPE", "wls");

    this.oimClient = new OIMClient(env);

    System.out.println("Logging in");
    this.oimClient.login(username, password);
    System.out.println("Log in successful");

    return oimClient;
  }
}
