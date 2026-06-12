package uo.ri.cws.domain;

import java.util.Optional;

public class Associations {

	public static class Owns {

		public static void link(Client client, Vehicle vehicle) {
			client._getVehicles().add(vehicle);
			vehicle._setClient(client);
		}

		public static void unlink(Client client, Vehicle vehicle) {
			vehicle._setClient(null);
			client._getVehicles().remove(vehicle);
			//The order is IMPORTANT, in this case its okay, but in other cases set
			// the client to null first is required in prder to avoid null pointers
			// in the remove as the hash code could have changed.
		}

	}

	public static class Classifies {

		public static void link(VehicleType vehicleType, Vehicle vehicle) {
			vehicleType._getVehicles().add(vehicle);
			vehicle._setVehicleType(vehicleType);
		}

		public static void unlink(VehicleType vehicleType, Vehicle vehicle) {
			vehicle._setVehicleType(null);
			vehicleType._getVehicles().remove(vehicle);
		}
	}

	public static class Holds {

		public static void link(PaymentMean mean, Client client) {
			client._getPaymentMeans().add(mean);
			mean._setClient(client);
		}

		public static void unlink(Client client, PaymentMean mean) {
			mean._setClient(null);
			client._getPaymentMeans().remove(mean);
		}
	}

	public static class Fixes {

		public static void link(Vehicle vehicle, WorkOrder workOrder) {
			vehicle._getWorkOrders().add(workOrder);
			workOrder._setVehicle(vehicle);
		}

		public static void unlink(Vehicle vehicle, WorkOrder workOrder) {
			workOrder._setVehicle(null);
			vehicle._getWorkOrders().remove(workOrder);
		}
	}


public static class Bills {
        public static void link(Invoice invoice, WorkOrder workOrder) {
            invoice._getWorkOrders().add(workOrder);
            workOrder._setInvoice(invoice);
        }

        public static void unlink(Invoice invoice, WorkOrder workOrder) {
            invoice._getWorkOrders().remove(workOrder);
            workOrder._setInvoice(null);
        }
    }

    public static class Settles {
        public static void link(Invoice invoice, Charge charge, PaymentMean mp) {
            invoice._getCharges().add(charge);
            mp._getCharges().add(charge);
            charge._setInvoice(invoice);
            charge._setPaymentMean(mp);
        }

        public static void unlink(Charge charge) {
            charge.getInvoice()._getCharges().remove(charge);
            charge.getPaymentMean()._getCharges().remove(charge);
            charge._setInvoice(null);
            charge._setPaymentMean(null);
        }
    }

    public static class Assigns {
        public static void link(Mechanic mechanic, WorkOrder workOrder) {
            mechanic._getAssigned().add(workOrder);
            workOrder._setMechanic(mechanic);
        }

        public static void unlink(Mechanic mechanic, WorkOrder workOrder) {
            mechanic._getAssigned().remove(workOrder);
            workOrder._setMechanic(null);
        }
    }

	public static class Intervenes {
		
	    public static void link(WorkOrder workOrder, Intervention intervention, Mechanic mechanic) {
	        // 1) fijar enlaces en la intervención
	        intervention._setWorkOrder(workOrder);
	        intervention._setMechanic(mechanic);
	
	        // 2) añadir a las colecciones (en ambos lados)
	        workOrder._getInterventions().add(intervention);
	        mechanic._getInterventions().add(intervention);
	    }
	
	    public static void unlink(Intervention intervention) {
	        Mechanic mechanic = intervention.getMechanic();
	        WorkOrder workOrder = intervention.getWorkOrder();
	
	        // Remover de colecciones primero
	        if (mechanic != null) {
				mechanic._getInterventions().remove(intervention);
			}
	        if (workOrder != null) {
				workOrder._getInterventions().remove(intervention);
			}
	
	        // Luego romper enlaces
	        intervention._setMechanic(null);
	        intervention._setWorkOrder(null);
	    }
	}


	public static class Substitutes {

		static void link(SparePart sparePart, Substitution substitution,
				Intervention intervention) {
			substitution._setIntervention(intervention);
			substitution._setSparePart(sparePart);
			
			sparePart._getSubstitutions().add(substitution);
			intervention._getSubstitutions().add(substitution);
		}

		public static void unlink(Substitution substitution) {
			SparePart sparePart = substitution.getSparePart();
			Intervention intervention = substitution.getIntervention();
			
			sparePart._getSubstitutions().remove(substitution);
			intervention._getSubstitutions().remove(substitution);
			
			substitution._setIntervention(null);
			substitution._setSparePart(null);
			
		}
	}

	public static class Employs {
	    
	    public static void link(Mechanic mechanic, Contract contract, 
	                           ContractType type, ProfessionalGroup group) {

	        Optional<Contract> currentContract = mechanic.getContractInForce();
	        if (currentContract.isPresent()) {
	            currentContract.get().terminate(contract.getStartDate());
	        }

	        mechanic._getContracts().add(contract);
	        type._getContracts().add(contract);
	        group._getContracts().add(contract);

	        contract._setMechanic(mechanic);
	        contract._setContractType(type);
	        contract._setProfessionalGroup(group);
	    }
	    
	    public static void unlink(Contract contract) {
	        Mechanic mechanic = contract.getMechanic();
	        ContractType type = contract.getContractType();
	        ProfessionalGroup group = contract.getProfessionalGroup();
	        
	        if (mechanic != null) {
	            mechanic._getContracts().remove(contract);
	        }
	        if (type != null) {
	            type._getContracts().remove(contract);
	        }
	        if (group != null) {
	            group._getContracts().remove(contract);
	        }
	        
	        contract._setMechanic(null);
	        contract._setContractType(null);
	        contract._setProfessionalGroup(null);
	    }
	}
}
