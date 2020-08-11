package com.nacid.timers;

import java.util.Date;
import java.util.TimerTask;

import com.nacid.bl.NacidDataProvider;
import com.nacid.data.DataConverter;

public class ReceiveMailsTask extends TimerTask {
	
    private NacidDataProvider nDP;
    
    public ReceiveMailsTask(NacidDataProvider nDP) {
        this.nDP = nDP;
    }

    public void run() {
		
        System.out.println("Started task receive mails task...time started:" + DataConverter.formatDateTime(new Date(), true) + " ...");
        nDP.getMailDataProvider().getMailFromServer();
		
        System.out.println("Ending task receive mails task...time ended:" + DataConverter.formatDateTime(new Date(), true) + " ...");
        
	}

}
