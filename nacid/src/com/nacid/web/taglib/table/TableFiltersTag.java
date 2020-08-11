package com.nacid.web.taglib.table;

import com.nacid.web.WebKeys;
import com.nacid.web.model.table.filters.FilterWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TableFiltersTag extends SimpleTagSupport {
    public void doTag() throws JspException, IOException {
        FiltersWebModel webmodel = (FiltersWebModel) getJspContext().getAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, PageContext.REQUEST_SCOPE);
        if (webmodel != null && webmodel.getFilters() != null && webmodel.getFilters().size() > 0) {

            Map<Boolean, List<FilterWebModel>> partitioned = webmodel.getFilters().stream().collect(Collectors.partitioningBy(r -> Objects.equals(r.getType(), "hidden")));
            getJspContext().setAttribute("hiddenFiltersList", partitioned.get(true));
            getJspContext().setAttribute("filtersList", partitioned.get(false));
            getJspBody().invoke(null);
        }
    }
}
