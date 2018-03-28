package com.marikris.fieldlogger;

import java.io.Serializable;

/**
 * The class defines an array of five Log Entries, 
 * where each Logs is composed of a date, conduct, ph, moisture, & oxygen
 * 
 */
@SuppressWarnings("serial")
public class LogEntry implements Serializable {
	String date;
	float conduct;
	float ph;
	int moist;
	int oxygen;
	// ---constructor---
	public LogEntry() {
	}

	public LogEntry(String date, float conduct, float ph, int moist, int oxygen) {
		super();
		this.date = date;
		this.conduct = conduct;
		this.ph = ph;
		this.moist = moist;
		this.oxygen = oxygen;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public float getConduct() {
		return conduct;
	}

	public void setConduct(float conduct) {
		this.conduct = conduct;
	}

	public float getPh() {
		return ph;
	}

	public void setPh(float ph) {
		this.ph = ph;
	}

	public int getMoist() {
		return moist;
	}

	public void setMoist(int moist) {
		this.moist = moist;
	}

	public int getOxygen() {
		return oxygen;
	}

	public void setOxygen(int oxygen) {
		this.oxygen = oxygen;
	}

}