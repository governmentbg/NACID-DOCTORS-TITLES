package com.nacid.utils;

import com.nacid.data.applications.PersonRecord;
import com.nacid.db.applications.ApplicationsDB;
import com.nacid.db.nomenclatures.NomenclaturesDB;
import com.nacid.db.utils.StandAloneDataSource;
import org.apache.commons.lang.StringUtils;

import java.sql.SQLException;
import java.util.List;

public class RemoveDuplicatePersons {

    /**
     * @param args
     */
    private static final String SQL = "select count(*)::integer as id, upper(fname) as fname, upper(sname) as sname, upper(lname) as lname, civil_id, civil_id_type,null as birth_country_id, null as birth_city, null as birth_date, null as citizenship_id " + 
                                        " from person " + 
                                        " where civil_id is not null " +
                                        " group by civil_id, civil_id_type, upper(fname), upper(sname), upper(lname) " +
                                        " having count(*) > 1";
    public static void main(String[] args) throws SQLException {
        // TODO Auto-generated method stub
        //StandAloneDataSource ds = new StandAloneDataSource();
        StandAloneDataSource ds = new StandAloneDataSource("jdbc:postgresql://localhost:9999/NACID/", "postgres", "postgres");
        ApplicationsDB db = new ApplicationsDB(ds, new NomenclaturesDB(ds));
        List<PersonRecord> persons = db.selectRecords(SQL, PersonRecord.class);
        for (PersonRecord p:persons) {
            List<PersonRecord> personsByCivilIdType = db.selectRecords(PersonRecord.class, "civil_id = ? and civil_id_type = ? and (case when ?::varchar is null then null is null else upper(fname) = ? end ) and (case when ?::varchar is null then null is null else upper(sname) = ? end ) and (case when ?::varchar is null then null is null else upper(lname) = ? end ) order by id", p.getCivilId(), p.getCivilIdType(), p.getFName(), p.getFName(), p.getSName(), p.getSName(), p.getLName(), p.getLName());
            Integer personToRemain = getBetterPerson(personsByCivilIdType); //potrebitelq, koito shte ostane - tozi s nai-mnogo popylneni danni
            System.out.println(StringUtils.join(personsByCivilIdType, "\n"));
            for (int i = 0; i < personsByCivilIdType.size(); i++) {
                PersonRecord pr = personsByCivilIdType.get(i);
                if (pr.getId().intValue() == personToRemain.intValue()) {
                    continue;
                }
                db.execute("UPDATE application set applicant_id = " + personToRemain + " where applicant_id = " + pr.getId());
                db.execute("UPDATE application set representative_id = " + personToRemain + " where representative_id = " + pr.getId());
                db.execute("DELETE FROM person where id = " + pr.getId());
            }
        }
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
