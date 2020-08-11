package com.nacid.bl.nomenclatures;

import java.util.Date;

public interface FlatNomenclature {
    
  public int getId();
  public String getName();
  public Date getDateFrom();
  public Date getDateTo();
  /**
   * @return edin ot tipovete, definirani v {@link NomenclaturesDataProvider}
   */
  public abstract int getNomenclatureType();
  public boolean isActive();
}
