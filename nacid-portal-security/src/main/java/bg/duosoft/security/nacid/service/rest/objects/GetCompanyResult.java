package bg.duosoft.security.nacid.service.rest.objects;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class GetCompanyResult extends BaseResult {
//    @JsonProperty
    private Integer companyId;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
