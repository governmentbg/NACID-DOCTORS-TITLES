<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="/META-INF/ext_nacid_taglib.tld" prefix="nacid_ext"%>

<v:form name="appform5" action="" method="post"
        backurl="${pathPrefix }/control/application/list/${application_type}?getLastTableState=1"
        skipSubmit="true">
        <nacid_ext:report_applicant_external attributePrefix="ext" attachmentPrefix="${pathPrefix }/control/applicantreportattachments_ext"/>
</v:form>