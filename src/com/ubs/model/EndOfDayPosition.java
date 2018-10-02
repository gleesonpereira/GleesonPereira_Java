package com.ubs.model;

public class EndOfDayPosition {
	private final String instrument;
	private final long account;
	private final char accountType;
	private final long quantity;
	private final long delta;
	
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
	
	public long getAccount() {
		return account;
	}
	
	public char getAccountType() {
		return accountType;
	}
	
	public long getQuantity() {
		return quantity;
	}
	
	public long getDelta() {
		return delta;
	}
	
	public int hashCode() {
		return instrument.hashCode() + new Long(account).hashCode() + new Character(accountType).hashCode() + new Long(quantity).hashCode() + new Long(delta).hashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof EndOfDayPosition) {
			EndOfDayPosition endOfDayPositionObject = (EndOfDayPosition) obj;
			return (this.instrument.equals(endOfDayPositionObject.getInstrument()) &&
					this.account == endOfDayPositionObject.getAccount() &&
					this.accountType == endOfDayPositionObject.getAccountType() &&
					this.quantity == endOfDayPositionObject.getQuantity() &&
					this.delta == endOfDayPositionObject.getDelta()
					);
		}
		return false;
	}
	
	public String toString() {
		return this.instrument+","+this.account+","+this.accountType+","+this.quantity+","+this.delta;
	}
}
