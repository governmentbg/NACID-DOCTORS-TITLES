package com.nacid.bl.comision;

import java.io.InputStream;

public interface CommissionCalendarProtocol {

    public int getId();
    public InputStream getContent();
    public String getContentType();
    public String getFileName();
}
