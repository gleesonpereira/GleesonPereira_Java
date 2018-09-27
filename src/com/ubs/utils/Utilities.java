package com.ubs.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.ubs.model.EndOfDayPosition;
import com.ubs.model.StartOfDayPosition;
import com.ubs.model.Transaction;


public class Utilities {
	
	public static List<StartOfDayPosition> readFileAndCreatePosition(String path) {
		if(path == null) {
			return null;
		}
		
		BufferedReader reader = null;
		String line;
		List<StartOfDayPosition> positionList = new ArrayList<>();
		try {
			reader = new BufferedReader(new FileReader(new File(path)));
			int lineNo = 0;
			while ((line = reader.readLine()) != null) {
				if(lineNo == 0) {
					
					lineNo++;
					continue;
				}
				StartOfDayPosition position = createPositionObject(line);
				positionList.add(position);
				lineNo++;
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Input position file not found at "+path, e);
		} catch (IOException e) {
			throw new RuntimeException("Error occured while processing input position file ", e);
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					throw new RuntimeException("Error occured while processing input position file ", e);
				}				
			}
		}
		return positionList;
	}
	
	private static StartOfDayPosition createPositionObject(String input) {
		String[] splittedInput = input.split(",");
		if(splittedInput.length != 4) {
			throw new RuntimeException("Input file is not correct or formatted correctly. Please check ");
		}
		
		return new StartOfDayPosition(splittedInput[0], new Long(splittedInput[1]).longValue(), splittedInput[2].charAt(0), new Long(splittedInput[3]).longValue());
	}

	public static List<Transaction> readAndCreateTransaction(String path){
		final Type TRANSACTION_TYPE = new TypeToken<List<Transaction>>() {
		}.getType();
		Gson gson = new Gson();
		JsonReader reader = null;
		try {
			reader = new JsonReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Transaction file not found", e);
		}
		List<Transaction> data = gson.fromJson(reader, TRANSACTION_TYPE);
		return data;
	}

	public static Set<String> getUniqueInstrumentNames(List<StartOfDayPosition> positions) {
		Set<String> instruments = new LinkedHashSet<>();
		if(positions != null) {
			for(StartOfDayPosition startOfDayPosition : positions) {
				if(!instruments.contains(startOfDayPosition.getInstrument())) {
					instruments.add(startOfDayPosition.getInstrument());
				}
			}
		}
		return instruments;
	}

	public static List<EndOfDayPosition> calculateEndOfDayForInstrument(Set<String> uniqueInstrumentNames, List<StartOfDayPosition> positions,
			List<Transaction> transactions) {
		if(!(uniqueInstrumentNames.size() > 0)) {
			throw new RuntimeException("No instruments names found to calculate end of day position");
		}
		List<EndOfDayPosition> endOfDayPositionsList = new ArrayList<>();
		for(String instrument : uniqueInstrumentNames) {
			List<StartOfDayPosition> filteredPositions = positions.stream().filter(position -> instrument.equals(position.getInstrument())).collect(Collectors.toList());
			List<Transaction> filteredTransactions = transactions.stream().filter(transaction -> instrument.equals(transaction.getInstrument())).collect(Collectors.toList());
			if(filteredPositions.size() > 0) {
				long external = filteredPositions.get(0).getQuantity();
				long internal = filteredPositions.get(1).getQuantity();
			
				for(Transaction transaction : filteredTransactions) {
					if('B' == transaction.getTransactionType()) {
						external = external + transaction.getTransactionQuantity();
						internal = internal - transaction.getTransactionQuantity();
					}
					else if('S' == transaction.getTransactionType()) {
						external = external - transaction.getTransactionQuantity();
						internal = internal + transaction.getTransactionQuantity();
					}
				}
				
				long externalDelta = external - filteredPositions.get(0).getQuantity();
				long internalDelta = internal - filteredPositions.get(1).getQuantity();
			
				createEndOfDayPositionObject(external, internal, externalDelta, internalDelta, instrument, 
						filteredPositions.get(0).getAccount(), filteredPositions.get(1).getAccount() ,endOfDayPositionsList);
			}
		}
		return endOfDayPositionsList;
	}

	private static void createEndOfDayPositionObject(long externalQuantity, long internalQuantity, long externalDelta,
			long internalDelta, String instrument, long accountExternal, long accountInternal, List<EndOfDayPosition> endOfDayPositionsList) {
		EndOfDayPosition endOfDayPositionExternal = new EndOfDayPosition(instrument, accountExternal, 'E', externalQuantity, externalDelta);
		EndOfDayPosition endOfDayPositionInternal = new EndOfDayPosition(instrument, accountExternal, 'I', internalQuantity, internalDelta);
		endOfDayPositionsList.add(endOfDayPositionExternal);
		endOfDayPositionsList.add(endOfDayPositionInternal);
	}

	public static void writeEndOfDayPositionFile(List<EndOfDayPosition> endOfDayPositionsList, String path) {
		if(endOfDayPositionsList != null && endOfDayPositionsList.size() > 0) {
			String header = "Instrument,Account,AccountType,Quantity,Delta";
			Writer output = null;
			try {
				output = new BufferedWriter(new FileWriter(new File(path + File.separatorChar + "Expected_EndOfDay_Positions.txt")));
				output.write(header);
				output.write(System.lineSeparator());
				for(EndOfDayPosition position : endOfDayPositionsList) {
					output.write(position.toString());
					output.write(System.lineSeparator());
				}
				output.flush();
			} catch (IOException e) {
				throw new RuntimeException("Error occured while creating end of day position file", e);
			} finally {
				try {
					output.close();
				} catch (IOException e) {
					throw new RuntimeException("Error occured while creating end of day position file", e);
				}
			}
		}
		else {
			System.out.println("End of day positions file not generated.");
		}
	}
	
	
}
