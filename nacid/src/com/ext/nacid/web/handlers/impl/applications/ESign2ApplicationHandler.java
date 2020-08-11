package com.ext.nacid.web.handlers.impl.applications;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;

public class ESign2ApplicationHandler extends NacidExtBaseRequestHandler {
    public ESign2ApplicationHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        BufferedReader r;
        logRequestHeaders(request);
        logRequestParams(request);
        try {
            r = request.getReader();
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            int n;
            while ((n = r.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            logger.debug("Received Content...Length=" + writer.toString().length());
            /*String line;
              while ((line = r.readLine()) != null) {
                logger.debug(line);
            }*/
            writeToResponse(response, "ready...");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
