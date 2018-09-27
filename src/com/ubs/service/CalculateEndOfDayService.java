package com.ubs.service;

import java.util.List;
import java.util.Set;

import com.ubs.model.EndOfDayPosition;
import com.ubs.model.StartOfDayPosition;
import com.ubs.model.Transaction;
import com.ubs.utils.Utilities;

public class CalculateEndOfDayService {

	public static void main(String[] args) throws Exception {
		if(args.length != 3) {
			System.out.println("Please provide path to input position file, path to transaction file & path to the folder where output should be generated as arguments");
			return;
		}
		List<StartOfDayPosition> positions = Utilities.readFileAndCreatePosition(args[0]);
		List<Transaction> transactions = Utilities.readAndCreateTransaction(args[1]);
		Set<String> uniqueInstrumentNames = Utilities.getUniqueInstrumentNames(positions);
		List<EndOfDayPosition> endOfDayPositionsList = Utilities.calculateEndOfDayForInstrument(uniqueInstrumentNames, positions, transactions);
		Utilities.writeEndOfDayPositionFile(endOfDayPositionsList, args[2]);
	}

}
