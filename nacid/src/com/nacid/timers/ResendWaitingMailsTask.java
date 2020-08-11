package com.nacid.timers;

import java.util.Date;
import java.util.TimerTask;

import com.nacid.data.DataConverter;

public class ResendWaitingMailsTask extends TimerTask {
	public void run() {
		//NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(DatabaseUtils.getDataSource());
		//nacidDataProvider.getMailDataProvider();
		System.out.println("Started task resend waiting mails task...time started:" + DataConverter.formatDateTime(new Date(), true) + " ...");
	
	}

}
