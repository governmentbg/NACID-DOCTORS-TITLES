<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid" %>
<%@ taglib uri="/META-INF/validation.tld" prefix="v" %>
<nacid:trainingCourseEdit>

    <h3 class="title"><span>Експерт</span></h3>

    <h3 class="names">${application_header }</h3>

    <div class="clr15"><!--  --></div>
    <p class="cc">
        <input class="back" type="button" onclick="document.location.href='${pathPrefix }/control/${back_screen}/list?application_type=${application_type}&getLastTableState=1';" value="Назад"/>
    </p>
    <script type="text/javascript">
        var applicationExperts = ${application_experts_size };
        function addApplicationExpert() {
            var el = $('application_expert_div').clone(true);
            el.id = el.id + applicationExperts;
            $('lastdiv').insert({before: el});
            el.show();
            /* Preobarzuva ID-tata i name=ovete na inputite/selectovete/textbox-a, kato im slaga po edno applicationExpertId v kraq*/
            //$$('#application_expert_div' + applicationExperts + ' input', '#application_expert_div' + applicationExperts + ' select', '#application_expert_div' + applicationExperts + ' textarea').each(function (element) {
            $('application_expert_div' + applicationExperts).descendants().each(function (element) {
                if (element.name != null) {
                    element.name = element.name.replace(/_ROW_ID_/g, applicationExperts);
                }
                if (element.getAttribute("for")!= null) {
                    element.setAttribute("for",element.getAttribute("for").replace(/_ROW_ID_/g, applicationExperts));
                }
                if (element.id != null) {
                    element.id = element.id.replace(/_ROW_ID_/g, applicationExperts);
                }
                if (element.getAttribute("onclick")!= null) {
                    element.setAttribute("onclick",element.getAttribute("onclick").replace(/_ROW_ID_/g, applicationExperts));
                }
                if (element.getAttribute("onkeydown")!= null) {
                    element.setAttribute("onkeydown",element.getAttribute("onkeydown").replace(/_ROW_ID_/g, applicationExperts));
                }

                /*if (element.name == 'application_expert_specialityId' || element.name == 'application_expert_qualificationId') {
                    //name-a stava raven na application_expert_specialityNId
                    element.name = element.name.replace('Id', applicationExperts + 'Id');
                    element.id = element.name;
                } else {
                    element.name = element.name + applicationExperts;
                    if (element.id != null) {
                        element.id = element.id + applicationExperts;
                    }
                }*/


            });
            /*Preobrazuva href-a na linka "premahni"*/
            $$('#application_expert_div' + applicationExperts + ' a[href="javascript:void(0);"]')[0].setAttribute('href', "javascript:$('application_expert_div" + applicationExperts + "').remove();void(0);");

            //$('application_expert_speciality' + applicationExperts).setAttribute("onkeydown", "if(!isSpecialKeyPressed(event)) nomenclatureChanged('application_expert_speciality" + applicationExperts + "');");

            new Autocomplete('applicationExpertSpeciality' + applicationExperts, {
                serviceUrl:'${pathPrefix}/control/specialitysuggest',
                width:600,
                useBadQueriesCache:false,
                onSelect:updateNomenclature
            });

            new Autocomplete('application_expert_qualification' + applicationExperts, {
                serviceUrl:'${pathPrefix}/control/flatnomenclaturesuggest?only_active=1&type=' + qualificationNomenclatureId,
                width:600,
                useBadQueriesCache:false,
                onSelect:updateNomenclature
            });

            applicationExperts++;
            $('application_experts_count').value = applicationExperts;



        }
        function updateExpertPositionLegalReasons(select) {
            var num = select.name.replace("application_expert_position", "");
            var expertPositionId = select.options[select.selectedIndex].value;
            var div = "application_expert_position_legal_reason_div" + num;
            if (expertPositionId == '-') {
                $(div).update("");
            } else {
                new Ajax.Request('${pathPrefix}/control/legal_reason_ajax?onlyActive=1&applicationType=${application_type}&expertPositionId=' + expertPositionId + "&elementName=application_expert_legal_reason" + num + "&style=w490 brd", {
                    onCreate : function(oXHR) {
                        setLoading($("div_form5"));
                    },
                    onSuccess: function(oXHR) {
                        $(div).update(oXHR.responseText);
                        removeLoading($("div_form5"));
                    },
                    onFailure : function(oXHR) {
                        alert("Възникна грешка при опит за прочитане на правните основания, свързани с този статус на заявление. Моля опитайте по-късно..." + oXHR.statusText);
                        $(div).update("");
                        removeLoading($("div_form5"));
                    },
                    method: 'GET'
                });
            }
        }
    </script>
    <v:form method="post" action="${pathPrefix }/control/applications_expert/save" name="appform5" ajax="ajaxUpdateForm(document.appform5);resetForm('appform5')" skipsubmitbuttons="true">


        <input id="application_experts_count" name="application_experts_count" value="${application_experts_size }" type="hidden"/>
        <input type="hidden" name="application_id" value="${application_id }"/>
        <fieldset>
            <legend>${messages.expert_assignment }</legend>
            <p class="flt_rgt"><a href="javascript:addApplicationExpert();">Добави експерт</a></p>

            <div class="clr">&nbsp;</div>
            <nacid:applicationExpert>
                <div id="application_expert_div${row_id}" style="display: block;">
                    <fieldset>
                        <legend>${messages.expert }</legend>
                        <p class="flt_rgt"><a href="javascript:void(0);" style="color: red" onclick="javascript:$('application_expert_div${row_id}').remove()">Премахни</a></p>

                        <fieldset><legend>${messages.expert_data }</legend>
                            <div class="clr">&nbsp;</div>
                            <p>
                                <span class="fieldlabel"><label for="application_expert_name${row_id }">Експерт</label></span>
                                <nacid:combobox name="application_expert${row_id }" id="application_expert_name${row_id }" attributeName="aeCombo" style="w490 brd"/>
                            </p>

                            <div class="clr"><!--  --></div>
                            <p>
                                <span class="fieldlabel"><label for="application_expert_notes${row_id }">${messages.notes }:</label></span>
                                <textarea class="brd w490" rows="3" cols="40" name="application_expert_notes${row_id }" id="application_expert_notes${row_id }">${application_expert_notes }</textarea>
                            </p>
                        </fieldset>
                        <div class="clr"><!--  --></div>
                        <fieldset><legend>Оценяване и становище</legend>
                            <p>
                                <span class="fieldlabel"><label for="application_expert_course_content${row_id }">${messages.courseContent }:</label></span>
                                <textarea class="brd w490" rows="6" cols="40" name="application_expert_course_content${row_id }" id="application_expert_course_content${row_id }">${application_expert_course_content }</textarea>
                            </p>

                            <div class="clr"><!--  --></div>
                            <p>
                                <span class="fieldlabel"><label for="application_expert_position${row_id }">${messages.expertPosition }:</label></span>
                                <nacid:combobox name="application_expert_position${row_id }" id="application_expert_position${row_id }" attributeName="expertPositionsCombo" style="w490 brd" onchange="updateExpertPositionLegalReasons(this);"/>
                            </p>
                            <p id="application_expert_position_legal_reason_div${row_id }">
                                <c:if test="${not empty expertLegalReasonsCombo }">
                                    <span class="fieldlabel"><label for="application_expert_legal_reason${row_id }">Правно основание</label></span>
                                    <nacid:combobox name="application_expert_legal_reason${row_id }" id="application_expert_legal_reason${row_id }" attributeName="expertLegalReasonsCombo" style="w490 brd"/>
                                </c:if>
                            </p>

                            <div class="clr"><!--  --></div>
                            <p>
                                <span class="fieldlabel"><label for="application_expert_edu_level_id${row_id }">${eduLevelLabel }:</label></span>
                                <nacid:combobox name="application_expert_edu_level_id${row_id }" id="application_expert_edu_level_id${row_id }" attributeName="ellCombo" style="w490 brd"/>
                            </p>

                            <div class="clr"><!--  --></div>
                            <div id="applicationExpertSpeciality_div${row_id}" style="${hideOnlyForDoctorateStyle}">
                                <p>
                                    <input type="hidden" name="applicationExpertSpeciality${row_id }Id" id="applicationExpertSpeciality${row_id }Id" value="" />

                                    <v:specialityinput id="applicationExpertSpeciality${row_id}" value="" comboAttribute="professionGroupCombo" class="brd w500"
                                                       chooseMultiple="true" specialityIdInput="$('applicationExpertSpeciality${row_id}Id')" specialityList="${applicationExpertSpecialities}"
                                                       specialityOnkeydown="if(!isSpecialKeyPressed(event)) nomenclatureChanged('applicationExpertSpeciality${row_id }');"
                                                       addOriginalSpeciality="false" />

                                    <!-- End of RayaWritten -->
                                    <script type="text/javascript">
                                        new Autocomplete('applicationExpertSpeciality${row_id }', {
                                            serviceUrl:'${pathPrefix}/control/specialitysuggest?only_active=1',
                                            width:600,
                                            onSelect:updateNomenclature
                                        });
                                    </script>
                                    <input type="hidden" id="applicationExpertSpeciality${row_id}_specialities_count" name="applicationExpertSpeciality${row_id}_specialities_count" value="${application_expert_specialities_count}" />


                                </p>
                                <div class="clr"><!--  --></div>
                            </div>

                            <div id="application_expert_qualification_div${row_id}" style="${hideOnlyForDoctorateStyle}">
                                <p>
                                    <span class="fieldlabel"><label for="application_expert_qualification${row_id }">${messages.Qualification }:</label></span>
                                    <input type="hidden" name="application_expert_qualification${row_id }Id" id="application_expert_qualification${row_id }Id" value="${application_expert_qualification_id}"/>
                                    <input class="brd w490" name="application_expert_qualification${row_id }" id="application_expert_qualification${row_id }" value="<c:out value="${application_expert_qualification}" />"  onkeydown="if(!isSpecialKeyPressed(event)) nomenclatureChanged('application_expert_qualificationy${row_id }');" />
                                    <script type="text/javascript">
                                        new Autocomplete('application_expert_qualification${row_id }', {
                                            serviceUrl:'${pathPrefix}/control/flatnomenclaturesuggest?only_active=1&type=' + qualificationNomenclatureId,
                                            width:600,
                                            useBadQueriesCache:false,
                                            onSelect:updateNomenclature
                                        });

                                    </script>
                                </p>

                                <div class="clr"><!--  --></div>
                            </div>
                            <p>
                                <span class="fieldlabel"><label for="application_expert_previous_board_decisions${row_id }">${previousBoardDecisionsLabel }:</label></span>
                                <textarea class="brd w490" rows="3" cols="40" name="application_expert_previous_board_decisions${row_id }" id="application_expert_previous_board_decisions${row_id }">${application_expert_previous_board_decisions}</textarea>
                            </p>

                            <div class="clr"><!--  --></div>
                            <p>
                                <span class="fieldlabel"><label for="application_expert_similar_bulgarian_programs${row_id }">${messages.similarBulgarianPrograms }:</label></span>
                                <textarea class="brd w490" rows="3" cols="40" name="application_expert_similar_bulgarian_programs${row_id }" id="application_expert_similar_bulgarian_programs${row_id }">${application_expert_similar_bulgarian_programs}</textarea>
                            </p>
                        </fieldset>
                        <div class="clr"><!--  --></div>
                        <p><span class="fieldlabel"><label>Статус</label></span>
                                ${applExpProcessStatTxt}
                        </p>

                        <div class="clr"><!--  --></div>
                        <p class="checkbox">
                            <span class="fieldlabel">&nbsp;</span>
                            <input name="application_expert_processStat${row_id }" value="1" ${applExpProcessStatVal} type="checkbox"/>
                            <label for="application_expert_processStat${row_id }">Експертът приключи работата по заявлението</label>
                        </p>

                        <div class="clr"><!--  --></div>
                    </fieldset>
                </div>
                <div class="clr"><!--  --></div>
            </nacid:applicationExpert>

            <nacid:applicationExpert type="empty">
                <div id="application_expert_div" style="display: none;">
                    <fieldset>
                        <legend>${messages.expert }</legend>
                        <p class="flt_rgt"><a href="javascript:void(0);" style="color: red">Премахни</a></p>

                        <div class="clr">&nbsp;</div>
                        <fieldset><legend>${messages.expert_data }</legend>
                            <p>
                                <span class="fieldlabel"><label for="application_expert_name_ROW_ID_">Експерт</label></span>
                                <nacid:combobox name="application_expert_ROW_ID_" id="application_expert_name_ROW_ID_" attributeName="applicationExpertsCombo" style="w490 brd"/>
                            </p>

                            <div class="clr"><!--  --></div>
                            <p><span class="fieldlabel"><label for="application_expert_notes_ROW_ID_">${messages.notes }:</label></span>
                                <textarea class="brd w490" rows="3" cols="40" name="application_expert_notes_ROW_ID_" id="application_expert_notes_ROW_ID_"></textarea>
                            </p>
                            <div class="clr"><!--  --></div>
                        </fieldset>
                        <fieldset><legend>Оценяване и становище</legend>
                            <p>
                                <span class="fieldlabel"><label for="application_expert_course_content_ROW_ID_">${messages.courseContent }:</label></span>
                                <textarea class="brd w490" rows="6" cols="40" name="application_expert_course_content_ROW_ID_" id="application_expert_course_content_ROW_ID_"></textarea>
                            </p>

                            <div class="clr"><!--  --></div>
                            <p>
                                <span class="fieldlabel"><label for="application_expert_position_ROW_ID_">${messages.expertPosition }:</label></span>
                                <nacid:combobox name="application_expert_position_ROW_ID_" id="application_expert_position_ROW_ID_" attributeName="expertPositionsCombo" style="w490 brd" onchange="updateExpertPositionLegalReasons(this);"/>
                            </p>
                            <p id="application_expert_position_legal_reason_div_ROW_ID_">
                            </p>

                            <div class="clr"><!--  --></div>
                            <p>
                                <span class="fieldlabel"><label for="application_expert_edu_level_id_ROW_ID_">${eduLevelLabel }:</label></span>
                                <nacid:combobox name="application_expert_edu_level_id" id="application_expert_edu_level_id_ROW_ID_" attributeName="eduLevelsCombo" style="w490 brd"/>
                            </p>

                            <div class="clr"><!--  --></div>
                            <div id="applicationExpertSpeciality_div_ROW_ID_" style="${hideOnlyForDoctorateStyle}">
                                <p>

                                    <input type="hidden" name="applicationExpertSpeciality_ROW_ID_Id" id="applicationExpertSpeciality_ROW_ID_Id" value="" />
                                    <v:specialityinput id="applicationExpertSpeciality_ROW_ID_" value="" comboAttribute="professionGroupCombo" class="brd w500"
                                                       chooseMultiple="true" specialityIdInput="$('applicationExpertSpeciality_ROW_ID_Id')"
                                                       specialityOnkeydown="if(!isSpecialKeyPressed(event)) nomenclatureChanged('applicationExpertSpeciality_ROW_ID_');"
                                                       addOriginalSpeciality="false" />
                                    <input type="hidden" id="applicationExpertSpeciality_ROW_ID__specialities_count" name="applicationExpertSpeciality_ROW_ID__specialities_count" value="0" />

                                </p>
                                <div class="clr"><!--  --></div>
                            </div>

                            <div id="application_expert_qualification_div_ROW_ID_" style="${hideOnlyForDoctorateStyle}">
                                <p>
                                    <span class="fieldlabel"><label for="application_expert_qualification_ROW_ID_">${messages.Qualification }:</label></span>
                                    <input type="hidden" name="application_expert_qualification_ROW_ID_Id" id="application_expert_qualification_ROW_ID_Id" value=""/>
                                    <input class="brd w490" name="application_expert_qualification_ROW_ID_" id="application_expert_qualification_ROW_ID_" value=""  />
                                </p>

                                <div class="clr"><!--  --></div>
                            </div>
                            <p>
                                <span class="fieldlabel"><label for="application_expert_previous_board_decisions_ROW_ID_">${previousBoardDecisionsLabel }:</label></span>
                                <textarea class="brd w490" rows="3" cols="40" name="application_expert_previous_board_decisions_ROW_ID_" id="application_expert_previous_board_decisions_ROW_ID_"></textarea>
                            </p>

                            <div class="clr"><!--  --></div>
                            <p>
                                <span class="fieldlabel"><label for="application_expert_similar_bulgarian_programs_ROW_ID_">${messages.similarBulgarianPrograms }:</label></span>
                                <textarea class="brd w490" rows="3" cols="40" name="application_expert_similar_bulgarian_programs_ROW_ID_" id="application_expert_similar_bulgarian_programs_ROW_ID_"></textarea>
                            </p>
                        </fieldset>
                        <div class="clr"><!--  --></div>
                        <p class="checkbox">
                            <span class="fieldlabel">&nbsp;</span>
                            <input name="application_expert_processStat_ROW_ID_" value="1" type="checkbox"/>
                            <label for="application_expert_processStat_ROW_ID_">Експертът приключи работата по заявлението</label>
                        </p>

                        <div class="clr"><!--  --></div>


                    </fieldset>
                </div>
                <div class="clr"><!--  --></div>
            </nacid:applicationExpert>
            <div class="clr20" id="lastdiv"><!--  --></div>
            <v:submit type="submit" value="Запис на данните"/>
            <div class="clr"><!--  --></div>
        </fieldset>

    </v:form>

    <div class="clr20"><!--  --></div>
    <nacid:list attribute="expertStatementTableWebModel" tablePrefix="expStatement"/>

    <c:if test="${expertStatementTableWebModel != null}">
        <script type="text/javascript">


            /*
             * маха линковете за сортиране на таблицата
             */
            $$('#expStatementmain_table td[class="dark"] > a').each(function (anchor) {
                anchor.up(0).innerHTML = anchor.innerHTML;
            });
            $$('#expStatementmain_table a[title="Преглед"]').each(function (link) {

                var cell = link.ancestors()[0];
                var row = cell.ancestors()[0];
                var fileName = (row.childElements())[4].innerHTML;


                var old = link.href;
                var ids = old.indexOf('id=') + 3;
                var id = old.substring(ids, old.length);
                link.href = '${pathPrefix }/control/expert_statement_attachment/' + fileName + '?id=' + id;

                var src = '${pathPrefix }/control/expert_statement_attachment/view?width=${messages.imgPreviewWidth }&id=' + id;
                (row.childElements())[6].innerHTML = '<img src="' + src + '" alt=""/>';

            });

            document.expStatementtableForm1.action = '${backUrlExpStatement }';
            <%--document.expStatementtableForm2.action = '${backUrlExpStatement }';--%>

        </script>
    </c:if>
