<%@ tag pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ attribute name="title" required="true"%>
<%@ attribute name="chosenTitle" required="true"%>
<%@ attribute name="labelTitle" required="true"%>
<%@ attribute name="serviceUrl" required="true" rtexprvalue="true"%>
<%@ attribute name="type" required="true" rtexprvalue="true"%>
<%@ attribute name="inputClass" required="false"%>

<fieldset><legend>${title }</legend>
     <input id="${type }Ids" type="hidden" name="${type }Ids" value="" />
     <input id="${type }NamesIds" type="hidden" name="${type }NamesIds" value="" />
     <div id="${type }Div" style="display:none;">
         <span class="fieldlabel2"><label>${chosenTitle}</label></span> 
         <table id="selected_${type }" >
             <tbody>
             </tbody>
         </table>
         <div class="clr10"><!--  --></div>
     </div>
     <div class="clr"><!--  --></div>
     <div id="${type }NamesDiv" style="display:none;">
         <span class="fieldlabel2"><label>Избрани маски</label></span> 
         <table id="selected_${type }Names" >
             <tbody>
             </tbody>
         </table>
         <div class="clr10"><!--  --></div>
     </div>
     <p>
        <span class="fieldlabel2"><label>${labelTitle }</label></span> 
        <span id="${type }_span"> 
         
             <input type="hidden" name="${type }Id" id="${type }Id" value="" />
             <v:textinput class="${not empty inputClass ? inputClass : 'brd w500'}" name="${type }" id="${type }"  value="" onkeydown="if(!isSpecialKeyPressed(event)) nomenclatureChanged('${type }');" />
        </span>
        <c:if test="${not empty serviceUrl}"> 
        <script type="text/javascript">
         new Autocomplete('${type }', { 
               serviceUrl:${serviceUrl},
               width:500,
               onSelect:updateNomenclature
         });
         
     	</script>
     	</c:if> 
        <a href="#" onclick= "addNomenclature('${type }'); return false;">Добави</a>
     </p>
</fieldset>