package com.nacid.web.taglib;

public class FormInputUtils {
  public static final String COMBO_BOX_SELECTED = "selected=\"selected\"";
  public static final String COMBO_BOX_NOT_SELECTED = "";
  
  public static final String CHECK_BOX_CHECKED = "checked=\"checked\"";
  public static final String CHECK_BOX_NOT_CHECKED = "";
  
  public static final String RADIO_BUTTON_CHECKED = "checked=\"checked\"";
  public static final String RADION_BUTTON_NOT_CHECKED = "";
  
  public static String getComboBoxSelectedText(boolean selected) {
    return selected ? COMBO_BOX_SELECTED : COMBO_BOX_NOT_SELECTED;
  }
  public static String getCheckBoxCheckedText(boolean selected) {
    return selected ? CHECK_BOX_CHECKED : CHECK_BOX_NOT_CHECKED;
  }
  public static String getRadioButtionCheckedText(boolean checked) {
    return checked ? RADIO_BUTTON_CHECKED : RADION_BUTTON_NOT_CHECKED;
  }
}
