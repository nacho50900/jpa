package uo.ri.cws.infrastructure.persistence.jpa.repository;

import java.util.List;

import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.domain.PaymentMean;
import uo.ri.cws.infrastructure.persistence.jpa.util.BaseJpaRepository;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class PaymentMeanJpaRepository
        extends BaseJpaRepository<PaymentMean>
        implements PaymentMeanRepository {

    @Override
    public List<PaymentMean> findPaymentMeansByClientId(Long id) {
        // Not needed for base use cases; kept for interface compliance
        return List.of();
    }

    @Override
    public List<PaymentMean> findPaymentMeansByClientNif(String nif) {
        return Jpa.getManager()
                .createNamedQuery("PaymentMean.findByClientNif", PaymentMean.class)
                .setParameter(1, nif)
                .getResultList();
    }

}