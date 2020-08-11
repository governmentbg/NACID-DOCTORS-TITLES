package com.nacid.data;

import java.lang.reflect.Method;

public class MethodsUtils {
  /**
   * @param variableName
   * @return - ako se podade variableName, vry6ta getVariableName
   */
  public static String generateGetterMethodName(String variableName) {
    return "get" + Character.toUpperCase(variableName.charAt(0)) + variableName.substring(1);
  }
  /**
   * @param variableName
   * @return vry6ta isVariableName - zashtoto po convenciq boolean parametrite mogat da imat ili isName ili getName getters
   */
  private static String generateIsMethodName(String variableName) {
    return "is" + Character.toUpperCase(variableName.charAt(0)) + variableName.substring(1);
  }
  /**
   * @param variableName
   * @return - ako se podade variableName, vry6ta setVariableName
   */
  private static String generateSetterMethodName(String variableName) {
    return "set" + Character.toUpperCase(variableName.charAt(0)) + variableName.substring(1);
  }
  public static Method getGetterMethod(Object o, String variableName) throws SecurityException, NoSuchMethodException {
    try {
      return o.getClass().getMethod(generateGetterMethodName(variableName));  
    } catch (SecurityException e) {
      return o.getClass().getMethod(generateIsMethodName(variableName));
    } catch (NoSuchMethodException e) {
      return o.getClass().getMethod(generateIsMethodName(variableName));
    }
    
  }
  public static Method getSetterMethod(Object o, String variableName) throws SecurityException, NoSuchMethodException {
    Method getterMehtod = getGetterMethod(o, variableName);
    return o.getClass().getMethod(generateSetterMethodName(variableName), getterMehtod.getReturnType());
  }
}
