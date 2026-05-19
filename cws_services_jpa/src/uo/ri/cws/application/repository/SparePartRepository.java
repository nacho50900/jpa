package uo.ri.cws.application.repository;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.domain.SparePart;

public interface SparePartRepository extends Repository<SparePart> {

	Optional<SparePart> findByCode(String code);

	List<SparePart> findUnderStockNotPending();

	List<SparePart> findByDescription(String description);

}
