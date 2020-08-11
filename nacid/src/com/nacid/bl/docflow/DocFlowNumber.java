package com.nacid.bl.docflow;

import com.nacid.bl.impl.Utils;
import com.nacid.data.DataConverter;

import java.util.Date;

public class DocFlowNumber {
    private int prepNo;
    private Date docflowDate;
    private String docflowId;
    private boolean isValid = true;
    
    public DocFlowNumber(String docflowId, Date docflowDate, boolean allowInvalid) {
        init(docflowId, docflowDate, allowInvalid);
    }
    
    
    
    public DocFlowNumber(String num, boolean allowInvalid) {
        int slashIndex = num.lastIndexOf('/');
        docflowDate = slashIndex > -1 ? DataConverter.parseDate(num.substring(slashIndex + 1, num.length())) : null;
        String docFlowId = slashIndex > -1 ? num.substring(0, num.lastIndexOf('/')) : ""; 
        init(docFlowId, docflowDate, allowInvalid);
    }
    
    private void init(String docflowId, Date docflowDate, boolean allowInvalid) {
        if (docflowId == null || !docflowId.matches("\\d{2,2}-\\d{2,2}-\\d{0,}")) {
            if (allowInvalid) {
                isValid = false;
                return;
            } else {
                throw Utils.logException(new Exception("Invalid docflow - date is null"));
            }
        }
        this.docflowId = docflowId;
        this.docflowDate = docflowDate;
        if(docflowDate == null) {
            if (allowInvalid) {
                isValid = false;
                return;
            } else {
                throw Utils.logException(new Exception("Invalid docflow - date is null"));  
            }
        }
        
        String yearNum = docflowId.substring(6);
        String[] splitted = yearNum.split("-");
        //System.out.println(yearNum + "  " + Arrays.asList(splitted) + "  " + splitted.length);
        prepNo = DataConverter.parseInt(splitted[0], -1);
        if(prepNo == -1) {
            if (allowInvalid) {
                isValid = false;
                return;
            } else {
                throw Utils.logException(new Exception("Invalid docflow " + docflowId));    
            }
            
        }


        if(splitted.length > 1) {
            if(prepNo == -1) {
                if (allowInvalid) {
                    isValid = false;
                    return;
                } else {
                    throw Utils.logException(new Exception("Invalid docflow " + docflowId));
                }
            }
        }
    }



    public int getPrepNo() {
        return prepNo;
    }


    public Date getDocflowDate() {
        return docflowDate;
    }



    public String getDocflowId() {
        return docflowId;
    }
    public boolean isValid() {
        return isValid;
    }
    
    

}
