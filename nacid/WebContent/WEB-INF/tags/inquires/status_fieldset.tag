<%@ tag pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ attribute name="addLegalReasons" required="true" rtexprvalue="true" type="java.lang.Boolean"%>
<fieldset><legend>Статус</legend>
    <p class="checkbox">
        <input id="only_actual_status0" name="only_actual_status0" value="1" type="checkbox"/>
        <label for="only_actual_status0">Търсене само в актуален статус</label>
    </p>
    <p>
        <span class="fieldlabel2"><label>Статус</label></span>
        <nacid:combobox id="status_id0" name="status_id0" attributeName="allStatusesCombo" style="brd w600" onchange="toggleInquireDates(this, 'status');"/>
    </p>
    <div id="status_dates_div0" style="display:none;">
        <p>
            <span class="fieldlabel2"><label>от дата</label></span>
            <v:dateinput name="statusDateFrom0" id="statusDateFrom0" value="дд.мм.гггг" style="brd w200 dateFrom" />
        </p>
        <div class="clr"><!--  --></div>
        <p>
            <span class="fieldlabel2"><label>до дата</label></span>
            <v:dateinput name="statusDateTo0" id="statusDateTo0" value="дд.мм.гггг" style="brd w200 dateTo" />
        </p>
        <div class="clr"><!--  --></div>
    </div>


    <div class="clr10">&nbsp;</div>
    <fieldset><legend>Актуален правен статус</legend>
        <p>
            <span class="fieldlabel2"><label>Актуален правен статус</label></span>
            <c:if test="${addLegalReasons}">
                <nacid:combobox id="final_status_id0" name="final_status_id0" attributeName="allLegalStatusesCombo" style="brd w600" onchange="_updateLegalReasons(this, $('InquireForm'), 'final');toggleInquireDates(this, 'final_status')"/>
            </c:if>
            <c:if test="${!addLegalReasons}">
                <nacid:combobox id="final_status_id0" name="final_status_id0" attributeName="allLegalStatusesCombo" style="brd w600" onchange="toggleInquireDates(this, 'final_status')"/>
            </c:if>

            <div id="final_status_dates_div0" style="display:none;">
                <p>
                    <span class="fieldlabel2"><label>от дата</label></span>
                    <v:dateinput name="finalStatusDateFrom0" id="finalStatusDateFrom0" value="дд.мм.гггг" style="brd w200 dateFrom" />
                </p>
                <div class="clr"><!--  --></div>
                <p>
                    <span class="fieldlabel2"><label>до дата</label></span>
                    <v:dateinput name="finalStatusDateTo0" id="finalStatusDateTo0" value="дд.мм.гггг" style="brd w200 dateTo" />
                </p>
                <div class="clr"><!--  --></div>
            </div>

            <div id="final_legal_reason_div0">
        </div>

        </p>
    </fieldset>
    <div class="clr10">&nbsp;</div>
    <p>
        <span class="fieldlabel2"><label>Актуален деловоден статус</label></span>
        <nacid:combobox id="docflow_status_id0" name="docflow_status_id0" attributeName="allDocflowStatusesCombo" style="brd w600" onchange="toggleInquireDates(this, 'docflow_status');"/>
    </p>
    <div id="docflow_status_dates_div0" style="display:none;">
        <p>
            <span class="fieldlabel2"><label>от дата</label></span>
            <v:dateinput name="docflowStatusDateFrom0" id="docflowStatusDateFrom0" value="дд.мм.гггг" style="brd w200 dateFrom" />
        </p>
        <div class="clr"><!--  --></div>
        <p>
            <span class="fieldlabel2"><label>до дата</label></span>
            <v:dateinput name="docflowStatusDateTo0" id="docflowStatusDateTo0" value="дд.мм.гггг" style="brd w200 dateTo" />
        </p>
        <div class="clr"><!--  --></div>

    </div>
