package com.furkanuluay.oim.apioperations;

import oracle.iam.platform.Platform;
import oracle.iam.scheduler.api.SchedulerService;
import oracle.iam.scheduler.exception.*;
import oracle.iam.scheduler.vo.JobDetails;
import com.furkanuluay.oim.ExternalOimClient;

/**
 * @author Furkan Uluay
 */
public class SchedulerOperations {

  SchedulerService schedulerService = null;

  public SchedulerOperations() throws Exception {

    if (ExternalOimClient.oimClient != null) {
      schedulerService = ExternalOimClient.oimClient.getService(SchedulerService.class);
    } else {
      schedulerService = Platform.getService(SchedulerService.class);
    }

  }


  public void triggerScheduleTask(String taskName) throws SchedulerAccessDeniedException, SchedulerException {
    schedulerService.triggerNow(taskName);
  }

  /**
   * @throws SchedulerAccessDeniedException
   * @throws SchedulerException
   */
  public void triggerEntListTask() throws SchedulerAccessDeniedException, SchedulerException {
    schedulerService.triggerNow("Entitlement List");
  }


  /**
   * @param jobName
   * @return
   * @throws SchedulerException
   */
  public JobDetails getJobDetail(String jobName) throws SchedulerException {

    return schedulerService.getJobDetail(jobName);
  }

  /**
   * @param jobdetails
   * @throws LastModifyDateNotSetException
   * @throws ParameterValueTypeNotSupportedException
   * @throws RequiredParameterNotSetException
   * @throws IncorrectScheduleTaskDefinationException
   * @throws SchedulerException
   * @throws SchedulerAccessDeniedException
   */
  public void updateJob(JobDetails jobdetails) throws LastModifyDateNotSetException, ParameterValueTypeNotSupportedException, RequiredParameterNotSetException, IncorrectScheduleTaskDefinationException, SchedulerException, SchedulerAccessDeniedException {
    schedulerService.updateJob(jobdetails);

  }
}





