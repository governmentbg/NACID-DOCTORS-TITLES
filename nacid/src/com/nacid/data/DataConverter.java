package com.nacid.data;

import com.nacid.bl.impl.Utils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataConverter {
	public static final String DATE_FORMAT = "dd.MM.yyyy";
	public static final String DATE_FORMAT_SHORT_YEAR = "dd.MM.yy";
	public static final String DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm";
	public static final String DATE_TIME_FORMAT_SECONDS = "dd.MM.yyyy HH:mm:ss";
	public static final String TIME_FORMAT = "HH:mm";
	public static final String TIME_FORMAT_SECONDS = "HH:mm:ss";
	public static final String YEAR_FORMAT = "yyyy";
	//public static DecimalFormat doubleFormatterDecPlaces2 = new DecimalFormat("0.00");
	public static DecimalFormatSymbols dfs;
	static {
		dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		//dfs.setGroupingSeparator(' ');
		//doubleFormatterDecPlaces2.setDecimalFormatSymbols(dfs);
	}
	/**
	 * @param date - ako date == null, vry6ta ""
	 * @return
	 */
	public static String formatDate(Date date) {
		return formatDate(date, "");
	}
	public static String formatDate(Date date, String returnValue) {
		if (date == null) {
			return returnValue;
		}
		SimpleDateFormat simpleDateFormat = null;
		simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		String formatedDate = simpleDateFormat.format(date);
		return formatedDate;
	}
	public static String formatYear(Date date) {
		return formatYear(date, "");
	}
	public static String formatYear(Date date, String returnValue) {
		if (date == null) {
			return returnValue;
		}
		SimpleDateFormat simpleDateFormat = null;
		simpleDateFormat = new SimpleDateFormat(YEAR_FORMAT);
		String formatedDate = simpleDateFormat.format(date);
		return formatedDate;
	}

	public static String formatDateTime(Date date, boolean addSeconds, String returnValue) {
		if (date == null) {
			return returnValue;
		}
		SimpleDateFormat simpleDateFormat = null;
		simpleDateFormat = new SimpleDateFormat(addSeconds ? DATE_TIME_FORMAT_SECONDS : DATE_TIME_FORMAT);
		String formatedDate = simpleDateFormat.format(date);
		return formatedDate;
	}
	public static String formatDateTime(Date date, boolean addSeconds) {
		return formatDateTime(date, addSeconds, "");
	}
	public static String formatTime(Date date, boolean addSeconds, String returnValue) {
		SimpleDateFormat simpleDateFormat = null;
		simpleDateFormat = new SimpleDateFormat(addSeconds ? TIME_FORMAT_SECONDS : TIME_FORMAT);
		String formatedDate = simpleDateFormat.format(date);
		return formatedDate;
	}
	public static String formatTime(Date date, boolean addSeconds) {
		return formatTime(date, addSeconds, "");
	}

	public static String formatDouble(Double value) {
		return formatDouble(value, 2);
	}
	public static String formatDouble(Double value, int digits) {
		if (value == null) {
			return "";
		}
		StringBuilder b = new StringBuilder("");
		for (int i=0; i < digits; i++) {
			b.append("0");
		}
		String pattern = "0." + b.toString();
		DecimalFormat formatter = new DecimalFormat(pattern);
		formatter.setDecimalFormatSymbols(dfs);
		return formatter.format(value);

	}
	public static String formatFloatingNumber(Number value) {
		return formatFloatingNumber(value, 2);
	}
	public static String formatFloatingNumber(Number value, int digits) {
		if (value == null) {
			return "";
		}
		StringBuilder b = new StringBuilder("");
		for (int i=0; i < digits; i++) {
			b.append("0");
		}
		String pattern = "0." + b.toString();
		DecimalFormat formatter = new DecimalFormat(pattern);
		formatter.setDecimalFormatSymbols(dfs);
		return formatter.format(value);

	}
	public static String formatInteger(Integer value) {
		if (value == null) {
			return "";
		}
		return value + "";
	}
	public static int parseInt(String value, int defaultValue) {
		try {
			return Integer.parseInt(value == null ? null : value.trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}
	public static Integer parseInteger(String value, Integer defaultValue) {
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	public static Float parseFloat(String value, Float defaultValue) {
		try {
			return Float.parseFloat(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	public static Double parseDouble(String value, Double defaultValue) {
		try {
			return Double.parseDouble(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	/*
	 * ako value == null || value == "" vry6ta defaultValue
	 */
	public static String parseString(String value, String defaultValue) {
		if (value == null || "".equals(value)) {
			return defaultValue;
		}
		return value;
	}
	//RayaWritten--------------------------------------------
	public static BigDecimal parseBigDecimal(String value, BigDecimal defaultValue){
	    if (StringUtils.isEmpty(value)) {
	    	return defaultValue;
		}
		BigDecimal bd = null;
	    value = value.replace(",", ".");
	    try{
	        bd = new BigDecimal(value);
	    } catch (NumberFormatException e){
	        return defaultValue;
	    }
	    return bd;
	}
	/**
	 * If there are redundant white spaces around the words - it removes them
	 * @param str - the string to be cleaned
	 */
	public static String removeRedundantWhitespaces(String str){
	    String[] words = str.trim().split("[\t ]+"); 
        String newStr = str;
        if(words.length>1){
            newStr = StringUtils.join(words, " ", 0, words.length);            
        } else if(words.length == 1){
            newStr = words[0];
        } else {
            newStr = null;
        }
        return newStr;
	}
	//---------------------------------------------------------
	public static boolean parseBoolean(String value) {
		return "1".equals(value) || Boolean.parseBoolean(value);
	}

	public static Date parseDate(String value) {
		if(value == null) {
			return null;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		try {
			return simpleDateFormat.parse(value);
		} catch (ParseException e) {
			return null;
		}
	}
	public static Date parseYear(String value) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YEAR_FORMAT);
		try {
			return simpleDateFormat.parse(value);
		} catch (Exception e) {
			return null;
		}
	}
	public static Date parseDateTime(String value, boolean addSeconds) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(addSeconds ? DATE_TIME_FORMAT_SECONDS : DATE_TIME_FORMAT);
		try {
			return simpleDateFormat.parse(value);
		} catch (ParseException e) {
			return null;
		}
	}
	public static String convertUrl(String url) {
		if (url == null || "".equals(url)) {
			return null;
		}
		if (url.contains("://")) {
			return url;
		}
		return "http://" + url;
	}
	public static java.sql.Date parseSqlDate(String date) {
		return Utils.getSqlDate(parseDate(date));
	}
	public static String formatSqlDate(java.sql.Date date) {
		return formatDate(date);
	}
	public static Timestamp parseTimestamp(String timestamp) {
		Date d = parseDateTime(timestamp, true);
		return d == null ? null : new Timestamp(d.getTime());
	}
	public static String formatTimestamp(Timestamp timestamp) {
		return formatDateTime(timestamp, true);
	}
	public static String escapeHtml(String html) {
		if (html == null) {
			return html;
		}
		html = html.replaceAll("&", "&amp;");
		html = html.replaceAll("\"", "&quot;");
		html = html.replaceAll("'", "&#039;");
		html = html.replaceAll("<", "&lt;");
		html = html.replaceAll(">", "&gt;");
		return html;
	}
	public static String unEscapeHtml(String html) {
		if (html == null) {
			return html;
		}
		html = html.replaceAll("&amp;", "&");
		html = html.replaceAll("&quot;", "\"");
		html = html.replaceAll("&#039;", "'");
		html = html.replaceAll("&lt;", "<");
		html = html.replaceAll("&gt;", ">");
		return html;
	}
	/**
	 * @return null, ako var == null; false, ako var == 0; true v drug slu4aj
	 */
	public static Boolean parseIntegerToBoolean(Integer var) {
		return var == null ? null :  (var == 0 ? false : true);
	}
	/**
	 * @return null ako var == null;<br />1 - ako var == true;<br /> 0 - ako var == false 
	 */
	public static  Integer parseBooleanToInteger(Boolean var) {
		return var == null ? null : (var ? 1 : 0);
	}

	
	public static Timestamp toTimestamp(java.util.Date date) {
		return date == null ? null : new Timestamp(date.getTime());
	}

	
	public static java.sql.Date parseLongShortDate(String date) {
		if (StringUtils.isEmpty(date)) {
			return null;
		}
		date = date.replace("г.", "").trim();
		String dateString = "^(\\d{1,2})[\\./]+(\\d{1,2})[\\./](\\d{2,4})$";
		if (!date.matches(dateString)) {
			return null;
		}
		Pattern p = Pattern.compile(dateString);
		Matcher m = p.matcher(date);
		String d = "";
		boolean isShort = false;
		if (m.find()) {
			d = m.group(1) + "." + m.group(2) + "." + m.group(3);
			if (m.group(3).length() != 4) {
				isShort = true;
			}
		}
		try {
			Date result = new SimpleDateFormat(isShort ? DATE_FORMAT_SHORT_YEAR : DATE_FORMAT).parse(d);
			return result == null ? null : new java.sql.Date(result.getTime());
		} catch (ParseException e) {
			return null;
		}
		
		
		/*
		  boolean dashes;
		String[] dateParts;
		  if (date.contains(".")) {
			dashes = false;
			dateParts = date.split("\\.");
		} else {
			dashes = true;
			dateParts = date.split("/");
		}
		if (dateParts.length != 3) {
			return null;
		}
		String dateFormat;
		if (dateParts[2].length() == 4) {
			if (dashes) {
				dateFormat = DATE_FORMAT_DASH;
			} else {
				dateFormat = DATE_FORMAT;
			}
		} else {
			if (dashes) {
				dateFormat = DATE_FORMAT_SHORT_YEAR_DASH;
			} else {
				dateFormat = DATE_FORMAT_SHORT_YEAR;
			}
		}
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		try {
			return df.parse(date);
		} catch (ParseException e) {
			return null;
		}*/
	}
	
	/**
     * slaga v map sydyrjanieto na lst, kato za key se polzva field-a zadaden s keyName, koito se trimva i mu se pravi toUpperCase
     * vika se getter methoda na T.keyName, primerno T = AutomakerRecord, keyName = "name" => vika se AutomakerRecord.getName() i stojnostta se zlaga za kliu4
     */
    public static <T, V> Map<V, T> convertListToMap(List<T> lst, String keyName, Class<V> cls) {
        if (lst == null) {
            return null;
        }
		/*return lst.stream().collect(Collectors.toMap(x -> {
			try {
				V key = (V) x.getClass().getMethod(MethodsUtils.generateGetterMethodName(keyName)).invoke(x);
				if (key instanceof String) {
					return (V) ((String) key).trim().toUpperCase();
				} else {
					return key;
				}
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
		}, Function.identity()));*/

        Map<V, T> result = new HashMap<V, T>();
        for (T el:lst) {
            try {
                V key = (V) el.getClass().getMethod(MethodsUtils.generateGetterMethodName(keyName)).invoke(el);
                if (key instanceof String) {
                    result.put((V) ((String)key).trim().toUpperCase(), el);    
                } else {
                    result.put(key, el);
                }
                
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            } catch (SecurityException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public static boolean equalIntegers(Integer i1, Integer i2) {
        if (i1 == null) {
            return i2 == null;
        }
        return i1.equals(i2);
    }








    public static boolean validateEGN(String input) {
        if (input == null) {
            return false;
        } else if (input.length() != 10) {
			return false;
		} else {

            int[] digits = new int[10];
            int[] coeffs = {2, 4, 8, 5, 10, 9, 7, 3, 6};
            int[] days = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};


            for (int i = 0; i < input.length(); i++) {
                Integer digit = DataConverter.parseInteger(input.charAt(i) + "", null);
                if (digit == null) {
                    break;
                }
                digits[i] = digit;
            }

            if (10 != digits.length) {
                return false;
            }

            int dd = digits[4] * 10 + digits[5];
            int mm = digits[2] * 10 + digits[3];
            int yy = digits[0] * 10 + digits[1];
            Integer yyyy = null;

            if (mm >= 1 && mm <= 12) {
                yyyy = 1900 + yy;
            }
            else if (mm >= 21 && mm <= 32) {
                mm -= 20; yyyy = 1800 + yy;
            }
            else if (mm >= 41 && mm <= 52) {
                mm -= 40; yyyy = 2000 + yy;
            } else {
                return false;
            }

            days[1] += isLeapYear(yyyy) ? 1 : 0;

            if (!(dd >= 1 && dd <= days[mm - 1])) {
                return false;
            }

            // Gregorian calendar adoption. 31 Mar 1916 was followed by 14 Apr 1916.
            if (yyyy == 1916 && mm == 4 && (dd >= 1 && dd < 14)) {
                return false;
            }

            int checksum = 0;

            for (int j = 0; j < coeffs.length; j++) {
                checksum = checksum + (digits[j] * coeffs[j]);
            }
            checksum %= 11;
            if (10 == checksum) {
                checksum = 0;
            }

            if (digits[9] != checksum) {
                return false;
            }
        }
        return true;
    }

	public static boolean validateLNCH(String input) {
		if (input == null) {
			return false;
		} else if (input.length() != 10) {
			return false;
		} else {

			int[] digits = new int[10];
			int[] coeffs = {21, 19, 17, 13, 11, 9, 7, 3, 1};
			for (int i = 0; i < input.length(); i++) {
				Integer digit = DataConverter.parseInteger(input.charAt(i) + "", null);
				if (digit == null) {
					break;
				}
				digits[i] = digit;
			}

			if (10 != digits.length) {
				return false;
			}

			int checksum = 0;

			for (int j = 0; j < coeffs.length; j++) {
				checksum = checksum + (digits[j] * coeffs[j]);
			}
			checksum %= 10;

			if (digits[9] != checksum) {
				return false;
			}
		}
		return true;
	}


    private static boolean isLeapYear(int yyyy) {
        if (yyyy % 400 == 0) {
            return true;
        }
        if (yyyy % 100 == 0) {
            return false;
        }
        if (yyyy % 4 == 0) {
            return true;
        }

        return false;
    }

    public static void main(String[] args) {
        System.out.println(validateEGN("asdb"));
    }
}
