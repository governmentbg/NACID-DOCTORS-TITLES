<%@ tag pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ attribute name="skipJointDegree" required="false" type="java.lang.Boolean" %>
<script type="text/javascript">
    function jointDegreeChanged() {
        var isChecked = $("joint_degree").checked;
        if (isChecked) {
            $('only_joint_degree').checked = false;
            if (document.tableForm1 != null) {
                document.tableForm1.only_joint_degree.value = 0;
            }

        }
        if (document.tableForm1 != null) {
            document.tableForm1.joint_degree.value = isChecked ? 1 : 0;
        }

    }
    function onlyJointDegreeChanged() {
        var isChecked = $("only_joint_degree").checked;
        if (isChecked) {
            $('joint_degree').checked = false;
            if (document.tableForm1 != null) {
                document.tableForm1.joint_degree.value = 0;
            }
        }
        if (document.tableForm1 != null) {
            document.tableForm1.only_joint_degree.value = isChecked ? 1 : 0;
        }
    }
</script>
<fieldset><legend>${messages.University }</legend>
        <c:if test="${skipJointDegree == null || skipJointDegree == false}">
        <p class="checkbox">
            <input id="joint_degree" name="joint_degree" value="1" type="checkbox" checked="checked" onclick="jointDegreeChanged();"/>
            <label for="joint_degree">Включително съвместна образователна степен</label>
        </p>
            <div class="clr"><!-- --></div>
            <p class="checkbox">
                <input id="only_joint_degree" name="only_joint_degree" value="1" type="checkbox" onclick="onlyJointDegreeChanged()"/>
                <label for="only_joint_degree">Само съвместна образователна степен</label>
            </p>
            <div class="clr"><!-- --></div>
            <p class="checkbox">
                <input id="universities_only_with_diploma_registers" name="universities_only_with_diploma_registers" value="1" type="checkbox" />
                <label for="universities_only_with_diploma_registers">Само с елекетронни регистри</label>
            </p>
        </c:if>
    <c:forEach items="${universities}" var="university" varStatus="status">    
        <div id="university${status.index }_div" class="university_class" >
            <fieldset><legend>${messages.University }</legend>
              
              <p class="flt_rgt"><a href="javascript:void(0);" style="color: red" onclick="javascript:$('university${status.index}_div').remove();">Премахни</a></p>
              
              <div class="clr"><!--  --></div>
              <p><span class="fieldlabel2"><label for="university${status.index }Country">${messages.Country }</label></span> 
                  <c:set target="${requestScope.countriesCombo}" property="selectedKey" value="${university.countryId}" />
                  <input type="hidden" name="_university${status.index }Country" id="_university${status.index }Country" value=""/>
                  <nacid:combobox id="university${status.index }Country" name="university${status.index }Country" attributeName="countriesCombo" style="w500 brd"  onchange="$(this).previous().value = $(this).value" />
              </p>
              <div class="clr"><!--  --></div>
                  <input type="hidden" id="university${status.index}Ids" name="university${status.index}Ids" />
                  <input type="hidden" id="university${status.index}NamesIds" name="university${status.index}NamesIds" />
                  <input type="hidden" id="org_university${status.index}NamesIds" name="org_university${status.index}NamesIds" />
                    <div id="university${status.index}Div">
                        <span class="fieldlabel2"><label>Избрани висши училища</label></span> 
                        <table id="selected_university${status.index}" >
                            <tbody>
                            </tbody>
                        </table>
                        <div class="clr10"><!--  --></div>
                    </div>
                    <div class="clr"><!--  --></div>
                    <div id="university${status.index}NamesDiv">
                        <span class="fieldlabel2"><label>Избрани маски на висши училища</label></span> 
                        <table id="selected_university${status.index}Names" >
                            <tbody>
                            </tbody>
                        </table>
                        <div class="clr10"><!--  --></div>
                    </div>
                    <div id="org_university${status.index}NamesDiv">
                        <span class="fieldlabel2"><label>Избрани маски на оригинални наименования на висши училища</label></span>
                        <table id="selected_org_university${status.index}Names" >
                            <tbody>
                            </tbody>
                        </table>
                        <div class="clr10"><!--  --></div>
                    </div>
                    <p>
                       <span class="fieldlabel2"><label>${messages.University }</label></span> 
                       <span id="university_span${status.index}"> 
                            <input type="hidden" name="university${status.index}Id" id="university${status.index}Id" value="" />
                            <v:textinput class="brd w500" name="university${status.index}" id="university${status.index}"  value="" onkeydown="if(!isSpecialKeyPressed(event)) inquireUniversityChanged('university${status.index}');" />
                       </span>
                          <script type="text/javascript">
                                new Autocomplete('university${status.index}', {
                                      serviceUrl:'${pathPrefix}/control/universitysuggest',
                                      width:500,
                                      onSelect:updateInquireUniversity,
                                      useBadQueriesCache:false,
                                      dynamicParameters: function (){
                                          return {country_id : $('university${status.index }Country').value};
                                      }
                                });

                          </script>
                    </p>
                    <p>
                        <span class="fieldlabel2"><label>Оригинално наименование </label></span>
                        <span id="org_university_span${status.index}">
                            <v:textinput class="brd w500" name="org_university${status.index}" id="org_university${status.index}"  value="" onkeydown="if(!isSpecialKeyPressed(event)) inquireUniversityChanged('org_university${status.index}');" />
                        </span>
                        <script type="text/javascript">
                            new Autocomplete('org_university${status.index}', {
                                serviceUrl:'${pathPrefix}/control/universitysuggest',
                                width:500,
                                onSelect:updateInquireUniversity,
                                useBadQueriesCache:false,
                                dynamicParameters: function (){
                                    return {country_id : $('university${status.index }Country').value, nametype : 2};
                                }
                            });

                        </script>
                        <a href="#" onclick="return addInquireUniversity('university${status.index}');">Добави</a>
                </p>
              <div class="clr10"><!--  --></div>
            </fieldset>
        </div>
        
        <c:if test="${status.index == 0}">
            <div class="clr10"><!--  --></div>
            <p class="flt_rgt" id="add_universities"><a href="javascript:addUniversity();">Добави държава/висше училище/</a></p>
            <div class="clr10"><!--  --></div>
        </c:if>
    </c:forEach>
    
        
    <div id="university_div" class="university_class" style="display:none;">
            <fieldset><legend>${messages.University }</legend>
              <p class="flt_rgt"><a href="javascript:void(0);" style="color: red">Премахни</a></p>
              <div class="clr"><!--  --></div>
              <p><span class="fieldlabel2"><label for="universityCountry">${messages.Country }</label></span> 
                  <c:set target="${requestScope.countriesCombo}" property="selectedKey" value="" />
                  <input type="hidden" name="_universityCountry" id="_universityCountry" value=""/>
                  <nacid:combobox id="universityCountry" name="universityCountry" attributeName="countriesCombo" style="w500 brd" onchange="$(this).previous().value = $(this).value" />
              </p>
              <div class="clr"><!--  --></div>
                  <input type="hidden" id="universityIds" name="universityIds" />
                  <input type="hidden" id="universityNamesIds" name="universityNamesIds" />
                  <input type="hidden" id="org_universityNamesIds" name="org_universityNamesIds" />
                    <div id="universityDiv" style="display:none;">
                        <span class="fieldlabel2"><label>Избрани висши училища</label></span> 
                        <table id="selected_university" >
                            <tbody>
                            </tbody>
                        </table>
                        <div class="clr10"><!--  --></div>
                    </div>
                    <div class="clr"><!--  --></div>
                    <div id="universityNamesDiv" style="display:none;">
                        <span class="fieldlabel2"><label>Избрани маски на висши училища</label></span> 
                        <table id="selected_universityNames" >
                            <tbody>
                            </tbody>
                        </table>
                        <div class="clr10"><!--  --></div>
                    </div>
                    <div id="org_universityNamesDiv" style="display:none;">
                        <span class="fieldlabel2"><label>Избрани маски на оригинални наименования на висши училища</label></span>
                        <table id="selected_org_universityNames" >
                            <tbody>
                            </tbody>
                        </table>
                        <div class="clr10"><!--  --></div>
                    </div>
                    <p>
                       <span class="fieldlabel2"><label>${messages.University }</label></span> 
                       <span id="university_span"> 
                            <input type="hidden" name="universityId" id="universityId" value="" />
                            <v:textinput class="brd w500" name="university" id="university"  value="" onkeydown="if(!isSpecialKeyPressed(event)) inquireUniversityChanged('university');" />
                       </span>
                    </p>
                    <p>
                        <span class="fieldlabel2"><label>Оригинално наименование</label></span>
                        <span id="org_university_span">
                                <v:textinput class="brd w500" name="org_university" id="org_university"  value="" onkeydown="if(!isSpecialKeyPressed(event)) inquireUniversityChanged('org_university');" />
                           </span>
                        <a href="#" >Добави</a>
                    </p>
              <div class="clr10"><!--  --></div>
            </fieldset>
        </div>
        <div id="afterUniversityDiv"><!--  --></div>
    </fieldset>
