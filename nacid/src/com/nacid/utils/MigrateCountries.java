package com.nacid.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.omg.CORBA.CharSeqHolder;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.db.utils.StandAloneDataSource;

public class MigrateCountries {
    public static void main(String[] args) throws Exception {
        URL url = new URL("http://publications.europa.eu/code/bg/bg-5000500.htm");
        URLConnection connection = url.openConnection();
        InputStream in = connection.getInputStream();
        InputStreamReader is = new InputStreamReader(in);
        StringBuilder sb=new StringBuilder();
        BufferedReader br = new BufferedReader(is);
        String read = br.readLine();

        while(read != null) {
            //System.out.println(read);
            sb.append(read);
            read =br.readLine();

        }
        Pattern p = Pattern.compile("<TD CLASS=\"(greenbg|borangeBackground)\">.*?</TD><TD>(<A NAME=\"[a-zA-Z]*\"></A>)?([а-яА-яA-Za-z- //]*).*?</TD><TD>([а-яА-яA-za-z- //]*).*?</TD><TD><STRONG>(.*?)</STRONG>");
        
        String theText = sb.toString().replace(" ROWSPAN=\"2\"", "").replace("&nbsp;", " ");
        //System.out.println(theText.indexOf("Oстров Норфолк"));
        //System.out.println(theText.indexOf("Остров Норфолк"));
        //System.out.println(theText.indexOf("Остров Ман"));
        Matcher m = p.matcher(theText);
        int i =0;
        NacidDataProvider nacid = new NacidDataProviderImpl(new StandAloneDataSource());
        NomenclaturesDataProvider nomDp = nacid.getNomenclaturesDataProvider();     
        while (m.find()) {
            i++;
            /*if(m.group(2)!= null){
                System.out.println(m.group(2));
            }
            System.out.println(m.group(3).trim()+ " " + m.group(4).trim() + " " + m.group(5));*/
            String name = MigrateCountries.replaceEnglish(m.group(3).trim());
            String formalName = MigrateCountries.replaceEnglish(m.group(4).trim());
            String code = m.group(5);       
            nomDp.updateCountryFormalName(code, formalName);
        }
        System.out.println("Countries: "+i);
        //System.out.println(sb);
        //System.out.println(m.matches());
    }
    
    public static String replaceEnglish(String s){
        s = s.replace('o', 'о');
        s = s.replace('a', 'а');
        s = s.replace('e', 'е');
        s = s.replace('O', 'О');
        s = s.replace('A', 'А');
        s = s.replace('E', 'Е');

        return s;
        
    }
}
