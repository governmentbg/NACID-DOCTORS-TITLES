package com.nacid.bl.impl.regprof;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.nacid.bl.RequestParameterInterface;
import com.nacid.bl.regprof.ProfessionalInstitution;
import com.nacid.data.annotations.Table;
//RayaWritten---------------------------------------------------------------------------------
@Table(name="regprof.professional_institution")
public class ProfessionalInstitutionImpl implements ProfessionalInstitution, RequestParameterInterface {

    private Integer id;
    private Integer countryId;
    private String bgName;
    private String city;
    private String addrDetails;
    private String phone;
    private String fax;
    private String email;
    private String webSite;
    @DateTimeFormat(pattern="dd.MM.yyyy") 
    private Date dateFrom = new Date();
    private Date dateTo;
    private String urlDiplomaRegister;
    private Integer professionalInstitutionTypeId;
    
    public ProfessionalInstitutionImpl() { }
    
    public ProfessionalInstitutionImpl(Integer id, Integer countryId,
            String bgName, String city, String addrDetails, String phone,
            String fax, String email, String webSite, Date dateFrom,
            Date dateTo, String urlDiplomaRegister,
            Integer professionalInstitutionTypeId) {
        super();
        this.id = id;
        this.countryId = countryId;
        this.bgName = bgName;
        this.city = city;
        this.addrDetails = addrDetails;
        this.phone = phone;
        this.fax = fax;
        this.email = email;
        this.webSite = webSite;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.urlDiplomaRegister = urlDiplomaRegister;
        this.professionalInstitutionTypeId = professionalInstitutionTypeId;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getCountryId() {
        return countryId;
    }
    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }
    public String getBgName() {
        return bgName;
    }
    public void setBgName(String bgName) {
        this.bgName = bgName;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getAddrDetails() {
        return addrDetails;
    }
    public void setAddrDetails(String addrDetails) {
        this.addrDetails = addrDetails;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getFax() {
        return fax;
    }
    public void setFax(String fax) {
        this.fax = fax;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getWebSite() {
        return webSite;
    }
    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }
    public Date getDateFrom() {
        return dateFrom;
    }
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }
    public Date getDateTo() {
        return dateTo;
    }
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
    public String getUrlDiplomaRegister() {
        return urlDiplomaRegister;
    }
    public void setUrlDiplomaRegister(String urlDiplomaRegister) {
        this.urlDiplomaRegister = urlDiplomaRegister;
    }
    public Integer getProfessionalInstitutionTypeId() {
        return professionalInstitutionTypeId;
    }
    public void setProfessionalInstitutionTypeId(
            Integer professionalInstitutionTypeId) {
        this.professionalInstitutionTypeId = professionalInstitutionTypeId;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ProfessionalInstitutionImpl [name=").append(bgName).append(", city=").append(city).append(
                ", addrDetails=").append(addrDetails).append(", phone=").append(phone).append(", fax=")
                .append(fax).append(", email=").append(email).append(", website=").append(webSite)
                .append(", id=").append(id).append(", dateFrom=").append(dateFrom).append(", dateTo=").append(
                        dateTo).append(", professionalInstitutionTypeId=").append(professionalInstitutionTypeId).append(", countryId=").append(
                        countryId).append("]");
        return builder.toString();
    }
    
}
//------------------------------------------------------------------------------