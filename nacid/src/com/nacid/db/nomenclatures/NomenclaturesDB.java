package com.nacid.db.nomenclatures;


import com.nacid.bl.OrderCriteria;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.NomenclatureOrderCriteria;
import com.nacid.data.common.IntegerValue;
import com.nacid.data.common.StringValue;
import com.nacid.data.nomenclatures.*;
import com.nacid.data.nomenclatures.regprof.*;
import com.nacid.db.utils.DatabaseService;
import com.nacid.db.utils.SQLUtils;
import com.nacid.db.utils.StandAloneDataSource;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class NomenclaturesDB extends DatabaseService {
    public static final String EDUCATION_AREA_TABLE = "nomenclatures.edu_area";
    public static final String PROF_GROUP_TABLE = "nomenclatures.prof_group";
    ///RayaWrittren-------------------------------------------------
    public static final String PROF_SCH_TABLE = "nomenclatures.profession_school";
    public static final String SECONDARY_PROF_QUALIF_TABLE = "nomenclatures.secondary_prof_qualification";
    ///--------------------------------------------------------------
    public static final String SPECIALITY_TABLE = "nomenclatures.speciality";
    public static final String COUNTRY_TABLE = "nomenclatures.country";

    public static final String PROF_GROUP_TABLE_COLUMNS = "id, name, edu_area_id, date_from, date_to ";
    public static final String SPECIALITY_TABLE_COLUMNS = "id, name, prof_group_id, date_from, date_to ";
    public static final String COUNTRY_TABLE_COLUMNS = "id, name, iso3166_code, official_name, date_from, date_to ";

    public static final String SELECT_APPLICATION_STATUSES = "select ass.*, is_legal from nomenclatures.app_status  ass join backoffice.app_status_entry_num_series ens on ens.app_status_id = ass.id and ens.entry_num_series_id = ?";
    public static final String SELECT_APPLICATION_DOCFLOW_STATUSES = "select asd.* from nomenclatures.app_status_docflow  asd join backoffice.app_status_docflow_entry_num_series dens on dens.app_status_docflow_id = asd.id and dens.entry_num_series_id = ?";
    public static final String SELECT_DOCUMENT_TYPE_RECORDS_EXTENDED = "select id, name, date_from, date_to, in_out as is_incoming, has_docflow_id, doc_template as document_template, sort, is_rejection, (select array(select doc_category_id from nomenclatures.doc_type_to_doc_category dtdc where dtdc.doc_type_id = t.id)) as document_category_ids\n" +
                                                                        " from nomenclatures.doc_types t\n";

    public NomenclaturesDB(DataSource ds) {
        super(ds);
    }

    private static void addToDateAndOrderCriteria(StringBuilder sql, List<Object> objects, java.sql.Date toDate, OrderCriteria orderCriteria) {
        sql.append(" 1 = 1");
        if (toDate != null) {
            sql.append(" AND (date_from <= ? OR date_from is null) ");
            sql.append(" AND (date_to >= ? OR date_to is null) ");
            objects.add(toDate);
            objects.add(toDate);
        }

        if (orderCriteria == null) {
            sql.append(" ORDER BY NAME ");
        } else if (orderCriteria.getOrderColumn() != null) {
            sql.append(((NomenclatureOrderCriteria) orderCriteria).getOrderSqlString());
        }
    }

    public static void main(String[] args) throws SQLException {
        DataSource ds = new StandAloneDataSource();
        NomenclaturesDB db = new NomenclaturesDB(ds);
        SecondarySpecialityRecord rec = new SecondarySpecialityRecord(2, "тестов запис", 2, 3, null, null, null);
        rec = db.insertRecord(rec);
        //ProfessionalSchoolRecord  rec = db.selectRecord(new ProfessionalSchoolRecord(), 1);
        System.out.println(rec.getProfQualificationId());
        //db.getFlatNomenclatureRecords(EducationAreaRecord.class, Utils.getSqlDate(Utils.getToday()), null);
        //System.out.println(db.getProfessionalSchoolRecords(null, null, null));

    }

    private ProfessionGroupRecord toProfessionGroupRecord(ResultSet rs, boolean addEduAreaName) throws SQLException {
        return
                new ProfessionGroupRecord(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getDate(4),
                        rs.getDate(5),
                        addEduAreaName ? rs.getString(6) : null
                );
    }

    private SpecialityRecord toSpecialityRecord(ResultSet rs, boolean addprofessionGroupName) throws SQLException {
        return
                new SpecialityRecord(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getDate(4),
                        rs.getDate(5),
                        addprofessionGroupName ? rs.getString(6) : null
                );
    }

    private CountryRecord toCountryRecord(ResultSet rs) throws SQLException {
        return
                new CountryRecord(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDate(5),
                        rs.getDate(6)
                );
    }

    public ProfessionGroupRecord getProfessionGroupRecord(int professionGroupId) throws SQLException {
        String sql = "SELECT prof_group.id, prof_group.name, edu_area_id, prof_group.date_from, prof_group.date_to, " + EDUCATION_AREA_TABLE + ".name" +
                " FROM " + PROF_GROUP_TABLE +
                " LEFT JOIN " + EDUCATION_AREA_TABLE +
                " ON (" + EDUCATION_AREA_TABLE + ".id = edu_area_id)" +
                " WHERE 1 = 1 ";
        sql += " AND prof_group.id = ? ";

        Connection connection = getConnection();

        try {
            PreparedStatement p = connection.prepareStatement(sql);
            try {
                int arg = 1;
                p.setInt(arg++, professionGroupId);
                //System.out.println(p);
                ResultSet rs = p.executeQuery();
                try {
                    if (rs.next()) {
                        return toProfessionGroupRecord(rs, true);
                    }
                    return null;
                } finally {
                    rs.close();
                }
            } finally {
                p.close();
            }
        } finally {
            release(connection);
        }
    }

    public List<ProfessionGroupRecord> getProfessionGroupRecords(int educationAreaId, java.sql.Date toDate, OrderCriteria orderCriteria) throws SQLException {
        String sql = "SELECT prof_group.id, prof_group.name, edu_area_id, prof_group.date_from, prof_group.date_to, " + EDUCATION_AREA_TABLE + ".name" +
                " FROM " + PROF_GROUP_TABLE +
                " LEFT JOIN " + EDUCATION_AREA_TABLE +
                " ON (" + EDUCATION_AREA_TABLE + ".id = edu_area_id)" +
                " WHERE 1 = 1 ";
        if (educationAreaId != 0) {
            sql += " AND edu_area_id = ? ";
        }

        if (toDate != null) {
            sql += " AND (prof_group.date_from <= ? OR prof_group.date_from is null) ";
            sql += " AND (prof_group.date_to >= ? OR prof_group.date_to is null) ";
        }

        /**
         * ne mi haresva nachina po koito se syzdava ORDER BY za getProfessionGroupRecords i getSpecialityRecords,
         * no ne izmilih nachin pri koito da podavam imeto na tablicata ("n_prof_group"), koqto shte se order-va pri syzdavane na orderCriteria Object-a
         */
        if (orderCriteria == null) {
            sql += " ORDER BY prof_group.name ";
        } else if (orderCriteria.getOrderColumn() != null) {
            sql += " ORDER BY prof_group." + orderCriteria.getOrderColumn() + (orderCriteria.isAscending() ? " ASC " : " DESC ");
        }

        Connection connection = getConnection();

        try {
            PreparedStatement p = connection.prepareStatement(sql);
            try {
                int arg = 1;
                if (educationAreaId != 0) {
                    p.setInt(arg++, educationAreaId);
                }
                if (toDate != null) {
                    p.setDate(arg++, toDate);
                    p.setDate(arg++, toDate);
                }

                ResultSet rs = p.executeQuery();
                try {
                    List<ProfessionGroupRecord> result = new ArrayList<ProfessionGroupRecord>();
                    while (rs.next()) {
                        result.add(toProfessionGroupRecord(rs, true));
                    }
                    return result;
                } finally {
                    rs.close();
                }
            } finally {
                p.close();
            }
        } finally {
            release(connection);
        }
    }

    public SpecialityRecord getSpecialityRecord(int specialityId) throws SQLException {
        String sql = "SELECT nomenclatures.speciality.id, speciality.name, prof_group_id, speciality.date_from, speciality.date_to, " + PROF_GROUP_TABLE + ".name" +
                " FROM " + SPECIALITY_TABLE +
                " LEFT JOIN " + PROF_GROUP_TABLE +
                " ON (" + PROF_GROUP_TABLE + ".id = prof_group_id)" +
                " WHERE 1 = 1 ";
        sql += " AND nomenclatures.speciality.id = ? ";

        Connection connection = getConnection();

        try {
            PreparedStatement p = connection.prepareStatement(sql);
            try {
                int arg = 1;
                p.setInt(arg++, specialityId);
                ResultSet rs = p.executeQuery();
                try {
                    if (rs.next()) {
                        return toSpecialityRecord(rs, true);
                    }
                    return null;
                } finally {
                    rs.close();
                }
            } finally {
                p.close();
            }
        } finally {
            release(connection);
        }
    }

    public List<SpecialityRecord> getSpecialityRecords(Integer professionGroupId, java.sql.Date toDate, OrderCriteria orderCriteria) throws SQLException {
        String sql = "SELECT speciality.id, speciality.name, prof_group_id, speciality.date_from, speciality.date_to, " + PROF_GROUP_TABLE + ".name" +
                " FROM " + SPECIALITY_TABLE +
                " LEFT JOIN " + PROF_GROUP_TABLE +
                " ON (" + PROF_GROUP_TABLE + ".id = prof_group_id)" +
                " WHERE 1 = 1 ";

        if (professionGroupId != null && professionGroupId != 0) {
            sql += " AND prof_group_id = ? ";
        }
        if (toDate != null) {
            sql += " AND (speciality.date_from <= ? OR speciality.date_from is null) ";
            sql += " AND (speciality.date_to >= ? OR speciality.date_to is null) ";
        }
        if (orderCriteria == null) {
            sql += " ORDER BY speciality.NAME ";
        } else if (orderCriteria.getOrderColumn() != null) {
            sql += " ORDER BY speciality." + orderCriteria.getOrderColumn() + (orderCriteria.isAscending() ? " ASC " : " DESC ");
        }

        Connection connection = getConnection();

        try {
            PreparedStatement p = connection.prepareStatement(sql);
            try {
                int arg = 1;
                if (professionGroupId != null && professionGroupId != 0) {
                    p.setInt(arg++, professionGroupId);
                }
                if (toDate != null) {
                    p.setDate(arg++, toDate);
                    p.setDate(arg++, toDate);
                }
                ResultSet rs = p.executeQuery();
                try {
                    List<SpecialityRecord> result = new ArrayList<SpecialityRecord>();
                    while (rs.next()) {
                        result.add(toSpecialityRecord(rs, true));
                    }
                    return result;
                } finally {
                    rs.close();
                }
            } finally {
                p.close();
            }
        } finally {
            release(connection);
        }
    }

    ///RayaWritten------------------------------------------------------------------------------------------------

    public List<ProfessionExperienceDocumentTypeRecord> getProfessionExperienceDocumentTypesRecords(java.sql.Date toDate, OrderCriteria orderCriteria) throws SQLException {
        String sql = " 1 = 1";
        List<Object> objects = new ArrayList<Object>();
        if (toDate != null) {
            sql += " AND (date_from <= ? OR date_from is null) ";
            sql += " AND (date_to >= ? OR date_to is null) ";
            objects.add(toDate);
            objects.add(toDate);
        }
        if (orderCriteria == null) {
            sql += " ORDER BY NAME ";
        } else if (orderCriteria.getOrderColumn() != null) {
            sql += ((NomenclatureOrderCriteria) orderCriteria).getOrderSqlString();
        }
        return selectRecords(ProfessionExperienceDocumentTypeRecord.class, sql, objects.size() == 0 ? null : objects.toArray());
    }

    public List<SpecialityRecord> getSpecialityRecords(String partOfName, boolean nameStartsWith, Integer professionGroupId, java.sql.Date toDate, OrderCriteria orderCriteria) throws SQLException {
        String sql = "SELECT speciality.id, speciality.name, prof_group_id, speciality.date_from, speciality.date_to, " + PROF_GROUP_TABLE + ".name" +
                " FROM " + SPECIALITY_TABLE +
                " LEFT JOIN " + PROF_GROUP_TABLE +
                " ON (" + PROF_GROUP_TABLE + ".id = prof_group_id)" +
                " WHERE 1 = 1 ";
        if (!StringUtils.isEmpty(partOfName)) {
            sql += " AND speciality.name ilike ? ";
        }
        if (professionGroupId != null && professionGroupId != 0) {
            sql += " AND prof_group_id = ? ";
        }
        if (toDate != null) {
            sql += " AND (speciality.date_from <= ? OR speciality.date_from is null) ";
            sql += " AND (speciality.date_to >= ? OR speciality.date_to is null) ";
        }
        if (orderCriteria == null) {
            sql += " ORDER BY speciality.NAME ";
        } else if (orderCriteria.getOrderColumn() != null) {
            sql += " ORDER BY speciality." + orderCriteria.getOrderColumn() + (orderCriteria.isAscending() ? " ASC " : " DESC ");
        }

        Connection connection = getConnection();

        try {
            PreparedStatement p = connection.prepareStatement(sql);
            try {
                int arg = 1;
                if (!StringUtils.isEmpty(partOfName)) {
                    p.setString(arg++, (nameStartsWith ? "" : "%") + partOfName + "%");
                }
                if (professionGroupId != null && professionGroupId != 0) {
                    p.setInt(arg++, professionGroupId);
                }
                if (toDate != null) {
                    p.setDate(arg++, toDate);
                    p.setDate(arg++, toDate);
                }
                ResultSet rs = p.executeQuery();
                try {
                    List<SpecialityRecord> result = new ArrayList<SpecialityRecord>();
                    while (rs.next()) {
                        result.add(toSpecialityRecord(rs, true));
                    }
                    return result;
                } finally {
                    rs.close();
                }
            } finally {
                p.close();
            }
        } finally {
            release(connection);
        }
    }

    public List<SecondaryProfessionalQualificationRecord> getSecondaryProfessionalQualificationRecords(java.sql.Date toDate, OrderCriteria orderCriteria, Integer profGroupId) throws SQLException {
        List<Object> args = new ArrayList<Object>();
        String sql = " 1 = 1";
        if (profGroupId != null && profGroupId != 0) {
            sql += " AND profession_group_id = ? ";
            args.add(profGroupId);
        }
        if (toDate != null) {
            sql += " AND (date_from <= ? OR date_from is null) ";
            args.add(toDate);
            sql += " AND (date_to >= ? OR date_to is null) ";
            args.add(toDate);
        }
        if (orderCriteria == null) {
            sql += " ORDER BY name ";
        } else if (orderCriteria.getOrderColumn() != null) {
            sql += " ORDER BY " + orderCriteria.getOrderColumn() + (orderCriteria.isAscending() ? " ASC " : " DESC ");

        }
        return selectRecords(SecondaryProfessionalQualificationRecord.class, sql, args.toArray());
    }

    public SecondaryProfessionalQualificationRecord getSecondaryProfessionalQualificationRecord(int qualifId) throws SQLException {
        String sql = "SELECT nspq.* , nspg.name profession_group_name FROM nomenclatures.secondary_prof_qualification nspq JOIN nomenclatures.secondary_profession_group nspg ON " +
                " nspq.profession_group_id = nspg.id AND nspq.id = ? ";
        List<SecondaryProfessionalQualificationRecord> records = selectRecords(sql, SecondaryProfessionalQualificationRecord.class, qualifId);
        if (records.size() < 1) {
            return null;
        } else {
            return records.get(0);
        }
    }

    public List<RegprofArticleItemRecord> getRegprofArticleItemRecords(java.sql.Date toDate, OrderCriteria orderCriteria, Integer articleId) throws SQLException {
        String sql = " 1 = 1 ";
        List<Object> objects = new ArrayList<Object>();
        if (toDate != null) {
            sql += " AND (date_from <= ? OR date_from is null) ";
            sql += " AND (date_to >= ? OR date_to is null) ";
            objects.add(toDate);
            objects.add(toDate);
        }

        if (articleId != null && articleId != 0) {
            sql += " and article_directive_id=?";
            objects.add(articleId);
        }

        if (orderCriteria == null) {
            sql += " ORDER BY NAME ";
        } else if (orderCriteria.getOrderColumn() != null) {
            sql += ((NomenclatureOrderCriteria) orderCriteria).getOrderSqlString();
        }
        return selectRecords(RegprofArticleItemRecord.class, sql, objects.size() == 0 ? null : objects.toArray());

    }

    public List<SecondaryProfessionGroupRecord> getSecondaryProfessionGroupRecords(java.sql.Date toDate, OrderCriteria orderCriteria) throws SQLException {
        String sql = " 1 = 1 ";
        List<Object> objects = new ArrayList<Object>();
        if (toDate != null) {
            sql += " AND (date_from <= ? OR date_from is null) ";
            sql += " AND (date_to >= ? OR date_to is null) ";
            objects.add(toDate);
            objects.add(toDate);
        }
        if (orderCriteria == null) {
            sql += " ORDER BY NAME ";
        } else if (orderCriteria.getOrderColumn() != null) {
            sql += ((NomenclatureOrderCriteria) orderCriteria).getOrderSqlString();
        }
        return selectRecords(SecondaryProfessionGroupRecord.class, sql, objects.size() == 0 ? null : objects.toArray());
    }

    public List<ServiceTypeRecord> getServiceTypeRecords(java.sql.Date toDate, OrderCriteria orderCriteria, Integer executionDays) throws SQLException {
        String sql = " 1 = 1 ";
        List<Object> objects = new ArrayList<Object>();
        if (toDate != null) {
            sql += " AND (date_from <= ? OR date_from is null) ";
            sql += " AND (date_to >= ? OR date_to is null) ";
            objects.add(toDate);
            objects.add(toDate);
        }

        if (executionDays != null && executionDays != 0) {
            sql += " and execution_days=?";
            objects.add(executionDays);
        }

        if (orderCriteria == null) {
            sql += " ORDER BY NAME ";
        } else if (orderCriteria.getOrderColumn() != null) {
            sql += ((NomenclatureOrderCriteria) orderCriteria).getOrderSqlString();
        }
        return selectRecords(ServiceTypeRecord.class, sql, objects.size() == 0 ? null : objects.toArray());

    }

    public List<OriginalEducationLevelRecord> getOriginalEducationLevelRecords(java.sql.Date toDate, OrderCriteria orderCriteria) throws SQLException {
        StringBuilder str = new StringBuilder();
        List<Object> objects = new ArrayList<Object>();
        addToDateAndOrderCriteria(str, objects, toDate, orderCriteria);
        return selectRecords(OriginalEducationLevelRecord.class, str.toString(), objects.size() == 0 ? null : objects.toArray());
    }

    public List<NationalQualificationsFrameworkRecord> getNationalQualificationsFrameworks(java.sql.Date toDate, Integer countryId, OrderCriteria orderCriteria) throws SQLException {
        StringBuilder str = new StringBuilder();
        List<Object> objects = new ArrayList<Object>();

        if (countryId != null) {
            objects.add(countryId);
            str.append(" country_id = ? AND ");
        }
        addToDateAndOrderCriteria(str, objects, toDate, orderCriteria);

        return selectRecords(NationalQualificationsFrameworkRecord.class, str.toString(), objects.size() == 0 ? null : objects.toArray());
    }

    public List<OriginalEducationLevelRecord> getOriginalEducationLevelRecordsByCountry(Integer countryId, List<Integer> eduLevelIds, java.sql.Date toDate, OrderCriteria orderCriteria) throws SQLException {
        if (eduLevelIds != null && eduLevelIds.size() == 0) {
            return new ArrayList<>();
        }
        StringBuilder str = new StringBuilder();
        List<String> sql = new ArrayList<String>();
        List<Object> objects = new ArrayList<Object>();

        if (countryId != null) {
            str.append(" country_id = ? AND ");
            objects.add(countryId);
        }
        if (eduLevelIds != null) {
            str.append(" edu_level_id in (" + SQLUtils.parametersCountToParameterList(eduLevelIds.size()) + ") AND ");
            objects.addAll(eduLevelIds);
        }

        addToDateAndOrderCriteria(str, objects, toDate, orderCriteria);
        return selectRecords(OriginalEducationLevelRecord.class, str.toString(), objects.size() == 0 ? null : objects.toArray());
    }

    ///------------------------------------------------------------------------------------------------------------
   /* public List<DocumentTypeRecordExtended> getDocumentTypeRecords(java.sql.Date toDate, OrderCriteria orderCriteria, int docCategoryId) throws SQLException {
        String sql = "WHERE 1 = 1 ";
        List<Object> objects = new ArrayList<Object>();
        if (toDate != null) {
            sql += " AND (date_from <= ? OR date_from is null) ";
            sql += " AND (date_to >= ? OR date_to is null) ";
            objects.add(toDate);
            objects.add(toDate);
        }

        if (docCategoryId > 0) {
            sql += " and t.id in (select doc_type_id from nomenclatures.doc_type_to_doc_category where doc_category_id=?)";
            objects.add(docCategoryId);
        }

        if (orderCriteria == null) {
            sql += " ORDER BY IN_OUT DESC, NAME ASC";
        } else if (orderCriteria.getOrderColumn() != null) {
            sql += ((NomenclatureOrderCriteria) orderCriteria).getOrderSqlString();
        }
        sql = SELECT_DOCUMENT_TYPE_RECORDS_EXTENDED + sql;
        return selectRecords(sql, DocumentTypeRecordExtended.class, objects.size() == 0 ? null : objects.toArray());

    }*/

    public List<DocumentTypeRecordExtended> getDocumentTypeRecords(java.sql.Date toDate, OrderCriteria orderCriteria, Boolean hasDocumentTemplate, List<Integer> docCategoryIds) throws SQLException {
        String sql = "WHERE  1 = 1 ";
        List<Object> objects = new ArrayList<Object>();
        if (toDate != null) {
            sql += " AND (date_from <= ? OR date_from is null) ";
            sql += " AND (date_to >= ? OR date_to is null) ";
            objects.add(toDate);
            objects.add(toDate);
        }

        if (docCategoryIds != null && docCategoryIds.size() > 0) {
            sql += " and t.id in (select doc_type_id from nomenclatures.doc_type_to_doc_category where doc_category_id  in ("+ SQLUtils.columnsToParameterList(StringUtils.join(docCategoryIds, ",")) + ")) ";
            objects.addAll(docCategoryIds);
        }
        if (hasDocumentTemplate != null) {
            if (hasDocumentTemplate) {
                sql += " and (doc_template is not null and doc_template != ? )";
                objects.add("");
            } else {
                sql += " and (doc_template is null or doc_template = ?) ";
                objects.add("");
            }
        }

        if (orderCriteria == null) {
            sql += " ORDER BY IN_OUT DESC, NAME ASC";
        } else if (orderCriteria.getOrderColumn() != null) {
            sql += ((NomenclatureOrderCriteria) orderCriteria).getOrderSqlString();
        }
        sql = SELECT_DOCUMENT_TYPE_RECORDS_EXTENDED + sql;
        return selectRecords(sql, DocumentTypeRecordExtended.class, objects.size() == 0 ? null : objects.toArray());

    }

    public DocumentTypeRecord getDocumentTypeRecord(int documentTypeId) throws SQLException {
        return selectRecord(new DocumentTypeRecord(), documentTypeId);
    }

    public List<CountryRecord> getCountryRecords(java.sql.Date toDate, OrderCriteria orderCriteria) throws SQLException {
        String sql = "SELECT " + COUNTRY_TABLE_COLUMNS +
                " FROM " + COUNTRY_TABLE + " WHERE 1 = 1 ";

        if (toDate != null) {
            sql += " AND (date_from <= ? OR date_from is null) ";
            sql += " AND (date_to >= ? OR date_to is null) ";
        }
        sql += ((NomenclatureOrderCriteria) orderCriteria).getOrderSqlString();
        Connection connection = getConnection();

        try {
            PreparedStatement p = connection.prepareStatement(sql);
            try {
                int arg = 1;
                if (toDate != null) {
                    p.setDate(arg++, toDate);
                    p.setDate(arg++, toDate);
                }
                ResultSet rs = p.executeQuery();
                try {
                    List<CountryRecord> result = new ArrayList<CountryRecord>();
                    while (rs.next()) {
                        result.add(toCountryRecord(rs));
                    }
                    return result;
                } finally {
                    rs.close();
                }
            } finally {
                p.close();
            }
        } finally {
            release(connection);
        }
    }

    public <T extends FlatNomenclatureRecord> List<T> getFlatNomenclatureRecords(Class<T> cls, java.sql.Date toDate, OrderCriteria orderCriteria) throws SQLException {
        StringBuilder sql = new StringBuilder();
        List<Object> objects = new ArrayList<>();
        addToDateAndOrderCriteria(sql, objects, toDate, orderCriteria);
        return selectRecords(cls, sql.toString(), objects.size() == 0 ? null : objects.toArray());
    }

    public <T extends FlatNomenclatureRecord> List<T> getFlatNomenclatureRecords(Class<T> cls, String name, java.sql.Date toDate, OrderCriteria orderCriteria) throws SQLException {
        StringBuilder sql = new StringBuilder("");

        List<Object> objects = new ArrayList<Object>();
        if (name != null) {
            sql.append("name ilike ? AND");
            objects.add(name);
        }
        addToDateAndOrderCriteria(sql, objects, toDate, orderCriteria);

        return selectRecords(cls, sql.toString(), objects.toArray());


    }

    public List<GraduationWayRecord> getGraduationWayRecords(int applicationType, java.sql.Date toDate, OrderCriteria orderCriteria) throws SQLException {
        StringBuilder sql = new StringBuilder(" ");
        List<Object> objects = new ArrayList<>();
        sql.append("id in (select way_of_graduation_id from nomenclatures.way_of_graduation_to_app_type where application_type = ?) AND ");
        objects.add(applicationType);
        addToDateAndOrderCriteria(sql, objects, toDate, orderCriteria);
        return selectRecords(GraduationWayRecord.class, sql.toString(), objects.size() == 0 ? null : objects.toArray());
    }

    public List<Integer> getApplicationTypesPerGraduationWay(int graduationWayId) throws SQLException {
        return selectRecords("select application_type as value from nomenclatures.way_of_graduation_to_app_type where way_of_graduation_id = ?", IntegerValue.class, graduationWayId).stream().map(IntegerValue::getValue).collect(Collectors.toList());
    }

    public void updateGraduationWayApplicationTypes(Integer graduationWayId, List<Integer> applicationTypes) throws SQLException {
        execute("DELETE FROM nomenclatures.way_of_graduation_to_app_type where way_of_graduation_id = ?", graduationWayId);
        if (applicationTypes != null) {
            for (Integer applicationType : applicationTypes) {
                execute("INSERT INTO nomenclatures.way_of_graduation_to_app_type (way_of_graduation_id, application_type) VALUES (?, ?)", graduationWayId, applicationType);
            }
        }
    }

    public List<EducationLevelRecord> getEducationLevelRecords(int applicationType, java.sql.Date toDate, OrderCriteria orderCriteria) throws SQLException {
        StringBuilder sql = new StringBuilder(" ");
        List<Object> objects = new ArrayList<>();
        sql.append("id in (select edu_level_id from nomenclatures.edu_level_to_app_type where application_type = ?) AND ");
        objects.add(applicationType);
        addToDateAndOrderCriteria(sql, objects, toDate, orderCriteria);
        return selectRecords(EducationLevelRecord.class, sql.toString(), objects.size() == 0 ? null : objects.toArray());
    }

    public List<Integer> getApplicationTypesPerEduLevel(int eduLevelId) throws SQLException {
        return selectRecords("select application_type as value from nomenclatures.edu_level_to_app_type where edu_level_id = ?", IntegerValue.class, eduLevelId).stream().map(IntegerValue::getValue).collect(Collectors.toList());
    }

    public void updateEduLevelApplicationTypes(Integer eduLevelId, List<Integer> applicationTypes) throws SQLException {
        execute("DELETE FROM nomenclatures.edu_level_to_app_type where edu_level_id = ?", eduLevelId);
        if (applicationTypes != null) {
            for (Integer applicationType : applicationTypes) {
                execute("INSERT INTO nomenclatures.edu_level_to_app_type (edu_level_id, application_type) VALUES (?, ?)", eduLevelId, applicationType);
            }
        }
    }
    public int saveLegalReason(int id, String name, Integer appStatusId, String ordinanceArticle, String regulationArticle, String regulationText,Date dateFrom, Date dateTo, List<Integer> applicationTypes) throws SQLException {
        int res = id;
        LegalReasonRecord rec = new LegalReasonRecord(id, name, appStatusId, ordinanceArticle, regulationArticle, regulationText, Utils.getSqlDate(dateFrom), Utils.getSqlDate(dateTo));
        if (id == 0) {
            rec = insertRecord(rec);
            res = rec.getId();
        } else {
            updateRecord(rec);
            execute("DELETE FROM nomenclatures.legal_reason_to_app_type where legal_reason_id = ?", rec.getId());
        }
        if (!CollectionUtils.isEmpty(applicationTypes)) {
            for (Integer at : applicationTypes) {
                execute("INSERT INTO nomenclatures.legal_reason_to_app_type (application_type, legal_reason_id) VALUES (?, ?)", at, res);
            }
        }

        return res;
    }

    public List<Integer> getApplicationTypesPerLegalReasonId(int legalReasonId) throws SQLException {
        return selectRecords("select application_type as value from nomenclatures.legal_reason_to_app_type where legal_reason_id = ?", IntegerValue.class, legalReasonId).stream().map(IntegerValue::getValue).collect(Collectors.toList());
    }

    public List<LegalReasonRecord> getLegalReasonRecords(int applicationType, java.sql.Date toDate, OrderCriteria orderCriteria, Integer appStatusId) throws SQLException {
        String sql = " 1 = 1 ";
        List<Object> objects = new ArrayList<Object>();
        if (toDate != null) {
            sql += " AND (date_from <= ? OR date_from is null) ";
            sql += " AND (date_to >= ? OR date_to is null) ";
            objects.add(toDate);
            objects.add(toDate);
        }

        if (appStatusId != null && appStatusId != 0) {
            sql += " and app_status_id=?";
            objects.add(appStatusId);
        }
        if (applicationType > 0) {
            sql += " and id in (select legal_reason_id from nomenclatures.legal_reason_to_app_type where application_type = ?)";
            objects.add(applicationType);
        }

        if (orderCriteria == null) {
            sql += " ORDER BY NAME ";
        } else if (orderCriteria.getOrderColumn() != null) {
            sql += ((NomenclatureOrderCriteria) orderCriteria).getOrderSqlString();
        }
        return selectRecords(LegalReasonRecord.class, sql, objects.size() == 0 ? null : objects.toArray());

    }

    public List<ApplicationStatusRecordExtended> getApplicationStatusRecords(int seriesId, java.sql.Date toDate, OrderCriteria orderCriteria, boolean onlyLegal) throws SQLException {
        String sql = SELECT_APPLICATION_STATUSES;
        List<Object> objects = new ArrayList<Object>();
        objects.add(seriesId);
        if (toDate != null) {
            sql += " AND (date_from <= ? OR date_from is null) ";
            sql += " AND (date_to >= ? OR date_to is null) ";
            objects.add(toDate);
            objects.add(toDate);
        }

        if (onlyLegal) {
            sql += " AND is_legal = 1";
        }

        if (orderCriteria == null) {
            sql += " ORDER BY NAME ";
        } else if (orderCriteria.getOrderColumn() != null) {
            sql += ((NomenclatureOrderCriteria) orderCriteria).getOrderSqlString();
        }
        return selectRecords(sql, ApplicationStatusRecordExtended.class, objects.size() == 0 ? null : objects.toArray());
    }

    public ApplicationStatusRecordExtended getApplicationStatusRecord(int seriesId, int id) throws SQLException {
        String sql = SELECT_APPLICATION_STATUSES;
        List<Object> objects = new ArrayList<Object>();
        objects.add(seriesId);
        sql += " AND id = ?";
        objects.add(id);
        List<ApplicationStatusRecordExtended> recs = selectRecords(sql, ApplicationStatusRecordExtended.class, objects.toArray());
        return recs.size() == 0 ? null : recs.get(0);
    }

    public ApplicationStatusRecord saveApplicationStatusRecord(ApplicationStatusRecord record, int seriesId, boolean isLegal) throws SQLException {
        if (record.getId() != 0) {
            updateRecord(record);
        } else {
            record = insertRecord(record);
        }
        execute("DELETE FROM backoffice.app_status_entry_num_series WHERE app_status_id = ? and entry_num_series_id = ?", record.getId(), seriesId);
        execute("INSERT INTO backoffice.app_status_entry_num_series (app_status_id, entry_num_series_id, is_legal) VALUES (?, ?, ?)", record.getId(), seriesId, isLegal ? 1 : 0);
        return record;
    }

    public List<ApplicationDocflowStatusRecord> getApplicationDocflowStatusRecords(int seriesId, java.sql.Date toDate, OrderCriteria orderCriteria) throws SQLException {
        String sql = SELECT_APPLICATION_DOCFLOW_STATUSES;
        List<Object> objects = new ArrayList<Object>();
        objects.add(seriesId);
        if (toDate != null) {
            sql += " AND (date_from <= ? OR date_from is null) ";
            sql += " AND (date_to >= ? OR date_to is null) ";
            objects.add(toDate);
            objects.add(toDate);
        }

        if (orderCriteria == null) {
            sql += " ORDER BY NAME ";
        } else if (orderCriteria.getOrderColumn() != null) {
            sql += ((NomenclatureOrderCriteria) orderCriteria).getOrderSqlString();
        }
        return selectRecords(sql, ApplicationDocflowStatusRecord.class, objects.size() == 0 ? null : objects.toArray());
    }

    public ApplicationDocflowStatusRecord getApplicationDocflowStatusRecord(int seriesId, int id) throws SQLException {
        String sql = SELECT_APPLICATION_DOCFLOW_STATUSES;
        List<Object> objects = new ArrayList<Object>();
        objects.add(seriesId);
        sql += " AND id = ?";
        objects.add(id);
        List<ApplicationDocflowStatusRecord> recs = selectRecords(sql, ApplicationDocflowStatusRecord.class, objects.toArray());
        return recs.size() == 0 ? null : recs.get(0);
    }

    public ApplicationDocflowStatusRecord saveApplicationDocflowStatusRecord(ApplicationDocflowStatusRecord record, int seriesId) throws SQLException {
        if (record.getId() != 0) {
            updateRecord(record);
        } else {
            record = insertRecord(record);
        }
        execute("DELETE FROM backoffice.app_status_docflow_entry_num_series WHERE app_status_docflow_id = ? and entry_num_series_id = ?", record.getId(), seriesId);
        execute("INSERT INTO backoffice.app_status_docflow_entry_num_series (app_status_docflow_id, entry_num_series_id) VALUES (?, ?)", record.getId(), seriesId);
        return record;
    }

    public List<SecondarySpecialityRecord> getSecondarySpecialityRecords(String partOfName, java.sql.Date toDate, OrderCriteria orderCriteria, Integer profQualificationId) throws SQLException {
        String sql = " 1 = 1 ";
        List<Object> objects = new ArrayList<Object>();
        if (partOfName != null) {
            sql += "AND name ilike ?";
            objects.add("%" + partOfName + "%");
        }
        if (toDate != null) {
            sql += " AND (date_from <= ? OR date_from is null) ";
            sql += " AND (date_to >= ? OR date_to is null) ";
            objects.add(toDate);
            objects.add(toDate);
        }
        if (profQualificationId != null) {
            sql += " and prof_qualification_id = ? ";
            objects.add(profQualificationId);
        }

        if (orderCriteria == null) {
            sql += " ORDER BY NAME ";
        } else if (orderCriteria.getOrderColumn() != null) {
            sql += ((NomenclatureOrderCriteria) orderCriteria).getOrderSqlString();
        }
        return selectRecords(SecondarySpecialityRecord.class, sql, objects.size() == 0 ? null : objects.toArray());
    }

    public FlatNomenclatureRecord insertNomenclatureRecord(FlatNomenclatureRecord o) throws SQLException {
        return super.insertRecord(o);
    }

    public List<String> getBgAcademicRecognitionCitizenshipSuggestion(String columnName, String partOfValue) throws SQLException {
        List<StringValue> result = selectRecords("SELECT distinct " + columnName + " as value from bg_academic_recognition_info WHERE " + columnName + " ilike ? ORDER BY " + columnName, StringValue.class, "%" + partOfValue + "%");
        return result.stream().map(r -> r.getValue()).collect(Collectors.toList());
    }

    public DocumentReceiveMethodRecord getDocumentReceiveMethod(int id) throws SQLException {
        return selectRecord(DocumentReceiveMethodRecord.class, "id = ?", id);
    }

    public List<DocumentReceiveMethodRecord> getDocumentReceiveMethodRecords(java.sql.Date toDate, OrderCriteria orderCriteria) throws SQLException {
        StringBuilder sql = new StringBuilder("");
        List<Object> objects = new ArrayList<>();
        addToDateAndOrderCriteria(sql, objects, toDate, orderCriteria);
        return selectRecords(DocumentReceiveMethodRecord.class, sql.toString(), objects.size() == 0 ? null : objects.toArray());

    }
}