</fieldset>
<div class="clr10"><!--  --></div>
<p class="flt_rgt" id="add_status"><a href="javascript:addStatus();">Добави статус</a></p>
<div class="clr10"><!--  --></div>

<div id="status" style="display:none;">
    <fieldset><legend>Статус</legend>
        <p class="flt_rgt"><a href="javascript:void(0);" style="color: red">Премахни</a></p>
        <div class="clr"><!--  --></div>
        <p class="checkbox">
            <input id="only_actual_status" name="only_actual_status" value="1" type="checkbox"/>
            <label for="only_actual_status">Търсене само в актуален статус</label>
        </p>
        <p>
            <span class="fieldlabel2"><label>Статус</label></span>
            <nacid:combobox id="status_id" name="status_id" attributeName="allStatusesCombo" style="brd w600" onchange="toggleInquireDates(this, 'status');" />
        </p>
        <div id="status_dates_div" style="display:none;">
            <p>
                <span class="fieldlabel2"><label>от дата</label></span>
                <v:dateinput name="statusDateFrom" id="statusDateFrom" value="дд.мм.гггг" style="brd w200 dateFrom" />
            </p>
            <div class="clr"><!--  --></div>
            <p>
                <span class="fieldlabel2"><label>до дата</label></span>
                <v:dateinput name="statusDateTo" id="statusDateTo" value="дд.мм.гггг" style="brd w200 dateTo" />
            </p>
            <div class="clr"><!--  --></div>
        </div>

        <div class="clr10">&nbsp;</div>
        <fieldset><legend>Актуален правен статус</legend>
            <p>
                <span class="fieldlabel2"><label>Актуален правен статус</label></span>
                <c:if test="${addLegalReasons}">
                    <nacid:combobox id="final_status_id" name="final_status_id" attributeName="allLegalStatusesCombo" style="brd w600" onchange="_updateLegalReasons(this, $('InquireForm'), 'final');toggleInquireDates(this, 'final_status')"/>
                </c:if>
                <c:if test="${!addLegalReasons}">
                    <nacid:combobox id="final_status_id" name="final_status_id" attributeName="allLegalStatusesCombo" style="brd w600" onchange="toggleInquireDates(this, 'final_status')"/>
                </c:if>
            </p>
            <div id="final_status_dates_div" style="display:none;">
                    <p>
                        <span class="fieldlabel2"><label>от дата</label></span>
                        <v:dateinput name="finalStatusDateFrom" id="finalStatusDateFrom" value="дд.мм.гггг" style="brd w200 dateFrom" />
                    </p>
                    <div class="clr"><!--  --></div>
                    <p>
                        <span class="fieldlabel2"><label>до дата</label></span>
                        <v:dateinput name="finalStatusDateTo" id="finalStatusDateTo" value="дд.мм.гггг" style="brd w200 dateTo" />
                    </p>
                    <div class="clr"><!--  --></div>

            </div>
            <div id="final_legal_reason_div"></div>
        </fieldset>
<div class="clr10">&nbsp;</div>
<p>
    <span class="fieldlabel2"><label>Актуален деловоден статус</label></span>
    <nacid:combobox id="docflow_status_id" name="docflow_status_id" attributeName="allDocflowStatusesCombo" style="brd w600" onchange="toggleInquireDates(this, 'docflow_status');"/>
</p>
<div id="docflow_status_dates_div" style="display:none;">
    <p>
        <span class="fieldlabel2"><label>от дата</label></span>
        <v:dateinput name="docflowStatusDateFrom" id="docflowStatusDateFrom" value="дд.мм.гггг" style="brd w200 dateFrom" />
    </p>
    <div class="clr"><!--  --></div>
    <p>
        <span class="fieldlabel2"><label>до дата</label></span>
        <v:dateinput name="docflowStatusDateTo" id="docflowStatusDateTo" value="дд.мм.гггг" style="brd w200 dateTo" />
    </p>
    <div class="clr"><!--  --></div>

</div>
</fieldset>

</div>
<div id="afterStatusDiv"><!--  --></div>