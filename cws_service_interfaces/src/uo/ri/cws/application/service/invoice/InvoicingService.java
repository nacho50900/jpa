package uo.ri.cws.application.service.invoice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import uo.ri.util.exception.BusinessException;

/**
 * This service is intended to be used by the Cashier 
 * It follows the ISP principle (@see SOLID principles from RC Martin) 
 */
public interface InvoicingService {

	/**
	 * Creates an invoice for the work orders indicated by its id. All the
	 * 		work orders must exist and be in FINISHED state.
	 * 
	 * @param workOrderIds, the ids of the work orders to be included
	 * @return the fully filled dto of the newly generated invoice
	 * @throws BusinessException if
	 * 	- Any of the indicated work orders are not in FINISHED state
	 * 	- There not exist any of the ids indicated
	 * @throws IllegalArgumentException if 
	 * 	- the list of workOrderIds is null
	 *  - the list of workOrderIds is empty
	 *  - any of the workOrderIds is null
	 */
	InvoiceDto create(List<String> workOrderIds)
			throws BusinessException;

	/**
	 * Returns a list with info of all the work orders of all the client's
	 * vehicles
	 * 
	 * @param nif of the client
	 * @return a list of InvoicingWorkOrderDto or empty list if there is none
	 * @throws BusinessException DOES NOT
	 * @throws IllegalArgumentException if the nif is null
	 */
	List<InvoicingWorkOrderDto> findWorkOrdersByClientNif(String nif)
			throws BusinessException;

	/**
	 * Returns a list with not invoiced work orders of all the client's vehicles
	 * @param nif of the client
	 * @return a list of InvoicingWorkOrderDto or empty list if there is none
	 * @throws BusinessException DOES NOT
	 * @throws IllegalArgumentException if nif is null 
	 */
	List<InvoicingWorkOrderDto> findNotInvoicedWorkOrdersByClientNif(String nif)
			throws BusinessException;

	/**
	 * Returns a list with info of all the work orders of a vehicle
	 * @param plate, the plate number of the vehicle
	 * @return a list of InvoicingWorkOrderDto or empty list if there is none
	 * @throws BusinessException DOES NOT
	 * @throws IllegalArgumentException if plate is null 
	 */
	List<InvoicingWorkOrderDto> findWorkOrdersByPlateNumber(String plate)
			throws BusinessException;

	/**
	 * @param number, of the invoice
	 * @return the InvoiceDto with the data of the invoice
	 * @throws BusinessException DOES NOT
	 * @throws IllegalArgumentException if number is null
	 */
	Optional<InvoiceDto> findInvoiceByNumber(Long number) throws BusinessException;

	/**
	 * @param nif of the client
	 * @return the list of the PaymentMeanDto of a client represented by the nif
	 * 		or an empty list if none
	 * @throws BusinessException DOES NOT
	 * @throws IllegalArgumentException if nif is null
	 */
	List<PaymentMeanDto> findPayMeansByClientNif(String nif)
			throws BusinessException;

	/**
	 * Creates the charges against the indicated payment means (with the amount
	 * indicated) and then pass the invoice to the PAID state.
	 *
	 * @param invoiceId, the id of the invoice to be paid
	 * @param charges, a Map<String, Double>
	 * 	whose:
	 * 	- Key (String) represents the payment mean id, and
	 * 	- Value (Double) represents the amount to be charged to the payment mean
	 *
	 * @throws BusinessException if
	 * 	- there is no invoice for the invoiceId provided
	 *  - the indicated invoice is already in PAID state
	 * 	- does not exist any of the payment means indicated by the id
	 *  - the addition of all amounts charged to each payment mean does not
	 *  	equals the amount of the invoice with a precision of two cents
	 *  	( Math.abs( total - amount) <= 0.01 )
	 * 	- any of the payment means cannot be used to pay the specified amount:
	 * 		- For a CreditCard, if the current date is after the validThough date
	 * 		- For a Voucher, if it has not enough available for the amount
	 * 		- For Cash there is no constraint, cash can be used always
	 *
	 *	@throws IllegalArgumentException if
	 *        - invoiceId is null
	 *        - charges is null
	 *        - charges is empty
	 *        - any of the payment mean ids is null
	 *        - any of the amounts is null
	 */
	void settleInvoice(String invoiceId, Map<String, Double> charges)
			throws BusinessException;

	public static class InvoiceDto {

		public String id;		// the surrogate id (UUID)
		public long version;

		public double amount;	// total amount (money) vat included
		public double vat;		// amount of vat (money)
		public long number;		// the invoice identity, a sequential number
		public LocalDate date;	// of the invoice
		public String state;	// the state as in InvoiceState
	}

	public static class InvoicingWorkOrderDto {
		public String id;
		public String description;
		public LocalDateTime date;
		public String state;
		public double amount;
	}

	public static abstract class PaymentMeanDto {
		public String id;
		public long version;

		public String clientId;
		public double accumulated;
	}

	public static class CashDto extends PaymentMeanDto {}

	public static class CardDto extends PaymentMeanDto {
		public String cardNumber;
		public LocalDate cardExpiration;
		public String cardType;

	}

	public static class VoucherDto extends PaymentMeanDto {
		public String code;
		public String description;
		public double available;

	}
}
