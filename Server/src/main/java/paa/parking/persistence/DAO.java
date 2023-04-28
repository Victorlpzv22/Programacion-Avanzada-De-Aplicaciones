package paa.parking.persistence;

import java.util.*;

public interface DAO<T, K> {

	/**
	 * Devuelve el elemento con el código dado
	 * 
	 * @param id código del elemento a obtener
	 * @return elemento indicado o null si no existe
	 */
	T find(K id);

	/**
	 * Devuelve una lista con todos los elementos almacenados
	 * 
	 * @return lista de elementos
	 */
	List<T> findAll();

	/**
	 * Añade un nuevo elemento a los almacenados
	 * 
	 * @param c elemento a guardar
	 * @throws DAOException si no se ha podido realizar la operación
	 */
	T create(T c);

	/**
	 * Actualiza un elemento de los almacenados
	 * 
	 * @param c elemento a guardar
	 * @throws DAOException si no se ha podido realizar la operación
	 */
	T update(T c);

	/**
	 * Borra el elemento indicado de los elementos almacenados
	 * 
	 * @param c elemento a borrar
	 * @throws DAOException si no se ha podido realizar la operación
	 */
	void delete(T c);

}
