<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
	  	<p class="cc">
            <input type="button" value="Назад" onclick="document.location.href='${pathPrefix }/control/applications/list?getLastTableState=1';" class="back" />
            <c:if test="${!operationView && !hideApplyButton}">
                <input type="submit" style="width: 85px;" value="Подай" class="save" />
                <object
	                classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93"
	                codebase="http://java.sun.com/update/1.6.0/jinstall-6-windows-i586.cab"
	                width="130" height="25" name="SmartCardSignerApplets">
	                <param name="code" value="org.sag.dsa.gui.SmartCardSignerApplet">
	                <param name="archive" value="${pathPrefix }/esign/SCSignerApplet.jar">
	                <param name="mayscript" value="true">
	                <param name="type" value="application/x-java-applet;version=1.6">
	                <param name="codebase_lookup" value="false">
	                <param name="scriptable" value="false">
	                <param name="xmlContentField" value="xmlContent">
	                <param name="signedXmlField" value="signedXml">
	                <%--<param name="keystoreType" value="PKCS#12"> --%>
	                <param name="form" value="appform4">
	                <%--<param name="xmlProcessingUrl" value="${host }${pathPrefix }/control/esignapplication/"> --%>
	                <param name="signButtonCaption" value="Подпиши">
	                <param name="callbackFunction" value="changeSubmitClass">
	                <param name="signButtonStyle" value="color:#3333FF; border:none; background-color:#FFFFFF; font: normal 1em arial, helvetica, sans-serif;">
	        
	                <comment>
	                    <embed
	                        type="application/x-java-applet;version=1.6"
	                        code="org.sag.dsa.gui.SmartCardSignerApplet" archive="${pathPrefix }/esign/SCSignerApplet.jar"
	                        width="130" height="25" scriptable="true"
	                        pluginspage="http://java.sun.com/products/plugin/index.html#download"
	                        xmlContentField="xmlContent"
	                        signedXmlField="signedXml"
	                        callbackFunction="changeSubmitClass"
	                        form="appform4"
	                        codebase_lookup="false"
	                        <%-- keystoreType="PKCS#12" --%>
	                        <%-- xmlProcessingUrl="${host }${pathPrefix }/control/esignapplication/" --%>
	                        signButtonCaption="Подпиши"
	                        signButtonStyle="color:#3333FF; border:none; background-color:#FFFFFF; font: normal 1em arial, helvetica, sans-serif;"
	                        >
	                    </embed>
	                    <noembed>
	                        Smart card signing applet can not be started because
	                        Java Plugin 1.6 or newer is not installed.
	                    </noembed>
	                </comment>
	              --</object>
            </c:if>
        </p>