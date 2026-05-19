package uo.ri.cws.infrastructure.persistence.jpa.repository;

import java.util.Optional;

import uo.ri.cws.application.repository.InvoiceRepository;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.infrastructure.persistence.jpa.util.BaseJpaRepository;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class InvoiceJpaRepository
		extends BaseJpaRepository<Invoice>
		implements InvoiceRepository {

	@Override
	public Optional<Invoice> findByNumber(Long number) {
	    return Jpa.getManager()
	        .createNamedQuery(
	        		"Invoice.findByNumber",
	        		Invoice.class)
	        .setParameter("number", number)
	        .getResultStream()
	        .findFirst();
	}

	@Override
	public Long getNextInvoiceNumber() {
	    Number result = (Number) Jpa.getManager()
	        .createNamedQuery("Invoice.getNextInvoiceNumber")
	        .getSingleResult();
	    
	    return result != null ? result.longValue() : 1L;
	}
}