package uo.ri.cws.application.service;

import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientHistoryService;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.spare.SparePartCrudService;
import uo.ri.cws.application.service.vehicle.VehicleCrudService;
import uo.ri.cws.application.service.vehicletype.VehicleTypeCrudService;
import uo.ri.cws.application.service.workorder.CloseWorkOrderService;
import uo.ri.cws.application.service.workorder.ViewAssignedWorkOrdersService;
import uo.ri.cws.application.service.workorder.WorkOrderCrudService;

public interface ServiceFactory {

	// Foreman use cases
	VehicleCrudService forVehicleCrudService();
	ClientCrudService forClientCrudService();
	ClientHistoryService forClientHistoryService();
	WorkOrderCrudService forWorkOrderService();

	// Mechanic use cases
	CloseWorkOrderService forClosingWorkOrder();
	ViewAssignedWorkOrdersService forViewAssignedWorkOrdersService();

	// Manager use cases
    MechanicCrudService forMechanicCrudService();
    VehicleTypeCrudService forVehicleTypeCrudService();

    // SpareParts use cases
    SparePartCrudService forSparePartCrudService();
    
    // Contracts use cases
    ContractCrudService forContractCrudService();
    ContractTypeCrudService forContractTypeCrudService();
    ProfessionalGroupCrudService forProfessionalGroupCrudService();

    // Payroll use cases
    PayrollService forPayrollService();

    // Cashier use cases
    InvoicingService forCreateInvoiceService();

}
