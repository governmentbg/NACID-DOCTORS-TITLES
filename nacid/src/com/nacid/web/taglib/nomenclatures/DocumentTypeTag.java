package com.nacid.web.taglib.nomenclatures;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.bl.impl.Utils;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.model.nomenclatures.DocumentTypeWebModel;

public class DocumentTypeTag extends SimpleTagSupport {

	public void doTag() throws JspException, IOException {
		DocumentTypeWebModel webmodel = (DocumentTypeWebModel) getJspContext().getAttribute( WebKeys.DOCUMENT_TYPE, PageContext.REQUEST_SCOPE);
		if (webmodel != null) {
			getJspContext().setAttribute("id", webmodel.getId());
			getJspContext().setAttribute("name", webmodel.getName());
			getJspContext().setAttribute("dateFrom", webmodel.getDateFrom());
			getJspContext().setAttribute("dateTo", webmodel.getDateTo());
			getJspContext().setAttribute("documentTemplate", webmodel.getDocumentTemplate());

		} else {
			getJspContext().setAttribute("id", "");
			getJspContext().setAttribute("name", "");
			getJspContext().setAttribute("dateFrom", DataConverter.formatDate(Utils.getToday()));
			getJspContext().setAttribute("dateTo", "дд.мм.гггг");
			getJspContext().setAttribute("documentTemplate", "");
		}
		getJspBody().invoke(null);
	}
}
