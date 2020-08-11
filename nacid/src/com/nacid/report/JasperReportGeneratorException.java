package com.nacid.report;

/**
 * @author bily
 *
 */
public class JasperReportGeneratorException extends Exception {


	/**
	 * Constructs new JasperReportGeneratorException
	 */
    JasperReportGeneratorException() {
        super();
    }

    /**
     * Constructs new JasperReportGeneratorException with specified message
     * @param message
     */
    JasperReportGeneratorException(String message) {
        super(message);
    }

    /**
     * Constructs new JasperReportGeneratorException with specified message and cause
     * @param message
     * @param cause
     */
    JasperReportGeneratorException(String message, Throwable cause) {
        super(message,cause);
    }

    /**
     * Constructs new JasperReportGeneratorException with cause
     * @param cause
     */
    JasperReportGeneratorException(Throwable cause) {
        super(cause);
    }

}
