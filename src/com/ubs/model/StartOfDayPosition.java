package com.ubs.model;

public class StartOfDayPosition {
	private final  String instrument;
	private final long account;
	private final char accountType;
	private final long quantity;
	
	public StartOfDayPosition(String instrument, long account, char accountType, long quantity) {
		this.instrument = instrument;
		this.account = account;
		this.accountType = accountType;
		this.quantity = quantity;
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
	
	public int hashCode() {
		return instrument.hashCode() + new Long(account).hashCode() + new Character(accountType).hashCode() + new Long(quantity).hashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof StartOfDayPosition) {
			StartOfDayPosition startOfDayPositionObject = (StartOfDayPosition) obj;
			return (this.instrument.equals(startOfDayPositionObject.getInstrument()) &&
					this.account == startOfDayPositionObject.getAccount() &&
					this.accountType == startOfDayPositionObject.getAccountType() &&
					this.quantity == startOfDayPositionObject.getQuantity()
					);
		}
		return false;
	}
	
}
