<%@ tag pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>

<fieldset><legend>Обучаващи институции</legend>
        <div id="institution1_div" class="university_class" >
            <fieldset><legend>Обучаваща институция</legend>      	
					<p class="flt_rgt"><a href="javascript:void(0);" style="color: red" onclick="javascript:$('institution1_div').remove();">Премахни</a></p>
					<div class="clr"><!--  --></div>
					<p><span class="fieldlabel2"><label for="institution1Name">Ново име</label><br/></span>
		               	<input type="hidden" name="institution1NameId" id="institution1NameId" value=""/>
		               	<v:textinput id="institution1Name" name="institution1Name" value="" class="brd w500" onkeydown="if(!isSpecialKeyPressed(event)) nomenclatureChanged('institution1Name');"/>
		               	<script type="text/javascript">
	                        new Autocomplete('institution1Name', { 
	                              serviceUrl:'${pathPrefix}/control/professionalinstitutionsuggest',
	                              width:500,
	                              onSelect:updateNomenclature,
	                              useBadQueriesCache:false
	                        });
                        </script> 
		            </p>
		            <input type="hidden" id="institution1OldNameIds" name="institution1OldNameIds" />
                    <div id="institution1OldNameDiv" style="display:none;">
                        <span class="fieldlabel2"><label>Избрани стари имена</label></span> 
                        <table id="selected_institution1OldName" >
                            <tbody>
                            </tbody>
                        </table>
                        <div class="clr10"><!--  --></div>
                    </div>
                    <div class="clr"><!--  --></div>
                    
					<p><span class="fieldlabel2"><label for="institution1OldName">Старо име</label><br/></span>
		               	<input type="hidden" name="institution1OldNameId" id="institution1OldNameId" value=""/>
		               	<v:textinput id="institution1OldName" name="institution1OldName" value="" class="brd w500" onkeydown="if(!isSpecialKeyPressed(event)) nomenclatureChanged('institution1OldName');"/>
		               	<script type="text/javascript">
	                        new Autocomplete('institution1OldName', { 
	                              serviceUrl:'${pathPrefix}/control/professionalinstitutionsuggest?formername=1',
	                              width:500,
	                              onSelect:updateNomenclature,
	                              useBadQueriesCache:false,
	                              dynamicParameters: function (){
	                                  return {institution : $('institution1NameId').value};
	                              }
	                        });
	                        
	                    </script>
	                    <a href="#" onclick="if ($('institution1OldNameId').value == '') return false; addNomenclature('institution1OldName'); $('institution1Name').disable(); return false;">Добави</a> 
		            </p>
		            <div class="clr"><!--  --></div>
		
		     </fieldset>
        </div>
        <div class="clr10"><!--  --></div>
        <p class="flt_rgt" id="add_institutions"><a href="#" onclick="javascript:addInstitution(); return false;">Добави институция</a></p>
        <div class="clr10"><!--  --></div>    
         
         
        <div id="institution_div" class="university_class" style="display:none;">
            <fieldset><legend>Обучаваща институция</legend>      	
					<p class="flt_rgt"><a href="javascript:void(0);" style="color: red">Премахни</a></p>
					<div class="clr"><!--  --></div>
					<p><span class="fieldlabel2"><label for="institutionName">Ново име</label><br/></span>
		               	<input type="hidden" name="institutionNameId" id="institutionNameId" value=""/>
		               	<v:textinput id="institutionName" name="institutionName" value="" class="brd w500" onkeydown="if(!isSpecialKeyPressed(event)) nomenclatureChanged('institution');"/> 
		            </p>
		            <input type="hidden" id="institutionOldNameIds" name="institutionOldNameIds" />
                    <div id="institutionOldNameDiv" style="display:none;">
                        <span class="fieldlabel2"><label>Избрани стари имена</label></span> 
                        <table id="selected_institutionOldName" >
                            <tbody>
                            </tbody>
                        </table>
                        <div class="clr10"><!--  --></div>
                    </div>
                    <div class="clr"><!--  --></div>
                    
					<p><span class="fieldlabel2"><label for="institutionOldName">Старо име</label><br/></span>
		               	<input type="hidden" name="institutionOldNameId" id="institutionOldNameId" value=""/>
		               	<v:textinput id="institutionOldName" name="institutionOldName" value="" class="brd w500" onkeydown="if(!isSpecialKeyPressed(event)) nomenclatureChanged('institutionOldName');"/>
	                    <a href="#" >Добави</a> 
		            </p>
		            <div class="clr"><!--  --></div>
		     </fieldset>
        </div>
        
        
        
           
            <%--
            <fieldset><legend>${messages.University }</legend>
              
              <p class="flt_rgt"><a href="javascript:void(0);" style="color: red" onclick="javascript:$('university${status.index}_div').remove();">Премахни</a></p>
              
              <div class="clr"><!--  --></div>
              <p><span class="fieldlabel2"><label for="university${status.index }Country">${messages.Country }</label></span> 
                  <c:set target="${requestScope.countriesCombo}" property="selectedKey" value="${university.countryId}" />
                  <input type="hidden" name="_university${status.index }Country" id="_university${status.index }Country" value=""/>
                  <nacid:combobox id="university${status.index }Country" name="university${status.index }Country" attributeName="countriesCombo" style="w600 brd"  onchange="$(this).previous().value = $(this).value" />
              </p>
              <div class="clr"><!--  --></div>
                  <input type="hidden" id="university${status.index}Ids" name="university${status.index}Ids" />
                  <input type="hidden" id="university${status.index}NamesIds" name="university${status.index}NamesIds" />
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
                    <p>
                       <span class="fieldlabel2"><label>${messages.University }</label></span> 
                       <span id="university_span${status.index}"> 
                            <input type="hidden" name="university${status.index}Id" id="university${status.index}Id" value="" />
                            <v:textinput class="brd w500" name="university${status.index}" id="university${status.index}"  value="" onkeydown="if(!isSpecialKeyPressed(event)) nomenclatureChanged('university${status.index}');" />
                       </span>
                          <script type="text/javascript">
                        new Autocomplete('university${status.index}', { 
                              serviceUrl:'${pathPrefix}/control/universitysuggest',
                              width:500,
                              onSelect:updateNomenclature,
                              useBadQueriesCache:false,
                              dynamicParameters: function (){
                                  return {country_id : $('university${status.index }Country').value};
                              }
                        });
                        
                    </script> 
                       <a href="#" onclick="addNomenclature('university${status.index}'); $('university${status.index}Country').disable(); return false;">Добави</a>
                    </p>
              <div class="clr10"><!--  --></div>
            </fieldset>
        </div>
        
        <c:if test="${status.index == 0}">
            <div class="clr10"><!--  --></div>
            <p class="flt_rgt" id="add_universities"><a href="javascript:addUniversity();">Добави държава/висше училище/</a></p>
            <div class="clr10"><!--  --></div>
        </c:if>
    
    
        
    <div id="university_div" class="university_class" style="display:none;">
            <fieldset><legend>${messages.University }</legend>
              <p class="flt_rgt"><a href="javascript:void(0);" style="color: red">Премахни</a></p>
              <div class="clr"><!--  --></div>
              <p><span class="fieldlabel2"><label for="universityCountry">${messages.Country }</label></span> 
                  <c:set target="${requestScope.countriesCombo}" property="selectedKey" value="" />
                  <input type="hidden" name="_universityCountry" id="_universityCountry" value=""/>
                  <nacid:combobox id="universityCountry" name="universityCountry" attributeName="countriesCombo" style="w600 brd" onchange="$(this).previous().value = $(this).value" />
              </p>
              <div class="clr"><!--  --></div>
                  <input type="hidden" id="universityIds" name="universityIds" />
                  <input type="hidden" id="universityNamesIds" name="universityNamesIds" />
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
                    <p>
                       <span class="fieldlabel2"><label>${messages.University }</label></span> 
                       <span id="university_span"> 
                            <input type="hidden" name="universityId" id="universityId" value="" />
                            <v:textinput class="brd w500" name="university" id="university"  value="" onkeydown="if(!isSpecialKeyPressed(event)) nomenclatureChanged('university');" />
                       </span>
                       <a href="#" >Добави</a>
                    </p>
              <div class="clr10"><!--  --></div>
            </fieldset>
        </div>
        --%>
        <div id="afterInstitutionsDiv"><!--  --></div>
    </fieldset>
 