package com.nacid.web.handlers;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.utils.CommonVariable;
import com.nacid.bl.utils.UtilsDataProvider;
import com.nacid.data.DataConverter;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class RequestParametersUtils {
	public static final String PARAM_NAME_ROW_BEGIN = "rowBegin";
	public static final String PARAM_NAME_ROWS_COUNT = "rowsCount";
	public static final String PARAM_NAME_ORDER_COL = "orderCol";
	public static final String PARAM_NAME_ORDER_ASCENDING = "orderAscending";
	public static final String PARAM_NAME_CHECKED_ROWS = "checkedRows";
	public static final String PARAM_NAME_UNCHECKED_ROWS = "uncheckedRows";
	public static final String PARAM_NAME_ONLY_CHECKED = "onlyChecked";
	public static final String PARAM_NAME_SUBMIT_FORM = "sub";
	public static final String PARAM_NAME_RELOAD_TABLE = "reload";
	public static final String PARAM_NAME_TO_DATE_FILTER = "toDateFilter";
	public static final String PARAM_NAME_GET_LAST_TABLE_STATE = "getLastTableState";
	/**
	 * Reads request for "rowBegin".
	 * 
	 * @returns 0 if not found.
	 */
	public static int getParameterRowBegin(HttpServletRequest req, String tablePrefix) {
		int result = isCurrentTableSubmitted(req, tablePrefix) ? (DataConverter.parseInt(req.getParameter(PARAM_NAME_ROW_BEGIN), 0)) : 0; 
		return result < 0 ? 0 : result;
	}

	/**
	 * Reads request for "rowsCount".
	 * 
	 * @returns UserBean.getSettingsTableRowStep() if "rowStep" not found in the
	 *          request.
	 */
	public static int getParameterRowsCount(HttpServletRequest req, String tablePrefix) {
		NacidDataProvider nacidDataProvider = BaseRequestHandler.getNacidDataProvider(req.getSession());
		UtilsDataProvider udp = nacidDataProvider.getUtilsDataProvider();
		String var = udp.getCommonVariableValue(CommonVariable.COMMON_VARIABLE_ROWS_COUNT);
		Integer maxCount = DataConverter.parseInt(var, 100);
	    int result = isCurrentTableSubmitted(req, tablePrefix) ? (DataConverter.parseInt(req.getParameter(PARAM_NAME_ROWS_COUNT), maxCount)) : -1;
		if (result < 0 || result > maxCount) {
			result = maxCount;
		}
		return result ;
	}

	public static String getParameterOrderColumn(HttpServletRequest request, String tablePrefix) {
		return !isCurrentTableSubmitted(request, tablePrefix) || "".equals(request.getParameter(PARAM_NAME_ORDER_COL)) ? null : request.getParameter(PARAM_NAME_ORDER_COL);
	}
	public static List<String> getParameterHiddenModifiableColumns(HttpServletRequest request, String tablePrefix) {
		String[] hiddenColumns = request.getParameterValues((StringUtils.isEmpty(tablePrefix) ? "" : tablePrefix) + "HIDE_COLUMN_NAME");
		return hiddenColumns == null || hiddenColumns.length == 0 ? new ArrayList<>() : Arrays.asList(hiddenColumns);
	}

	public static boolean getParameterOrderAscending(HttpServletRequest request, String tablePrefix) {
		return isCurrentTableSubmitted(request, tablePrefix) && DataConverter.parseBoolean(request.getParameter(PARAM_NAME_ORDER_ASCENDING));
	}
	/**
	 * vry6ta markiranite redove v tablicata  
	 * markiranite redove izglejdat v toq vid
	 * vsqko ID e unikalnoto ID na konkretniq red! 
	 * 1;2;6;7
	 * @param request
	 * @return
	 */
	public final static TreeSet<Integer> getParameterCheckedRows(HttpServletRequest request, String tablePrefix) {
		if (!isCurrentTableSubmitted(request, tablePrefix)) {
			return null;
		}
		String parameters = request.getParameter(PARAM_NAME_CHECKED_ROWS);
		return convertRequestParameterToIntegerTreeSet(parameters);
	}
	/**
	 * prevry6ta request parameter vyv vida 1;3;4;5 v TreeSet s takiva elementi
	 * @return
	 */
	public static final TreeSet<Integer> convertRequestParameterToIntegerTreeSet(String paramValue) {
		if (StringUtils.isEmpty(paramValue)) {
			return null;
		}
        List<Integer> list = convertRequestParameterToIntegerList(paramValue);
        return list == null ? null : new TreeSet<Integer>(list);
	}

    public static final List<Integer> convertRequestParameterToIntegerList(String paramValue) {
        if (StringUtils.isEmpty(paramValue)) {
            return null;
        }
        List<String> strings = new ArrayList<String>(Arrays.asList(paramValue.split(";")));
        if (strings == null || strings.size() == 0) {
            return null;
        }
        List<Integer> result = new ArrayList<Integer>();
        for (String s:strings) {
            Integer i = DataConverter.parseInteger(s, null);
            if (i != null) {
                result.add(i);
            }
        }
        return result;
    }
	/**
	 * vry6ta redovete, koito ne trqbva da se sydyrjat v tablicata markiranite
	 * redove izglejdat v toq vid vsqko ID e unikalnoto ID na konkretniq red!
	 * 1;2;6;7
	 * 
	 * @param request
	 * @return
	 */
	public final static Set<String> getParameterUnCheckedRows(HttpServletRequest request, String tablePrefix) {
		if (!isCurrentTableSubmitted(request, tablePrefix)) {
			return null;
		}
		String parameters = request.getParameter(PARAM_NAME_UNCHECKED_ROWS);
		if (parameters == null) {
			return null;
		}
		Set<String> strings = new TreeSet<String>(Arrays.asList(parameters.split(";")));
		if (strings == null || strings.size() == 0) {
			return null;
		}
		return strings;
	}


	/**
	 * @param request
	 * @return - izpolzva se za tablicata - dali da se vry6tat samo markiranite redove!
	 */
	public final static boolean getParameterOnlyChecked(HttpServletRequest request, String tablePrefix) {
		return isCurrentTableSubmitted(request, tablePrefix) && DataConverter.parseBoolean(request.getParameter(PARAM_NAME_ONLY_CHECKED));
	}
	/**
	 * shte pokazva dali dadena forma e submitnata!
	 * @param request
	 * @return
	 */
	public final static boolean getParameterFormSubmitted(HttpServletRequest request) {
		return DataConverter.parseBoolean(request.getParameter(PARAM_NAME_SUBMIT_FORM));
	}
	/**
	 * shte pokazva dali trqbva da se pre4ertava tablicata
	 * @param request
	 * @return
	 */
	public final static boolean getParameterReloadTable(HttpServletRequest request) {
		return getParameterReloadTable(request, null);
	}
	public final static boolean getParameterReloadTable(HttpServletRequest request, String tablePrefix) {
		return isCurrentTableSubmitted(request, tablePrefix) && DataConverter.parseBoolean(request.getParameter(PARAM_NAME_RELOAD_TABLE));
	}
	/**
	 * shte pokazva dali dadena tablica shte trqbva da se filtrira po tekushtoto toDate...
	 * zasega vyv vsi4ki nomenklaturi ima takyv filtyr!
	 * @param request
	 * @return
	 */
	public final static boolean getParameterToDateFilter(HttpServletRequest request, String tablePrefix) {
		return isCurrentTableSubmitted(request, tablePrefix) && DataConverter.parseBoolean(request.getParameter(PARAM_NAME_TO_DATE_FILTER)); 
	}


	/**
	 * shte pokazva dali trqbva da se izpolzva tableState-a koeto e v sesiqta ili shte se pravi nanovo tablestate!
	 * tyi kato pri opredeleni situacii, na request-a ne se podavat parametrite za filtrite na tablicata, a te ne trqbva da iz4ezvat,
	 * zatova se slagat v sesiqta - i ako e settnat tozi parametyr, togava TableState-a se vzema ot sesiqta!
	 * Ako getParameterReloadTable == true toq parametyr trqbva da vyrne false, zashtoto ako shte se pre4ertava tablicata, sy6toto vaji i za filtrite!
	 * @return
	 */
	public final static boolean getParameterGetLastTableState(HttpServletRequest request) {
		return getParameterGetLastTableState(request, null);
	}
	public final static boolean getParameterGetLastTableState(HttpServletRequest request, String tablePrefix) {
		//Ideqta e ako na edna stranica ima 2-3 formi i ne e submitnata tazi s tyrseniq tablePrefix 
		//(pr.submitnata e s pref1, a se pita dali pref2 ima setnat parametyr lastTableState), togava za neq se vry6ta true - za da moje da se pre4ertae tablicata po stariq na4in...
		if (!isCurrentTableSubmitted(request, tablePrefix)) {
			return true;
		}
		if (getParameterReloadTable(request, tablePrefix)) {
			return false;
		}
		return DataConverter.parseBoolean(request.getParameter(PARAM_NAME_GET_LAST_TABLE_STATE));
	}

	public final static boolean isCurrentTableSubmitted(HttpServletRequest request, String tablePrefix) {
		//Ako nqma settnat tablePrefix parametyr (t.e. otvarq se tablicata nanovo - primerno pri cykane na http://localhost:8080/nacid/control/nom_educationarea/list?reload=1)
		//Trqbva da se "garantira" 4e e submitnata tekushtata tablica. 
		if (request.getParameter("tablePrefix") == null) {
			return true;
		}
		if (tablePrefix == null ) {
			return "".equals(request.getParameter("tablePrefix"));
		}
		return tablePrefix.equals(request.getParameter("tablePrefix"));

	}
	public static TreeSet<String> convertRequestParamToList(String paramValue) {
        if (StringUtils.isEmpty(paramValue)) {
            return null;
        }
        TreeSet<String> strings = new TreeSet<String>(Arrays.asList(paramValue.split(";")));
        if (strings == null || strings.size() == 0) {
            return null;
        }
        return strings;
    }

    public static Integer parseInteger(HttpServletRequest request, String parameter, Integer defaultValue) {
        return DataConverter.parseInteger(request.getParameter(parameter), defaultValue);
    }

    public static String parseString(HttpServletRequest request, String parameter, String defaultValue) {
        return DataConverter.parseString(request.getParameter(parameter), defaultValue);
    }
    public static Date parseDate(HttpServletRequest request, String parameter) {
        return DataConverter.parseDate(request.getParameter(parameter));
    }
}
