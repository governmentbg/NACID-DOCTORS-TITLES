package com.nacid.web.model.common;

import java.util.ArrayList;
import java.util.List;

public class SystemMessageWebModel {
  public static final int MESSAGE_TYPE_ERROR = 1;
  public static final int MESSAGE_TYPE_CORRECT = 2;
  private String title;
  private int messageType = MESSAGE_TYPE_ERROR;
  private List<String> attributes = new ArrayList<String>();
  /**
   * syzdava system message ot tip MESSAGE_TYPE_ERROR
   */
  public SystemMessageWebModel() {
    this("", MESSAGE_TYPE_ERROR);
  }
  /**
   * syzdava system message ot tip MESSAGE_TYPE_ERROR
   * @param title
   */
  public SystemMessageWebModel(String title) {
    this(title, MESSAGE_TYPE_ERROR);
  }
  public SystemMessageWebModel(String title, int messageType) {
    this.title = title;
    this.messageType = messageType;
  }
  public void addAttribute(String attribute) {
    attributes.add(attribute);
  }
  public void setAttributes(List<String> attributes) {
    this.attributes = attributes;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }
  public boolean hasAttributes() {
    return attributes.size() != 0;
  }
  public String getTitle() {
    return title;
  }
  public List<String> getAttributes() {
    return attributes;
  }
  public int getMessageType() {
    return messageType;
  }
  public void setMessageType(int messageType) {
    this.messageType = messageType;
  }
  public String getStyle() {
    if (messageType == MESSAGE_TYPE_ERROR) {
      return "error";
    } else if (messageType == MESSAGE_TYPE_CORRECT) {
      return "correct";
    } else {
      return "";
    }
  }
  public static SystemMessageWebModel createDataInsertedWebMessage() {
      return new SystemMessageWebModel("Данните бяха въведени в базата", MESSAGE_TYPE_CORRECT);
  }
}
