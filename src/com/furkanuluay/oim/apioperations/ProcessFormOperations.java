package com.furkanuluay.oim.apioperations;

import Thor.API.Exceptions.*;
import Thor.API.Operations.tcFormInstanceOperationsIntf;
import Thor.API.tcResultSet;
import oracle.iam.platform.Platform;
import com.furkanuluay.oim.ExternalOimClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Furkan Uluay
 */
public class ProcessFormOperations implements AutoCloseable {

  tcFormInstanceOperationsIntf tcFormInstanceOperationsIntf = null;

  /**
   * @throws Exception
   */
  public ProcessFormOperations() throws Exception {

    if (ExternalOimClient.oimClient != null) {
      tcFormInstanceOperationsIntf = ExternalOimClient.oimClient.getService(tcFormInstanceOperationsIntf.class);
    } else {
      tcFormInstanceOperationsIntf = Platform.getService(tcFormInstanceOperationsIntf.class);
    }
  }


  public Map<String, Object> getProcessFormData(long processInstanceKey) throws tcAPIException, tcProcessNotFoundException, tcNotAtomicProcessException, tcFormNotFoundException {
    tcResultSet processForm = tcFormInstanceOperationsIntf.getProcessFormData(processInstanceKey);

    Map<String, Object> formData = new HashMap<>();

    int count = processForm.getRowCount();
    for (int i = 0; i < count; i++) {
      processForm.goToRow(i);

      String columnNames[] = processForm.getColumnNames();
      for (String column : columnNames) {
        try {
          formData.put(column, processForm.getStringValue(column));
        } catch (tcAPIException | tcColumnNotFoundException e) {
          e.printStackTrace();
        }
      }
    }

    return formData;
  }


  /**
   * @param processInstanceKey
   * @return
   * @throws tcAPIException
   * @throws tcProcessNotFoundException
   * @throws tcNotAtomicProcessException
   * @throws tcFormNotFoundException
   */
  public tcResultSet getProcessFormByProcessInsKey(long processInstanceKey) throws tcAPIException, tcProcessNotFoundException, tcNotAtomicProcessException, tcFormNotFoundException {
    return tcFormInstanceOperationsIntf.getProcessFormData(processInstanceKey);
  }

  /**
   * @param processInstanceKey
   * @param childFormDefinitionKey
   * @return
   * @throws tcAPIException
   * @throws tcFormNotFoundException
   * @throws tcProcessNotFoundException
   */
  public tcResultSet getChildFormByProcessInsKey(long childFormDefinitionKey, long processInstanceKey) throws tcAPIException, tcFormNotFoundException, tcProcessNotFoundException {
    return tcFormInstanceOperationsIntf.getProcessFormChildData(childFormDefinitionKey, processInstanceKey);
  }

  /**
   * @param childFormDefinitionKey
   * @param processInstanceKey
   * @param attributeListMap
   * @return
   * @throws tcFormNotFoundException
   * @throws tcInvalidValueException
   * @throws tcProcessNotFoundException
   * @throws tcRequiredDataMissingException
   * @throws tcAPIException
   * @throws tcNotAtomicProcessException
   */
  public long addChildFormData(long childFormDefinitionKey, long processInstanceKey,
                               Map<Object, Object> attributeListMap) throws tcFormNotFoundException,
          tcInvalidValueException, tcProcessNotFoundException, tcRequiredDataMissingException, tcAPIException, tcNotAtomicProcessException {
    return tcFormInstanceOperationsIntf.addProcessFormChildData(childFormDefinitionKey, processInstanceKey, attributeListMap);
  }

  /**
   * @param childFormDefinitionKey
   * @param processInstanceKey
   * @throws tcFormEntryNotFoundException
   * @throws tcFormNotFoundException
   * @throws tcAPIException
   */
  public void removeChildFormData(long childFormDefinitionKey, long processInstanceKey) throws tcFormEntryNotFoundException, tcFormNotFoundException, tcAPIException {
    tcFormInstanceOperationsIntf.removeProcessFormChildData(childFormDefinitionKey, processInstanceKey);
  }


