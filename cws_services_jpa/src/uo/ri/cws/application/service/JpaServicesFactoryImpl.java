package uo.ri.cws.application.service;

import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientHistoryService;
import uo.ri.cws.application.service.client.crud.ClientCrudServiceImpl;
import uo.ri.cws.application.service.client.history.ClientHistoryServiceImpl;
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
import uo.ri.cws.application.service.spare.crud.SparePartCrudServiceImpl;
import uo.ri.cws.application.service.vechicle.crud.VehicleCrudServiceImpl;
import uo.ri.cws.application.service.vehicle.VehicleCrudService;
import uo.ri.cws.application.service.vehicletype.VehicleTypeCrudService;
import uo.ri.cws.application.service.vehicletype.crud.VehicleTypeCrudServiceImpl;
import uo.ri.cws.application.service.workorder.CloseWorkOrderService;
import uo.ri.cws.application.service.workorder.ViewAssignedWorkOrdersService;
import uo.ri.cws.application.service.workorder.WorkOrderCrudService;
import uo.ri.cws.application.service.workorder.assigned.ViewAssignedWorkOrdersServiceImpl;
import uo.ri.cws.application.service.workorder.close.CloseWorkOrderServiceImpl;
import uo.ri.cws.application.service.workorder.crud.WorkOrderCrudServiceImpl;

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

    @Override
    public SparePartCrudService forSparePartCrudService() {
        return new SparePartCrudServiceImpl();
    }

    @Override
    public ClientCrudService forClientCrudService() {
        return new ClientCrudServiceImpl();
    }

    @Override
    public CloseWorkOrderService forClosingWorkOrder() {
        return new CloseWorkOrderServiceImpl();
    }

    @Override
    public VehicleTypeCrudService forVehicleTypeCrudService() {
        return new VehicleTypeCrudServiceImpl();
    }

    @Override
    public ClientHistoryService forClientHistoryService() {
        return new ClientHistoryServiceImpl();
    }

    @Override
    public WorkOrderCrudService forWorkOrderService() {
        return new WorkOrderCrudServiceImpl();
    }

    @Override
    public ViewAssignedWorkOrdersService forViewAssignedWorkOrdersService() {
        return new ViewAssignedWorkOrdersServiceImpl();
    }

}