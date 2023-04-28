package paa.parking.business;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import paa.parking.model.*;

public class BookingRemoteService implements IBookingService {

	String url = "http://localhost:8080/Practica4-Server/?action=";

	private String enviarPeticion (String url) {
		String entrada, respuesta ="";
		try {
			URL urlEnvio = new URL(url);
			BufferedReader in = new BufferedReader(new InputStreamReader(urlEnvio.openStream()));
			while ((entrada = in.readLine()) != null)
				respuesta = respuesta + entrada;
			in.close();
		} catch (IOException e) { e.printStackTrace(); }
		return respuesta;
	}

	@Override
	public Parking createParking(String name, String address, int totalSpaces, int bookableSpaces, double longitude, double latitude) {
		if(longitude >= -180 & longitude <= 180 & latitude >= -90 & latitude <= 90 & totalSpaces >= 0 & bookableSpaces >= 0 & totalSpaces >= bookableSpaces) {
			Parking parking = new Parking();

			String n = String.valueOf(name);
			String n1 = n.replace(" ", "-");
			String n2 = n1.replace("á", "_a");
			String n3 = n2.replace("é", "_e");
			String n4 = n3.replace("í", "_i");
			String n5 = n4.replace("ó", "_o");
			String n6 = n5.replace("ú", "_u");
			String n7 = n6.replace("ü", "_v");
			String n8 = n7.replace("(", "_1");
			String n9 = n8.replace(")", "_2");
			String nr = n9.replace("/", "_3");
			
			String a = String.valueOf(address);
			String a1 = a.replace(" ", "-");
			String a2 = a1.replace("á", "_a");
			String a3 = a2.replace("é", "_e");
			String a4 = a3.replace("í", "_i");
			String a5 = a4.replace("ó", "_o");
			String a6 = a5.replace("ú", "_u");
			String a7 = a6.replace("ü", "_v");
			String a8 = a7.replace("(", "_1");
			String a9 = a8.replace(")", "_2");
			String ar = a9.replace("/", "_3");
			
			String createParkingUrl = url+"createParking&name="+nr+"&address="+ar+"&totalSpaces="+totalSpaces+"&bookableSpaces="+bookableSpaces+"&longitude="+longitude+"&latitude="+latitude;
			
			String respuesta = enviarPeticion(createParkingUrl);

			JSONParser parser = new JSONParser();
			
			JSONObject JSONParking;
			
			try {
				JSONParking = (JSONObject) parser.parse(respuesta);
				
				parking.setCode(Long.parseLong(String.valueOf(JSONParking.get("Code"))));
				parking.setName(String.valueOf(JSONParking.get("Name")));
				parking.setAddress(String.valueOf(JSONParking.get("Address")));
				parking.setSpaces(Integer.parseInt(String.valueOf(JSONParking.get("TotalSpaces"))));
				parking.setBookableSpaces(Integer.parseInt(String.valueOf(JSONParking.get("BookableSpaces"))));
				parking.setLongitude(Double.parseDouble(String.valueOf(JSONParking.get("Longitude"))));
				parking.setLatitude(Double.parseDouble(String.valueOf(JSONParking.get("Latitude"))));
			}  catch (ParseException e1) {
				e1.printStackTrace();	
			}
			return parking;
		} else
			throw new BookingServiceException("Los datos introducidos son incorrectos");
	}

	@Override
	public Parking findParking(Long parkingCode) {	
		String findParkingUrl = url+"findParking&parkingCode="+parkingCode;

		String respuesta = enviarPeticion(findParkingUrl);

		JSONParser parser = new JSONParser();

		JSONArray JSONBookingArray;

		JSONObject JSONParking;
		JSONObject JSONBooking;

		Parking parking = new Parking();
		Booking booking;

		try {
			JSONParking = (JSONObject) parser.parse(respuesta);
			
			parking.setCode(Long.parseLong(String.valueOf(JSONParking.get("Code"))));
			parking.setName(String.valueOf(JSONParking.get("Name")));
			parking.setAddress(String.valueOf(JSONParking.get("Address")));
			parking.setSpaces(Integer.parseInt(String.valueOf(JSONParking.get("TotalSpaces"))));
			parking.setBookableSpaces(Integer.parseInt(String.valueOf(JSONParking.get("BookableSpaces"))));
			parking.setLongitude(Double.parseDouble(String.valueOf(JSONParking.get("Longitude"))));
			parking.setLatitude(Double.parseDouble(String.valueOf(JSONParking.get("Latitude"))));
			
			JSONBookingArray = (JSONArray) JSONParking.get("Bookings");

			for(Object b : JSONBookingArray) {
				booking = new Booking();
				
				JSONBooking = (JSONObject) b;
				booking.setCode(Long.parseLong(String.valueOf(JSONBooking.get("Code"))));
				booking.setDate(LocalDate.parse(String.valueOf(JSONBooking.get("Date"))));
				booking.setLicencePlate(String.valueOf(JSONBooking.get("LicencePlate")));
				booking.setParking(parking);
			}
		}  catch (ParseException e) {
			e.printStackTrace();	
		}
		return parking;
	}

