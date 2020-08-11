package com.nacid.db.nomenclatures;

import com.nacid.data.common.StringValue;
import com.nacid.db.utils.DatabaseService;
import org.apache.commons.lang.StringUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

/**
 * User: ggeorgiev
 * Date: 23.10.2019 г.
 * Time: 18:40
 */
public class RasNomenclaturesMapDb extends DatabaseService {
    public static final String NOMENCLATURE_TYPE_EDU_LEVEL = "EDU_LEVEL";
    public static final String NOMENCLATURE_TYPE_COUNTRY = "COUNTRY";
    public static final String NOMENCLATURE_TYPE_PROF_GROUP = "PROF_GROUP";
    public static final String NOMENCLATURE_TYPE_INSTITUTION = "INSTITUTION";
    public static final String NOMENCLATURE_TYPE_RUDI_APPLICATION_TYPE = "RUDI_APPLICATION_TYPE";
    public static final String NOMENCLATURE_TYPE_LANGUAGE = "LANGUAGE";

    public static final String INSTITUTION_KEY_NACID = "NACID";


    public static final String SELECT_RAS_NOMENCLATURE_VALUE = "select external_nom_id as value from ras_nomenclatures_map where nomenclature_type = ? and internal_nom_id = ?";


    public RasNomenclaturesMapDb(DataSource ds) {
        super(ds);
    }

    public String getNomenclatureValue(String type, String key) throws SQLException {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        List<StringValue> res = selectRecords(SELECT_RAS_NOMENCLATURE_VALUE, StringValue.class, type, key);
        return res.size() == 0 ? null : res.get(0).getValue();
    }

    public Integer getNomenclatureValueAsInteger(String type, String key) throws SQLException {
        List<StringValue> res = selectRecords(SELECT_RAS_NOMENCLATURE_VALUE, StringValue.class, type, key);
        return res.size() == 0 ? null : Integer.parseInt(res.get(0).getValue());
    }
}
