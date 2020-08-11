package com.nacid.utils.csv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

import com.nacid.data.DataConverter;
import com.nacid.data.MethodsUtils;
import com.nacid.data.applications.AppStatusHistoryRecord;

/**
*    Parses CSV file and sets all parsed values into record, which class is given to the Constructor, invoking it's setter methods.
*    <pre>
*    Configuration file shall be presented to map CSV file headers to fieldNames
*    Format is in the form:
*    {field_name}=datatypecolumn_position_in_csv_file
*    {field_name} is the name of the field - it's setter method is invoked (example - fieldName = "name", invokes setName)
*    example for file:
*    name=I1
*    value=C2
*    </pre>
*
*/

public class Csv2Record<T> {
	public static final String CHARACTER = "C";
	public static final String INTEGER = "I";
	public static final String LONG = "L";
	public static final String DATE = "D";
	public static final String TIMESTAMP = "M";
	public static final String NUMERIC = "N";
	private static final Map<String, String> typeToErrorMessage = new HashMap<String, String>();
	static {
		typeToErrorMessage.put(CHARACTER, "Символ");
		typeToErrorMessage.put(INTEGER, "Цяло число");
		typeToErrorMessage.put(LONG, "Цяло число");
		typeToErrorMessage.put(DATE, "Дата във форма дд.мм.гггг");
		typeToErrorMessage.put(TIMESTAMP, "Дата във форма дд.мм.гггг чч:нн");
		typeToErrorMessage.put(NUMERIC, "Десетично число");
	}
	private List<ConfigItem> configItems;
	private Class<T> cls;
	private Logger log = Logger.getLogger(Csv2Record.class);

	/** ',' - comma */
	private char separator = CSVReader.DEFAULT_SEPARATOR;
	/** double qout */
	private char quotechar = CSVReader.DEFAULT_QUOTE_CHARACTER;
	/** '\' - backslash */
	private char escape = CSVReader.DEFAULT_ESCAPE_CHARACTER;
	/** 0 - do not skip any */
	private int skipLines = 1;// CSVReader.DEFAULT_SKIP_LINES;

	public Csv2Record(Class<T> cls, String configPath) throws IOException {
		this.cls = cls;
		parseConfig(new FileInputStream(configPath));

	}

	public Csv2Record(Class<T> cls, File config) throws IOException {
		this.cls = cls;
		parseConfig(new FileInputStream(config));
	}
	public Csv2Record(Class<T> cls, InputStream is) throws IOException {
		this.cls = cls;
		parseConfig(is);
	}

	

	public List<T> generateRecords(String path) throws ParseException, IOException {
		return generateRecords(new File(path));
	}

	public List<T> generateRecords(File f) throws ParseException, IOException {
		return generateRecords(new FileReader(f));
	}

	public List<T> generateRecords(Reader fr) throws ParseException, IOException  {
		int lineNum = skipLines;
		List<T> result = new ArrayList<T>();
		try {
			CSVReader reader = new CSVReader(fr, separator, quotechar, escape, skipLines);
			String[] nextLine;

			while ((nextLine = reader.readNext()) != null) {
				T obj;
				try {
					obj = cls.newInstance();
				} catch (Exception e) {
					throw new IllegalArgumentException("cannot create instance of :" + cls.getName(), e);
				}
				for (ConfigItem ci : configItems) {
					if (ci.position - 1 < nextLine.length) {
						try {
							//System.out.println(MethodsUtils.getSetterMethod(obj, ci.fieldName) + "   " + typeCast(nextLine[ci.position - 1], ci.type));
							MethodsUtils.getSetterMethod(obj, ci.fieldName).invoke(obj, typeCast(nextLine[ci.position - 1], ci.type));
							//cls.getMethod(setterName, cls.getMethod(getterName).getReturnType()).invoke(obj, typeCast(nextLine[ci.position - 1], ci.type));
						} catch (ParseException e) {
							e.printStackTrace();
							throw new ParseException ("Грешка при прочитане на записа на ред " + lineNum + "  колона " + ci.position + ". Oчакван тип данни: " + typeToErrorMessage.get(ci.type) + " (Име на полето: " + ci.fieldName + ")" + " Стойност:" + nextLine[ci.position - 1], -1);
						} catch (RuntimeException e) {
							e.printStackTrace();
							throw new ParseException ("Грешка при прочитане на записа на ред " + lineNum + "  колона " + ci.position + ". Oчакван тип данни: " + typeToErrorMessage.get(ci.type) + " (Име на полето: " + ci.fieldName + ")" + " Стойност:" + nextLine[ci.position - 1], -1);
						}catch (IllegalAccessException e) {
							throw new IllegalArgumentException("Грешка при извикване на getter/setter/ метода на променливата..." + ci.fieldName, e);
						} catch (InvocationTargetException e) {
							throw new IllegalArgumentException("Грешка при извикване на getter/setter/ метода на променливата..." + ci.fieldName, e);
						} catch (NoSuchMethodException e) {
							throw new IllegalArgumentException("Грешка при извикване на getter/setter/ метода на променливата..." + ci.fieldName, e);
						} 
					}
				}
				result.add(obj);
				lineNum++;
				//System.out.println(obj);
			}
		} finally {
			if (fr != null)
				fr.close();
		}
		return result;
	}

