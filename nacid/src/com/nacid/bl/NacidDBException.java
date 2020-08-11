package com.nacid.bl;

@SuppressWarnings("serial")
public class NacidDBException extends RuntimeException {
  
  public NacidDBException(Throwable t) {
    super(t);
  }
  
  public NacidDBException(String message, Throwable t) {
    super(message, t);
  }
}
