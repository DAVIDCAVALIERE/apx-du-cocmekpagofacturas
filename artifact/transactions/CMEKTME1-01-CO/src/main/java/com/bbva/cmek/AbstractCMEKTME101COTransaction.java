package com.bbva.cmek;

import com.bbva.cmek.dto.bill.PaymentDTO;
import com.bbva.elara.transaction.AbstractTransaction;

/**
 * In this class, the input and output data is defined automatically through the setters and getters.
 */
public abstract class AbstractCMEKTME101COTransaction extends AbstractTransaction {

	public AbstractCMEKTME101COTransaction(){
	}


	/**
	 * Return value for input parameter payment
	 */
	protected PaymentDTO getPayment(){
		return (PaymentDTO)this.getParameter("payment");
	}
}
