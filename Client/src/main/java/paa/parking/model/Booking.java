package paa.parking.model;

import java.time.LocalDate;
import javax.persistence.*;
import java.io.*;

public class Booking {
	
	private Long code;
	private String licencePlate;
	private LocalDate date;
	
	private Parking parking;
	
	public Booking(String licencePlate, LocalDate date, Parking parking) {
		super();
		this.licencePlate = licencePlate;
		this.date = date;
		this.parking = parking;
	}
	
	public Booking() {
		super();
	}
	
	public Long getCode() {
		return code;
	}
	
	public void setCode(Long code) {
		this.code = code;
	}
	
	public String getLicencePlate() {
		return licencePlate;
	}
	
	public void setLicencePlate(String licencePlate) {
		this.licencePlate = licencePlate;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public Parking getParking() {
		return parking;
	}
	
	public void setParking(Parking parking) {
		this.parking = parking;
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
		Booking other = (Booking) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Matrícula: " + licencePlate + ", fecha: " + date;
	}

}
