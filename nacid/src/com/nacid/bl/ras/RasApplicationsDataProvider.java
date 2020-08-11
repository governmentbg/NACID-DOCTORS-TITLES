package com.nacid.bl.ras;

/**
 * User: ggeorgiev
 * Date: 25.10.2019 г.
 * Time: 14:28
 */
public interface RasApplicationsDataProvider {
    boolean isApplicationTransferredInRas(int applicationId);

    void registerRasDoctorateApplication(int applicationId);
}
