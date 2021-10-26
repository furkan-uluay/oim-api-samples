package com.furkanuluay.oim.apioperations;

import Thor.API.Operations.tcITResourceInstanceOperationsInternalIntf;
import Thor.API.tcResultSet;
import oracle.iam.platform.Platform;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Furkan Uluay
 * <p>
 * tcITResourceInstanceOperationsInternalIntf is used for getting password
 */
public class ITResourceInternalOperations implements AutoCloseable {

  tcITResourceInstanceOperationsInternalIntf itResourceInternalOperations = null;


  /**
   * @throws Exception OimClient can not access tcITResourceInstanceOperationsInternalIntf operations because of it
   *                   resource password protection This class should be used only on server by getting instance from
   *                   Platform
   */
  public ITResourceInternalOperations() throws Exception {
    itResourceInternalOperations = Platform.getService(tcITResourceInstanceOperationsInternalIntf.class);
  }


  /**
   * @param itResName
   * @return
   * @throws Exception
   */
  public Map<String, String> getITResourceParamByName(String itResName)
          throws Exception {

    Map<String, Object> params = new HashMap<>();
    params.put("IT Resource.Name", itResName);
    tcResultSet itRes = itResourceInternalOperations.findITResourceInstances(params);
    long itResKey = itRes.getLongValue("IT Resource.Key");

    if (itResKey != 0) {

      return this.getITResourceParamsByKey(itResKey);
    } else {
      throw new Exception("IT Resource Not Found");
    }
  }


  public HashMap<String, String> getITResourceParamsByKey(long itResourceKey) throws Exception {
    HashMap<String, String> paramsMap = new HashMap<String, String>();
    tcResultSet paramsResult = itResourceInternalOperations.getAllParameters(itResourceKey);
    for (int i = 0; i < paramsResult.getRowCount(); i++) {
      paramsResult.goToRow(i);
      paramsMap.put(paramsResult.getStringValue("IT Resources Type Parameter.Name"),
              paramsResult.getStringValue("IT Resource.Parameter.Value"));
    }

    return paramsMap;
  }


  public String getITResourceName(long itResourceKey) throws Exception {
    Map<String, Long> map = new HashMap<String, Long>();
    map.put("IT Resources.Key", itResourceKey);
    tcResultSet resultSet = itResourceInternalOperations.findITResourceInstances(map);
    resultSet.goToRow(0);
    return resultSet.getStringValue("IT Resource.Key");
  }


  @Override
  public void close() throws Exception {
    if (itResourceInternalOperations != null) {
      itResourceInternalOperations.close();
    }
  }
}
