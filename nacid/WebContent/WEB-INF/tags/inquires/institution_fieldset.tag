<%@ tag pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<fieldset><legend>Институция, провела обучението</legend>
    <c:forEach items="${institutions}" var="institution" varStatus="status">
        <div id="institution${status.index }_div" class="institution_class" >
            <fieldset><legend>Институция, провела обучението</legend>
              
              <p class="flt_rgt"><a href="javascript:void(0);" style="color: red" onclick="javascript:$('institution${status.index}_div').remove();">Премахни</a></p>
              
              <div class="clr"><!--  --></div>
              <p><span class="fieldlabel2"><label for="institution${status.index }Country">${messages.Country }</label></span> 
                  <c:set target="${requestScope.countriesCombo}" property="selectedKey" value="${institution.countryId}" />
                  <input type="hidden" name="_institution${status.index }Country" id="_institution${status.index }Country" value=""/>
                  <nacid:combobox id="institution${status.index }Country" name="institution${status.index }Country" attributeName="countriesCombo" style="w500 brd"  onchange="$(this).previous().value = $(this).value" />
              </p>
              <div class="clr"><!--  --></div>
                  <input type="hidden" id="institution${status.index}Ids" name="institution${status.index}Ids" />
                  <input type="hidden" id="institution${status.index}NamesIds" name="institution${status.index}NamesIds" />
                    <div id="institution${status.index}Div">
                        <span class="fieldlabel2"><label>Избрани институции</label></span> 
                        <table id="selected_institution${status.index}" >
                            <tbody>
                            </tbody>
                        </table>
                        <div class="clr10"><!--  --></div>
                    </div>
                    <div class="clr"><!--  --></div>
                    <div id="institution${status.index}NamesDiv">
                        <span class="fieldlabel2"><label>Избрани маски на институции</label></span> 
                        <table id="selected_institution${status.index}Names" >
                            <tbody>
                            </tbody>
                        </table>
                        <div class="clr10"><!--  --></div>
                    </div>
                    <p>
                       <span class="fieldlabel2"><label>Институция, провела обучението</label></span> 
                       <span id="institution_span${status.index}"> 
                            <input type="hidden" name="institution${status.index}Id" id="institution${status.index}Id" value="" />
                            <v:textinput class="brd w500" name="institution${status.index}" id="institution${status.index}"  value="" onkeydown="if(!isSpecialKeyPressed(event)) nomenclatureChanged('institution${status.index}');" />
                       </span>
                          <script type="text/javascript">
                        new Autocomplete('institution${status.index}', { 
                              serviceUrl:'${pathPrefix}/control/institutionsuggest',
                              width:500,
                              onSelect:updateNomenclature,
                              useBadQueriesCache:false,
                              dynamicParameters: function (){
                                  return {country_id : $('institution${status.index }Country').value};
                              }
                        });
                        
                    </script> 
                       <a href="#" onclick="addNomenclature('institution${status.index}'); $('institution${status.index}Country').disable(); return false;">Добави</a>
                    </p>
              <div class="clr10"><!--  --></div>
            </fieldset>
        </div>
        
        <c:if test="${status.index == 0}">
            <div class="clr10"><!--  --></div>
            <p class="flt_rgt" id="add_institutions"><a href="javascript:addInstitution();">Добави държава/институция, провела обучението/</a></p>
            <div class="clr10"><!--  --></div>
        </c:if>
    </c:forEach>
    
        
    <div id="institution_div" class="institution_class" style="display:none;">
            <fieldset><legend>Институция, провела обучението</legend>
              <p class="flt_rgt"><a href="javascript:void(0);" style="color: red">Премахни</a></p>
              <div class="clr"><!--  --></div>
              <p><span class="fieldlabel2"><label for="institutionCountry">${messages.Country }</label></span>
                  <c:set target="${requestScope.countriesCombo}" property="selectedKey" value="" />
                  <input type="hidden" name="_institutionCountry" id="_institutionCountry" value=""/>
                  <nacid:combobox id="institutionCountry" name="institutionCountry" attributeName="countriesCombo" style="w500 brd" onchange="$(this).previous().value = $(this).value" />
              </p>
              <div class="clr"><!--  --></div>
                  <input type="hidden" id="institutionIds" name="institutionIds" />
                  <input type="hidden" id="institutionNamesIds" name="institutionNamesIds" />
                    <div id="institutionDiv" style="display:none;">
                        <span class="fieldlabel2"><label>Избрани институции</label></span>
                        <table id="selected_institution" >
                            <tbody>
                            </tbody>
                        </table>
                        <div class="clr10"><!--  --></div>
                    </div>
                    <div class="clr"><!--  --></div>
                    <div id="institutionNamesDiv" style="display:none;">
                        <span class="fieldlabel2"><label>Избрани маски на институции</label></span>
                        <table id="selected_institutionNames" >
                            <tbody>
                            </tbody>
                        </table>
                        <div class="clr10"><!--  --></div>
                    </div>
                    <p>
                       <span class="fieldlabel2"><label>Институция, провела обучението</label></span> 
                       <span id="institution_span">
                            <input type="hidden" name="institutionId" id="institutionId" value="" />
                            <v:textinput class="brd w500" name="institution" id="institution"  value="" onkeydown="if(!isSpecialKeyPressed(event)) nomenclatureChanged('institution');" />
                       </span>
                       <a href="#" >Добави</a>
                    </p>
              <div class="clr10"><!--  --></div>
            </fieldset>
        </div>
        <div id="afterInstitutionDiv"><!--  --></div>
    </fieldset>
