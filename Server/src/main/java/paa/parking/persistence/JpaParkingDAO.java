package paa.parking.persistence;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;
import paa.parking.business.*;
import paa.parking.model.*;

public class JpaParkingDAO extends JpaDAO<Parking, Long>{

	public JpaParkingDAO(EntityManager em, Class<Parking> entityClass) {
		super(em, entityClass);
	}
	
	public int plazasDisponibles(Long parkingCode, LocalDate date) {
		Parking parking = find(parkingCode);
		
		Query q = em.createQuery("select t from Booking t where t.date = :fecha and t.parking = :codigo");
		q.setParameter("fecha", date);
		q.setParameter("codigo", parking);
		
		List<Booking> lista = q.getResultList();
		
		int plazasReservables = parking.getBookableSpaces();
		int plazasReservadas = lista.size();
		int plazasDisponibles = plazasReservables - plazasReservadas;
		
		return plazasDisponibles;
	}
	
}