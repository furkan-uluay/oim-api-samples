package com.furkanuluay.oim.apioperations;

import oracle.iam.accesspolicy.api.AccessPolicyService;
import oracle.iam.accesspolicy.exception.AccessPolicyEvaluationException;
import oracle.iam.accesspolicy.exception.AccessPolicyEvaluationUnauthorizedException;
import oracle.iam.accesspolicy.exception.AccessPolicyServiceException;
import oracle.iam.accesspolicy.vo.AccessPolicy;
import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.platform.Platform;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import com.furkanuluay.oim.ExternalOimClient;

import java.util.HashMap;
import java.util.List;

/**
 * @author Furkan Uluay
 */
public class AccessPolicyOperations {

  AccessPolicyService accessPolicyService;

  public AccessPolicyOperations() throws Exception {

    if (ExternalOimClient.oimClient != null) {
      accessPolicyService = ExternalOimClient.oimClient.getService(AccessPolicyService.class);
    } else {
      accessPolicyService = Platform.getService(AccessPolicyService.class);
    }

  }

  /**
   * @param accessPolicy
   * @throws AccessPolicyServiceException
   */
  public void updateAccessPolicy(AccessPolicy accessPolicy) throws AccessPolicyServiceException {

    accessPolicyService.updateAccessPolicy(accessPolicy);
  }


  /**
   * @param accessPolicyName
   * @return
   * @throws AccessPolicyServiceException
   */
  public AccessPolicy getAccessPolicy(String accessPolicyName) throws AccessPolicyServiceException {

    SearchCriteria searchCriteriaPolicyName = new SearchCriteria(AccessPolicy.ATTRIBUTE.NAME.getID(), accessPolicyName
            , SearchCriteria.Operator.EQUAL);
    List<AccessPolicy> accessPolicies = accessPolicyService.findAccessPolicies(searchCriteriaPolicyName, new HashMap<>());
    if (accessPolicies.size() > 0) {
      return accessPolicyService.getAccessPolicy(accessPolicies.get(0).getEntityId(), true);
    } else {
      throw new AccessPolicyServiceException("Access Policy Not Found. " + accessPolicyName);
    }
  }

  /**
   * @param accessPolicyKey
   * @return
   * @throws AccessPolicyServiceException
   */
  public AccessPolicy getAccessPolicy(long accessPolicyKey) throws AccessPolicyServiceException {
    return accessPolicyService.getAccessPolicy(String.valueOf(accessPolicyKey), true);
  }


  /**
   * @return
   * @throws AccessPolicyServiceException
   */
  public long getLowestPriority() throws AccessPolicyServiceException {
    return accessPolicyService.getLowestPriority();
  }

  /**
   * @param accessPolicy
   * @return
   * @throws AccessPolicyServiceException
   */
  public String createAccessPolicy(AccessPolicy accessPolicy) throws AccessPolicyServiceException {

    return accessPolicyService.createAccessPolicy(accessPolicy);
  }

  /**
   * @param usrKey
   * @throws AccessPolicyEvaluationUnauthorizedException
   * @throws NoSuchUserException
   * @throws AccessPolicyServiceException
   * @throws AccessPolicyEvaluationException
   */
  public void evalutePoliciesForUser(String usrKey) throws AccessPolicyEvaluationUnauthorizedException, NoSuchUserException, AccessPolicyServiceException, AccessPolicyEvaluationException {
    accessPolicyService.evalutePoliciesForUser(usrKey);
  }

}
