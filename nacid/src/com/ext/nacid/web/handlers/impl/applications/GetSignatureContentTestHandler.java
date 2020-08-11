package com.ext.nacid.web.handlers.impl.applications;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;

public class GetSignatureContentTestHandler extends NacidExtBaseRequestHandler {

    public GetSignatureContentTestHandler(ServletContext servletContext) {
        super(servletContext);
    }
    
    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Signature />");
            /*response.getWriter().write(Base64.encode(IOUtils.toByteArray(new FileInputStream("D:/install/postgresql-8.4.3-1-windows.exe"))));
            response.getWriter().write("</test>");*/
            response.getWriter().flush();
            //writeToResponse(response, "<test>" + new String(new Base64().encode(readFile("D:/install/postgresql-8.4.3-1-windows.exe").getBytes()))+ "</test>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String readFile( String file ) throws IOException {
        BufferedReader reader = new BufferedReader( new FileReader (file));
        String         line = null;
        StringBuilder  stringBuilder = new StringBuilder();
        String         ls = System.getProperty("line.separator");

        while( ( line = reader.readLine() ) != null ) {
            stringBuilder.append( line );
            stringBuilder.append( ls );
        }

        return stringBuilder.toString();
    }
}
