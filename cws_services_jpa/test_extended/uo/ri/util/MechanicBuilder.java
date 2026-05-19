package uo.ri.util;

import uo.ri.cws.domain.Mechanic;

public class MechanicBuilder implements Builder<Mechanic> {
    private String dni = Values.newNif();
    private String name = Values.newName();
    private String surname = Values.newSurname();

    @Override
    public Mechanic build() {
        return new Mechanic(dni, name, surname);
    }

    public MechanicBuilder withDni(String dni) {
        this.dni = dni;
        return this;
    }

    public MechanicBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public MechanicBuilder withSurname(String surname) {
        this.surname = surname;
        return this;
    }

}