	private void parseConfig(InputStream is) throws IOException {
		try {
			Properties p = new Properties();
			configItems = new ArrayList<ConfigItem>();
			p.load(is);
			for (Object k : p.keySet()) {
				String colName = (String) k;
				String txt = p.getProperty(colName);
				// System.out.println(colName + "   " + el[0] + "   " + el[1]);
				configItems.add(new ConfigItem(colName, txt));
			}
		} finally {
			is.close();
		}
	}

	private Object typeCast(String value, String type) throws ParseException, NumberFormatException {
		if (value == null || "".equals(value))
			return null;
		Object res = null;
		if (INTEGER.equals(type)) {
			res = Integer.parseInt(value);
		} else if (LONG.equals(type)) {
			res = Long.parseLong(value);
		} else if (NUMERIC.equals(type)) {
			res = StringUtils.isEmpty(value) ? null : new BigDecimal(value.replace(" ", "").replace(" ", "").replace(",", ".")); //втория "празен" символ не е точно празен - да не се изтриват и двата!
		} else if (CHARACTER.equals(type)) {
			res = value;
		} else if (DATE.equals(type)) {
			res = DataConverter.parseLongShortDate(value);
		} else if (TIMESTAMP.equals(type)) {
			throw new ParseException("TIMESTAMP not supported yet", -1);
		} else {
			throw new ParseException("Unknown key type: \"" + res + "\"!", -1);
		}
		return res;
	}

	/** Inner helper class */
	private class ConfigItem {
		private String fieldName;
		private int position;
		private String type;

		public ConfigItem(String databaseColumn, String expression) {
			this.fieldName = databaseColumn;
			this.type = expression.substring(0, 1);
			this.position = Integer.parseInt(expression.substring(1));
		}
	}

	public static void main(String[] args) throws Exception {
		
		Csv2Record<AppStatusHistoryRecord> historyRecords = new Csv2Record<AppStatusHistoryRecord>(AppStatusHistoryRecord.class, "D:/ggeorgiev/gravis projects/NACID/src/com/nacid/utils/appstatushistory-import.properties");
		historyRecords.setSeparator(';');
		historyRecords.setSkipLines(1);
		List<AppStatusHistoryRecord> records = historyRecords.generateRecords("C:/Users/ggeorgiev/Desktop/statuses_final.csv");
		System.out.println(records);
		//ApplicationId app = new ApplicationId();
		//Object obj = 5;
		//System.out.println(MethodsUtils.getSetterMethod(app, "applicationId").invoke(app, obj));
		
		
	}
	public void setSeparator(char separator) {
		this.separator = separator;
	}
	/**
	 * sets how many lines from the beginning of file to skip
	 * @param skipLines
	 */
	public void setSkipLines(int skipLines) {
		this.skipLines = skipLines;
	}

}