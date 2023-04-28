package paa.parking.persistence;

import  paa.parking.model.*;
import java.sql.*;
import java.util.List;
import javax.persistence.*;

public  class JpaDAO<T,K> implements DAO<T,K>{
	
	protected EntityManager em;
	protected Class<T> clazz;
	
	public JpaDAO(EntityManager em, Class<T> entityClass){
		this.clazz = entityClass;
		this.em = em;
	}
	
	@Override
	public T find(K id) { return em.find(clazz, id); }
	
	@Override
	public T create(T t) {
		try {
			em.persist(t);
			em.flush();
			em.refresh(t);
			return t;
		} catch (EntityExistsException ex) {
			throw new DAOException("La entidad ya existe", ex);
		}
	}
	
	@Override
	public T update(T t) { return (T) em.merge(t); }
	
	@Override
	public void delete(T t) { t = em.merge(t); em.remove(t); }
	
	@Override
	public List<T> findAll() {
		Query q = em.createQuery("select t from " + clazz.getName() + " t");
		List<T> lista = q.getResultList();
		
		return lista;
	}

}