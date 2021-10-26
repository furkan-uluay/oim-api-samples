package com.furkanuluay.oim;


import com.furkanuluay.oim.apioperations.OrganizationOperations;
import com.furkanuluay.oim.apioperations.UserOperations;
import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;
import oracle.iam.identity.orgmgmt.vo.Organization;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.identity.usermgmt.vo.UserManagerResult;

import java.util.HashMap;

public class Samples {
  public static void main(String[] args) throws Exception {
    ExternalOimClient externalOimClient = new ExternalOimClient();
    externalOimClient.getOimClient(ExternalOimClient.Environment.DEV);

    Samples samples = new Samples();

    samples.createUser();;

    User user = samples.getUSer("TestUser");

  }

  // USER Operations Samples
  public void createUser() throws Exception {
    HashMap<String, Object> attributes = new HashMap<>();
    attributes.put("User Login", "furkan.uluay1");
    attributes.put("First Name", "Furkan");
    attributes.put("Last Name", "Uluay");
    attributes.put("act_key", Long.parseLong("1"));
    attributes.put(oracle.iam.identity.utils.Constants.PASSWORD, "Welcome1");
    attributes.put("Xellerate Type", "End-User");
    attributes.put("Role", "Full-Time");
    UserOperations userOps = new UserOperations();
    UserManagerResult result = userOps.createUser(attributes);
    System.out.println(result.getSucceededResults());
  }


  public User getUSer(String usrLogin) throws Exception {
    UserOperations userOperations = new UserOperations();
    return userOperations.searchUser(usrLogin, true);
  }




  // ORGANIZATION Operations Samples
  public void createOrganization() throws Exception {
    Organization org = new Organization();
    org.setAttribute(OrganizationManagerConstants.AttributeName.ORG_NAME.getId(), "TestOrg1");
    org.setAttribute(OrganizationManagerConstants.AttributeName.ORG_PARENT_KEY.getId(), Long.parseLong("4"));
    org.setAttribute(OrganizationManagerConstants.AttributeName.ORG_TYPE.getId(), "Company");

    OrganizationOperations orgOps = new OrganizationOperations();
    String organization = orgOps.createOrganization(org);
    System.out.println("organization created: "+ organization);

  }

  public void getOrgDetails(String orgName) throws Exception {
    OrganizationOperations orgOps = new OrganizationOperations();
    Organization org = orgOps.getOrganizationDetails(orgName);

    System.out.println("Organization ID :: " + org.getEntityId());
    System.out.println("Organization Name :: " + org.getAttribute(OrganizationManagerConstants.AttributeName.ORG_NAME.getId()));
    System.out.println("Parent Organization Name :: " + org.getAttribute(OrganizationManagerConstants.AttributeName.ORG_PARENT_NAME.getId()));
    System.out.println("Organization Password Policy Name :: " + org.getAttribute(OrganizationManagerConstants.AttributeName.ORG_PASSWORD_POLICY_NAME.getId()));

    System.out.println(org.getAttributeNames());
  }








  //Provisioning Operations Sample


}
