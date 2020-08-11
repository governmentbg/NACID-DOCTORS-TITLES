package com.ext.nacid.regprof.web.model.applications;

import com.ext.nacid.web.model.applications.ExtApplicationCommentExtendedWebModel;
import com.nacid.bl.regprof.external.ExtRegprofApplicationCommentExtended;
import com.nacid.bl.users.UsersDataProvider;

/**
 * User: ggeorgiev
 * Date: 10.10.2019 г.
 * Time: 18:30
 */
public class ExtRegprofApplicationCommentExtendedWebModel extends ExtApplicationCommentExtendedWebModel {
    public ExtRegprofApplicationCommentExtendedWebModel(ExtRegprofApplicationCommentExtended ac, UsersDataProvider usersDataProvider) {
        super(ac, usersDataProvider);
    }
}
