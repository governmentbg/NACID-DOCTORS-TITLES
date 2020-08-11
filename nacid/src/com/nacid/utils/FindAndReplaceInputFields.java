package com.nacid.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * promenq vyv vshichki .jsp files <input....type="text"..../> s <v:textinput..... />
 * @author ggeorgiev
 *
 */
public class FindAndReplaceInputFields {
    private static Pattern INPUT_PATTERN = Pattern.compile("<input(.*?)type=\"text\"");
    private static Pattern VALIDATION_PATTERN = Pattern.compile("<%@(.*?)validation.tld(.*?)prefix=\"(.*?)\"");
    // whatever other info you need
    private static int i = 1;
    private static String lineSeparator = System.getProperty("line.separator"); 
    public static void main(String[] args) throws IOException {
        searchInTextFiles(new File("D:/ggeorgiev/abbaty/NACID/WebContent"));
        //searchInFile(new File("D:/ggeorgiev/abbaty/NACID/WebContent/js/autocompletedoc.txt"));
        //System.out.println(">" + System.getProperty("line.separator").charAt(0) + "<");
    }
    public static void searchInTextFiles(File dir) throws IOException {
        File[] a = dir.listFiles();
        for (File f : a) {
            if (f.isDirectory()) {
                searchInTextFiles(f);
            } else if (!f.getName().contains("textinput.tag") 
                    && (f.getName().endsWith(".jsp") ||
                    f.getName().endsWith(".tag"))) {
                searchInFile(f);
            }
        }
    }

    private static void searchInFile(File f) throws IOException {
        // do whatever you need to do on f using fields from this class
        BufferedReader reader = new BufferedReader(new FileReader(f));
        String line;
        String prefix = null;
        StringBuffer l = new StringBuffer();
        boolean has = false;
        while ((line = reader.readLine()) != null) {
            Matcher inputMatcher = INPUT_PATTERN.matcher(line);
            if (inputMatcher.find()) {
                
                inputMatcher.appendReplacement(l, "<" + (prefix == null ? "v" : prefix) + ":textinput$1");
                inputMatcher.appendTail(l);
                has = true;
                //break;
            } else {
                l.append(line);
            }
            l.append(lineSeparator);
            Matcher prefixMatcher = VALIDATION_PATTERN.matcher(line);
            if (prefixMatcher.find()) {
                prefix = prefixMatcher.group(3);
            }
        }
        
        if (!has) {
            return;
        }
        //Ako nqma prefix (t.e. ne e import-nat tozi taglib, togava go importva...
        if (prefix == null) {
            String[] lines = l.toString().split(System.getProperty("line.separator"));
            l = new StringBuffer();
            for (int j = 0; j < lines.length; j++) {
                if (j == 2) {
                    l.append("<%@ taglib uri=\"/META-INF/validation.tld\" prefix=\"v\"%>");
                    l.append(lineSeparator);
                }
                l.append(lines[j]);
                l.append(lineSeparator);
            }
        }
        System.out.println(i++ + " " + f.getAbsolutePath() + "  " + prefix);
        //System.out.println(l);
        OutputStream os = new FileOutputStream(f.getAbsolutePath());
        os.write(l.toString().getBytes());
        os.flush();
        os.close();
        //System.out.println("End...");
        
    }
}