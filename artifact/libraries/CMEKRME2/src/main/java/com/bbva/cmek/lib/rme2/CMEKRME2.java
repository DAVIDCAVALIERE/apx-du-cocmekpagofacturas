package com.bbva.cmek.lib.rme2;

/**
 * The  interface CMEKRME2 class.
 */
public interface CMEKRME2 {

	boolean executeValidateAccountToPayment(String accountNumber, long amountToValidate);
}