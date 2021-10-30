package com.furkanuluay.oim;

import com.furkanuluay.oim.apioperations.AccessPolicyOperations;
import com.furkanuluay.oim.apioperations.EntitlementOperations;
import oracle.iam.accesspolicy.exception.AccessPolicyServiceException;
import oracle.iam.accesspolicy.vo.AccessPolicy;
import oracle.iam.accesspolicy.vo.AccessPolicyElement;
import oracle.iam.accesspolicy.vo.DefaultData;
import oracle.iam.platform.OIMClient;
import oracle.iam.provisioning.vo.ApplicationInstance;
import oracle.iam.provisioning.vo.Entitlement;
import oracle.iam.provisioning.vo.FormField;
import oracle.iam.provisioning.vo.FormInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccessPolicyOperationsSample {

static AccessPolicyOperations accessPolicyOperations = null;

  public static void main(String[] args) throws Exception {
    ExternalOimClient externalOimClient = new ExternalOimClient();
    OIMClient oimClient = externalOimClient.setOimClient(ExternalOimClient.Environment.DEV);

    accessPolicyOperations= new AccessPolicyOperations(oimClient);

    AccessPolicy policy1 = accessPolicyOperations.getAccessPolicy("Policy1");
    Entitlement entitlement = new EntitlementOperations().findEntitlement("5~Entitlement");


    AccessPolicyOperationsSample accessPolicyOperationsSample= new AccessPolicyOperationsSample();
    accessPolicyOperationsSample.assignEntitlementToAccessPolicy(policy1,entitlement);


  }


  private void assignEntitlementToAccessPolicy(AccessPolicy accessPolicy, Entitlement entitlement ) throws Exception {

    String accessPolicyName = accessPolicy.getName();


    ApplicationInstance applicationInstance = entitlement.getAppInstance();

    FormField entitlementFieldName = findEntitlementFieldName(entitlement.getChildForm());


    if (!isAccessPolicyHasApplicationInstance(accessPolicy, applicationInstance)) {
      accessPolicy = addApplicationInstanceToAccessPolicy(accessPolicy, applicationInstance);
    }


    List<AccessPolicyElement> policyElements = accessPolicy.getPolicyElements();


    DefaultData defaultData = policyElements.stream()
            .filter(accessPolicyElement -> accessPolicyElement.getApplicationInstanceID() == applicationInstance.getApplicationInstanceKey())
            .findFirst()
            .orElseThrow(() -> new AccessPolicyServiceException("Access Policy does not have Application Instance. " +
                    "Access Policy: " + accessPolicyName + ", Application Instance: " + applicationInstance.getApplicationInstanceName()))
            .getDefaultData();

    if (defaultData.getChildAttributes()
            .stream()
            .anyMatch(childAttribute -> childAttribute.getAttributeValue().equalsIgnoreCase(entitlement.getEntitlementCode()))) {
      throw new AccessPolicyServiceException("Access Policy already has the Entitlement. " +
              "Access Policy : " + accessPolicyName + ", Entitlement : " + entitlement.getEntitlementCode());
    } else {

      //Add entitlemen to DefaultData object
      HashMap<String, String> childData = new HashMap<>();
      childData.put(entitlementFieldName.getName(), entitlement.getEntitlementCode());
      defaultData.addChildData(entitlement.getChildForm().getName(), childData);

      //Update Access Policy with new DefaultData
      accessPolicyOperations.updateAccessPolicy(accessPolicy);
      System.out.println("Entitlement assigned to Access Policy. Entitlement Code: "+entitlement.getEntitlementCode() +
              ", Access Policy: "+accessPolicyName);
    }


  }


  /**
   * @param childFormInfo
   * @return
   * @throws Exception
   */
  public FormField findEntitlementFieldName(FormInfo childFormInfo) throws Exception {

    List<FormField> childFormFields = childFormInfo.getFormFields();

    List<FormField> deletedList = new ArrayList<>();
    for (FormField formField : childFormFields) {
      boolean isFound = false;
      if (formField.getProperties().size() == 0) {
        deletedList.add(formField);
      } else {
        for (Map.Entry<String, Object> entry : formField.getProperties().entrySet()) {
          if (entry.getKey().equalsIgnoreCase("Entitlement")) {
            isFound = true;
          }
        }
        if (!isFound) {
          deletedList.add(formField);
        }
      }
    }
    childFormFields.removeAll(deletedList);
    if (childFormFields.size() == 0) {
      throw new Exception("No Match for entitlement Field");
    } else {
      return childFormFields.get(0);
    }
  }


  private AccessPolicy addApplicationInstanceToAccessPolicy(AccessPolicy accessPolicy,
                                                            ApplicationInstance applicationInstance) throws AccessPolicyServiceException {
    AccessPolicyElement e1 = new AccessPolicyElement(applicationInstance.getApplicationInstanceKey(), false,
            AccessPolicyElement.ACTION_IF_NOT_APPLICABLE.DISABLE);
    List<AccessPolicyElement> elements = new ArrayList<>();
    elements.add(e1);

    accessPolicy.setPolicyElements(elements);

    accessPolicyOperations.updateAccessPolicy(accessPolicy);

    return accessPolicyOperations.getAccessPolicy(Long.parseLong(accessPolicy.getEntityId()));

  }


  /**
   * @param accessPolicy
   * @param applicationInstance
   * @return
   */
  private boolean isAccessPolicyHasApplicationInstance(AccessPolicy accessPolicy, ApplicationInstance applicationInstance) {

    List<AccessPolicyElement> policyElements = accessPolicy.getPolicyElements();
    if (policyElements != null) {
      for (AccessPolicyElement policyElement : policyElements) {
        if (policyElement.getApplicationInstanceID() == applicationInstance.getApplicationInstanceKey()) {
          return true;
        }
      }
    }
    return false;
  }

}
