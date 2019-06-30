package com.miamioh.registertaxiclient.model.request;

import java.util.concurrent.atomic.AtomicInteger;

public class Taxi {
	
	private String taxiId;
	private double longitude;
	private double latitude;
	private AtomicInteger noOfPassenger; //automatically incremented counter, atomic operation
	public String getTaxiId() {
		return taxiId;
	}
	public void setTaxiId(String taxiId) {
		this.taxiId = taxiId;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public AtomicInteger getNoOfPassenger() {
		return noOfPassenger;
	}
	public void setNoOfPassenger(AtomicInteger noOfPassenger) {
		this.noOfPassenger = noOfPassenger;
	}
	
}
