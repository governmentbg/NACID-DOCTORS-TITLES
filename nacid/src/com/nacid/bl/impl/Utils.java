package com.nacid.bl.impl;


import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.nacid.bl.NacidDBException;
import com.nacid.data.DataConverter;

public class Utils {

  public static String getLocalized(boolean en, boolean tranlateBGtoLatin, String bgLabel, String enLabel) {
    //return (en) ? ((enLabel != null) ? enLabel : (tranlateBGtoLatin ? DataTypeUtils.translateToLatin(bgLabel) : null)) : ((bgLabel != null) ? bgLabel : enLabel);
  	return null;
  }

  public static NacidDBException logException(Object source, String message, Exception e) {
	  e.printStackTrace();
	  //    Logger logger = Logger.getLogger((source != null) ? source.getClass() : Utils.class);
    if (message != null) {
  //    logger.error(message, e);
    } else {
   //   logger.error("", e);
    }
    
    return new NacidDBException(e);
  }

  public static NacidDBException logException(Object source, Exception e) {
    return logException(source, null, e);
  }
  
  public static NacidDBException logException(Exception e) {
    return logException(null, null, e);
  }
  
  public static Date getToday() {
    GregorianCalendar cal = new GregorianCalendar();
    clearTimeFields(cal);
    return cal.getTime();
  }
  
  public static Date getYesterday() {
    GregorianCalendar cal = new GregorianCalendar();
    clearTimeFields(cal);
    cal.add(Calendar.DATE, -1);
    return cal.getTime();
  }
  
  public static Date getTomorrow() {
    GregorianCalendar cal = new GregorianCalendar();
    clearTimeFields(cal);
    cal.add(Calendar.DATE, 1);
    return cal.getTime();
  }
  
  public static void clearTimeFields(Calendar calendar) {
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
  }

  
  public static long yearAndMonthToMillis(int year, int month) {
    GregorianCalendar cal = new GregorianCalendar(year, month, 1);
    cal.getTimeInMillis();
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    
    return cal.getTimeInMillis();
  }
  
  public static java.sql.Date getSqlDate(java.util.Date date) {
    return date == null ? null : new java.sql.Date(date.getTime());
  }
  public static java.sql.Timestamp getTimestamp(java.util.Date date) {
      return date == null ? null : new java.sql.Timestamp(date.getTime());
  }
  
  public static boolean isRecordActive(Date dateFrom, Date dateTo) {
      Date d = Utils.getToday();
      if ( (dateTo == null || dateTo.getTime() >= d.getTime()) &&
          (dateFrom == null || dateFrom.getTime() <= d.getTime() )) {
        return true;
      }
      return false;
  }
  public static String isRecordActiveText(boolean active) {
	  return active ? "" : " (inactive) ";
  }
  
  public static String getMD5HashString(String string) {
      try {
          MessageDigest md = MessageDigest.getInstance("MD5");
          md.update(string.getBytes());
          byte[] md5 = md.digest();
          String ret = "";
          for(byte b : md5) {
              ret += Integer.toString((b & 0xFF) + 0x100, 16).substring(1);
          }
          return ret;
      } catch (Exception e) {
          throw Utils.logException(e);
      }
  }
  
  public static boolean isEmptyString(String s) {
      return (s == null || s.length() == 0);
  }
  
  public static String escapeUrl(String url) {
      try {
        return URLEncoder.encode(url, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        throw logException(e);
      }
  }
  
  public static String unEscapeUrl(String encodedUrl) {
      try {
        return URLDecoder.decode(encodedUrl, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        throw logException(e);
      }
  }
  
  public static <T> T getListFirstElement(List<T> list) {
      if(list == null || list.isEmpty())
          return null;
      
      return list.get(0);
  }
  public static String generateDocFlowNumber(Date appDate, String appNum) {
      return appNum + "/" + DataConverter.formatDate(appDate);
  }
  public static <T> T getListLastElement(List<T> list) {
      if(list == null || list.isEmpty())
          return null;
      
      return list.get(list.size() - 1);
  }
  public static final boolean isEmpty(List<?> list) {
      return list == null || list.size() == 0;
  }
  /**
   * 
   * @param textToSign
   * @param key
   * @param algorithm - examples - HmacSHA1, HmacSHA256, HmacMD5
   * @return
   * @throws NoSuchAlgorithmException
   */
  public static String signText(String textToSign, String key, String algorithm) throws NoSuchAlgorithmException {
      final Charset asciiCs = Charset.forName("US-ASCII");
      final Mac sha1_HMAC = Mac.getInstance(algorithm);
      final SecretKeySpec secret_key = new javax.crypto.spec.SecretKeySpec(asciiCs.encode(key).array(), algorithm);
      try {
          sha1_HMAC.init(secret_key);
      } catch (InvalidKeyException e) {
          e.printStackTrace();
      }
      final byte[] mac_data = sha1_HMAC.doFinal(asciiCs.encode(textToSign).array());
      String signedText = "";
      for (final byte element : mac_data) {
          signedText += Integer.toString((element & 0xff) + 0x100, 16).substring(1);
      }
      return signedText;
      //System.out.println("SignedText:  " + signedText);
  }
  //RayaWritten-------------------------------------------------------
  public static boolean fieldChanged(Object old, Object changed){
      if(old != null && changed != null){
          if(!old.equals(changed)){
              return true;
          } else {
              return false;
          }
      } else if(old == null && changed == null){
          return false;
      } else {
          return true;
      }
  }
  //------------------------------------------------------------------------
  public static Date localDateToDate(LocalDate localDate) {
      return localDate == null ? null : Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }

    public static LocalDate dateToLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date instanceof java.sql.Date ? ((java.sql.Date) date).toLocalDate() : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    public static boolean isUrlValid(String value ) {
        try {
            URL url = new URL(value);
            return true;
        } catch (Exception e ) {
            return false;
        }
    }
}
