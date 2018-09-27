package com.ubs.model;

public class EndOfDayPosition {
	private String instrument;
	private long account;
	private char accountType;
	private long quantity;
	private long delta;
	
	public EndOfDayPosition(String instrument, long account, char accountType, long quantity, long delta) {
		this.instrument = instrument;
		this.account = account;
		this.accountType = accountType;
		this.quantity = quantity;
		this.delta = delta;
	}
	
	public String getInstrument() {
		return instrument;
	}
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}
	public long getAccount() {
		return account;
	}
	public void setAccount(long account) {
		this.account = account;
	}
	public char getAccountType() {
		return accountType;
	}
	public void setAccountType(char accountType) {
		this.accountType = accountType;
	}
	public long getQuantity() {
		return quantity;
	}
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	public long getDelta() {
		return delta;
	}
	public void setDelta(long delta) {
		this.delta = delta;
	}
	
	public String toString() {
		return this.instrument+","+this.account+","+this.accountType+","+this.quantity+","+this.delta;
	}
}
