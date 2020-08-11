<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="/META-INF/ext_regprof_nacid_taglib.tld" prefix="regprof_ext"%>

<v:form name="appform5" action="" method="post"
        backurl="${pathPrefix }/control/applications/list?getLastTableState=1" 
        skipSubmit="true">
        <regprof_ext:regprof_report_applicant_external attributePrefix="ext" attachmentPrefix="${pathPrefix }/control/applicantreportattachments_ext"/>
</v:form>