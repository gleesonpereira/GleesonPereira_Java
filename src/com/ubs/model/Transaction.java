package com.ubs.model;

public class Transaction {
	private final long TransactionId;
	private final String Instrument;
	private final char TransactionType;
	private final long TransactionQuantity;
	
	public Transaction(long TransactionId, String Instrument, char TransactionType, long TransactionQuantity) {
		this.TransactionId = TransactionId;
		this.Instrument = Instrument;
		this.TransactionType = TransactionType;
		this.TransactionQuantity = TransactionQuantity;
	}
	
	public long getTransactionId() {
		return TransactionId;
	}
	
	public String getInstrument() {
		return Instrument;
	}
	
	public char getTransactionType() {
		return TransactionType;
	}
	
	public long getTransactionQuantity() {
		return TransactionQuantity;
	}
	
	public int hashCode() {
		return new Long(TransactionId).hashCode() + Instrument.hashCode() + new Character(TransactionType).hashCode() + new Long(TransactionQuantity).hashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Transaction) {
			Transaction transactionObject = (Transaction) obj;
			return (this.Instrument.equals(transactionObject.getInstrument()) &&
					this.TransactionId == transactionObject.getTransactionId() &&
					this.TransactionType == transactionObject.getTransactionType() &&
					this.TransactionQuantity == transactionObject.getTransactionQuantity()
					);
		}
		return false;
	}
	
	
}
