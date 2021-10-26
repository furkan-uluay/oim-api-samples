package com.furkanuluay.oim.apioperations;

import oracle.iam.platform.Platform;
import oracle.iam.provisioning.api.ApplicationInstanceService;
import oracle.iam.provisioning.exception.ApplicationInstanceNotFoundException;
import oracle.iam.provisioning.exception.GenericAppInstanceServiceException;
import oracle.iam.provisioning.vo.ApplicationInstance;
import com.furkanuluay.oim.ExternalOimClient;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author Furkan Uluay
 */

public class ApplicationInstanceOperations {
  ApplicationInstanceService applicationInstanceService;

  public ApplicationInstanceOperations() throws Exception {

    if (ExternalOimClient.oimClient != null) {
      applicationInstanceService = ExternalOimClient.oimClient.getService(ApplicationInstanceService.class);
    } else {
      applicationInstanceService = Platform.getService(ApplicationInstanceService.class);
    }

  }

  /**
   * @param applicationInstanceName
   * @return
   * @throws GenericAppInstanceServiceException
   * @throws ApplicationInstanceNotFoundException
   */
  public ApplicationInstance getAppInstanceByName(String applicationInstanceName)
          throws GenericAppInstanceServiceException, ApplicationInstanceNotFoundException {
    return applicationInstanceService.findApplicationInstanceByName(applicationInstanceName);
  }

  /**
   * @param appInsKey
   * @return
   * @throws ApplicationInstanceNotFoundException
   * @throws GenericAppInstanceServiceException
   */
  public ApplicationInstance getAppInstanceByKey(long appInsKey)
          throws ApplicationInstanceNotFoundException, GenericAppInstanceServiceException {
    return applicationInstanceService.findApplicationInstanceByKey(appInsKey);
  }


  /**
   * @param message
   */
  private void outLog(String message) {
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = new Date();

    System.out.println(new StringBuilder().append(formatter.format(date)).append(" [")
            .append(this.getClass().getName()).append("] --> ").append(message).toString());

  }
}
