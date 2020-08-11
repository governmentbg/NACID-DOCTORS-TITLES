package com.nacid.bl.applications;

import com.nacid.bl.applications.base.ApplicationKindBase;
import com.nacid.bl.nomenclatures.ApplicationStatus;

/**
 * Created by georgi.georgiev on 30.09.2015.
 */
public interface ApplicationKind extends ApplicationKindBase {
    public ApplicationStatus getFinalStatus();
}
