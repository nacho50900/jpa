package uo.ri.cws.infrastructure.persistence.jpa.repository;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.repository.SparePartRepository;
import uo.ri.cws.domain.SparePart;
import uo.ri.cws.infrastructure.persistence.jpa.util.BaseJpaRepository;

@SuppressWarnings("unused")
public class SparePartJpaRepository
		extends BaseJpaRepository<SparePart>
		implements SparePartRepository {

	@Override
	public Optional<SparePart> findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SparePart> findUnderStockNotPending() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SparePart> findByDescription(String description) {
		// TODO Auto-generated method stub
		return null;
	}

}
