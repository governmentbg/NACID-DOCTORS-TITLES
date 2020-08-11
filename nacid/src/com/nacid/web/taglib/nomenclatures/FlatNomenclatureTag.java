package com.nacid.web.taglib.nomenclatures;

import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.web.MessagesBundle;
import com.nacid.web.ValidationStrings;
import com.nacid.web.WebKeys;
import com.nacid.web.model.nomenclatures.FlatNomenclatureWebModel;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class FlatNomenclatureTag extends SimpleTagSupport {

    public void doTag() throws JspException, IOException {
        FlatNomenclatureWebModel webmodel = (FlatNomenclatureWebModel) getJspContext().getAttribute(WebKeys.FLAT_NOMENCLATURE, PageContext.REQUEST_SCOPE);
        if (webmodel != null) {
            getJspContext().setAttribute("id", webmodel.getId());
            getJspContext().setAttribute("name", webmodel.getName());
            getJspContext().setAttribute("dateFrom", webmodel.getDateFrom());
            getJspContext().setAttribute("dateTo", webmodel.getDateTo());
            getJspContext().setAttribute("url", webmodel.getGroupName());
            getJspContext().setAttribute("nomenclatureName", webmodel.getNomenclatureName());
            int nomenclatureNameLength = 80;
            String validationString;
            String validationErrorMessage;
            switch (webmodel.getNomenclatureType()) {
                case NomenclaturesDataProvider.FLAT_NOMENCLATURE_QUALIFICATION:
                    nomenclatureNameLength = 255;
                    break;
                case NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_LEVEL:
                    nomenclatureNameLength = 100;
                    break;
                case NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSION:
                    nomenclatureNameLength = 600;
                    break;
                case NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_PROF_QUALIFICATION:
                    nomenclatureNameLength = 150;
                    break;
                case NomenclaturesDataProvider.FLAT_NOMENCLATURE_CERTIFICATE_PROF_QUALIFICATION:
                    nomenclatureNameLength = 150;
                    break;
                case NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_DOCUMENT_TYPE:
                    nomenclatureNameLength = 255;
                    break;
                case NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_SPECIALITY:
                    nomenclatureNameLength = 150;
                    break;
                case NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSION_EXPERIENCE_DOCUMENT_TYPE:
                    nomenclatureNameLength = 100;
                    break;
                case NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSIONAL_INSTITUTION_TYPE:
                    nomenclatureNameLength = 255;
                    break;
                case NomenclaturesDataProvider.FLAT_NOMENCLATURE_SECONDARY_CALIBER:
                    nomenclatureNameLength = 150;
                    break;
                case NomenclaturesDataProvider.FLAT_NOMENCLATURE_QUALIFICATION_DEGREE:
                    nomenclatureNameLength = 150;
                    break;
                case NomenclaturesDataProvider.FLAT_NOMENCLATURE_ORIGINAL_SPECIALITY:
                    nomenclatureNameLength = 255;
                    break;
                case NomenclaturesDataProvider.FLAT_NOMENCLATURE_GRADUATION_DOCUMENT_TYPE:
                    nomenclatureNameLength = 255;
                    break;
                default:
                    nomenclatureNameLength = 80;
            }


            switch (webmodel.getNomenclatureType()) {
                case NomenclaturesDataProvider.FLAT_NOMENCLATURE_ORIGINAL_SPECIALITY:
                case NomenclaturesDataProvider.FLAT_NOMENCLATURE_GRADUATION_DOCUMENT_TYPE:
                    validationString = ValidationStrings.getValidationStrings().get("allButQuote");
                    validationErrorMessage = MessagesBundle.getMessagesBundle().get("err_allButQuote");
                    break;
                default:
                    validationString = ValidationStrings.getValidationStrings().get("nomenclature_name_with_fullstop_digits");
                    validationErrorMessage = MessagesBundle.getMessagesBundle().get("err_nomenclature_name_with_fullstop_digits");
                    break;
            }

            getJspContext().setAttribute("nomenclatureNameLength", nomenclatureNameLength);
            getJspContext().setAttribute("validationString", validationString);
            getJspContext().setAttribute("validationErrorMessage", validationErrorMessage);
        }
        getJspBody().invoke(null);

    }
}
