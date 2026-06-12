package uo.ri.cws.infrastructure.persistence.jpa.repository;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.repository.VehicleTypeRepository;
import uo.ri.cws.domain.VehicleType;
import uo.ri.cws.infrastructure.persistence.jpa.util.BaseJpaRepository;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class VehicleTypeJpaRepository
        extends BaseJpaRepository<VehicleType>
        implements VehicleTypeRepository {

    @Override
    public List<VehicleType> findAll() {
        return Jpa.getManager()
                .createNamedQuery("VehicleType.findAll", VehicleType.class)
                .getResultList();
    }

    /** @return the vehicle type identified by name, or empty */
    public Optional<VehicleType> findByName(String name) {
        return Jpa.getManager()
                .createNamedQuery("VehicleType.findByName", VehicleType.class)
                .setParameter(1, name)
                .getResultStream()
                .findFirst();
    }

}