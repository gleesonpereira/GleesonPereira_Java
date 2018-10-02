package com.ubs.service;

import java.util.List;

import com.ubs.model.EndOfDayPosition;
import com.ubs.model.StartOfDayPosition;
import com.ubs.model.Transaction;
import com.ubs.utils.Utilities;

public final class EndOfDayPositionServiceImpl {
	
	public List<StartOfDayPosition> readInputPositionFileAndCreatePosition(String path) {
		return Utilities.readInputPositionFileAndCreatePosition(path);
	}
	
	public List<Transaction> readInputTransactionFileAndCreateTransaction(String path){
		return Utilities.readInputTransactionFileAndCreateTransaction(path);
	}

	
	public List<EndOfDayPosition> calculateEndOfDayForInstrument(String inputFilePath, String transactionFilePath) {
		List<StartOfDayPosition> positions = readInputPositionFileAndCreatePosition(inputFilePath);
		List<Transaction> transactions = Utilities.readInputTransactionFileAndCreateTransaction(transactionFilePath);
		return Utilities.calculateEndOfDayForInstrument(positions, transactions);
	}

	
	public boolean writeEndOfDayPositionFile(List<EndOfDayPosition> endOfDayPositionsList, String path) {
		return Utilities.writeEndOfDayPositionFile(endOfDayPositionsList, path);
	}

}
