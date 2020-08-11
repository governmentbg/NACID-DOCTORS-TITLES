package com.nacid.web.handlers.impl.suggestions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.ApplicationsDataProvider;
import com.nacid.bl.applications.Person;
import com.nacid.bl.applications.PersonDocument;
import com.nacid.bl.applications.regprof.RegprofApplicationDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.data.applications.PersonDocumentRecord;
import com.nacid.web.handlers.NacidBaseRequestHandler;

public class CivilIdSuggestionHandler extends NacidBaseRequestHandler{

  public CivilIdSuggestionHandler(ServletContext servletContext) {
    super(servletContext);
  }

  public void processRequest(HttpServletRequest request, HttpServletResponse response) {
    int civilIdType = DataConverter.parseInt(request.getParameter("civilidtype"), -1);
    if (civilIdType == -1) {
      return;
    }
    String partOfCivilId = DataConverter.parseString(request.getParameter("query"), "");
    NacidDataProvider nacidDataProvider = getNacidDataProvider();
    ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
    RegprofApplicationDataProvider regprofAppDataProvider = nacidDataProvider.getRegprofApplicationDataProvider();
    List<Person> persons = applicationsDataProvider.getPersons(civilIdType, partOfCivilId, true);
    List<PersonDocument> documents = regprofAppDataProvider.getPersonsDocuments(persons);
    try {
      JSONObject jsonObj = new JSONObject();
      jsonObj.put("query", partOfCivilId);
      List<String> suggestions = new ArrayList<String>();
      List<String> inputvalues = new ArrayList<String>();
      List<String> data = new ArrayList<String>();
      if (persons != null) {
        for (int i=0; i<persons.size(); i++) {
          
          JSONObject d = new JSONObject();
          inputvalues.add(persons.get(i).getCivilId() );
          suggestions.add(persons.get(i).getCivilId() + " - " + persons.get(i).getFName() + "  " + persons.get(i).getLName());

          d.put("recordid", persons.get(i).getId());
          d.put("fname", DataConverter.parseString(persons.get(i).getFName(), ""));
          d.put("sname", DataConverter.parseString(persons.get(i).getSName(), ""));
          d.put("lname", DataConverter.parseString(persons.get(i).getLName(), ""));
          d.put("civilid", DataConverter.parseString(persons.get(i).getCivilId(), ""));
          d.put("civilidtype", persons.get(i).getCivilIdTypeId());
          d.put("birthcountryid", persons.get(i).getBirthCountryId());
          d.put("birthcity", DataConverter.parseString(persons.get(i).getBirthCity(), ""));
          d.put("birthdate", DataConverter.formatDate(persons.get(i).getBirthDate()));
          d.put("citizenshipid", persons.get(i).getCitizenshipId());
          if(documents.get(i) != null){
              d.put("documentid", documents.get(i).getId());
              d.put("dateofissue", DataConverter.formatDate(documents.get(i).getDateOfIssue()));
              d.put("issuedby", DataConverter.parseString(documents.get(i).getIssuedBy(), ""));
              d.put("number", DataConverter.parseString(documents.get(i).getNumber(), ""));
          } else {
              d.put("documentid", "");
              d.put("dateofissue", "");
              d.put("issuedby", "");
              d.put("number", "");
          }
          data.add(d.toString());
        }        
      }

      jsonObj.put("suggestions", suggestions);
      jsonObj.put("inputvalues", inputvalues);
      jsonObj.put("data", data);

      writeToResponse(response, jsonObj.toString());
      
    } catch (JSONException e) {
      e.printStackTrace();
    }

  }
  public static void main(String[] args) {
  }

}
