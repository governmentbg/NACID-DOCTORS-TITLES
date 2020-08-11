package com.nacid.web.taglib.table;

import com.nacid.bl.users.User;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.UserOperationsUtils;
import com.nacid.web.model.table.TableRowWebModel;
import com.nacid.web.model.table.TableWebModel;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;


public class TableOperationAccessTag extends SimpleTagSupport {
	
	private int operationId;
	public void setOperationName(String operationName) {
		this.operationId = UserOperationsUtils.getOperationId(operationName);
	}

	public void doTag() throws JspException, IOException {
	    
	    User user = (User) getJspContext().getAttribute(WebKeys.LOGGED_USER, PageContext.SESSION_SCOPE);
        TableTag parent = (TableTag) findAncestorWithClass(this, TableTag.class);
        TableWebModel table = parent == null ? null : parent.getWebModel();
        
	    
        Boolean dontCheck = (Boolean)getJspContext().getAttribute(
                WebKeys.DONT_CHECK_TABLE_USER_OPERATION_ACCESS, PageContext.REQUEST_SCOPE);
	    if(dontCheck != null && dontCheck.booleanValue()) {
	        if(!table.isOperationIdHidden(operationId)) {
	            getJspBody().invoke(null);
	        }
	        return;
	    }
	    
		
		TableRowWebModel currentRow = parent == null ? null : parent.getWebModel().getRow(parent.getCurrentRowId());
	    Integer groupId = (Integer) getJspContext().getAttribute(WebKeys.GROUP_ID, PageContext.REQUEST_SCOPE);
	    Integer webApplicationId = (Integer) ((PageContext)getJspContext()).getServletContext().getAttribute(WebKeys.WEB_APPLICATION_ID);
		if (user == null || groupId == null || table == null || 
				(currentRow != null && operationId == UserOperationsUtils.OPERATION_LEVEL_EDIT && !currentRow.isEditable()) //Operaciqta e edit, no reda e markiran kato not editable
		        || (currentRow != null && operationId == UserOperationsUtils.OPERATION_LEVEL_DELETE && !currentRow.isDeletable())//Operaciqta e delete, no reda e markiran kato not deletable
                || (currentRow != null && operationId == UserOperationsUtils.OPERATION_LEVEL_VIEW && !currentRow.isViewable())//Operaciqta e view, no reda e markiran kato not viewable
                ) {
			return;
		}
		if (user.hasAccess(groupId, operationId, webApplicationId) && !table.isOperationIdHidden(operationId)) {
			getJspBody().invoke(null);
		} else {
			return;
		}
	}

	
}
