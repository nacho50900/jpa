package uo.ri.cws.application.repository;

import java.util.List;

import uo.ri.cws.domain.WorkOrder;

public interface WorkOrderRepository extends Repository<WorkOrder>{

	/**
	 * @param workOrderIds, list of work order ids to retrieve
	 * @return a list with the work order objects for each existing id
	 * 		or an empty list
	 */
	List<WorkOrder> findByIds(List<String> workOrderIds);

	/**
	 * @param nif of the client
	 * @return a list with the work orders of the vehicles of the client
	 * 		or an empty list
	 */
	List<WorkOrder> findByClientNif(String nif);

	/**
	 * @param plate of the vehicle
	 * @return a list with the work orders of the vehicle or an empty list
	 */
	List<WorkOrder> findByPlateNumber(String plate);

	/**
	 * @param nif of the client
	 * @return a list with the work orders of the vehicles of the client that
	 *         are not invoiced yet or an empty list
	 */
	List<WorkOrder> findNotInvoicedByClientNif(String nif);

}