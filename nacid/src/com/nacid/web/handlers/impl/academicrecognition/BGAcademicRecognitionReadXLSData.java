package com.nacid.web.handlers.impl.academicrecognition;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.academicrecognition.AcademicRecognitionDataProvider;
import com.nacid.bl.impl.academicrecognition.BGAcademicRecognitionInfoImpl;
import com.nacid.data.DataConverter;
import com.nacid.db.utils.StandAloneDataSource;

public class BGAcademicRecognitionReadXLSData {


    public static void main(String[] args) throws IOException {
        List<BGAcademicRecognitionInfoImpl> cells = readExcel("C:/Users/raya_2/Desktop/nacid_int/Γáí½¿µá_τ½.19_17.12.2015_NACID_.xls", 
        		5, 2, "123", "456");
        NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource());
        AcademicRecognitionDataProvider dp = nacidDataProvider.getAcademicRecognitionDataProvider();
        for (BGAcademicRecognitionInfoImpl c:cells) {
            System.out.println(c);
           // dp.saveBGAcademicRecognitionRecord(c);
        }
    }
    
    public static List<BGAcademicRecognitionInfoImpl> readExcel(String fileName, int startRow, int startColumn,
    		String inNumber, String outNumber) throws IOException {	
        FileInputStream myInput = new FileInputStream(fileName);
        return readExcel(myInput, fileName, startRow, startColumn, inNumber, outNumber);
    }   
    
    public static List<BGAcademicRecognitionInfoImpl> readExcel(InputStream myInput, String fileName,
    		int startRow, int startColumn, String inNumber, String outNumber) throws IOException, IllegalArgumentException {
    	Date createdDate = new Date();
    	int startR = startRow <=0 ? 0:startRow-1;
    	int startC = startColumn <=0 ? 0:startColumn-1;
    	if(fileName.endsWith(".xlsx")){
    		return readExcelRecords(myInput, startR, startC, true, inNumber, outNumber, createdDate);
    	} else if(fileName.endsWith(".xls")){
    		return readExcelRecords(myInput, startR, startC, false, inNumber, outNumber, createdDate);
    	}
    	throw new IllegalArgumentException("Only xls and xlsx files allowed");
    }
    
    public static List<BGAcademicRecognitionInfoImpl> readExcelRecords(InputStream myInput, int startRow, int startColumn, boolean xlsx,
    		String inNumber, String outNumber, Date createdDate) throws IOException {
        List<BGAcademicRecognitionInfoImpl> result = new ArrayList<BGAcademicRecognitionInfoImpl>();
        
        Workbook myWorkBook = null;
        if(xlsx){
        	myWorkBook = new XSSFWorkbook(myInput);
        } else {
        	POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
        	myWorkBook = new HSSFWorkbook(myFileSystem);       	
        }
        Sheet mySheet = myWorkBook.getSheetAt(0);
        Integer rows = mySheet.getPhysicalNumberOfRows();
        for(int i = startRow; i< rows; i++){
            Row myRow = mySheet.getRow(i);
            BGAcademicRecognitionInfoImpl record = new BGAcademicRecognitionInfoImpl();
            int j = startColumn;
            record.setApplicant(trim(myRow.getCell(j++).getStringCellValue()));
            if (StringUtils.isEmpty(record.getApplicant())) {
                continue;
            }
            record.setCitizenship(trim(myRow.getCell(j++).getStringCellValue()));
            record.setUniversity(trim(myRow.getCell(j++).getStringCellValue()));
            record.setUniversityCountry(trim(myRow.getCell(j++).getStringCellValue()));
            record.setEducationLevel(trim(myRow.getCell(j++).getStringCellValue()));
            record.setDiplomaSpeciality(trim(myRow.getCell(j++).getStringCellValue()));
            Cell cell = myRow.getCell(j++);
            record.setDiplomaNumber(trim(numericCellToString(cell)));

            cell = myRow.getCell(j++);
            record.setDiplomaDate(trim(cell.getCellType() == Cell.CELL_TYPE_NUMERIC ? DataConverter.formatDate(cell.getDateCellValue()) : cell.getStringCellValue()));
            cell = myRow.getCell(j++);
            record.setProtocolNumber(trim(numericCellToString(cell)));
            record.setDenialProtocolNumber(trim(numericCellToString(myRow.getCell(j++))));
            record.setRecognizedSpeciality(trim(myRow.getCell(j++).getStringCellValue()));
            
            record.setInputNumber(inNumber);
            record.setOutputNumber(outNumber);
            record.setCreatedDate(createdDate);
            result.add(record);
        }
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



}
