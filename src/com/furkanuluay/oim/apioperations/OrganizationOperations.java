package com.furkanuluay.oim.apioperations;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcOrganizationNotFoundException;
import oracle.iam.identity.exception.OrganizationManagerException;
import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;
import oracle.iam.identity.orgmgmt.vo.Organization;
import oracle.iam.platform.Platform;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import com.furkanuluay.oim.ExternalOimClient;

import java.util.List;

/**
 * @author Furkan Uluay
 */
public class OrganizationOperations {
  OrganizationManager orgManager = null;

  public OrganizationOperations() throws Exception {

    if (ExternalOimClient.oimClient != null) {
      orgManager = ExternalOimClient.oimClient.getService(OrganizationManager.class);
    } else {
      orgManager = Platform.getService(OrganizationManager.class);
    }
  }

  /**
   * @param organization
   * @return
   * @throws OrganizationManagerException
   */
  public String createOrganization(Organization organization) throws OrganizationManagerException {
    return orgManager.create(organization);
  }

  /**
   * @param orgName
   * @return Organization
   * @throws OrganizationManagerException
   */
  public Organization getOrganizationDetails(String orgName) throws OrganizationManagerException {
    return orgManager.getDetails(orgName, null, true);
  }

  /**
   * @param orgKey
   * @return Organization
   * @throws OrganizationManagerException
   */
  public Organization getOrganizationDetails(long orgKey) throws OrganizationManagerException {

    SearchCriteria sc = new SearchCriteria("act_key", orgKey, SearchCriteria.Operator.EQUAL);

    List<Organization> organizationList = orgManager.search(sc, null, null);
    return organizationList.get(0);
  }

  /**
   * @param organization
   * @return
   * @throws OrganizationManagerException
   */
  public String modify(Organization organization) throws OrganizationManagerException {
    return orgManager.modify(organization);
  }

  /**
   * @return
   * @throws OrganizationManagerException
   * @throws tcOrganizationNotFoundException
   * @throws tcColumnNotFoundException
   * @throws tcAPIException
   */
  public List<Organization> getAllOrganizations() throws OrganizationManagerException, tcOrganizationNotFoundException, tcColumnNotFoundException, tcAPIException {
    SearchCriteria sc1 = new SearchCriteria(OrganizationManagerConstants.AttributeName.ORG_STATUS.getId(), OrganizationManagerConstants.AttributeValue.ORG_STATUS_ACTIVE.getId(), SearchCriteria.Operator.EQUAL);
    //System dışındakiler alınıyor
    SearchCriteria sc2 = new SearchCriteria(OrganizationManagerConstants.AttributeName.ORG_TYPE.getId(), OrganizationManagerConstants.AttributeValue.ORG_TYPE_SYSTEM.getId(), SearchCriteria.Operator.NOT_EQUAL);
    SearchCriteria sc = new SearchCriteria(sc1, sc2, SearchCriteria.Operator.AND);
    return orgManager.search(sc, null, null);
  }

  public List<Organization> getChildOrganizations(Organization org) throws OrganizationManagerException, tcOrganizationNotFoundException, tcAPIException, tcColumnNotFoundException {
    return orgManager.getChildOrganizations(org.getEntityId(), null, null);
  }

}