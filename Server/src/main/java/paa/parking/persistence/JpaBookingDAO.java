package paa.parking.persistence;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import paa.parking.business.*;
import paa.parking.model.*;

public class JpaBookingDAO extends JpaDAO<Booking, Long>{

	public JpaBookingDAO(EntityManager em, Class<Booking> entityClass) {
		super(em, entityClass);
	}
	
}