</nacid:trainingCourseEdit>
<div class="clr20"><!-- --></div>

<c:choose>
    <c:when test="${showDocuments}">
        <nacid:applicationAttachmentEdit>
            <nacid:systemmessage name="attachmentStatusMessage" />


            <v:form action="${pathPrefix }/control/application_finalization/save?applicationId=${applicationId}&activeForm=5"
                    method="post" name="finalizationForm" skipSubmit="${not empty disableGeneration }"
                    onsubmit="startProgressUpdater();" enctype="multipart/form-data"
                    backurl="${pathPrefix }/control/${back_screen}/list?application_type=${application_type}&getLastTableState=1"
                    addGenerateButton="${(id == 0 || (id != 0 && secondApplicationAttchWebModel.id == 0)) && empty disableGeneration}" >

                <input id="id" type="hidden" name="id" value="${id }" />
                <input id="applicationId" type="hidden" name="applicationId" value="${applicationId }" />
                <input type="hidden" id="docflowNum" value="${docflowNum}" />
                <input type="hidden" id="docflowNum2" value="${secondApplicationAttchWebModel.docflowNum}" />

                <fieldset id="suggestion_fieldset" ${disableGeneration}><legend>${firstFileLabel}</legend>

                    <c:choose>
                        <c:when test="${id != 0}">
                            <div class="clr"><!--  --></div>
                            <c:if test="${docflowUrl != null && (docflowNum == null || docflowNum == '')}">
                                <div id="doc_flow_enter">
                                    <a class="flt_rgt" href="${pathPrefix }/${docflowUrl }" onclick="return docflowConfirmation()">Генерирай деловоден номер</a>
                                    <div class="clr"><!--  --></div>
                                </div>
                            </c:if>
                            <a class="flt_rgt" href="${pathPrefix }/control/application_finalization/${fileName }?id=${id }&amp;original=1" target="_blank">Свали прикачения файл</a>
                            <div class="clr"><!--  --></div>
                            <c:if test="${scannedFileName != null }" >
                                <a class="flt_rgt" href="${pathPrefix }/control/application_finalization/${scannedFileName }?id=${id }"
                                   target="_blank">Свали сканираното изображение</a>
                                <div class="clr"><!--  --></div>
                            </c:if>
                        </c:when>
                    </c:choose>

                    <input type="hidden" name="docflowUrl" value="${docflowUrl }" />
                    <c:if test="${docflowNum != null && docflowNum != ''}">
                        <p id="docflow_num"><span class="fieldlabel2"><label>Деловоден номер:</label><br /></span>
                                ${docflowNum }
                        </p>
                        <div class="clr"><!--  --></div>
                    </c:if>
                    <input type="hidden" value="${docType}" name="docTypeId" />

                    <div class="clr"><!--  --></div>
                    <p><span class="fieldlabel2"><label for="docDescr">Описание</label><br />
		</span> <textarea id="docDescr" class="brd w600" rows="3" cols="40"
                          name="docDescr">${docDescr }</textarea></p>
                    <div class="clr"><!--  --></div>
                    <p><span class="fieldlabel2"><label for="doc_content">Файл</label><br />
		</span> <input class="brd w240" name="doc_content" id="doc_content" type="file" value="" />&nbsp;&nbsp;${fileName }</p>
                    <div class="clr"><!--  --></div>

                    <c:if test="${id!=0}">
                        <p><span class="fieldlabel2"><label for="scanned_content">Сканирано изображение</label><br /></span>
                            <input class="brd w240" name="scanned_content" id="scanned_content" type="file" value="" />&nbsp;&nbsp;${scannedFileName }</p>
                        <div class="clr"><!--  --></div>
                    </c:if>

                </fieldset>

                <c:if test="${id != 0 && showSecondFileFrame}">
                    <div class="clr5"><!--  --></div>
                    <fieldset ${disableGeneration}><legend>${secondFileLabel}</legend>

                        <c:if test="${secondApplicationAttchWebModel.id != 0}">
                            <div class="clr"><!--  --></div>

                            <c:if test="${secondApplicationAttchWebModel.docflowUrl != null && (secondApplicationAttchWebModel.docflowNum == null || secondApplicationAttchWebModel.docflowNum == '')}">
                                <div id="doc_flow_enter2">
                                    <a class="flt_rgt" href="${pathPrefix }/${secondApplicationAttchWebModel.docflowUrl }" onclick="return docflowConfirmation()">Генерирай деловоден номер</a>
                                    <div class="clr"><!--  --></div>
                                </div>
                            </c:if>
                            <a class="flt_rgt" href="${pathPrefix }/control/application_finalization/${secondApplicationAttchWebModel.fileName }?id=${secondApplicationAttchWebModel.id }&amp;original=1" target="_blank">Свали прикачения файл</a>
                            <div class="clr"><!--  --></div>
                            <c:if test="${secondApplicationAttchWebModel.scannedFileName != null }" >
                                <a class="flt_rgt" href="${pathPrefix }/control/application_finalization/${secondApplicationAttchWebModel.scannedFileName }?id=${secondApplicationAttchWebModel.id }" target="_blank">Свали сканираното изображение</a>
                                <div class="clr"><!--  --></div>
                            </c:if>
                            <div class="clr2"><!--  --></div>
                        </c:if>

                        <c:if test='${secondApplicationAttchWebModel.docflowNum != null && secondApplicationAttchWebModel.docflowNum != "" }'>
                            <p><span class="fieldlabel2"><label>Деловоден номер:</label><br /></span>
                                    ${secondApplicationAttchWebModel.docflowNum }
                            </p>
                            <div class="clr"><!--  --></div>
                        </c:if>
                        <p><span class="fieldlabel2"><label for="docDescr">Описание</label><br /></span>
                            <textarea id="docDescr2" class="brd w600" rows="3" cols="40" name="docDescr2" >${secondApplicationAttchWebModel.docDescr}</textarea></p>
                        <div class="clr"><!--  --></div>
                        <p><span class="fieldlabel2"><label for="doc_content">Файл</label><br /></span>
                            <input class="brd w240" id="doc_content2" name="doc_content2" type="file" value="" />&nbsp;&nbsp;${secondApplicationAttchWebModel.fileName }</p>
                        <div class="clr"><!--  --></div>
                        <c:if test="${secondApplicationAttchWebModel.id != 0}">
                            <p><span class="fieldlabel2"><label for="scanned_content">Сканирано изображение</label><br /></span>
                                <input class="brd w240" id="scanned_content2" name="scanned_content2" type="file" value="" />&nbsp;&nbsp;${secondApplicationAttchWebModel.scannedFileName }</p>
                            <div class="clr"><!--  --></div>
                        </c:if>
                        <v:hidden id="id2" name="id2" value="${secondApplicationAttchWebModel.id }" />
                        <v:hidden id="docTypeId2" name="docTypeId2" value="${secondApplicationAttchWebModel.docType}" />
                        <v:hidden id="certNumber" name="certNumber" value="${certNumber}" />
                    </fieldset>
                </c:if>



                <nacid:loadingBar />

            </v:form>
        </nacid:applicationAttachmentEdit>
    </c:when>
    <c:otherwise>
        <div class="clr10"><!--  --></div>
        <p class="cc">
            <input class="back" type="button"
                   onclick="document.location.href='${pathPrefix }/control/${back_screen}/list?application_type=${application_type}&getLastTableState=1';"
                   value="Назад"/>
        </p>
    </c:otherwise>
</c:choose>
<c:if test="${showRasTransferButton || showRasTransferredMessage}">
    <div class="clr20"><!--  --></div>
    <form method="post" action="${pathPrefix }/control/ras_service/save" name="rasServiceForm" id="rasServiceForm">
        <fieldset><legend>Прехвърляне в регистър за академични длъжности и дисертации</legend>
            <c:if test="${showRasTransferredMessage}">
                Заявлението вече е прехвърлено в регистъра!
            </c:if>
            <c:if test="${showRasTransferButton}">
                <input type="hidden" name="application_id" value="${application_id }"/>
                <input type="button" value="Прехвърляне" name="sbmt" class="save" style="width:150px" onclick="if (confirm('Сигурни ли сте че желаете да прехвърлите заявлнието в регистъра за академични длъжности и дисертации?')) {$('rasServiceForm').submit();}">
            </c:if>
        </fieldset>
    </form>
</c:if>