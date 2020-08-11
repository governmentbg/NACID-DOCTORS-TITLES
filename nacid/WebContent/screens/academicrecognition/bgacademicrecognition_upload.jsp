<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"  %>
<%@ include file="/screens/header.jsp" %>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<h3 class="title"><span>Въвеждане на нов файл</span></h3>

	<v:form action="${pathPrefix }/control/bgacademicrecognitionupload/edit" method="post" additionalvalidation="validateFile()"
			name="form_bgacademicrecognitionupload" backurl="${pathPrefix }/control/bgacademicrecognition/list?getLastTableState=1" enctype="multipart/form-data">

		<nacid:systemmessage />
		
		<div id="fileErrorDiv" style="display:none" class="error">
			Трябва да изберете файл за въвеждане
		</div>
		<v:comboBoxValidator input="recognizedUniversityId" required="true" />
		<v:textValidator input="inputNumber" maxLength="150" regex="${validations.allButQuote}" errormessage="${messages.err_allButQuote}" required="true" />
		<v:textValidator input="outputNumber" maxLength="150" regex="${validations.allButQuote}" errormessage="${messages.err_allButQuote}" required="true"/>
		<v:textValidator input="startRows" regex="${validations.number}" errormessage="${messages.err_number}"></v:textValidator>
		<v:textValidator input="startColumns" regex="${validations.number}" errormessage="${messages.err_number}"></v:textValidator>
		
		<fieldset><legend>Въвеждане на нов файл</legend>
	       <div class="clr"><!--  --></div>
           <p><span class="fieldlabel2"><label for="recognizedUniversityId">Български университет, признал дипломата</label><br/></span>
                <nacid:combobox name="recognizedUniversityId" attributeName="recognizedUniversities" id="recognizedUniversityId" style="brd w500"/>
           </p>
           <div class="clr"><!--  --></div>
           <p><span class="fieldlabel2"><label for="inputNumber">Входящ номер</label><br/></span>
               <v:textinput id="inputNumber" class="brd w240" name="inputNumber" path="inputNumber" />
           </p>
           <div class="clr"><!--  --></div>
           <p><span class="fieldlabel2"><label for="outputNumber">Изходящ номер</label><br/></span>
               <v:textinput id="outputNumber" class="brd w240" name="outputNumber" path="outputNumber" />
           </p>
           <div class="clr"><!--  --></div>
           <p><span class="fieldlabel2"><label for="startRows">Започни от ред номер</label><br/></span>
               <v:textinput id="startRows" class="brd w240" name="startRows" path="startRows" />
           </p>
           <div class="clr"><!--  --></div>
           <p><span class="fieldlabel2"><label for="startColumns">Започни от колона номер</label><br/></span>
               <v:textinput id="startColumns" class="brd w240" name="startColumns" path="startColumns" />
           </p>
           <div class="clr"><!--  --></div>
           <p><span class="fieldlabel"><label for="doc_content">Файл</label></span>
           <input class="brd w240" name="doc_content" id="doc_content" type="file" value="" /></p>
           <div class="clr"><!--  --></div>
		</fieldset>

	</v:form>
	
	<script type="text/javascript">	
		function validateFile(){
			if($("doc_content").value == ''){
				$("fileErrorDiv").show();
				return false;
			} else {
				return true;
			}
		}
	</script>

<%@ include file="/screens/footer.jsp" %>