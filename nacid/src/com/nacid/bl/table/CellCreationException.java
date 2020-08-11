package com.nacid.bl.table;

public class CellCreationException extends Exception {
  String message;
  public CellCreationException(String message) {
    this.message = message;
  }
  @Override
  public String getMessage() {
    return message;
  }
  
}
