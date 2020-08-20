<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="/screens/header.jsp" %>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid" %>
<%@ taglib uri="/META-INF/validation.tld" prefix="v" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<h3 class="title"><span>Разглеждане на запис от системния журнал</span></h3>
<v:form name="syslogForm" action="${pathPrefix }/control/syslog/save"
        method="post"
        skipSubmit="true"
        backurl="${pathPrefix }/control/syslog/list?getLastTableState=1">
    Потребителско име&nbsp;:&nbsp;${syslog.username}<br />
    Имена&nbsp;:&nbsp;${syslog.userFullName}<br />
    ID на сесия&nbsp;:&nbsp;${syslog.sessionId}<br />
    IP&nbsp;:&nbsp;${syslog.remoteAddress}<br />
    Дата на вход&nbsp;:&nbsp;${syslog.timeLogin}<br />
    Дата на изход&nbsp;:&nbsp;${syslog.timeLogout}<br />
    Страница&nbsp;:&nbsp;${syslog.groupName}<br />
    Операция&nbsp;:&nbsp;${syslog.operationName}<br />
    Дата на създаване&nbsp;:&nbsp;${syslog.dateCreated}<br />
    Параметри&nbsp;:&nbsp;${syslog.queryString}<br />
</v:form>
<%@ include file="/screens/footer.jsp" %>