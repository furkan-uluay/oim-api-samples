package com.furkanuluay.oim.apioperations;

import com.furkanuluay.oim.ExternalOimClient;
import oracle.iam.identity.exception.*;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.identity.usermgmt.vo.UserManagerResult;
import oracle.iam.platform.Platform;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.provisioning.exception.UserNotFoundException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author Furkan Uluay
 */
public class UserOperations {

  UserManager userManager = null;

  /**
   * @throws Exception
   */
  public UserOperations() throws Exception {

    if (ExternalOimClient.oimClient != null) {
      userManager = ExternalOimClient.oimClient.getService(UserManager.class);
    } else {
      userManager = Platform.getService(UserManager.class);
    }
  }

  /**
   * @param userAttributes
   * @return
   * @throws UserAlreadyExistsException
   * @throws ValidationFailedException
   * @throws UserCreateException
   */
  public UserManagerResult createUser(HashMap<String, Object> userAttributes) throws UserAlreadyExistsException, ValidationFailedException, UserCreateException {
    User user = new User(null, userAttributes);
    return userManager.create(user);
  }


  /**
   * @param user
   * @return
   * @throws UserAlreadyExistsException
   * @throws ValidationFailedException
   * @throws UserCreateException
   */
  public UserManagerResult createUser(User user) throws UserAlreadyExistsException, ValidationFailedException,
          UserCreateException {
    return userManager.create(user);
  }


  /**
   * @param userKey
   * @param attributeName
   * @param attributeValue
   * @return
   * @throws NoSuchUserException
   * @throws UserModifyException
   * @throws ValidationFailedException
   */
  public UserManagerResult modifyUserAttribute(String userKey, String attributeName, String attributeValue) throws NoSuchUserException, UserModifyException, ValidationFailedException {
    User usr = new User(userKey);
    usr.setAttribute(attributeName, attributeValue);
    return userManager.modify(usr);
  }

  /**
   * @param userLogin
   * @return
   * @throws UserLookupException
   * @throws NoSuchUserException
   */
  public User searchUser(String userLogin) throws UserLookupException, NoSuchUserException {
    return searchUser(userLogin, true);
  }

  /**
   * @param id
   * @param isUserLogin
   * @return
   * @throws UserLookupException
   * @throws NoSuchUserException
   */
  public User searchUser(String id, boolean isUserLogin) throws UserLookupException, NoSuchUserException {
    Set<String> retAttrs = new HashSet<String>();
    return userManager.getDetails(id, retAttrs, isUserLogin);
  }

  /**
   * @param userLogin
   * @return
   * @throws NoSuchUserException
   * @throws UserDisableException
   * @throws ValidationFailedException
   */
  public UserManagerResult disableUser(String userLogin, boolean isUsrLogin) throws NoSuchUserException,
          UserDisableException, ValidationFailedException {
    return userManager.disable(userLogin, isUsrLogin);
  }


  /**
   * @param userLogin
   * @return
   * @throws NoSuchUserException
   * @throws ValidationFailedException
   * @throws UserEnableException
   */
  public UserManagerResult enableUser(String userLogin) throws NoSuchUserException, ValidationFailedException, UserEnableException {
    return userManager.enable(userLogin, true);
  }

  /**
   * @param userKey
   * @return
   * @throws NoSuchUserException
   * @throws ValidationFailedException
   * @throws UserEnableException
   */
  public UserManagerResult enableUser(int userKey) throws NoSuchUserException, ValidationFailedException,
          UserEnableException {
    return userManager.enable(String.valueOf(userKey), false);
  }

  /**
   * @param user
   * @return
   * @throws NoSuchUserException
   * @throws UserModifyException
   * @throws ValidationFailedException
   */
  public UserManagerResult modifyUser(User user) throws NoSuchUserException, UserModifyException, ValidationFailedException {
    return userManager.modify(user);
  }


  /**
   * @return
   * @throws UserSearchException
   */
  public List<User> getAllUsers() throws UserSearchException {
    HashSet<String> attrQuery = new HashSet<>();
    SearchCriteria criteriaUserLogin = new SearchCriteria(UserManagerConstants.AttributeName.USER_LOGIN.getId(), "*", SearchCriteria.Operator.EQUAL);
    return getUsers(criteriaUserLogin, attrQuery);
  }

