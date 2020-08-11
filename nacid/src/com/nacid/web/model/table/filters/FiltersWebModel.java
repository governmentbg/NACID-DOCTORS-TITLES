package com.nacid.web.model.table.filters;

import java.util.ArrayList;
import java.util.List;

public class FiltersWebModel {
  List<FilterWebModel> filters;

  public void addFiler(FilterWebModel f) {
    if (filters == null) {
      filters = new ArrayList<FilterWebModel>();
    }
    filters.add(f);
  }
  
  public List<FilterWebModel> getFilters() {
	  return filters;
  }
  
}
