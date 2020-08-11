package com.nacid.web.model.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.applications.University;
import com.nacid.bl.applications.UniversityValidity;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclatureOrderCriteria;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.DataConverter;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class UniversityValidityWebModel {

    private int id = 0;
    private String examinationDate = DataConverter.formatDate(new Date());
    private String university = "";
    private String universityId = "";
    private String universityCountryId = "";
    private String recognized = "";//boolean
    private String communicated = "";//boolean
    private List<NameValueHolder> trainingLocations;
    private List<NameValueHolder> trainingForms;
    private String hasJoinedDegrees = "";//boolean
    private String trainingFormOtherText = "";
    private String institution = ""; //coma sep ids
    private String otherFormSelected = "";
    private String notes;
    private int appId = 0;
    private String appOper = "";
    private String groupName = "";
    
    private final static String CHECKED = "checked=\"checked\"";
    //private final static String SELECTED = "selected=\"selected\"";
    
    public UniversityValidityWebModel(NacidDataProvider nDP, UniversityValidity univValidity,
            University university, int appId, String appOper, String groupName) {
        
        this.university = university.getBgName();
        this.universityId = university.getId() + "";
        this.universityCountryId = university.getCountryId() + "";
        this.appId = appId;
        this.appOper = appOper;
        this.groupName = groupName;
        
        NomenclaturesDataProvider nomDP = nDP.getNomenclaturesDataProvider();
        
        trainingLocations = new ArrayList<NameValueHolder>();
        List<FlatNomenclature> locs = nomDP.getFlatNomenclatures(
                NomenclaturesDataProvider.FLAT_NOMENCLATURE_TRAINING_LOCATION, new Date(), OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, true));
        for(FlatNomenclature trLoc : locs) {
            NameValueHolder element = new NameValueHolder();
            element.setName(trLoc.getName());
            element.setValue(trLoc.getId() + "");
            if(univValidity != null && univValidity.getTrainingLocationId() != null
                    && univValidity.getTrainingLocationId().intValue() == trLoc.getId()) {
                element.setSelected(CHECKED);
            }
            trainingLocations.add(element);
        }
        
        trainingForms = new ArrayList<NameValueHolder>();
        List<FlatNomenclature> forms = nomDP.getFlatNomenclatures(
                NomenclaturesDataProvider.FLAT_NOMENCLATURE_TRAINING_FORM, new Date(), OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, true));
        Object[] selectedForms = null;
        if(univValidity != null) {
            selectedForms = univValidity.getTrainingForms();
        }
        for(FlatNomenclature trForm : forms) {
            NameValueHolder element = new NameValueHolder();
            element.setName(trForm.getName());
            element.setValue(trForm.getId() + "");
            if(selectedForms != null) {
                for(Object o : selectedForms) {
                    if(o instanceof Integer && ((Integer)o).intValue() == trForm.getId()) {
                        element.setSelected(CHECKED);
                        break;
                    }
                }
            }
            trainingForms.add(element);
        }
        
        if(univValidity == null) {
            return;
        }
        
        if (selectedForms != null) {
            for (Object o : selectedForms) {
                if (o instanceof String) {
                    trainingFormOtherText = (String) o;
                    otherFormSelected = CHECKED;
                    break;
                }
            }
        }
        
        id = univValidity.getId();
        examinationDate = DataConverter.formatDate(univValidity.getExaminationDate());
        
        if(univValidity.isRecognized()) {
            recognized = CHECKED;
        }
        if(univValidity.isComunicated()) {
            communicated =  CHECKED;
        }
        
        if(univValidity.isHasJoinedDegrees()) {
            hasJoinedDegrees = CHECKED;
        }
        
        if(univValidity.getSourcesOfInformation() != null) {
            List<String> insts = new ArrayList<String>();
            for(int i : univValidity.getSourcesOfInformation()) {
                insts.add(i + "");
            }
            institution = StringUtils.join(insts, ";");
        }
        notes = univValidity.getNotes();
    }

    public int getId() {
        return id;
    }

    public String getExaminationDate() {
        return examinationDate;
    }

    public String getUniversity() {
        return university;
    }

    public String getRecognized() {
        return recognized;
    }

    public String getCommunicated() {
        return communicated;
    }

    public List<NameValueHolder> getTrainingLocations() {
        return trainingLocations;
    }

    public List<NameValueHolder> getTrainingForms() {
        return trainingForms;
    }

    public String getHasJoinedDegrees() {
        return hasJoinedDegrees;
    }

    public String getTrainingFormOtherText() {
        return trainingFormOtherText;
    }

    public String getInstitution() {
        return institution;
    }

    public String getOtherFormSelected() {
        return otherFormSelected;
    }

    public String getUniversityId() {
        return universityId;
    }

    public String getNotes() {
        return notes;
    }

    public int getAppId() {
        return appId;
    }

    public String getAppOper() {
        return appOper;
    }

	public String getGroupName() {
		return groupName;
	}

    public String getUniversityCountryId() {
        return universityCountryId;
    }
}
