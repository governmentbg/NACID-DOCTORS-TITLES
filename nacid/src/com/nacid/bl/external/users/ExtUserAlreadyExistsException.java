package com.nacid.bl.external.users;

import com.nacid.bl.exceptions.UserAlreadyExistsException;

public class ExtUserAlreadyExistsException extends UserAlreadyExistsException {
  public ExtUserAlreadyExistsException(String message) {
    super(message);
  }
}
