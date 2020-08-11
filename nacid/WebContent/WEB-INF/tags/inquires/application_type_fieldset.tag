<%@ tag pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script type="text/javascript">
    function addApplicationType() {
        var entryNumsLabel = generateEntryNumsLabel();
        var selectedOption = $('applicationType').options[$('applicationType').selectedIndex];
        var optionId = selectedOption.value;
        var optionTxt = selectedOption.innerHTML + (entryNumsLabel == '' ? '' : ' - ' + entryNumsLabel);
        removeApplicationTypeRowIfExists(optionId);//pyrvo maham zapisa ot spisyka (ako go ima), tyj kato moje da sa promeneni entry num-ovete - za da moje da se generira noviq label!!
        // console.log(optionTxt + '...' + optionId + "..." + entryNumsLabel);
        _addElement('applicationType', optionId, optionTxt);
    }
    function removeApplicationTypeRowIfExists(selectedApplicationType) {
        $$('[id^=applicationType_row_id_]').each(function(element) {
            var value = $$('#' + element.id + ' input')[0].value;
            // console.log(value + '...' + selectedApplicationType);
            if (value == selectedApplicationType) {
                // console.log("removing item..." + element.id);
                removeItem('applicationType', element.id.replace('applicationType_row_id_', ''));
            }
        });
    }
    function generateEntryNumsLabel() {
        var entryNumLabel = [];
        var applicationType = $('applicationType').value;
        $$('input[name=entryNumSeries_' + applicationType + ']').each(function(element){
            if (element.checked) {
                entryNumLabel.push($$('label[for=entryNumSeries_' + applicationType + "_" + element.value + ']')[0].innerHTML);
                // console.log("checked...");
            }
        });
        // console.log(JSON.stringify(entryNumLabel));
        return entryNumLabel.length > 0 ? entryNumLabel.join("/") : '';

    }
    function applicationTypeChanged() {
        $$('.applicationType_entryNumSeries').each(function(element) {
            element.hide();
        });
        if ($("applicationType").value != '') {
            $('applicationType_entryNumSeries_' + $("applicationType").value).show();
        }
    }
</script>
<fieldset><legend>Тип заявление</legend>
        <input id="applicationTypeIds" type="hidden" name="applicationTypeIds" value="" />
        <div id="applicationTypeDiv" style="display:none;">
            <span class="fieldlabel2"><label>Избрани типове заявления</label></span> 
            <table id="selected_applicationType" >
                <tbody>
                <%--<c:forEach var="el" items="${requestScope[selectedElements]}">
                    <tr id="applicationType_row_id_${el.id }">
                        <td>${el.name }</td>
                        <td><a href="#" onclick="removeItem('applicationType', ${el.id }); return false;" ><img src="${pathPrefix}/img/icon_delete.png" /></a></td>
                    </tr>
                </c:forEach>--%>
                </tbody>
            </table>
            <div class="clr10"><!--  --></div>
        </div>
        <p>
           <span class="fieldlabel2"><label>Тип заявление</label></span> 
           <span id="applicationType_span"> 
               <nacid:combobox id="applicationType" name="applicationType" attributeName="applicationTypesCombo" style="brd w500" onchange="applicationTypeChanged()" />
           </span>
           <a href="#" onclick= "addApplicationType(); return false;">Добави</a>
        </p>
        <p>
            <c:forEach items="${applicationTypeToEntryNumSeries}" var="atens">
                <span  class="applicationType_entryNumSeries" id="applicationType_entryNumSeries_${atens.key}" style="display: none;">
                    <span <c:if test="${fn:length(atens.value) <= 1}" >style="display: none"</c:if>>
                        <span class="fieldlabel2"><label>Вид услуга</label></span>
                        <c:forEach items="${atens.value}" var="en">
                            <v:checkboxinput name="entryNumSeries_${atens.key}" id="entryNumSeries_${atens.key}_${en.key}" value="${en.key}" checked="true" />
                            <label for="entryNumSeries_${atens.key}_${en.key}">${en.value}</label>
                        </c:forEach>
                        <br />
                        <span class="fieldlabel2"><label>Тип</label></span>
                        <input type="radio" name="entryNumSeries_join_type_${atens.key}" id="entryNumSeries_join_type_2" value="2" checked/>
                        <label for="entryNumSeries_join_type_2">Някой от изброените</label>
                        <input type="radio" name="entryNumSeries_join_type_${atens.key}" id="entryNumSeries_join_type_1" value="1"/>
                        <label for="entryNumSeries_join_type_1">Всички изброени</label>
                        <input type="radio" name="entryNumSeries_join_type_${atens.key}" id="entryNumSeries_join_type_3" value="3"/>
                        <label for="entryNumSeries_join_type_3">Само всички изброени</label>
                    </span>

                </span>
            </c:forEach>
        </p>
    </fieldset>
    <div class="clr10"><!--  --></div>