package uo.ri.cws.associations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Intervention;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.SparePart;
import uo.ri.cws.domain.Substitution;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.VehicleType;
import uo.ri.cws.domain.WorkOrder;

class SubstituteTests {
	private Mechanic mechanic;
	private WorkOrder workOrder;
	private Intervention intervention;
	private SparePart sparePart;
	private Substitution sustitution;
	private Vehicle vehicle;
	private VehicleType vehicleType;
	private Client client;

	@BeforeEach
	void setUp() {
		client = new Client("nif-cliente", "nombre", "apellidos");
		vehicle = new Vehicle("1234 GJI", "seat", "ibiza");
		Associations.Owns.link(client, vehicle );

		vehicleType = new VehicleType("coche", 50.0);
		Associations.Classifies.link(vehicleType, vehicle);
		
		workOrder = new WorkOrder(vehicle, "falla la junta la trocla");

		mechanic = new Mechanic("nif-mecanico", "nombre", "apellidos");
	
		intervention = new Intervention(mechanic, workOrder, 60);
		
		sparePart = new SparePart("R1001", "junta la trocla", 100.0);
		sustitution = new Substitution(sparePart, intervention, 2);
	}
	
	@Test
	void testSustitutionInterventionLinked() {
		assertEquals( intervention, sustitution.getIntervention() );
		assertEquals( sparePart, sustitution.getSparePart() );
		
		assertNotNull( sparePart.getSubstitutions() );
		assertFalse( sparePart.getSubstitutions().isEmpty() );
		assertNotNull( intervention.getSubstitutions() );
		assertFalse( intervention.getSubstitutions().isEmpty() );
	}

	@Test
	void testSustitutionSparePartUnlinked() {
		Associations.Substitutes.unlink( sustitution );
		
		assertNull( sustitution.getIntervention() );
		assertNull( sustitution.getSparePart() );
		
		assertFalse( sparePart.getSubstitutions().contains( sustitution ) );
		assertTrue( sparePart.getSubstitutions().isEmpty() );

		assertFalse( intervention.getSubstitutions().contains( sustitution ) );
		assertTrue( intervention.getSubstitutions().isEmpty() );
	}

	@Test
	void testSafeReturnOnIntervention() {
		Set<Substitution> sustituciones = intervention.getSubstitutions();
		sustituciones.remove( sustitution );

		assertTrue( sustituciones.isEmpty() );
		assertEquals( 1, intervention.getSubstitutions().size() );
	}

	@Test
	void testSafeReturnOnSparePart() {
		Set<Substitution> sustituciones = sparePart.getSubstitutions();
		sustituciones.remove( sustitution );

		assertTrue( sustituciones.isEmpty() );
		assertEquals( 1, sparePart.getSubstitutions().size() );
	}
}