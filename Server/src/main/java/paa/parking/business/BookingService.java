package paa.parking.business;

import java.time.LocalDate;
import java.util.*;

import javax.persistence.*;
import paa.parking.model.*;
import paa.parking.persistence.*;

public class BookingService implements IBookingService {

	final String PERSISTENCE_UNIT_NAME = "paa-p2"; // Def. en xml
	EntityManagerFactory factory;

	/**
	 * Constructor de la clase.
	 * @param name nombre de la unidad de persistencia PERSISTENCE_UNIT_NAME
	 * @param path Es el path que generado en tiempo de ejecución
	 */
	@SuppressWarnings("unchecked")
	public BookingService (String path) {
		Map properties = new HashMap();
		System.out.println(path);
		properties.put("javax.persistence.jdbc.url", "jdbc:ucanaccess://"+path+";newdataversion=2010");
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, properties);
	}

	@Override
	public Parking createParking(String name, String address, int totalSpaces, int bookableSpaces, double longitude, double latitude) {
		if(longitude >= -90 & longitude <= 90 & latitude >= -180 & latitude <= 180 & totalSpaces >= 0 & bookableSpaces >= 0 & totalSpaces >= bookableSpaces) {
			Parking parking = new Parking(name, address, totalSpaces, bookableSpaces, longitude, latitude);

			EntityManager em = factory.createEntityManager();
			EntityTransaction tx = em.getTransaction();
			tx.begin();

			JpaDAO<Parking, Long> jpa = new JpaDAO<Parking, Long>(em, Parking.class);

			jpa.create(parking);

			tx.commit();
			em.close();

			return parking;
		} else
			throw new BookingServiceException("Los datos introducidos son incorrectos");
	}

	@Override
	public Parking findParking(Long parkingCode) {
		EntityManager em = factory.createEntityManager();
		JpaParkingDAO jpa = new JpaParkingDAO(em, Parking.class);
		
		Parking parkingEncontrado = null;
		parkingEncontrado = jpa.find(parkingCode);
		
		return parkingEncontrado;
	}

	@Override
	public List<Parking> findParkings() {
		EntityManager em = factory.createEntityManager();
		
		JpaDAO jpa = new JpaDAO(em, Parking.class);
		
		return jpa.findAll();
	}

	@Override
	public int availableSpaces(Long parkingCode, LocalDate date) {
		EntityManager em = factory.createEntityManager();
		
		JpaParkingDAO jpaParkingDao = new JpaParkingDAO(em, Parking.class);
		
		return jpaParkingDao.plazasDisponibles(parkingCode, date);
	}
	
	@Override
	public Booking createBooking(String licencePlate, LocalDate date, Long parkingCode) {
		EntityManager em = factory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		JpaDAO<Booking, Long> jpaBooking = new JpaDAO(em, Booking.class);
		JpaDAO<Parking, Long> jpaParking = new JpaDAO(em, Parking.class);
		JpaParkingDAO jpaParkingDao = new JpaParkingDAO(em, Parking.class);

		int plazasDisponibles = jpaParkingDao.plazasDisponibles(parkingCode, date);
		
		LocalDate fechaActual = null;
		LocalDate fechaReserva = null;

		fechaActual = fechaActual.now();
		fechaReserva = date;

		List<Booking> lista = findBookings();

		for(Booking b : lista) {
			if(licencePlate.equals(b.getLicencePlate()) && b.getDate().equals(date))
				throw new BookingServiceException("No se puede hacer más de 1 reserva diaria por coche.");	
		}

		if(fechaReserva.minusDays(15).isAfter(fechaActual))
			throw new BookingServiceException("No se pueden hacer reservas con más de 15 dias de antelación.");

		if(fechaActual.isAfter(fechaReserva))
			throw new BookingServiceException("No se pueden hacer reservas anteriores al día actual.");
		
		if(plazasDisponibles == 0)
			throw new BookingServiceException("No quedan plazas disponibles en el parking seleccionado.");

		Booking booking = new Booking(licencePlate, date, jpaParking.find(parkingCode));
		jpaBooking.create(booking);

		tx.commit();
		em.close();
		return booking;
}

	@Override
	public void cancelBooking(Long bookingCode) {
		EntityManager em = factory.createEntityManager();
		
		JpaDAO<Booking, Long> jpa = new JpaDAO(em, Booking.class);
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		LocalDate fechaActual = null;
		LocalDate fechaReserva = null;
		Booking reserva = jpa.find(bookingCode);
		
		fechaActual = fechaActual.now();
		fechaReserva = reserva.getDate();
		
		if(fechaReserva.compareTo(fechaActual) > 0)
			jpa.delete(jpa.find(bookingCode));
		else
			throw new BookingServiceException("Solo se puede cancelar una reserva con antelación.");
		
		tx.commit();
		em.close();
	}
	
	@Override
	public List<Booking> findBookings() {
		EntityManager em = factory.createEntityManager();
		
		JpaDAO jpa = new JpaDAO(em, Booking.class);
		
		return jpa.findAll();
	}

}