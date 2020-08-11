<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"  %>
<%@ include file="/screens/header.jsp" %>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<h3 class="title"><span>Въвеждане на нов файл с признати дипломи от български университети</span></h3>
	<v:form action="${pathPrefix }/control/bgacademicrecognitionupload/save" method="post" 
            name="form_bgacademicrecognitionupload" submitBtnText="Въведи и публикувай" backurl="${pathPrefix }/control/bgacademicrecognitionupload/back">
            <input type="hidden" value="1" name="save_bg_academic_recognition_records"/>
            <div id="tableContainer">
            	<nacid:list skipFilters="true"/>
            </div>
    </v:form>


	<div id="view_similar_modal" style="display: none">
		<div id="similar_success" style="display:none" class="correct">Операцията беше извършена успешно</div>
		<div id="similar_choose" style="display:none" class="error">Изберете подобна диплома</div>
		<div id="similar_error" style="display:none" class="error">Възникна проблем при изпълнение на операцията</div>
		<div id="view_similar_modal_content">
			
		</div>
		<div>
			<button id="saveSimilarBtn"  onclick="saveNewRelatedId()">Анулирай</button>
			<button onclick="closeModal()">Затвори</button>
		</div>
	</div>


	<script type="text/javascript">	
		init();	
	
		function init(){
			$$('#main_table a[title="Редактиране"]').each(function(link) {
	
				var cell = link.ancestors()[0];
				var row = cell.ancestors()[0];
				var checkbox = row.childElements()[13].childElements("input")[0];
				var checked = checkbox.readAttribute("checked");
				if(checked == "checked"){
					row.addClassName("error");
				} else {
					row.addClassName("correct");
				}
				var index = row.id.substring(row.id.indexOf('_')+1, row.id.length);
				link.href = 'javascript:executeSimilarFetch('+index+")";
		
			});
			
			$$('#main_table a[title="Изтриване"]').each(function(link) {
	
				var cell = link.ancestors()[0];
				var row = cell.ancestors()[0];
				var index = row.id.substring(row.id.indexOf('_')+1, row.id.length);
				link.href = 'javascript:removeUploadedRow('+index+")";
		
			});
		}
		
		function executeSimilarFetch(index){
			$('similar_success').hide();
			$('similar_error').hide();
			$('similar_choose').hide();
			new Ajax.Updater('view_similar_modal_content', '/nacid/control/bgacademicrecognitionupload_ajax/edit', {
				parameters: {index: index} ,
				onSuccess: function(transport){
					$('view_similar_modal').show();
				},
				onComplete: function(transport){
					if (200 == transport.status){
						setInitialChosen();
						var checkboxCount = $$(".similar_checkbox").length;
						if(checkboxCount <=0){
							$("saveSimilarBtn").hide();
						} else {
							$("saveSimilarBtn").show();
						}
					}
					window.scroll(window.top);
				},
				onFailure: function(){
					alert("error");
				}
			});
		}
		
		function saveNewRelatedId(){
			var id = $("similarSelectedHidden").value;
			var indexVal = $("similarOfIndex").value;
			if(id == ''){
				$('similar_choose').show();
				return;
			}
			new Ajax.Updater('view_similar_modal_content', '/nacid/control/bgacademicrecognitionupload_ajax/save', {
				parameters: {relatedId: id, index: indexVal } ,
				onSuccess: function(transport){
					$('view_similar_modal').hide();
				}, 
				onComplete: function(transport){
					if (200 == transport.status){
						setInitialChosen();
					}
				},
				onFailure: function(){
					$('similar_success').hide();
					$('similar_error').show();
					$('similar_choose').hide();
				}
			});
		}
		
		function closeModal(){
			$('view_similar_modal').hide();
		}
		
		function setInitialChosen(){
			var val = $("similarSelectedHidden").value;	
			$$(".similar_checkbox").each(function(ch){
				if(ch.value+"" == val+""){
					ch.checked = true;
				}
			});
		}
		
		function setChosen(newVal){
			$("similarSelectedHidden").value = newVal;
			setChosenChecked(newVal);
		}
		
		function setChosenChecked(chosen){
			$$(".similar_checkbox").each(function(ch){
				if(ch.value+"" == chosen+""){
					if(ch.checked == true){
						$("similarSelectedHidden").value = chosen;
					} else {
						$("similarSelectedHidden").value = "";
					}
				} else {
					ch.checked = false;
				}
			});
		}
		
		function removeUploadedRow(indexVal){
			new Ajax.Updater('tableContainer', '/nacid/control/bgacademicrecognitionupload_ajax/delete', {
				parameters: {index: indexVal } ,
				onSuccess: function(transport){
				}, 
				onComplete: function(transport){
					if (200 == transport.status){
						init();
					}
				},
				onFailure: function(){
					alert("Възникна грешка");
				}
			});
		}

	</script>
<%@ include file="/screens/footer.jsp" %>