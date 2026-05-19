package uo.ri.cws.application.repository;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.domain.Mechanic;

public interface MechanicRepository extends Repository<Mechanic> {

	/**
	 * @param nif
	 * @return the mechanic identified by the nif or null if none
	 */
	Optional<Mechanic> findByNif(String nif);

	/**
	 * @return a list with all mechanics (might be empty)
	 */
	List<Mechanic> findAll();
}
