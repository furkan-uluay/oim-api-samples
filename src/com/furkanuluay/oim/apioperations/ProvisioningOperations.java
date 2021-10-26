package com.furkanuluay.oim.apioperations;

import oracle.iam.platform.Platform;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.provisioning.api.ProvisioningConstants;
import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.exception.*;
import oracle.iam.provisioning.vo.Account;
import oracle.iam.provisioning.vo.ApplicationInstance;
import oracle.iam.provisioning.vo.Entitlement;
import oracle.iam.provisioning.vo.EntitlementInstance;
import com.furkanuluay.oim.ExternalOimClient;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Furkan Uluay
 */

public class ProvisioningOperations {

  ProvisioningService provisioningService = null;

  /**
   * @throws Exception
   */
  public ProvisioningOperations() throws Exception {

    if (ExternalOimClient.oimClient != null) {
      provisioningService = ExternalOimClient.oimClient.getService(ProvisioningService.class);
    } else {
      provisioningService = Platform.getService(ProvisioningService.class);
    }
  }

  /**
   * @param usrKey
   * @param account
   * @return
   * @throws UserNotFoundException
   * @throws ApplicationInstanceNotFoundException
   * @throws GenericProvisioningException
   */
  public long provisionAccount(String usrKey, Account account) throws UserNotFoundException, ApplicationInstanceNotFoundException, GenericProvisioningException {
    return provisioningService.provision(usrKey, account);
  }

  /**
   * @param accountKey
   * @throws AccountNotFoundException
   * @throws GenericProvisioningException
   * @throws ImproperAccountStateException
   */
  public void enableAccount(long accountKey) throws AccountNotFoundException, GenericProvisioningException, ImproperAccountStateException {
    provisioningService.enable(accountKey);
  }

  /**
   * @param accountKey
   * @throws AccountNotFoundException
   * @throws GenericProvisioningException
   * @throws ImproperAccountStateException
   */
  public void disableAccount(long accountKey) throws AccountNotFoundException, GenericProvisioningException, ImproperAccountStateException {
    provisioningService.disable(accountKey);
  }


  /**
   * @param account
   * @throws AccountNotFoundException
   * @throws GenericProvisioningException
   */
  public void modifyAccount(Account account) throws AccountNotFoundException, GenericProvisioningException {
    provisioningService.modify(account);
  }

  /**
   * @param accountKey
   * @throws AccountNotFoundException
   * @throws GenericProvisioningException
   */
  public void revokeAccount(long accountKey) throws AccountNotFoundException, GenericProvisioningException {
    provisioningService.revoke(accountKey);
  }


  /**
   * @param entitlementInstanceList
   * @throws BulkProvisioningException
   */
  public void grantEntitlementToAccount(EntitlementInstance entitlementInstanceList) throws EntitlementNotFoundException, AccountNotFoundException, ImproperAccountStateException, EntitlementAlreadyProvisionedException, GenericProvisioningException {
    provisioningService.grantEntitlement(entitlementInstanceList);
  }

  /**
   * @param entitlementInstance
   * @throws BulkProvisioningException
   * @throws GenericProvisioningException
   * @throws AccountNotFoundException
   * @throws EntitlementNotProvisionedException
   */
  public void revokeEntitlementFromAccount(EntitlementInstance entitlementInstance) throws BulkProvisioningException,
          GenericProvisioningException, AccountNotFoundException, EntitlementNotProvisionedException {
    provisioningService.revokeEntitlement(entitlementInstance);
  }

  /**
   * @param entitlementInstance
   * @throws EntitlementNotFoundException
   * @throws AccountNotFoundException
   * @throws GenericProvisioningException
   */
  public void updateEntitlementToAccount(EntitlementInstance entitlementInstance) throws EntitlementNotFoundException, AccountNotFoundException, GenericProvisioningException {
    provisioningService.updateEntitlement(entitlementInstance);
  }