	@Override
	public List<Parking> findParkings() {
		List<Parking> parkings = new ArrayList();
		
		String findParkingsUrl = url+"findParkings";
		
		String respuesta = enviarPeticion(findParkingsUrl);
		
		JSONParser parser = new JSONParser();
		
		JSONArray JSONParkingArray;
		JSONArray JSONBookingArray;
		
		JSONObject JSONParking;
		JSONObject JSONBooking;
		
		Parking parking;
		Booking booking;
		
		try {
			JSONParkingArray = (JSONArray) parser.parse(respuesta);
			
			for(Object p : JSONParkingArray) {
				parking = new Parking();
				
				JSONParking = (JSONObject) p;
				
				parking.setCode(Long.parseLong(String.valueOf(JSONParking.get("Code"))));
				parking.setName(String.valueOf(JSONParking.get("Name")));
				parking.setAddress(String.valueOf(JSONParking.get("Address")));
				parking.setSpaces(Integer.parseInt(String.valueOf(JSONParking.get("TotalSpaces"))));
				parking.setBookableSpaces(Integer.parseInt(String.valueOf(JSONParking.get("BookableSpaces"))));
				parking.setLongitude(Double.parseDouble(String.valueOf(JSONParking.get("Longitude"))));
				parking.setLatitude(Double.parseDouble(String.valueOf(JSONParking.get("Latitude"))));
				
				JSONBookingArray = (JSONArray) JSONParking.get("Bookings");

				for(Object b: JSONBookingArray) {
					booking = new Booking();
					
					JSONBooking = (JSONObject) b;
					
					booking.setCode(Long.parseLong(String.valueOf(JSONBooking.get("Code"))));
					booking.setDate(LocalDate.parse(String.valueOf(JSONBooking.get("Date"))));
					booking.setLicencePlate(String.valueOf(JSONBooking.get("LicencePlate")));
					booking.setParking(parking);
				}
				parkings.add(parking);
			}
		}  catch (ParseException e) {
			e.printStackTrace();	
		}
		return parkings;
	}

	@Override
	public int availableSpaces(Long parkingCode, LocalDate date) {
		String availableSpacesUrl = url+"availableSpaces&parkingCode="+parkingCode+"&date="+date;

		String respuesta = enviarPeticion(availableSpacesUrl);

		int plazasDisponibles = 0;
		
		JSONParser parser = new JSONParser();
		
		JSONObject JSONPlazas;
		
		try {
			JSONPlazas = (JSONObject) parser.parse(respuesta);

			plazasDisponibles = Integer.parseInt(String.valueOf(JSONPlazas.get("AvailableSpaces")));
		}  catch (ParseException e1) {
			e1.printStackTrace();	
		}

		return plazasDisponibles;
	}

	@Override
	public Booking createBooking(String licencePlate, LocalDate date, Long parkingCode) {
		LocalDate fechaActual= null;
		LocalDate fechaReserva = null;
		
		Booking booking = new Booking();
		fechaActual = fechaActual.now();
		fechaReserva = date;
		
		String createBookingUrl = url+"createBooking&licencePlate="+licencePlate+"&date="+date.toString()+"&parkingCode="+parkingCode;
		
		List<Booking> lista = findBookings();
		
		for(Booking b : lista)
			if(licencePlate.equals(b.getLicencePlate()) && b.getDate().equals(date))
				throw new BookingServiceException("No se puede hacer más de 1 reserva diaria por coche.");	
	    
		if(fechaReserva.minusDays(15).isAfter(fechaActual))
			throw new BookingServiceException("No se pueden hacer reservas con más de 15 dias de antelación.");
		
	    if(fechaActual.isAfter(fechaReserva))
			throw new BookingServiceException("No se pueden hacer reservas anteriores al día actual.");
	    
		if(availableSpaces(parkingCode, date) == 0)
			throw new BookingServiceException("No quedan plazas disponibles en el parking seleccionado.");
	    
	    String respuesta = enviarPeticion(createBookingUrl);
	    
		JSONParser parser = new JSONParser();

		JSONObject JSONBooking;
	    
		try {
			JSONBooking = (JSONObject) parser.parse(respuesta);

			booking.setCode(Long.parseLong(String.valueOf(JSONBooking.get("Code"))));
			booking.setLicencePlate(String.valueOf(JSONBooking.get("LicencePlate")));
			booking.setDate(LocalDate.parse(String.valueOf(JSONBooking.get("Date"))));
			booking.setParking(findParking(Long.parseLong(String.valueOf(JSONBooking.get("ParkingCode")))));
		}  catch (ParseException e1) {
			e1.printStackTrace();	
		}
		return booking;
	}

	@Override
	public void cancelBooking(Long bookingCode) {
		List<Booking> bookings = findBookings();
		
		Booking aCancelar = null;
		
		for(Booking b: bookings)
			if(b.getCode() == bookingCode)
				aCancelar = b;

		LocalDate fechaReserva = aCancelar.getDate();

		if(fechaReserva.compareTo(LocalDate.now()) > 0) {
			String cancelBookingUrl = url+"removeBooking&bookingCode="+bookingCode;
			String respuesta = enviarPeticion(cancelBookingUrl);
		} else
			throw new BookingServiceException("Solo se puede cancelar una reserva con antelación.");
	}

	@Override
	public List<Booking> findBookings() {
		List<Booking> bookings = new ArrayList();

		String findBookingsUrl = url+"findBookings";

		String respuesta = enviarPeticion(findBookingsUrl);

		JSONParser parser = new JSONParser();

		JSONArray JSONBookingArray;

		JSONObject JSONBooking;

		Booking booking;

		try {
			JSONBookingArray = (JSONArray) parser.parse(respuesta);

				for(Object b : JSONBookingArray) {
					booking = new Booking();

					JSONBooking = (JSONObject) b;

					booking.setCode(Long.parseLong(String.valueOf(JSONBooking.get("Code"))));
					booking.setDate(LocalDate.parse(String.valueOf(JSONBooking.get("Date"))));
					booking.setLicencePlate(String.valueOf(JSONBooking.get("LicencePlate")));
					booking.setParking(findParking(Long.parseLong(String.valueOf(JSONBooking.get("ParkingCode")))));
					
					bookings.add(booking);
				}

		}  catch (ParseException e) {
			e.printStackTrace();	
		}
		return bookings;
	}

}