  /**
   * @param processInstanceKey
   * @param attributeListMap
   * @throws tcAPIException
   * @throws tcInvalidValueException
   * @throws tcRequiredDataMissingException
   * @throws tcProcessNotFoundException
   * @throws tcFormNotFoundException
   * @throws tcNotAtomicProcessException
   */
  public void setProcessFormData(long processInstanceKey, Map<Object, Object> attributeListMap) throws tcAPIException, tcInvalidValueException, tcRequiredDataMissingException, tcProcessNotFoundException, tcFormNotFoundException, tcNotAtomicProcessException {
    tcFormInstanceOperationsIntf.setProcessFormData(processInstanceKey, attributeListMap);
  }

  /**
   * @param childFormDefinitionKey
   * @param childFormPrimaryKey
   * @param attributeListMap
   * @throws tcFormEntryNotFoundException
   * @throws tcAPIException
   * @throws tcInvalidValueException
   * @throws tcFormNotFoundException
   */
  public void updateChildFormData(long childFormDefinitionKey, long childFormPrimaryKey, Map<Object, Object> attributeListMap) throws tcFormEntryNotFoundException, tcAPIException, tcInvalidValueException, tcFormNotFoundException {
    tcFormInstanceOperationsIntf.updateProcessFormChildData(childFormDefinitionKey, childFormPrimaryKey,
            attributeListMap);
  }

  /**
   * @param processInstanceKey
   * @return
   * @throws tcAPIException
   * @throws tcFormNotFoundException
   * @throws tcProcessNotFoundException
   */
  public long getFormDefinitionKey(long processInstanceKey) throws tcAPIException, tcFormNotFoundException, tcProcessNotFoundException {
    return tcFormInstanceOperationsIntf.getProcessFormDefinitionKey(processInstanceKey);
  }


  //this method used because of entitlement instance update process throwing error for second time it is called
  public void updateChildTable(String childFormName, String entFormFieldName, String entitlementName,
                               HashMap<String, String> attrChildData,
                               String pik) throws tcAPIException, tcFormNotFoundException, tcVersionNotFoundException, tcNotAtomicProcessException, tcProcessNotFoundException, tcVersionNotDefinedException, tcColumnNotFoundException, tcInvalidValueException, tcFormEntryNotFoundException {

    long processInstanceKey = Long.valueOf(pik);
    long formKey = tcFormInstanceOperationsIntf.getProcessFormDefinitionKey(processInstanceKey);
    int version = tcFormInstanceOperationsIntf.getProcessFormVersion(processInstanceKey);

    tcResultSet childFormDef = tcFormInstanceOperationsIntf.getChildFormDefinition(formKey, version);
    long childKey = 0;
    for (int i = 0; i <= childFormDef.getRowCount() - 1; i++) {
      childFormDef.goToRow(i);
      if (childFormName.equals(childFormDef.getStringValue("Structure Utility.Table Name"))) {
        childKey = childFormDef.getLongValue("Structure Utility.Child Tables.Child Key");
      }
    }
    tcResultSet chilFormData = tcFormInstanceOperationsIntf.getProcessFormChildData(childKey, processInstanceKey);
    long childFormPrimaryKey = 0;

    for (int i = 0; i <= chilFormData.getRowCount() - 1; i++) {
      String row = "";
      chilFormData.goToRow(i);

      if (chilFormData.getStringValue(entFormFieldName).equals(entitlementName)) {
        childFormPrimaryKey = chilFormData.getLongValue(childFormName + "_KEY");
      }
    }

    tcFormInstanceOperationsIntf.updateProcessFormChildData(childKey, childFormPrimaryKey, attrChildData);

  }


  @Override
  public void close() throws Exception {
    if (tcFormInstanceOperationsIntf != null) {
      tcFormInstanceOperationsIntf.close();
    }
  }
}
