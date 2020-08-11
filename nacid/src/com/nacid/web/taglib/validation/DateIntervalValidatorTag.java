package com.nacid.web.taglib.validation;


public class DateIntervalValidatorTag extends AbstractValidatorTag {

    String dateFrom;
    String dateTo;
    String format;
    private boolean dontAllowEquals;
    String endFormat;

    public String generateValidationFunction()  {
        String val = "validateDateInterval(" + getFormName() + "['" + dateFrom + "'],";
        val += "" + getFormName() + "['" + dateTo + "'],";
        val += "'" + format/*.toUpperCase()*/ + "',";
        val += endFormat == null ? "null," : "'" + endFormat + "',";
        val += "'" + errormessage + "',";
        val += dontAllowEquals + ")";
        return val;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setDontAllowEquals(boolean dontAllowEquals) {
        this.dontAllowEquals = dontAllowEquals;
    }

    public void setEndFormat(String endFormat) {
        this.endFormat = endFormat;
    }
}
