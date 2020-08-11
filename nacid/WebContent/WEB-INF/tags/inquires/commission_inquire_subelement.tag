<%@ tag pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ attribute name="title" required="true"%>
<%@ attribute name="chosenTitle" required="true"%>
<%@ attribute name="labelTitle" required="true"%>
<%@ attribute name="type" required="true" rtexprvalue="true"%>
<%@ attribute name="selectedElements" required="true"%>
<%@ attribute name="comboAttribute" required="true"%>
<%@ attribute name="elementIds" required="true"%>
<%@ attribute name="comboClass" required="false"%>

<fieldset><legend>${title }</legend>
        <input id="${type }Ids" type="hidden" name="${type }Ids" value="${requestScope[elementIds] }" />
        <div id="${type}Div"
       		<c:if test="${empty requestScope[selectedElements]}">style="display:none;"</c:if>
        >
            <span class="fieldlabel2"><label>${chosenTitle}</label></span> 
            <table id="selected_${type}" >
                <tbody>
                <c:forEach var="el" items="${requestScope[selectedElements]}">
                    <tr id="${type}_row_id_${el.id }">
                        <td>${el.name }</td>
                        <td><a href="#" onclick="removeItem('${type}', ${el.id }); return false;" ><img src="${pathPrefix}/img/icon_delete.png" /></a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div class="clr10"><!--  --></div>
        </div>
        <p>
           <span class="fieldlabel2"><label>${labelTitle }</label></span> 
           <span id="${type}_span"> 
               <nacid:combobox id="${type}" name="${type}" attributeName="${comboAttribute}" style="${not empty comboClass ? comboClass : 'brd w500'}" />
           </span> 
           <a href="#" onclick= "addElement('${type}'); return false;">Добави</a>
        </p>
    </fieldset>
    <div class="clr10"><!--  --></div>