package com.furkanuluay.oim.apioperations;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcInvalidLookupException;
import Thor.API.Exceptions.tcInvalidValueException;
import Thor.API.Operations.tcLookupOperationsIntf;
import Thor.API.tcResultSet;
import oracle.iam.platform.Platform;
import com.furkanuluay.oim.ExternalOimClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Furkan Uluay
 */
public class LookupOperations implements AutoCloseable {


  public tcLookupOperationsIntf lookupService = null;

  /**
   * @throws Exception
   */
  public LookupOperations() throws Exception {

    if (ExternalOimClient.oimClient != null) {
      lookupService = ExternalOimClient.oimClient.getService(tcLookupOperationsIntf.class);
    } else {
      lookupService = Platform.getService(tcLookupOperationsIntf.class);
    }
  }

  /**
   * @param lookupName
   * @return
   * @throws tcInvalidLookupException
   * @throws tcAPIException
   * @throws tcColumnNotFoundException
   */
  public Map<String, String> getLookupValues(String lookupName) throws tcInvalidLookupException, tcAPIException, tcColumnNotFoundException {
    Map<String, String> response = new HashMap<>();
    tcResultSet lookupValues = lookupService.getLookupValues(lookupName);
    for (int i = 0; i < lookupValues.getRowCount(); i++) {
      lookupValues.goToRow(i);
      response.put(lookupValues.getStringValue("Lookup Definition.Lookup Code Information.Code Key"),
              lookupValues.getStringValue("Lookup Definition.Lookup Code Information.Decode"));
    }
    return response;
  }

  /**
   * @param lookupName
   * @param lookupValue
   * @throws Exception
   */
  public void addOrUpdateLookupValues(String lookupName, Map<String, String> lookupValue) throws Exception {

    for (Map.Entry<String, String> entry : lookupValue.entrySet()) {
      try {
        lookupService.addLookupValue(lookupName, entry.getKey(), entry.getValue(), "en", "US");
      } catch (tcInvalidValueException e) {
        System.out.println("Code Key: " + entry.getKey() + " is already exist in Lookup:" + lookupName + ". It will be updated with Decode Key: " +
                entry.getValue());

        Map<String, String> updatedValue = new HashMap<>();
        updatedValue.put("Lookup Definition.Lookup Code Information.Code Key", entry.getKey());
        updatedValue.put("Lookup Definition.Lookup Code Information.Decode", entry.getValue());
        lookupService.updateLookupValue(lookupName, entry.getKey(), updatedValue);
      }
    }

  }


  /**
   * @param lookupName
   * @param code
   * @param decode
   * @throws Exception
   */
  public void addOrUpdateLookupValue(String lookupName, String code, String decode) throws Exception {

    try {
      lookupService.addLookupValue(lookupName, code, decode, "en", "US");
      System.out.println("A row added into Lookup. Lookup name: " + lookupName + ", Code: " + code + ", " +
              "Decode: " + decode);
    } catch (tcInvalidValueException e) {

      Map<String, String> updatedValue = new HashMap<>();
      updatedValue.put("Lookup Definition.Lookup Code Information.Code Key", code);
      updatedValue.put("Lookup Definition.Lookup Code Information.Decode", decode);
      lookupService.updateLookupValue(lookupName, code, updatedValue);

    }

  }


  public void updateLookupValue(String lookupName, String code, String newDecode) throws Exception {

    Map<String, String> updatedValue = new HashMap<>();
    updatedValue.put("Lookup Definition.Lookup Code Information.Code Key", code);
    updatedValue.put("Lookup Definition.Lookup Code Information.Decode", newDecode);
    lookupService.updateLookupValue(lookupName, code, updatedValue);

  }


  /**
   * @param lookupName
   * @param lookupValue
   * @throws Exception
   */
  public void addLookupValues(String lookupName, Map<String, String> lookupValue) throws Exception {

    for (Map.Entry<String, String> entry : lookupValue.entrySet()) {
      try {
        lookupService.addLookupValue(lookupName, entry.getKey(), entry.getValue(), "en", "US");
      } catch (tcInvalidValueException e) {
        System.out.println("Code Key: " + entry.getKey() + " is already exist in Lookup:" + lookupName);
      }
    }

  }


  /**
   * @param lookupName
   * @param encodeValues
   * @throws tcInvalidLookupException
   * @throws tcAPIException
   */
  public void removeLookupValues(String lookupName, List<String> encodeValues) throws tcInvalidLookupException, tcAPIException {

    for (String encode : encodeValues) {
      try {
        lookupService.removeLookupValue(lookupName, encode);
      } catch (tcInvalidValueException e) {
        System.out.println("Encode Key: " + encode + " not exist in Lookup: " + lookupName);
      }
    }
  }

  /**
   * @param lookupName
   * @param lookupEncode
   * @return
   * @throws tcAPIException
   */
  public String getSingleLookupValue(String lookupName, String lookupEncode) throws tcAPIException {
    return lookupService.getDecodedValueForEncodedValue(lookupName, lookupEncode);
  }

  /**
   * @param fromLookupName
   * @param toLookupName
   * @throws tcInvalidLookupException
   * @throws tcColumnNotFoundException
   * @throws tcAPIException
   */
  public void copyLookup(String fromLookupName, String toLookupName) throws tcInvalidLookupException, tcColumnNotFoundException, tcAPIException {
    Map<String, String> lookupValues = this.getLookupValues(fromLookupName);
    lookupService.addBulkLookupValues(toLookupName, lookupValues, "en", "us");
  }


  /**
   * @param lookupName
   * @param lookupValues
   * @throws tcInvalidLookupException
   * @throws tcAPIException
   */
  public void addMultipleLookupValues(String lookupName, Map<String, String> lookupValues) throws tcInvalidLookupException, tcAPIException {
    lookupService.addBulkLookupValues(lookupName, lookupValues, "en", "us");

  }


  /**
   * @param lookupName
   * @param lookupEncode
   * @throws tcAPIException
   * @throws tcInvalidLookupException
   */
  public void removeMultipleLookupValues(String lookupName, Set<String> lookupEncode) throws tcAPIException, tcInvalidLookupException {
    lookupService.removeBulkLookupValues(lookupName, lookupEncode);
  }


  /**
   * @param lookupName
   * @param lookupEncode
   * @throws tcAPIException
   * @throws tcInvalidLookupException
   * @throws tcInvalidValueException
   */
  public void addSingleLookupValue(String lookupName, String lookupEncode, String lookupDecode) throws tcAPIException,
          tcInvalidLookupException, tcInvalidValueException {
    lookupService.addLookupValue(lookupName, lookupEncode, lookupDecode, "en", "us");

  }

  /**
   * @param lookupName
   * @param lookupEncode
   * @throws tcAPIException
   * @throws tcInvalidLookupException -> The lookup is not exist
   * @throws tcInvalidValueException  -> The codeKey is not exist in lookup
   */
  public void removeSingleLookupValue(String lookupName, String lookupEncode) throws tcAPIException, tcInvalidLookupException, tcInvalidValueException {
    lookupService.removeLookupValue(lookupName, lookupEncode);

  }


  @Override
  public void close() {
    if (lookupService != null) {
      lookupService.close();
    }
  }
}