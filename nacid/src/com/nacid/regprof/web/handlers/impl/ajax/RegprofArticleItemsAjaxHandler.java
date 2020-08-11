package com.nacid.regprof.web.handlers.impl.ajax;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nacid.bl.OrderCriteria;
import com.nacid.bl.impl.nomenclatures.regprof.RegprofArticleItemImpl;
import com.nacid.bl.nomenclatures.NomenclatureOrderCriteria;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.RegprofArticleItem;
import com.nacid.data.DataConverter;
import com.nacid.regprof.web.handlers.RegProfBaseRequestHandler;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.model.common.ComboBoxWebModel;

//RayaWritten-------------------------------------------------
public class RegprofArticleItemsAjaxHandler extends RegProfBaseRequestHandler{
    public RegprofArticleItemsAjaxHandler(ServletContext servletContext) {
        super(servletContext);
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        NomenclaturesDataProvider nomDp = getNacidDataProvider().getNomenclaturesDataProvider();
        String type = request.getParameter("type");
        Boolean addInactive = DataConverter.parseBoolean(request.getParameter("addinactive"));
        generateArticleItemCombo(request, response, nomDp, 0, addInactive, WebKeys.ARTICLE_ITEM_COMBO);
        
        if ("inquire".equals(type)) {
            request.setAttribute(WebKeys.NEXT_SCREEN, "inquire_directive_item_ajax");
        } else {
            request.setAttribute(WebKeys.NEXT_SCREEN, "article_item_ajax");    
        }

    }

    public static void generateArticleItemCombo(HttpServletRequest request, HttpServletResponse response, NomenclaturesDataProvider nomDp, Integer itemId, boolean addInactive, String comboName){        
        RegprofArticleItem item = null;
        if(itemId != null){
            item = nomDp.getRegprofArticleItem(itemId);
        }
        Integer articleId = DataConverter.parseInteger(request.getParameter("articleId"), null);
        if(articleId == null && item != null && item.getArticleDirectiveId() != null){
            articleId = item.getArticleDirectiveId();
        } else if( articleId == null && item == null){
            request.setAttribute(comboName, new ComboBoxWebModel(null, true));
            return;
        }
        if(articleId != null){
            List<RegprofArticleItem> items = nomDp.getRegprofArticleItems(null, OrderCriteria
                    .createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, false),articleId);
            if (items != null) {
                ComboBoxUtils.generateNomenclaturesComboBox(itemId, items, addInactive ? false : true, request, comboName, true);    
            } else {
                ComboBoxWebModel comboBox = new ComboBoxWebModel(null, true);
                request.setAttribute(comboName, comboBox);    
            }
            
        }
    }

}
//-----------------------------------------------------------------
