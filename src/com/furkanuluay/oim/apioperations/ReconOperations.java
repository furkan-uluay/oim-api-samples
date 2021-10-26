package com.furkanuluay.oim.apioperations;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcAttributeNotFoundException;
import Thor.API.Exceptions.tcEventDataReceivedException;
import Thor.API.Exceptions.tcEventNotFoundException;
import oracle.iam.platform.Platform;
import oracle.iam.reconciliation.api.EventAttributes;
import oracle.iam.reconciliation.api.ReconOperationsService;
import com.furkanuluay.oim.ExternalOimClient;

import java.util.Map;

/**
 * @author Furkan Uluay
 */
public class ReconOperations {

  ReconOperationsService reconOperationsService = null;

  /**
   *
   */
  public ReconOperations() {

    if (ExternalOimClient.oimClient != null) {
      reconOperationsService = ExternalOimClient.oimClient.getService(ReconOperationsService.class);
    } else {
      reconOperationsService = Platform.getService(ReconOperationsService.class);
    }
  }

  /**
   * @param objectName
   * @param inputData
   * @param eventAttribs
   * @return
   */
  public long createReconciliationEvent(String objectName, Map<String, Object> inputData, EventAttributes eventAttribs) {
    return reconOperationsService.createReconciliationEvent(objectName, inputData, eventAttribs);
  }


  /**
   * @param reconciliationEventKey
   * @param childFieldName
   * @param multiAttributeDataFlag
   * @throws tcAPIException
   */
  public void providingAllMultiAttributeData(long reconciliationEventKey, java.lang.String childFieldName, boolean multiAttributeDataFlag) throws tcAPIException {
    reconOperationsService.providingAllMultiAttributeData(reconciliationEventKey, childFieldName, multiAttributeDataFlag);
  }

  /**
   * @param reconciliationEventKey
   * @param childFieldName
   * @param inputData
   * @return The key of the parent table which has been retrieved based on the reconciliationEventKey
   * @throws tcAPIException
   * @throws tcEventNotFoundException
   * @throws tcAttributeNotFoundException
   * @throws tcEventDataReceivedException
   */
  public long addMultiAttributeData(long reconciliationEventKey, String childFieldName, Map<String, Object> inputData) throws tcAPIException, tcEventNotFoundException, tcAttributeNotFoundException, tcEventDataReceivedException {
    return reconOperationsService.addMultiAttributeData(reconciliationEventKey, childFieldName, inputData);

  }

  /**
   * @param reconciliationEventKey
   * @throws tcEventNotFoundException
   * @throws tcAPIException
   * @throws tcEventDataReceivedException
   */
  public void finishReconciliationEvent(long reconciliationEventKey) throws tcEventNotFoundException, tcAPIException, tcEventDataReceivedException {
    reconOperationsService.finishReconciliationEvent(reconciliationEventKey);
  }

  /**
   * @param reconciliationEventKey
   * @throws tcAPIException
   */
  public void processReconciliationEvent(long reconciliationEventKey) throws tcAPIException {
    reconOperationsService.processReconciliationEvent(reconciliationEventKey);

  }

  /**
   * @param reconciliationEventKey
   * @throws tcAPIException
   */
  public void closeReconciliationEvent(long reconciliationEventKey) throws tcAPIException {
    reconOperationsService.closeReconciliationEvent(reconciliationEventKey);
  }

}
