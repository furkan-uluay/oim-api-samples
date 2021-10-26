package com.furkanuluay.oim.apioperations;

import oracle.iam.platform.Platform;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.provisioning.api.EntitlementService;
import oracle.iam.provisioning.api.ProvisioningConstants;
import oracle.iam.provisioning.exception.*;
import oracle.iam.provisioning.vo.Entitlement;
import com.furkanuluay.oim.ExternalOimClient;

import java.util.HashMap;
import java.util.List;


/**
 * @author Furkan Uluay
 */

public class EntitlementOperations {
  EntitlementService entitlementService = null;

  /**
   * @throws Exception
   */
  public EntitlementOperations() throws Exception {

    if (ExternalOimClient.oimClient != null) {
      entitlementService = ExternalOimClient.oimClient.getService(EntitlementService.class);
    } else {
      entitlementService = Platform.getService(EntitlementService.class);
    }
  }


  /**
   * @param entitlementCode
   * @return
   * @throws GenericEntitlementServiceException
   * @throws EntitlementNotFoundException
   */
  public Entitlement findEntitlement(String entitlementCode) throws GenericEntitlementServiceException, EntitlementNotFoundException {

    // Get specific Entitlement Definitions
    SearchCriteria criteria =
            new SearchCriteria(ProvisioningConstants.EntitlementSearchAttribute.ENTITLEMENT_CODE.getId(), entitlementCode, SearchCriteria.Operator.EQUAL);
    return findEntitlementWithSearchCriteria(criteria);
  }


  /**
   * @param entitlementCode
   * @return
   * @throws GenericEntitlementServiceException
   * @throws EntitlementNotFoundException
   */
  public Entitlement findEntitlementContains(String entitlementCode) throws GenericEntitlementServiceException,
          EntitlementNotFoundException {
    // Get specific Entitlement Definitions
    SearchCriteria criteria =
            new SearchCriteria(ProvisioningConstants.EntitlementSearchAttribute.ENTITLEMENT_CODE.getId(),
                    entitlementCode, SearchCriteria.Operator.CONTAINS);
    return findEntitlementWithSearchCriteria(criteria);
  }


  /**
   * @param criteria
   * @return
   * @throws GenericEntitlementServiceException
   * @throws EntitlementNotFoundException
   */
  private Entitlement findEntitlementWithSearchCriteria(SearchCriteria criteria) throws GenericEntitlementServiceException,
          EntitlementNotFoundException {

    // Get specific Entitlement Definitions
    HashMap<String, Object> entConfigParams = new HashMap<String, Object>();
    List<Entitlement> entitlements = entitlementService.findEntitlements(criteria, entConfigParams);

    if (entitlements.size() < 1) {
      throw new EntitlementNotFoundException("Entitlement Not Found", "Entitlement Not Found", null, null);
    } else if (entitlements.size() > 1) {
      throw new GenericEntitlementServiceException("Multiple Entitlement Found", "Multiple Entitlement Found", null, null);
    } else {
      return entitlements.get(0);
    }
  }


  /**
   * @param entitlementKey
   * @return
   * @throws GenericEntitlementServiceException
   * @throws EntitlementNotFoundException
   */
  public Entitlement findEntitlement(long entitlementKey) throws GenericEntitlementServiceException, EntitlementNotFoundException {
    return entitlementService.findEntitlement(entitlementKey);
  }


  /**
   * @param entitlement
   * @return
   * @throws LookupValueNotFoundException
   * @throws GenericEntitlementServiceException
   * @throws ObjectNotFoundException
   * @throws ITResourceNotFoundException
   * @throws FormNotFoundException
   * @throws FormFieldNotFoundException
   * @throws DuplicateEntitlementException
   */
  public Entitlement addEntitlement(Entitlement entitlement) throws LookupValueNotFoundException, GenericEntitlementServiceException, ObjectNotFoundException, ITResourceNotFoundException, FormNotFoundException, FormFieldNotFoundException, DuplicateEntitlementException {
    return entitlementService.addEntitlement(entitlement);
  }

  /**
   * @param entitlementList
   * @return
   * @throws BulkException
   */
  public List<Entitlement> addEntitlements(List<Entitlement> entitlementList) throws BulkException {
    return entitlementService.addEntitlements(entitlementList);
  }


  /**
   * @param entitlementKey
   * @return
   * @throws GenericEntitlementServiceException
   */
  public boolean deleteEntitlement(long entitlementKey) throws GenericEntitlementServiceException {
    return entitlementService.deleteEntitlement(entitlementKey);
  }

  /**
   * @param entitlementArray
   * @throws BulkException
   */
  public void deleteEntitlements(long[] entitlementArray) throws BulkException {
    entitlementService.deleteEntitlements(entitlementArray);
  }

  /**
   * @param entitlement
   * @return
   * @throws EntitlementNotFoundException
   * @throws GenericEntitlementServiceException
   */
  public Entitlement updateEntitlement(Entitlement entitlement) throws EntitlementNotFoundException, GenericEntitlementServiceException {
    return entitlementService.updateEntitlement(entitlement);

  }

  /**
   * @param entitlementList
   * @return
   * @throws BulkException
   */
  public List<Entitlement> updateEntitlements(List<Entitlement> entitlementList) throws BulkException {
    return entitlementService.updateEntitlements(entitlementList);

  }
}
