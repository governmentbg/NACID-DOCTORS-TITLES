package com.nacid.regprof.sppoo;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.nacid.bl.impl.applications.regprof.ColumnsOfSPPOOImpl;
import com.nacid.bl.impl.nomenclatures.regprof.SPPOOImpl;
import com.nacid.data.DataConverter;
import com.nacid.db.utils.DatabaseService;
import com.nacid.db.utils.StandAloneDataSource;
import com.nacid.utils.csv.Csv2Record;

public class ReadSPPOO {

    private static void readData() throws IOException, ParseException, SQLException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Csv2Record<ColumnsOfSPPOOImpl> csv2record = new Csv2Record<ColumnsOfSPPOOImpl>(ColumnsOfSPPOOImpl.class, "D:/Projects/NACID/src/com/nacid/regprof/sppoo/sppoo.properties");
        csv2record.setSeparator(';');
        csv2record.setSkipLines(0);
        List<ColumnsOfSPPOOImpl> impls = csv2record.generateRecords("D:/SPPOO2.csv");
        
        StandAloneDataSource ds = new StandAloneDataSource();
        DatabaseService service = new DatabaseService(ds) {};
        
        String qualificationCode = "";
        String qualificationName = "";
        String professionCode = "";
        String professionName = "";
        String specialityCode = "";
        String specialityName = "";
        int degree = 0;
        boolean qualificationFlag = false;
        boolean professionFlag = false;
        boolean skipLine = false;
        
        for (ColumnsOfSPPOOImpl rec : impls) {
            if ((rec.getC1() == null || rec.getC1().isEmpty()) && !skipLine) {
                if (rec.getC2() != null && !rec.getC2().isEmpty()) {
                    qualificationCode = rec.getC2();
                    qualificationFlag = true;
                } else {
                    qualificationFlag = false;
                }
                if (rec.getC10() != null && !rec.getC10().isEmpty()) {
                    professionCode = rec.getC10();
                    professionFlag = true;
                } else {
                    professionFlag = false;
                }
                List<Method> methods = Arrays.asList(ColumnsOfSPPOOImpl.class.getDeclaredMethods());
                Collections.sort(methods, new Comparator<Method>() {
                    public int compare(Method o1, Method o2) {
                        Integer c1 = DataConverter.parseInt(o1.getName().replace("getC", ""), 0);
                        Integer c2 = DataConverter.parseInt(o2.getName().replace("getC", ""), 0);
                        return c1 - c2;
                    }
                });
                for (java.lang.reflect.Method method : methods) {
                    if (method.getGenericParameterTypes().length == 0 && !method.getName().equalsIgnoreCase("getC2") && !method.getName().equalsIgnoreCase("getC10")) {
                        String column = (String) method.invoke(rec, null);
                        if (column != null && !column.isEmpty()) {
                            if (qualificationFlag) {
                                qualificationName = column;
                                qualificationFlag = false;
                            } else if (professionFlag) {
                                professionName = column;
                                professionFlag = false;
                            } else if (specialityCode.isEmpty()) {
                                specialityCode = column;
                            } else if (specialityName.isEmpty()) {
                                specialityName = column;
                            }
                        }
                    }
                }
                if (DataConverter.parseInt(qualificationCode, 0) < 341) { // SLED 341 SE PROMENQ LOGIKATA V KOLONITE
                    degree = getDegreeFromFirstPart(rec);
                } else {
                    degree = getDegreeFromSecondPart(rec);
                }
                
                SPPOOImpl sppoo = new SPPOOImpl(0, qualificationCode, qualificationName, professionCode, professionName, specialityCode, specialityName, degree);
                service.insertRecord(sppoo);
                System.out.println(sppoo);
                specialityCode = "";
                specialityName = "";
                skipLine = false;
            } else if (rec.getC2() == null || rec.getC2().isEmpty()) {
                skipLine = true;
            } else {
                skipLine = false;
            }
        }
    }
    
    private static int getDegreeFromFirstPart(ColumnsOfSPPOOImpl sppoo) {
        int degree = 0;
        if (sppoo.getC18() != null && !sppoo.getC18().isEmpty()) {
            degree = 1;
        } else if ((sppoo.getC19() != null && !sppoo.getC19().isEmpty()) || (sppoo.getC20() != null && !sppoo.getC20().isEmpty())) {
            degree = 2;
        } else if ((sppoo.getC21() != null && !sppoo.getC21().isEmpty()) || (sppoo.getC22() != null && !sppoo.getC22().isEmpty())) {
            degree = 3;
        } else if ((sppoo.getC23() != null && !sppoo.getC23().isEmpty()) || (sppoo.getC24() != null && !sppoo.getC24().isEmpty())) {
            degree = 4;
        }
        return degree;
    }
    
    private static int getDegreeFromSecondPart(ColumnsOfSPPOOImpl sppoo) {
        int degree = 0;
        if ((sppoo.getC18() != null && !sppoo.getC18().isEmpty()) || (sppoo.getC19() != null && !sppoo.getC19().isEmpty())) {
            degree = 1;
        } else if ((sppoo.getC20() != null && !sppoo.getC20().isEmpty()) || (sppoo.getC21() != null && !sppoo.getC21().isEmpty())) {
            degree = 2;
        } else if ((sppoo.getC22() != null && !sppoo.getC22().isEmpty()) || (sppoo.getC23() != null && !sppoo.getC23().isEmpty())) {
            degree = 3;
        } else if (sppoo.getC24() != null && !sppoo.getC24().isEmpty()) {
            degree = 4;
        }
        return degree;
    }
    
    public static void main(String[] args) throws IllegalArgumentException, IOException, ParseException, SQLException, IllegalAccessException, InvocationTargetException {
        readData();
    }

}