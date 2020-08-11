package com.nacid.bl.nomenclatures;


public interface LegalReason extends FlatNomenclature{
  public Integer getApplicationStatusId();
  public ApplicationStatus getApplicationStatus();

  public String getOrdinanceArticle();

  public String getRegulationArticle();

  public String getRegulationText();

  public String getRegulationTextPerEducationLevelId(int educationLevelId);
}
