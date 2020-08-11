package com.nacid.web.handlers.impl.academicrecognition;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.academicrecognition.AcademicRecognitionDataProvider;
import com.nacid.bl.impl.academicrecognition.BGAcademicRecognitionInfoImpl;
import com.nacid.data.DataConverter;
import com.nacid.db.utils.StandAloneDataSource;

public class BGAcademicRecognitionMigrateData {

	
	public static void main(String[] args){
		//TODO if necessary - do db changes, delete info from tables, restart sequences
		//StandAloneDataSource sd = new StandAloneDataSource("jdbc:postgresql://192.168.0.37:5433/NACID", "postgres", "p0stGr#s");
		//StandAloneDataSource sd = new StandAloneDataSource("jdbc:postgresql://fly.nacid.bg:5432/nacid_development", "postgres", "postgres");
		StandAloneDataSource sd = new StandAloneDataSource("jdbc:postgresql://172.16.0.20:5432/nacid", "postgres", "postgres");
		List<BGAcademicRecognitionInfoImpl> cells = null;
		try {
			cells = readExcel("D:/Temp/migrate/2016.07.19.chlen_19_Naredba.oper-07.xls", 2, 5, sd);
		} catch (IOException e) {

			e.printStackTrace();
		} catch (IllegalArgumentException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(cells != null && !cells.isEmpty()){
			NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(sd);
			AcademicRecognitionDataProvider dp = nacidDataProvider.getAcademicRecognitionDataProvider();
			for (BGAcademicRecognitionInfoImpl c:cells) {
				System.out.println(c);
				dp.saveBGAcademicRecognitionRecord(c);
			}
			
		}
	}

	public static List<BGAcademicRecognitionInfoImpl> readExcel(String fileName, int startRow, int startColumn, StandAloneDataSource sd) throws Exception {	
		FileInputStream myInput = new FileInputStream(fileName);
		return readExcel(myInput, fileName, startRow, startColumn, sd);
	}   

	public static List<BGAcademicRecognitionInfoImpl> readExcel(InputStream myInput, String fileName,
			int startRow, int startColumn, StandAloneDataSource sd) throws Exception {
		Date createdDate = new Date();
		int startR = startRow <=0 ? 0:startRow-1;
		int startC = startColumn <=0 ? 0:startColumn-1;
		if(fileName.endsWith(".xlsx")){
			return readExcelRecords(myInput, startR, startC, true, createdDate, sd);
		} else if(fileName.endsWith(".xls")){
			return readExcelRecords(myInput, startR, startC, false, createdDate, sd);
		}
		throw new IllegalArgumentException("Only xls and xlsx files allowed");
	}

	public static List<BGAcademicRecognitionInfoImpl> readExcelRecords(InputStream myInput, int startRow, int startColumn, boolean xlsx,
			Date createdDate, StandAloneDataSource sd) throws Exception {
		List<BGAcademicRecognitionInfoImpl> result = new ArrayList<BGAcademicRecognitionInfoImpl>();
		Connection c = sd.getConnection();
		Workbook myWorkBook = null;
		if(xlsx){
			myWorkBook = new XSSFWorkbook(myInput);
		} else {
			POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
			myWorkBook = new HSSFWorkbook(myFileSystem);       	
		}
		Sheet mySheet = myWorkBook.getSheetAt(18);
		Integer rows = mySheet.getPhysicalNumberOfRows();
		for(int i = startRow; i< rows; i++){
			System.out.println("row"+i);
			Row myRow = mySheet.getRow(i);
			BGAcademicRecognitionInfoImpl record = new BGAcademicRecognitionInfoImpl();
			int j = startColumn;
			
			record.setInputNumber(trim(myRow.getCell(j++).getStringCellValue()));
			if(record.getInputNumber() == null || record.getInputNumber().isEmpty()){
				continue;
			}
			record.setOutputNumber(trim(myRow.getCell(j++).getStringCellValue()));

			String universityName = trim(myRow.getCell(j++).getStringCellValue());
			j++;
			Integer foundOrSaved = findOrSaveUniversity(c, universityName);
			
			if(foundOrSaved == null){
				throw new Exception("Could not save or find university");
			} else {
				record.setRecognizedUniversityId(foundOrSaved);
			}

			String applicant = trim(myRow.getCell(j++).getStringCellValue());
			record.setApplicant(processApplicant(applicant));
			
			record.setCitizenship(trim(myRow.getCell(j++).getStringCellValue()));
			record.setUniversity(trim(myRow.getCell(j++).getStringCellValue()));
			record.setUniversityCountry(trim(myRow.getCell(j++).getStringCellValue()));
			record.setEducationLevel(trim(myRow.getCell(j++).getStringCellValue()));
			
			if(record.getEducationLevel() != null){
				String edu = record.getEducationLevel().toLowerCase();
				if(edu.equals("во")){
					edu = edu.toUpperCase();
				}else if(edu.startsWith("во ")){
					edu = edu.replaceFirst("во ", "ВО ");
				} else if(edu.contains(" во ")){
					edu = edu.replaceFirst(" во ", " ВО ");
				} else if(edu.endsWith(" во")){
					edu = edu.replaceFirst(" во", " ВО");
				}
				record.setEducationLevel(edu);
			}
			
			record.setDiplomaSpeciality(trim(myRow.getCell(j++).getStringCellValue()));
			Cell cell = myRow.getCell(j++);
			record.setDiplomaNumber(trim(numericCellToString(cell)));

			cell = myRow.getCell(j++);
			record.setDiplomaDate(trim(cell.getCellType() == Cell.CELL_TYPE_NUMERIC ? DataConverter.formatDate(cell.getDateCellValue()) : cell.getStringCellValue()));
			cell = myRow.getCell(j++);
			record.setProtocolNumber(trim(numericCellToString(cell)));
			record.setDenialProtocolNumber(trim(numericCellToString(myRow.getCell(j++))));
			if(record.getDenialProtocolNumber().equalsIgnoreCase("прекратена процедура")){
				record.setRecognitionStatusId(3);
			} else if (record.getDenialProtocolNumber().equalsIgnoreCase("отказ")){
				record.setRecognitionStatusId(4);
			} else {
				record.setRecognitionStatusId(1);
			}
			
			record.setRecognizedSpeciality(trim(myRow.getCell(j++).getStringCellValue()));


			record.setCreatedDate(createdDate);
			result.add(record);
		}
		sd.close();
		return result;
	}

	private static String numericCellToString(Cell cell) {
		return (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) ? numberToString(cell.getNumericCellValue()) : cell.getStringCellValue();
	}

	private static String numberToString(double number) {
		return new DecimalFormat("#").format(number);
	}
	private static String trim(String str) {
		return str == null ? null : str.trim();
	}
	
	private static String processApplicant(String str) {
		if(str == null){
			return null;
		}
		str = str.toLowerCase();
		char[] chArr = str.toCharArray();
		String start = "";
		if(chArr.length >0){
			if(!Character.isAlphabetic(chArr[0])){
				chArr = str.substring(1, str.length()).toCharArray();
			}
			
			
			if(str.startsWith("г-н") ){
				chArr = str.substring(4, str.length()).toCharArray();
				start = "г-н ";
			} else if(str.startsWith("г-жа")){
				chArr = str.substring(5, str.length()).toCharArray();
				start = "г-жа ";
			}
			
			
			for(int i = 0; i<chArr.length; i++){
				if(i == 0 || (i>0 && (Character.isWhitespace(chArr[i-1]) || chArr[i-1] == '-'))){
					chArr[i] = Character.toUpperCase(chArr[i]);
				}
			}
		}
		
		return start+new String(chArr);
		
	}

	public static Integer findOrSaveUniversity(Connection c, String universityName) throws SQLException{

		PreparedStatement searchFull = c.prepareStatement("Select id from university where lower(translate(bg_name, \' ,.-\'\'\"+=_‘´\', \'\')) = lower(translate(?, \' ,.-\'\'\"+=_‘´\', \'\')) ");
		searchFull.setString(1, universityName);

		ResultSet rsFull = searchFull.executeQuery();
		while(rsFull.next()){
			return rsFull.getInt(1);
		}
		
		if(universityName.lastIndexOf('-') != -1){
			String separatedUniversityName = universityName.substring(0, universityName.lastIndexOf('-')).trim();
			String separatedUniversityCity = universityName.substring(universityName.lastIndexOf('-')+1, universityName.length()).trim();
			
			PreparedStatement searchSeparated = c.prepareStatement("Select id from university where lower(translate(bg_name, \' ,.-\'\'\"+=_‘´\', \'\')) = lower(translate(?, \' ,.-\'\'\"+=_‘´\', \'\')) ");
			searchSeparated.setString(1, separatedUniversityName);

			ResultSet rsSeparated = searchSeparated.executeQuery();
			while(rsSeparated.next()){
				return rsSeparated.getInt(1);
			}


			PreparedStatement insert = c.prepareStatement("insert into university (bg_name, country_id, city) values (?, (select id from nomenclatures.country where iso3166_code=\'BG\' limit 1), ?)");
			insert.setString(1, separatedUniversityName);
			insert.setString(2, separatedUniversityCity);
			System.out.println("inserting "+separatedUniversityName+" and city "+separatedUniversityCity+"...");
			insert.execute();

			
			PreparedStatement searchLast = c.prepareStatement("Select id from university where bg_name=? and city =? ");
			searchLast.setString(1, separatedUniversityName);
			searchLast.setString(2, separatedUniversityCity);
			
			ResultSet rsLastSelect = searchLast.executeQuery();
			while(rsLastSelect.next()){
				return rsLastSelect.getInt(1);
			}
		} else {
			
			PreparedStatement insert = c.prepareStatement("insert into university (bg_name, country_id, city) values (?, (select id from nomenclatures.country where iso3166_code=\'BG\' limit 1), ?)");
			insert.setString(1, universityName);
			insert.setString(2, "");
			
			System.out.println("inserting "+universityName+"...");
			insert.execute();
			
			ResultSet rsLastSelect = searchFull.executeQuery();
			while(rsLastSelect.next()){
				return rsLastSelect.getInt(1);
			}
		}

		return null;
	}

}
