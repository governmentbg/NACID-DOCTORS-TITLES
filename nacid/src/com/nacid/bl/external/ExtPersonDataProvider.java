package com.nacid.bl.external;

import java.util.Date;

public interface ExtPersonDataProvider {

    public ExtPerson getExtPerson(int id);
    
    public ExtPerson activateExtPerson(String hashCode);

    public ExtPerson getExtPerson(int civilIdType, String civilId);

    public ExtPerson getExtPersonByUserId(int userId);

    public int saveExtPerson(int id, String fname, String sname, String lname, String civilId, Integer civilIdType, Integer birthCountryId,
            String birthCity, Date birthDate, Integer citizenshipId, String email, String hashCode, Integer userId);

    public ExtPersonDocument getExtPersonActiveDocument(int extPersonId);
    
    public ExtPersonDocument getExtPersonDocument(int documentId);
}
