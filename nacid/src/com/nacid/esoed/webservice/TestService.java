package com.nacid.esoed.webservice;

import org.apache.log4j.Logger;

public class TestService {

	private static final Logger log = Logger.getLogger(TestService.class);

    public String addBooks(String book) {
        return "getBooks sercvice";
    }

    public String  findBooks(String book) {
    	log.debug("TestService findBooks arg="+ book);
        return "findBooks service";
    }

}


