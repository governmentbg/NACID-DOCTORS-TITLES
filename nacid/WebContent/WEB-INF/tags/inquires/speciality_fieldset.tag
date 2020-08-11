<%@ tag pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ attribute name="type" required="true" %>
<script type="text/javascript">
  var ${type}_count = 1;
</script>
<fieldset><legend>Специалности</legend>    
        <div id="${type }0_div" class="${type }_class" >
            <fieldset><legend>${messages.Speciality }</legend>
              <p class="flt_rgt"><a href="javascript:void(0);" style="color: red" onclick="javascript:$('${type }0_div').remove();">Премахни</a></p>
              
              <div class="clr"><!--  --></div>
              <p><span class="fieldlabel2"><label for="${type }0ProfGroup">Професионално направление</label></span> 
                  <input type="hidden" name="_${type }0ProfGroup" id="_${type }0ProfGroup" value=""/>
                  <nacid:combobox id="${type }0ProfGroup" name="${type }0ProfGroup" attributeName="profGroupCombo" style="w500 brd"  onchange="$(this).previous().value = $(this).value" />
              </p>
              <div class="clr"><!--  --></div>
                  <input type="hidden" id="${type }0Ids" name="${type }0Ids" />
                  <input type="hidden" id="${type }0NamesIds" name="${type }0NamesIds" />
                    <div id="${type }0Div">
                        <span class="fieldlabel2"><label>Избрани специалности</label></span> 
                        <table id="selected_${type }0" >
                            <tbody>
                            </tbody>
                        </table>
                        <div class="clr10"><!--  --></div>
                    </div>
                    <div class="clr"><!--  --></div>
                    <div id="${type }0NamesDiv">
                        <span class="fieldlabel2"><label>Избрани маски на специалности</label></span> 
                        <table id="selected_${type }0Names" >
                            <tbody>
                            </tbody>
                        </table>
                        <div class="clr10"><!--  --></div>
                    </div>
                    <p>
                       <span class="fieldlabel2"><label>${messages.Speciality }</label></span> 
                       <span id="${type }0_span"> 
                            <input type="hidden" name="${type }0Id" id="${type }0Id" value="" />
                            <v:textinput class="brd w500" name="${type }0" id="${type }0"  value="" onkeydown="if(!isSpecialKeyPressed(event)) nomenclatureChanged('${type }0');" />
                       </span>
                          <script type="text/javascript">
                        new Autocomplete('${type }0', { 
                              serviceUrl:'${pathPrefix}/control/specialitysuggest',
                              width:500,
                              onSelect:updateNomenclature,
                              useBadQueriesCache:false,
                              dynamicParameters: function (){
                                  return {group_id : $('${type }0ProfGroup').value};
                              }
                        });
                    </script> 
                       <a href="#" onclick="addNomenclature('${type }0'); $('${type }0ProfGroup').disable(); return false;">Добави</a>
                    </p>
              <div class="clr10"><!--  --></div>
            </fieldset>
        </div>
        
        
        <div class="clr10"><!--  --></div>
        <p class="flt_rgt" id="add_specialities"><a href="javascript:addSpeciality('${type }', ${type }_count);${type }_count++;void(0);">Добави направление/специалност/</a></p>
        <div class="clr10"><!--  --></div>
        

    
        
    <div id="${type }_div" class="${type }_class" style="display:none;">
            <fieldset><legend>${messages.Speciality }</legend>
              <p class="flt_rgt"><a href="javascript:void(0);" style="color: red">Премахни</a></p>
              <div class="clr"><!--  --></div>
              <p><span class="fieldlabel2"><label for="${type }ProfGroup">Професионално направление</label></span> 
                  <input type="hidden" name="_${type }ProfGroup" id="_${type }ProfGroup" value=""/>
                  <nacid:combobox id="${type }ProfGroup" name="${type }ProfGroup" attributeName="profGroupCombo" style="w500 brd" onchange="$(this).previous().value = $(this).value" />
              </p>
              <div class="clr"><!--  --></div>
                  <input type="hidden" id="${type }Ids" name="${type }Ids" />
                  <input type="hidden" id="${type }NamesIds" name="${type }NamesIds" />
                    <div id="${type }Div" style="display:none;">
                        <span class="fieldlabel2"><label>Избрани специалности</label></span> 
                        <table id="selected_${type }" >
                            <tbody>
                            </tbody>
                        </table>
                        <div class="clr10"><!--  --></div>
                    </div>
                    <div class="clr"><!--  --></div>
                    <div id="${type }NamesDiv" style="display:none;">
                        <span class="fieldlabel2"><label>Избрани маски на специалности</label></span> 
                        <table id="selected_${type }Names" >
                            <tbody>
                            </tbody>
                        </table>
                        <div class="clr10"><!--  --></div>
                    </div>
                    <p>
                       <span class="fieldlabel2"><label>${messages.Speciality }</label></span> 
                       <span id="${type }_span"> 
                            <input type="hidden" name="${type }Id" id="${type }Id" value="" />
                            <v:textinput class="brd w500" name="${type }" id="${type }"  value="" onkeydown="if(!isSpecialKeyPressed(event)) nomenclatureChanged('${type }');" />
                       </span>
                       <a href="#" >Добави</a>
                    </p>
              <div class="clr10"><!--  --></div>
            </fieldset>
        </div>
        <div id="after${type }Div"><!--  --></div>
        <input type="hidden" name="${type }_count" id="${type }_count" value="1" />
    </fieldset>
 <script type="text/javascript">
    showHideDiv('${type}0');
    showHideDiv('${type}0Names');
 </script>
