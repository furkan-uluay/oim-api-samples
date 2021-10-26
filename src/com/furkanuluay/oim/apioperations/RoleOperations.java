package com.furkanuluay.oim.apioperations;

import oracle.iam.identity.exception.*;
import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;
import oracle.iam.identity.rolemgmt.vo.Role;
import oracle.iam.identity.rolemgmt.vo.RoleManagerResult;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.identity.vo.Relationship;
import oracle.iam.platform.Platform;
import com.furkanuluay.oim.ExternalOimClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Furkan Uluay
 */
public class RoleOperations {
  private RoleManager roleManager = null;

  /**
   * If Class being called on local environment use this constructor with OIMClient
   */

  public RoleOperations() throws Exception {

    if (ExternalOimClient.oimClient != null) {
      roleManager = ExternalOimClient.oimClient.getService(RoleManager.class);
    } else {
      roleManager = Platform.getService(RoleManager.class);
    }
  }


  /**
   * @param role
   * @return
   * @throws RoleAlreadyExistsException
   * @throws ValidationFailedException
   * @throws RoleCreateException
   */
/*
      Example for creating Role object:
      HashMap<String, Object> createAttributes = new HashMap<String, Object>();
      createAttributes.put(RoleManagerConstants.ROLE_NAME, roleName);
      createAttributes.put(RoleManagerConstants.ROLE_DISPLAY_NAME, roleName);
      createAttributes.put(RoleManagerConstants.ROLE_DESCRIPTION, roleDescription);
      Role role = new Role(createAttributes);
 */
  public RoleManagerResult createRole(Role role) throws RoleAlreadyExistsException, ValidationFailedException, RoleCreateException {
    return roleManager.create(role);
  }


  /**
   * @param roleName
   * @return
   * @throws ValidationFailedException
   * @throws RoleDeleteException
   * @throws RoleLookupException
   * @throws SearchKeyNotUniqueException
   * @throws NoSuchRoleException
   */
  public RoleManagerResult deleteRole(String roleName) throws ValidationFailedException, RoleDeleteException, RoleLookupException, SearchKeyNotUniqueException, NoSuchRoleException {
    return roleManager.delete(RoleManagerConstants.ROLE_NAME, roleName);
  }

  /**
   * @param roleName
   * @return
   * @throws SearchKeyNotUniqueException
   * @throws RoleLookupException
   * @throws NoSuchRoleException
   */
  public Role getRole(String roleName) throws SearchKeyNotUniqueException, RoleLookupException, NoSuchRoleException {

    HashSet retAttrs = new HashSet();
    retAttrs.add(RoleManagerConstants.ACCESS_POLICIES);
    retAttrs.add(RoleManagerConstants.ROLE_NAME);
    retAttrs.add(RoleManagerConstants.ROLE_DESCRIPTION);
    retAttrs.add(RoleManagerConstants.ROLE_DISPLAY_NAME);
    retAttrs.add(RoleManagerConstants.ROLE_UNIQUE_NAME);
    return roleManager.getDetails(RoleManagerConstants.ROLE_NAME, roleName, retAttrs);

  }

  /**
   * @param role
   * @return
   * @throws RoleModifyException
   * @throws ValidationFailedException
   * @throws NoSuchRoleException
   */
  public RoleManagerResult modifyRole(Role role) throws RoleModifyException, ValidationFailedException, NoSuchRoleException {
    return roleManager.modify(role);
  }

  /**
   * @param usrKey
   * @param roleKey
   * @return
   * @throws SearchKeyNotUniqueException
   * @throws RoleLookupException
   * @throws NoSuchRoleException
   * @throws ValidationFailedException
   * @throws RoleGrantException
   */
  public RoleManagerResult grantRoleToUserDirect(String usrKey, String roleKey) throws ValidationFailedException, RoleGrantException {
    List<String> userKeys = new ArrayList<>();
    userKeys.add(usrKey);
    return roleManager.grantRoleDirect(roleKey, userKeys, null, true);
  }


  /**
   * @param usrKey
   * @param roleKey
   * @return
   * @throws SearchKeyNotUniqueException
   * @throws RoleLookupException
   * @throws NoSuchRoleException
   * @throws ValidationFailedException
   * @throws RoleGrantException
   * @throws RoleGrantRevokeException
   * @throws NoSuchUserException
   */
  public RoleManagerResult revokeRoleFromUserDirect(String usrKey, String roleKey) throws ValidationFailedException, RoleGrantRevokeException {
    //Role role = getRole(roleKey);
    Set<String> userKeys = new HashSet<>();
    userKeys.add(usrKey);
    return roleManager.revokeRoleGrantDirect(roleKey, userKeys, true);
  }


  /**
   * @param roleName
   * @return
   * @throws SearchKeyNotUniqueException
   * @throws RoleLookupException
   * @throws NoSuchRoleException
   * @throws RoleMemberException
   */
  public List<User> getRoleMembersDirect(String roleName) throws SearchKeyNotUniqueException, RoleLookupException, NoSuchRoleException, RoleMemberException {
    Role role = getRole(roleName);

    //"false" parameters means return only direct members
    return roleManager.getRoleMembers(role.getEntityId(), false);

  }

  /**
   * @param usrKey
   * @return
   * @throws UserMembershipException
   */
  public List<Role> getUserMemberships(String usrKey) throws UserMembershipException {
    return roleManager.getUserMemberships(usrKey, true);
  }

  /**
   * @param roleKey
   * @param usrKey
   * @return if user is member of Role, returns RelationShip object, if not it returns null
   * @throws SearchKeyNotUniqueException
   * @throws RoleLookupException
   * @throws NoSuchRoleException
   * @throws NoSuchRoleGrantException
   * @throws RoleGrantLookupException
   */
  public Relationship getRoleGrantDetails(String roleKey, String usrKey) {
    try {
      return roleManager.getRoleGrantDetails(roleKey, usrKey, null);
    } catch (NoSuchRoleGrantException | RoleGrantLookupException e) {
      e.printStackTrace();
      return null;
    }

  }

}
