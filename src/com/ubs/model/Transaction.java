package com.ubs.model;

public class Transaction {
	private long TransactionId;
	private String Instrument;
	private char TransactionType;
	private long TransactionQuantity;
	
	public Transaction(long TransactionId, String Instrument, char TransactionType, long TransactionQuantity) {
		this.TransactionId = TransactionId;
		this.Instrument = Instrument;
		this.TransactionType = TransactionType;
		this.TransactionQuantity = TransactionQuantity;
	}
	
	public long getTransactionId() {
		return TransactionId;
	}
	public void setTransactionId(long TransactionId) {
		this.TransactionId = TransactionId;
	}
	public String getInstrument() {
		return Instrument;
	}
	public void setInstrument(String Instrument) {
		this.Instrument = Instrument;
	}
	public char getTransactionType() {
		return TransactionType;
	}
	public void setTransactionType(char TransactionType) {
		this.TransactionType = TransactionType;
	}
	public long getTransactionQuantity() {
		return TransactionQuantity;
	}
	public void setTransactionQuantity(long TransactionQuantity) {
		this.TransactionQuantity = TransactionQuantity;
	}
	
	
}