  /**
   * @return
   * @throws UserSearchException
   */
  public List<User> getActiveUsers() throws UserSearchException {
    HashSet<String> attrQuery = new HashSet<>();
    SearchCriteria criteriaUserLogin = new SearchCriteria(UserManagerConstants.AttributeName.STATUS.getId(),
            "Active", SearchCriteria.Operator.EQUAL);
    return getUsers(criteriaUserLogin, attrQuery);
  }


  /**
   * @return
   * @throws UserSearchException
   */
  public List<User> getDisabledUsers() throws UserSearchException {
    HashSet<String> attrQuery = new HashSet<>();
    SearchCriteria criteriaUserLogin = new SearchCriteria(UserManagerConstants.AttributeName.STATUS.getId(),
            "Disabled", SearchCriteria.Operator.EQUAL);
    return getUsers(criteriaUserLogin, attrQuery);
  }


  /**
   * @param criteria
   * @param attrQuery
   * @return
   * @throws UserSearchException
   */
  public List<User> getUsers(SearchCriteria criteria, HashSet<String> attrQuery) throws UserSearchException {
    return userManager.search(criteria, attrQuery, new HashMap<>());
  }


  public User searchUserByEmployeeNumber(String employeeNumber) throws UserSearchException, UserNotFoundException {
    HashSet<String> attrQuery = new HashSet<>();

    List<User> users = searchUserByAttribute(UserManagerConstants.AttributeName.EMPLOYEE_NUMBER.getId(), employeeNumber);
    if (users.size() < 1) {
      throw new UserNotFoundException("", "User Not Found. Employee number: " + employeeNumber, null);
    } else {
      return users.get(0);
    }
  }


  /**
   * @param attributeName
   * @param attributeValue
   * @return
   * @throws UserSearchException
   */
  public List<User> searchUserByAttribute(String attributeName, String attributeValue) throws UserSearchException {
    HashSet<String> attrQuery = new HashSet<>();
    SearchCriteria criteria = new SearchCriteria(attributeName, attributeValue, SearchCriteria.Operator.EQUAL);
    return userManager.search(criteria, attrQuery, new HashMap<>());
  }


  /**
   * @param attributeName
   * @param attributeValue
   * @return
   * @throws UserSearchException
   */
  public List<User> searchActiveByAttribute(String attributeName, String attributeValue) throws UserSearchException {
    HashSet<String> attrQuery = new HashSet<>();
    SearchCriteria criteriaAttribute = new SearchCriteria(attributeName, attributeValue, SearchCriteria.Operator.EQUAL);
    SearchCriteria criteriaUserStatus = new SearchCriteria(UserManagerConstants.AttributeName.STATUS.getId(),
            "Active", SearchCriteria.Operator.EQUAL);
    SearchCriteria criteria = new SearchCriteria(criteriaAttribute, criteriaUserStatus, SearchCriteria.Operator.AND);
    return userManager.search(criteria, attrQuery, new HashMap<>());
  }


  /**
   * @return
   * @throws UserSearchException
   */
  public boolean isUserExist(String usrLogin) throws UserSearchException {
    HashSet<String> attrQuery = new HashSet<>();
    SearchCriteria criteriaUserLogin = new SearchCriteria(UserManagerConstants.AttributeName.USER_LOGIN.getId(), usrLogin,
            SearchCriteria.Operator.EQUAL);
    return !userManager.search(criteriaUserLogin, attrQuery, new HashMap<>()).isEmpty();
  }


  /**
   * @param userLogin
   * @throws UserManagerException
   */
  public void resetUserPassword(String userLogin) throws UserManagerException {
    /**
     * if first boolean is true, it means first attribute is userLogin , if false it means it is usrKey
     * second boolean is for notification, if it is true, it sends notification according to reset password
     * notification configuration
     */
    userManager.resetPassword(userLogin, true, true);
  }

  /**
   * @param usrLogin
   * @param password
   * @throws UserManagerException
   */
  public void changePassword(String usrLogin, char[] password) throws UserManagerException {
    userManager.changePassword(usrLogin, password, true, false);
  }

}
