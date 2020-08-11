package com.nacid.utils;

import com.nacid.data.applications.PersonRecord;
import com.nacid.data.nomenclatures.CountryRecord;
import com.nacid.db.applications.ApplicationsDB;
import com.nacid.db.nomenclatures.NomenclaturesDB;
import com.nacid.db.utils.StandAloneDataSource;
import org.apache.commons.lang.StringUtils;

import java.sql.SQLException;
import java.util.*;

public class GetDuplicatePersons {

    /**
     * @param args
     */
    private static final String SQL = "select count(*)::integer as id, null as fname, null as sname, null as lname, civil_id, civil_id_type,null as birth_country_id, null as birth_city, null as birth_date, null as citizenship_id " + 
                                        " from person " + 
                                        " where civil_id is not null " +
                                        " group by civil_id, civil_id_type" +
                                        " having count(*) > 1";
    public static void main(String[] args) throws SQLException {
        // TODO Auto-generated method stub
        //StandAloneDataSource ds = new StandAloneDataSource();
        StandAloneDataSource ds = new StandAloneDataSource("jdbc:postgresql://localhost:9999/NACID/", "postgres", "postgres");
        ApplicationsDB db = new ApplicationsDB(ds, new NomenclaturesDB(ds));
        List<PersonRecord> persons = db.selectRecords(SQL, PersonRecord.class);
        List<CountryRecord> countries = db.selectRecords(CountryRecord.class,null);
        Map<Integer, CountryRecord> countriesMap = new HashMap<Integer, CountryRecord>();
        for (CountryRecord c:countries) {
            countriesMap.put(c.getId(), c);
        }
        Set<Integer> ids = new HashSet<Integer>();
        for (PersonRecord p:persons) {
            List<PersonRecord> personsByCivilIdType = db.selectRecords(PersonRecord.class, "civil_id = ? and civil_id_type = ? order by id", p.getCivilId(), p.getCivilIdType());
            for (int i = 0; i < personsByCivilIdType.size(); i++) {
                PersonRecord pr = personsByCivilIdType.get(i);
                ids.add(pr.getId());
            }
        }
        persons = db.selectRecords(PersonRecord.class, "id in ( " + StringUtils.join(ids, ",") + ") order by civil_id, civil_id_type");
        for (PersonRecord p:persons) {
            CountryRecord birthCountry = countriesMap.get(p.getBirthCountryId());
            CountryRecord citizenship = countriesMap.get(p.getCitizenshipId());
            System.out.println(p.getFName() + "\t" + p.getSName() + "\t" + p.getLName() + "\t" + p.getCivilIdType() + "\t`" + p.getCivilId() + "\t" + (birthCountry == null ? "" : birthCountry.getName()) + "\t" + (citizenship == null ? "" : citizenship.getName()) + "\t" + p.getBirthDate() + "\t" + p.getBirthCity());
        }
        System.out.println(StringUtils.join(persons, "\n"));
    }
    
    /**
     * @param string
     * @return string == null ? null : string.trim().toUpperCase()
     */
    public static String toUpperCase(String string) {
        return string == null ? null : string.trim().toUpperCase();
    }
    
    
    /**
     * vry6ta PersonRecord-a s poveche popylneni danni
     */
    private static int getBetterPerson(List<PersonRecord> persons) {
        Integer bestCnt = 0;
        Integer result = null;
        for (PersonRecord p:persons) {
            int cnt = 0;
            if (p.getBirthCountryId() != null) {
                cnt++;
            }
            if (!StringUtils.isEmpty(p.getBirthCity())) {
                cnt++;
            }
            if (p.getBirthDate() != null) {
                cnt++;
            }
            if (p.getCitizenshipId() != null) {
                cnt++;
            }
            if (bestCnt < cnt) {
                bestCnt = cnt;
                result = p.getId();
            }
        }
        return result == null ? persons.get(0).getId() : result;
    }

}
