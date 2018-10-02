package com.ubs.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.ubs.exception.EndOfDayPositionServiceException;
import com.ubs.model.EndOfDayPosition;
import com.ubs.model.StartOfDayPosition;
import com.ubs.model.Transaction;


public class Utilities {
	
	public static List<StartOfDayPosition> readInputPositionFileAndCreatePosition(String path) {
		if(path == null || path.isEmpty()) {
			throw new EndOfDayPositionServiceException("Input position file path ("+path+") is not valid. Please check.");
		}
		
		List<StartOfDayPosition> positionList = new ArrayList<>();
		try {
			List<String> lines = Files.readAllLines(Paths.get(path)).stream().skip(1).collect(Collectors.toList());
			
			for(String line : lines) {
				StartOfDayPosition position = createPositionObject(line);
				positionList.add(position);
			}
		
		} catch (NoSuchFileException e) {
			throw new EndOfDayPositionServiceException("Input position file "+Paths.get(path).getFileName()+" not found ", e);
		} catch (IOException e) {
			throw new EndOfDayPositionServiceException("Error occured while processing input position file ", e);
		}
		return positionList;
	}
	
	private static StartOfDayPosition createPositionObject(String input) {
		String[] splittedInput = input.split(",");
		if(splittedInput.length != 4) {
			throw new EndOfDayPositionServiceException("Input position file is not correct or incorrectly formatted. Please check ");
		}
		
		return new StartOfDayPosition(splittedInput[0], new Long(splittedInput[1]).longValue(), splittedInput[2].charAt(0), new Long(splittedInput[3]).longValue());
	}

	public static List<Transaction> readInputTransactionFileAndCreateTransaction(String path){
		final Type TRANSACTION_TYPE = new TypeToken<List<Transaction>>() {
		}.getType();
		if(path == null || path.isEmpty()) {
			return null;
		}
		Gson gson = new Gson();
		JsonReader reader = null;
		List<Transaction> data = null;
		if(path != null) {
			try {
				reader = new JsonReader(new FileReader(path));
				data = gson.fromJson(reader, TRANSACTION_TYPE);
			} catch (FileNotFoundException e) {
				throw new EndOfDayPositionServiceException("Transaction file not found", e);
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					throw new EndOfDayPositionServiceException("Error occured while processing transaction file ", e);
				}
			}
		}
		return data;
	}

	private static Set<String> getUniqueInstrumentNames(List<StartOfDayPosition> positions) {
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

	public static List<EndOfDayPosition> calculateEndOfDayForInstrument(List<StartOfDayPosition> positions, List<Transaction> transactions) {
		Set<String> uniqueInstrumentNames = getUniqueInstrumentNames(positions);
		
		if(!(uniqueInstrumentNames.size() > 0)) {
			throw new EndOfDayPositionServiceException("No instruments names found to calculate end of day position. Please check input position file.");
		}
		List<EndOfDayPosition> endOfDayPositionsList = new ArrayList<>();
		List<StartOfDayPosition> filteredPositions = null;
		List<Transaction> filteredTransactions = null;
		for(String instrument : uniqueInstrumentNames) {
			filteredPositions = positions.stream().filter(position -> instrument.equals(position.getInstrument())).collect(Collectors.toList());
			
			if(transactions != null && transactions.size() > 0)
				filteredTransactions = transactions.stream().filter(transaction -> instrument.equals(transaction.getInstrument())).collect(Collectors.toList());
			
			if(filteredPositions.size() > 0) {
				long external = filteredPositions.get(0).getQuantity();
				long internal = filteredPositions.get(1).getQuantity();
				
				if(filteredTransactions != null) {
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
		EndOfDayPosition endOfDayPositionInternal = new EndOfDayPosition(instrument, accountInternal, 'I', internalQuantity, internalDelta);
		endOfDayPositionsList.add(endOfDayPositionExternal);
		endOfDayPositionsList.add(endOfDayPositionInternal);
	}

	public static boolean writeEndOfDayPositionFile(List<EndOfDayPosition> endOfDayPositionsList, String path) {
		boolean outputFlag = false;
		if(endOfDayPositionsList != null && endOfDayPositionsList.size() > 0) {
			if(path == null || path.isEmpty()) {
				throw new EndOfDayPositionServiceException("No output path specified for end of day position file.");
			}
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
				System.out.println("Transaction file Expected_EndOfDay_Positions.txt created successfully at "+path);
				outputFlag = true;
			} catch (IOException e) {
				throw new EndOfDayPositionServiceException("Error occured while creating end of day position file", e);
				
			} finally {
				try {
					output.close();
				} catch (IOException e) {
					throw new EndOfDayPositionServiceException("Error occured while creating end of day position file", e);
				}
			}
		}
		else {
			throw new EndOfDayPositionServiceException("End of day positions file not generated.");
		}
		return outputFlag;
	}
	
	
}
