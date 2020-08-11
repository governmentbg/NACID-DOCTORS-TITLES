package com.nacid.timers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TimerFactory {
	private static volatile TimerFactory instance;
	private Map<String, Timer> timers = new HashMap<String, Timer>();
	private TimerFactory() {
	}
	public static TimerFactory getTimerFactor() {
		if (instance == null) {
			synchronized (TimerFactory.class) {
				if (instance == null) {
					instance = new TimerFactory();
				}
			}
		}
		return instance;
	}
	public void addTaskToTimer(String name, Date firstTime, int seconds, TimerTask task) {
		Timer t = new Timer();
		if (firstTime == null) {
			t.schedule(task, 0, seconds * 1000);
		} else {
			t.schedule(task, firstTime, seconds * 1000);	
		}
		timers.put(name, t);
	}
	public void killTimers() {
		for (Timer t :timers.values()) {
			t.cancel();
		}
	}
}
