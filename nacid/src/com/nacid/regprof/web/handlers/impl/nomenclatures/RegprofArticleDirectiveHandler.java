package com.nacid.regprof.web.handlers.impl.nomenclatures;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclatureOrderCriteria;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.RegprofArticleDirective;
import com.nacid.bl.nomenclatures.regprof.RegprofArticleItem;
import com.nacid.bl.table.*;
import com.nacid.data.DataConverter;
import com.nacid.db.utils.StandAloneDataSource;
import com.nacid.regprof.web.handlers.RegProfBaseRequestHandler;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.handlers.impl.nomenclatures.FlatNomenclatureHandler;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

//RayaWritten----------------------------------------------------------------------
public class RegprofArticleDirectiveHandler extends RegProfBaseRequestHandler {
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_NAME = "Наименование";
    private static final String COLUMN_NAME_ITEMS = "Точки и букви";
    private static final String COLUMN_NAME_DATE_FROM = "От дата";
    private static final String COLUMN_NAME_DATE_TO = "До дата";

    public RegprofArticleDirectiveHandler(ServletContext servletContext) {
        super(servletContext);
    }

    public static void main(String[] args) {
        NacidDataProviderImpl n = new NacidDataProviderImpl(new StandAloneDataSource());
        NomenclaturesDataProvider nom = n.getNomenclaturesDataProvider();
        RegprofArticleDirective article = nom.getRegprofArticleDirectiveWithItems(11);
        for (RegprofArticleItem i : article.getArticleItems()) {
            Date d = Utils.getToday();
            System.out.println(i.getDateTo().getTime() == d.getTime());
            System.out.println(i.isActive());
        }

    }

    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        generateTable(request);
    }

    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        Integer id = DataConverter.parseInteger(request.getParameter("id"), null);
        if (id == null) {
            throw new UnknownRecordException("Unknown Article Directive ID:" + id);
        }
        request.setAttribute(WebKeys.NEXT_SCREEN, "article_directive_edit");
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        RegprofArticleDirective article = nomenclaturesDataProvider.getRegprofArticleDirectiveWithItems(id);
        if (article == null) {
            throw new UnknownRecordException("Unknown Article Directive ID:" + id);
        }
        request.setAttribute(WebKeys.REGPROF_ARTICLE_DIRECTIVE, article);
    }

    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(WebKeys.NEXT_SCREEN, "article_directive_edit");
    }

    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        int id = DataConverter.parseInt(request.getParameter("id"), 0);

        String name = request.getParameter("name");
        Date dateFrom = DataConverter.parseDate(request.getParameter("dateFrom"));

        // If new record is added and dateFrom is not set, then dateFrom is set
        // to today
        if (id == 0 && dateFrom == null) {
            dateFrom = Utils.getToday();
        }
        Date dateTo = DataConverter.parseDate(request.getParameter("dateTo"));
        Integer nomenclatureId = NomenclaturesDataProvider.FLAT_NOMENCLATURE_REGPROF_ARTICLE_DIRECTIVE;
        if (id != 0 && nomenclaturesDataProvider.getFlatNomenclature(nomenclatureId, id) == null) {
            throw new UnknownRecordException("Unknown article directive ID:" + id);
        }

        if (name == null || "".equals(name)) {
            SystemMessageWebModel webmodel = new SystemMessageWebModel("Грешно въведени данни!");
            webmodel.addAttribute("- грешно въведено име!");
            request.setAttribute(WebKeys.SYSTEM_MESSAGE, webmodel);
            RegprofArticleDirective article = nomenclaturesDataProvider.getRegprofArticleDirectiveWithItems(id);
            if (article == null) {
                throw new UnknownRecordException("Unknown Article Directive ID:" + id);
            }
            request.setAttribute(WebKeys.REGPROF_ARTICLE_DIRECTIVE, article);
        } else {
            int newId = nomenclaturesDataProvider.saveFlatNomenclature(nomenclatureId, id, name, dateFrom, dateTo);

            //Dealing with the article items - creating objects for the items and saving all
            String[] articleItems = request.getParameterValues("itemUnit");
            String[] articleDeleteItems = request.getParameterValues("itemDeleteUnit");
            String singleItemName = request.getParameter("item");
            List<String> itemNames = new ArrayList<String>();
            List<String> qualificationLevelLabels = new ArrayList<>();
            if (articleItems != null) {
                itemNames = new ArrayList<String>(Arrays.asList(articleItems));
                qualificationLevelLabels = new ArrayList<>(Arrays.asList(request.getParameterValues("qll")));
            }
            if (singleItemName != null && singleItemName != "" && !itemNames.contains(singleItemName)) {
                itemNames.add(singleItemName);
                qualificationLevelLabels.add(request.getParameter("qualification_level_label"));
            }

            for (int cnt = 0; cnt < itemNames.size(); cnt++) {
                String i = itemNames.get(cnt);
                String q = qualificationLevelLabels.get(cnt);
                String idString = i.substring(0, i.lastIndexOf('_') != -1 ? i.lastIndexOf('_') : 0);
                int itemId = DataConverter.parseInt(idString, 0);
                logger.debug("item id (save/update): " + itemId);
                String itemName = i.substring(i.lastIndexOf('_') != -1 ? i.lastIndexOf('_') + 1 : 0, i.length());
                String qll = q.substring(q.lastIndexOf('_') != -1 ? q.lastIndexOf('_') + 1 : 0, q.length());
                if (StringUtils.isEmpty(qll)) {
                    qll = null;
                }
                if (itemId == 0) {
                    nomenclaturesDataProvider.saveRegprofArticleItem(itemId, itemName, newId, qll, dateFrom, null);
                }
            }
            //Save the new items
            for (String i : itemNames) {

            }
            //Set today's date to the items that have to be deleted
            if (articleDeleteItems != null && articleDeleteItems.length > 0) {
                for (int i = 0; i < articleDeleteItems.length; i++) {
                    String idString = articleDeleteItems[i].substring(0, articleDeleteItems[i].lastIndexOf('_'));
                    int itemId = DataConverter.parseInt(idString, 0);
                    logger.debug("item id (delete): " + itemId);
                    if (itemId > 0) {
                        RegprofArticleItem item = nomenclaturesDataProvider.getRegprofArticleItem(itemId);
                        nomenclaturesDataProvider.saveRegprofArticleItem(item.getId(), item.getName(),
                                item.getArticleDirectiveId(), item.getQualificationLevelLabel(), item.getDateFrom(), new Date());
                    }
                }
            }
            ///////////////////////////////////////////////////////////////////////////////////
            FlatNomenclatureHandler.refreshCachedNomenclatures(request);
            request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Данните бяха въведени в базата",
                    SystemMessageWebModel.MESSAGE_TYPE_CORRECT));

            RegprofArticleDirective article = nomenclaturesDataProvider.getRegprofArticleDirectiveWithItems(newId);
            if (article == null) {
                throw new UnknownRecordException("Unknown Article Directive ID:" + newId);
            }
            request.setAttribute(WebKeys.REGPROF_ARTICLE_DIRECTIVE, article);
        }
        request.setAttribute(WebKeys.NEXT_SCREEN, "article_directive_edit");
        request.getSession().removeAttribute(WebKeys.TABLE_REGPROF_ARTICLE_DIRECTIVE);


    }

    public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
        String groupName = getGroupName(request);
        Integer flatNomenclatureId = DataConverter.parseInteger(request.getParameter("id"), null);
        if (flatNomenclatureId == null) {
            throw new UnknownRecordException("Unknown Flat Nomenclature ID:" + flatNomenclatureId + " for nomenclature name:" + groupName);
        }
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        FlatNomenclature flatNomenclature = nomenclaturesDataProvider.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_REGPROF_ARTICLE_DIRECTIVE, flatNomenclatureId);
        if (flatNomenclature == null) {
            throw new UnknownRecordException("Unknown FlatNomenclature ID:" + flatNomenclatureId + " NomenclatureName:" + getGroupName(request));
        }
        nomenclaturesDataProvider.saveFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_REGPROF_ARTICLE_DIRECTIVE, flatNomenclature.getId(), flatNomenclature.getName(), flatNomenclature
                .getDateFrom(), new Date());
        request.getSession().removeAttribute(WebKeys.TABLE_REGPROF_ARTICLE_DIRECTIVE);
        handleList(request, response);
    }

    private void generateTable(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_REGPROF_ARTICLE_DIRECTIVE);
        boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();
            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_ITEMS, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_DATE_FROM, CellValueDef.CELL_VALUE_TYPE_DATE);
            table.addColumnHeader(COLUMN_NAME_DATE_TO, CellValueDef.CELL_VALUE_TYPE_DATE);

            session.setAttribute(WebKeys.TABLE_REGPROF_ARTICLE_DIRECTIVE, table);
            resetTableData(request);

        }

        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_REGPROF_ARTICLE_DIRECTIVE + WebKeys.TABLE_STATE);
        /**
         * ako tableState-a e null ili ne e kazano v request-a che trqbva da se
         * vzeme posledniq tableState, togava se generira nov tableState!
         */
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            TableStateAndFiltersUtils.addToDateFilterToTableState(request, table, tableState);
            session.setAttribute(WebKeys.TABLE_REGPROF_ARTICLE_DIRECTIVE + WebKeys.TABLE_STATE, tableState);
        }

        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel("Член Директива");
        webmodel.setGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");

        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_REGPROF_ARTICLE_DIRECTIVE
                + WebKeys.FILTER_WEB_MODEL);
        /**
         * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva
         * da se vzeme posledniq tableState(filterWebModel), togava se generira
         * nov i se slaga v sesiqta!
         */
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            filtersWebModel.addFiler(TableStateAndFiltersUtils.getToDateFilterWebModel(request));
            session.setAttribute(WebKeys.TABLE_REGPROF_ARTICLE_DIRECTIVE + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);

    }

    private void resetTableData(HttpServletRequest request) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_REGPROF_ARTICLE_DIRECTIVE);

        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        List<FlatNomenclature> flatNomenclatures = nomenclaturesDataProvider.getFlatNomenclatures(
                NomenclaturesDataProvider.FLAT_NOMENCLATURE_REGPROF_ARTICLE_DIRECTIVE, null, OrderCriteria
                        .createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, false));

        if (flatNomenclatures != null) {
            for (FlatNomenclature cp : flatNomenclatures) {
                try {
                    List<RegprofArticleItem> items = nomenclaturesDataProvider.getRegprofArticleItems(null, null, cp.getId());
                    StringBuilder builder = new StringBuilder("");
                    if (items != null) {
                        for (RegprofArticleItem i : items) {
                            builder.append(i.getName());

                            if (!i.getIsActive()) {
                                builder.append(" (inactive)");
                            }
                            builder.append("<br/>");
                        }
                    }
                    String tmpString = builder.toString();
                    table.addRow(cp.getId(), cp.getName(), tmpString, cp.getDateFrom(), cp.getDateTo());
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }

}
//----------------------------------------------------------------------------------
