package paa.parking.model;

import java.util.*;
import javax.persistence.*;
import java.io.*;

public class Parking {
	
	private Long code;
	private String name;
	private String address;
	private int bookableSpaces;
	private int totalSpaces;
	private double longitude;
	private double latitude;

	private List<Booking> bookings;
	
	public Parking(String name, String address, int totalSpaces, int bookableSpaces, double longitude, double latitude) {
		super();
		this.name = name;
		this.address = address;
		this.totalSpaces = totalSpaces;
		this.bookableSpaces = bookableSpaces;
		this.longitude = longitude;
		this.latitude = latitude;
		this.bookings = new ArrayList<Booking>();
	}


	public Parking(Object object, String name, String address, int totalSpaces, int bookableSpaces, double longitude, double latitude) {
		super();
		this.name = name;
		this.address = address;
		this.totalSpaces = totalSpaces;
		this.bookableSpaces = bookableSpaces;
		this.longitude = longitude;
		this.latitude = latitude;
		this.bookings = new ArrayList<Booking>();
	}
	
	public Parking() {
		super();
	}
	
	public Long getCode() {
		return code;
	}

	public int getBookableSpaces() {
		return bookableSpaces;
	}

	public void setBookableSpaces(int plazasReservables) {
		this.bookableSpaces = plazasReservables;
	}

	public void setCode(Long id) {
		this.code = id;
	}

	public int getSpaces() {
		return totalSpaces;
	}

	public void setSpaces(int spaces) {
		this.totalSpaces = spaces;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
	
	public void setBooking(Booking booking) {
	    bookings.add(booking);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Parking other = (Parking) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name;
	}
	
}
