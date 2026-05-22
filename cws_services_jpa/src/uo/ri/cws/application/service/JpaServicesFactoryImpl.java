package uo.ri.cws.application.service;

import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientHistoryService;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.crud.ContractCrudServiceImpl;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.contracttype.crud.ContractTypeCrudServiceImpl;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.create.InvoicingServiceImpl;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.crud.MechanicCrudServiceImpl;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.payroll.crud.PayrollServiceImpl;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.professionalgroup.crud.ProfessionalGroupCrudServiceImpl;
import uo.ri.cws.application.service.spare.SparePartCrudService;
import uo.ri.cws.application.service.vechicle.crud.VehicleCrudServiceImpl;
import uo.ri.cws.application.service.vehicle.VehicleCrudService;
import uo.ri.cws.application.service.vehicletype.VehicleTypeCrudService;
import uo.ri.cws.application.service.workorder.CloseWorkOrderService;
import uo.ri.cws.application.service.workorder.ViewAssignedWorkOrdersService;
import uo.ri.cws.application.service.workorder.WorkOrderCrudService;
import uo.ri.util.exception.NotYetImplementedException;

public class JpaServicesFactoryImpl implements ServiceFactory {

    @Override
    public MechanicCrudService forMechanicCrudService() {
        return new MechanicCrudServiceImpl();
    }

    @Override
    public InvoicingService forCreateInvoiceService() {
        return new InvoicingServiceImpl();
    }

    @Override
    public ContractCrudService forContractCrudService() {
        return new ContractCrudServiceImpl();
    }

    @Override
    public ContractTypeCrudService forContractTypeCrudService() {
        return new ContractTypeCrudServiceImpl();
    }

    @Override
    public PayrollService forPayrollService() {
        return new PayrollServiceImpl();
    }

    @Override
    public ProfessionalGroupCrudService forProfessionalGroupCrudService() {
        return new ProfessionalGroupCrudServiceImpl();
    }

    @Override
    public VehicleCrudService forVehicleCrudService() {
        return new VehicleCrudServiceImpl();
    }

    // the not yet implemented section ------------------------------

 	@Override
 	public SparePartCrudService forSparePartCrudService() {
 		throw new NotYetImplementedException();
 	}

 	@Override
 	public ClientCrudService forClientCrudService() {
 		throw new NotYetImplementedException();
 	}

 	@Override
 	public CloseWorkOrderService forClosingWorkOrder() {
 		throw new NotYetImplementedException();
 	}

 	@Override
 	public VehicleTypeCrudService forVehicleTypeCrudService() {
 		throw new NotYetImplementedException();
 	}

 	@Override
 	public ClientHistoryService forClientHistoryService() {
 		throw new NotYetImplementedException();
 	}

 	@Override
 	public WorkOrderCrudService forWorkOrderService() {
 		throw new NotYetImplementedException();
 	}

 	@Override
 	public ViewAssignedWorkOrdersService forViewAssignedWorkOrdersService() {
 		throw new NotYetImplementedException();
 	}
    
}