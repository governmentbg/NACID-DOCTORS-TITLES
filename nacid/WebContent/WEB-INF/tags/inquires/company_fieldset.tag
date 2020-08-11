<%@ tag pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<fieldset><legend>Заявител - Юридическо лице</legend>
    <script type="text/javascript">
        function updateCompanyData(value, data, el) {
            updateNomenclature(value, data, el);
            var d = data.evalJSON(true);
            el.value = d.name;
        }
    </script>
    <div class="clr"><!--  --></div>
      <input type="hidden" id="companyIds" name="companyIds" />
      <input type="hidden" id="companyNamesIds" name="companyNamesIds" />
        <div id="companyDiv">
            <span class="fieldlabel2"><label>Избрани юридически лица</label></span>
            <table id="selected_company" >
                <tbody>
                </tbody>
            </table>
            <div class="clr10"><!--  --></div>
        </div>
        <div class="clr"><!--  --></div>
        <div id="companyNamesDiv">
            <span class="fieldlabel2"><label>Избрани маски на юридически лица</label></span>
            <table id="selected_companyNames" >
                <tbody>
                </tbody>
            </table>
            <div class="clr10"><!--  --></div>
        </div>
        <p>
            <span class="fieldlabel2"><label>Вид</label></span>
            <input type="checkbox" value="2" name="companyApplicantType" id="companyApplicantType_2">
            <label for="companyApplicantType_2">Юридическо лице</label>
            <input type="checkbox" value="5" checked="checked" name="companyApplicantType" id="companyApplicantType_5">
            <label for="companyApplicantType_5">Университет</label>
        </p>
        <p>
           <span class="fieldlabel2"><label>Име</label></span>
           <span id="company_span">
                <input type="hidden" name="companyId" id="companyId" value="" />
                <v:textinput class="brd w500" name="company" id="company"  value="" onkeydown="if(!isSpecialKeyPressed(event)) nomenclatureChanged('company');" />
           </span>
              <script type="text/javascript">
                    new Autocomplete('company', {
                          serviceUrl:'${pathPrefix}/control/companysuggestion',
                          width:500,
                          onSelect:updateCompanyData,
                          useBadQueriesCache:false,
                          dynamicParameters: function (){
                              return {type:'name'};
                          }
                    });

              </script>
            <a href="#" onclick="addNomenclature('company'); return false;">Добави</a>
        </p>
    <div class="clr10"><!--  --></div>
    
    </fieldset>
