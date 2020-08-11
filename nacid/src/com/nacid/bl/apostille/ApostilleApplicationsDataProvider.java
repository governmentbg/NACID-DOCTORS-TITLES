package com.nacid.bl.apostille;

/**
 * User: Georgi
 * Date: 1.4.2020 г.
 * Time: 12:26
 */
public interface ApostilleApplicationsDataProvider {
    public boolean isApplicationTransferredInApostilleSystem(int applicationId);
    public void registerApplicationInApostilleSystem(int applicationId);
}
