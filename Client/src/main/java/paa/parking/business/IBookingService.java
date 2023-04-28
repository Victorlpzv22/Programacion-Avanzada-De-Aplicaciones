package paa.parking.business;

import java.time.LocalDate;
import java.util.List;

import paa.parking.model.*;

public interface IBookingService {
	/**
	 * Crea un nuevo parking con los parámetros indicados. Debe comprobar que los
	 * parámetros tienen sentido (p.ej., el número total de plazas debe ser igual o
	 * superior al número de plazas reservables, ambos positivos; la longitud y
	 * latitud deben estar en los rangos correctos…); en caso contrario, deberá
	 * lanzar una excepción de tipo BookingServiceException.
	 * 
	 * @return el parking creado
	 * @throws BookingServiceException si no se cumplen las reglas
	 */
	Parking createParking(String name, String address, int totalSpaces, int bookableSpaces, double longitude, double latitude);

	/**
	 * Busca el parking con el código indicado y lo devuelve. En caso de que no
	 * exista devuelve null.
	 * 
	 * @return el parking buscado o null si no se encuenta
	 */
	Parking findParking(Long parkingCode);

	/**
	 * Devuelve todos los parkings existentes.
	 * 
	 * @return la lista con los parkings
	 */
	List<Parking> findParkings();

	/**
	 * Devuelve el número de plazas que aún están disponibles en un parking en una
	 * fecha concreta.
	 * 
	 * @return el número de plazas disponibles
	 */
	int availableSpaces(Long parkingCode, LocalDate date);

	/**
	 * Crea una nueva reserva con los parámetros indicados, siempre y cuando se
	 * cumplan las siguientes reglas de negocio 1. Un mismo coche no puede realizar
	 * más de una reserva el mismo día en el sistema de parking, ni en el mismo ni
	 * en parkings distintos. 2. No se pueden realizar reservas con más de quince
	 * días de antelación. En caso de infracción de cualquiera de estas reglas de
	 * negocio se lanzará una excepción de tipo BookingServiceException y no se
	 * realizará la reserva.
	 * 
	 * @return la reserva creada
	 * @throws BookingServiceException si no se cumplen las reglas
	 */
	Booking createBooking(String licencePlate, LocalDate date, Long parkingCode);

	/**
	 * Cancela la reserva indicada, siempre y cuando esta operación se haga con
	 * antelación a la fecha de reserva. Si se intenta cancelar una reserva en la
	 * misma fecha de reserva no se eliminará la reserva y se lanzará una excepción
	 * de tipo BookingServiceException.
	 * 
	 * @throws BookingServiceException si no se cumplen las reglas
	 */
	void cancelBooking(Long bookingCode);
	
	/**
	 * Devuelve todos los bookings existentes.
	 * 
	 * @return la lista con los bookings
	 */
	List<Booking> findBookings();
}
