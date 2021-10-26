package com.furkanuluay.oim.apioperations;

import oracle.iam.platform.Platform;
import oracle.iam.platformservice.api.PlatformUtilsService;
import oracle.iam.platformservice.exception.InvalidCacheCategoryException;
import com.furkanuluay.oim.ExternalOimClient;

/**
 * @author Furkan Uluay
 */
public class PlatformUtilsServiceOperations {


  PlatformUtilsService platformUtilsService = null;

  /**
   * @throws Exception
   */
  public PlatformUtilsServiceOperations() throws Exception {

    if (ExternalOimClient.oimClient != null) {
      platformUtilsService = ExternalOimClient.oimClient.getService(PlatformUtilsService.class);
    } else {
      platformUtilsService = Platform.getService(PlatformUtilsService.class);
    }
  }


  /**
   * @throws InvalidCacheCategoryException
   */
  public void purgeCacheAll() throws InvalidCacheCategoryException {
    platformUtilsService.purgeCache("ALL");
  }
}