  /**
   * @param usrKey
   * @return
   * @throws UserNotFoundException
   * @throws GenericProvisioningException
   */
  public List<Account> getAccountsProvisionedToUser(String usrKey) throws UserNotFoundException, GenericProvisioningException {
    return provisioningService.getAccountsProvisionedToUser(usrKey, true);
  }


  /**
   * @param usrKey
   * @param applicationInstanceName
   * @return
   * @throws UserNotFoundException
   * @throws GenericProvisioningException
   */
  public List<Account> getAccountsProvisionedToUser(String usrKey, String applicationInstanceName, boolean populateAccountData)
          throws UserNotFoundException, GenericProvisioningException {

    //SearchCriteria -> Provisioned or Enabled
    SearchCriteria statusCriteriaProvisioned =
            new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.ACCOUNT_STATUS.getId(),
                    "Provisioned", SearchCriteria.Operator.EQUAL);
    SearchCriteria statusCriteriaEnabled =
            new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.ACCOUNT_STATUS.getId(),
                    "Enabled", SearchCriteria.Operator.EQUAL);
    SearchCriteria statusCriteria =
            new SearchCriteria(statusCriteriaProvisioned, statusCriteriaEnabled, SearchCriteria.Operator.OR);

    //SearchCriteria -> Application Instance Name
    SearchCriteria appInstCriteria = new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.DISPLAY_NAME.getId(),
            applicationInstanceName, SearchCriteria.Operator.EQUAL);

    SearchCriteria finalCriteria =
            new SearchCriteria(statusCriteria, appInstCriteria, SearchCriteria.Operator.AND);

    HashMap<String, Object> acctConfigParams = new HashMap();

    return provisioningService.getAccountsProvisionedToUser(usrKey, finalCriteria, acctConfigParams,
            populateAccountData);
  }


  /**
   * @param usrKey
   * @param resObjName
   * @param accountType
   * @return
   * @throws UserNotFoundException
   * @throws GenericProvisioningException
   */
  public List<Account> getUserAccountByResObjNameAndType(String usrKey, String resObjName, String accountType) throws UserNotFoundException, GenericProvisioningException {

    SearchCriteria resourceObjectCriteria = new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.OBJ_NAME.getId(),
            resObjName, SearchCriteria.Operator.EQUAL);


    SearchCriteria accountTypeCriteria = new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.ACCOUNT_TYPE.getId(),
            accountType, SearchCriteria.Operator.EQUAL);
    SearchCriteria criteriaFinal = new SearchCriteria(resourceObjectCriteria,
            accountTypeCriteria, SearchCriteria.Operator.EQUAL);

    return provisioningService.getAccountsProvisionedToUser(usrKey, criteriaFinal, null, false);
  }

  /**
   * @param usrKey
   * @param accountStatus
   * @return
   * @throws UserNotFoundException
   * @throws GenericProvisioningException
   */
  public List<Account> getUserAccountByStatus(String usrKey, String accountStatus) throws UserNotFoundException,
          GenericProvisioningException {

    SearchCriteria criteria = new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.ACCOUNT_STATUS.getId(),
            accountStatus, SearchCriteria.Operator.EQUAL);

    return provisioningService.getAccountsProvisionedToUser(usrKey, criteria, null, true);
  }

  /**
   * @param usrKey
   * @param accountType
   * @return
   * @throws UserNotFoundException
   * @throws GenericProvisioningException
   */
  public List<Account> getUserAccountByType(String usrKey, String accountType) throws UserNotFoundException,
          GenericProvisioningException {

    SearchCriteria criteria = new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.ACCOUNT_TYPE.getId(),
            accountType, SearchCriteria.Operator.EQUAL);

    return provisioningService.getAccountsProvisionedToUser(usrKey, criteria, null, true);
  }

  /**
   * @param usrKey
   * @param processInstanceKey
   * @return
   * @throws UserNotFoundException
   * @throws GenericProvisioningException
   */
  public Account getUserResourceAccountWithPInsKey(String usrKey, long processInstanceKey, boolean populateAccountData) throws UserNotFoundException, GenericProvisioningException, AccountNotFoundException {


    List<Account> accountList = provisioningService.getAccountsProvisionedToUser(usrKey, populateAccountData).
            stream()
            .filter(account -> account
                    .getProcessInstanceKey()
                    .equalsIgnoreCase(String.valueOf(processInstanceKey)))
            .collect(Collectors.toList());
    if (accountList.size() == 0) {
      throw new AccountNotFoundException(null,
              "Account Not Found: [usrKey: " + usrKey + ", processInstanceKey: " + processInstanceKey, null);
    } else {
      return accountList.get(0);
    }

  }

  /**
   * @param usrKey
   * @return
   * @throws UserNotFoundException
   * @throws GenericProvisioningException
   */
  public List<EntitlementInstance> getEntitlementsForUser(String usrKey) throws UserNotFoundException, GenericProvisioningException {
    return provisioningService.getEntitlementsForUser(usrKey);
  }

  /**
   * @param entitlementInstanceKey
   * @return
   * @throws UserNotFoundException
   * @throws GenericProvisioningException
   * @throws EntitlementInstanceNotFoundException
   */
  public EntitlementInstance getEntitlementInstance(long entitlementInstanceKey) throws UserNotFoundException,
          GenericProvisioningException, EntitlementInstanceNotFoundException {
    return provisioningService.getEntitlementInstance(entitlementInstanceKey);
  }

  /**
   * @param accountID
   * @param entitlementKey
   * @return
   * @throws UserNotFoundException
   * @throws GenericProvisioningException
   * @throws EntitlementInstanceNotFoundException
   */
  public EntitlementInstance getEntitlementInstance(long accountID, long entitlementKey) throws UserNotFoundException,
          GenericProvisioningException, EntitlementInstanceNotFoundException {
    return provisioningService.getEntitlementInstance(accountID, entitlementKey);
  }

  /**
   * @param usrKey
   * @param entitlement
   * @return
   * @throws UserNotFoundException
   * @throws EntitlementNotFoundException
   * @throws GenericProvisioningException
   */
  public boolean isEntitlementProvisionedToUser(String usrKey, Entitlement entitlement) throws UserNotFoundException, EntitlementNotFoundException, GenericProvisioningException {
    return provisioningService.isEntitlementProvisionedToUser(usrKey, entitlement);
  }

  /**
   * @param usrKey
   * @param applicationInstance
   * @return
   * @throws UserNotFoundException
   * @throws ApplicationInstanceNotFoundException
   * @throws GenericProvisioningException
   */
  public boolean isApplicationInstanceProvisionedToUser(String usrKey, ApplicationInstance applicationInstance) throws UserNotFoundException, ApplicationInstanceNotFoundException, GenericProvisioningException {
    return provisioningService.isApplicationInstanceProvisionedToUser(usrKey, applicationInstance);
  }

  /**
   * @param accountID
   * @return
   * @throws AccountNotFoundException
   * @throws GenericProvisioningException
   */
  public Account getAccountByAccountId(long accountID) throws AccountNotFoundException, GenericProvisioningException {
    return provisioningService.getAccountDetails(accountID);
  }


  /**
   * @param userId
   * @param appInstanceKey
   * @param populateAccountData
   * @return
   * @throws AccountNotFoundException
   * @throws GenericProvisioningException
   * @throws UserNotFoundException
   * @throws ApplicationInstanceNotFoundException
   */
  public List<Account> getUserAccountDetailsInApplicationInstance(String userId, long appInstanceKey, boolean populateAccountData) throws GenericProvisioningException, UserNotFoundException, ApplicationInstanceNotFoundException {
    return provisioningService.getUserAccountDetailsInApplicationInstance(userId, appInstanceKey, populateAccountData);
  }


}
