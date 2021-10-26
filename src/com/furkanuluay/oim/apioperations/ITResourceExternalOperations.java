package com.furkanuluay.oim.apioperations;


import Thor.API.Operations.tcITResourceInstanceOperationsIntf;
import Thor.API.tcResultSet;
import oracle.iam.platform.Platform;
import com.furkanuluay.oim.ExternalOimClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Furkan Uluay
 */
public class ITResourceExternalOperations implements AutoCloseable {
  tcITResourceInstanceOperationsIntf itResourceOperations = null;

  /**
   * @throws Exception
   */
  public ITResourceExternalOperations() throws Exception {

    if (ExternalOimClient.oimClient != null) {
      itResourceOperations = ExternalOimClient.oimClient.getService(tcITResourceInstanceOperationsIntf.class);
    } else {
      itResourceOperations = Platform.getService(tcITResourceInstanceOperationsIntf.class);
    }

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
    tcResultSet itRes = itResourceOperations.findITResourceInstances(params);
    long itResKey = itRes.getLongValue("IT Resource.Key");

    if (itResKey != 0) {

      return this.getITResourceParamsKey(itResKey);
    } else {
      throw new Exception("IT Resource Not Found");
    }
  }


  /**
   * @param itResourceKey
   * @return
   * @throws Exception
   */
  public HashMap<String, String> getITResourceParamsKey(long itResourceKey) throws Exception {
    HashMap<String, String> paramsMap = new HashMap<String, String>();
    tcResultSet paramsResult = itResourceOperations.getITResourceInstanceParameters(itResourceKey);
    for (int i = 0; i < paramsResult.getRowCount(); i++) {
      paramsResult.goToRow(i);
      paramsMap.put(paramsResult.getStringValue("IT Resources Type Parameter.Name"),
              paramsResult.getStringValue("IT Resource.Parameter.Value"));
    }

    return paramsMap;
  }


  /**
   * @param itResourceName
   * @return
   * @throws Exception
   */
  public long getITResourceKey(String itResourceName) throws Exception {
    Map<String, String> map = new HashMap<String, String>();
    map.put("IT Resources.Name", itResourceName);
    tcResultSet resultSet = itResourceOperations.findITResourceInstances(map);
    resultSet.goToRow(0);
    long result = Long.parseLong(resultSet.getStringValue("IT Resource.Key"));
    return result;
  }

  /**
   * @param itResourceKey
   * @return
   * @throws Exception
   */
  public String getITResourceName(long itResourceKey) throws Exception {

    Map<String, Long> map = new HashMap<String, Long>();
    map.put("IT Resources.Key", itResourceKey);
    tcResultSet resultSet = itResourceOperations.findITResourceInstances(map);
    resultSet.goToRow(0);
    return resultSet.getStringValue("IT Resource.Key");
  }


  @Override
  public void close() throws Exception {
    if (itResourceOperations != null) {
      itResourceOperations.close();
    }
  }
}
