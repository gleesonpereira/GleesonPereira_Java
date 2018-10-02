package com.ubs.service;

public class CalculateEndOfDayService {

	public static void main(String[] args) throws Exception {
		if(args.length != 3) {
			System.out.println("Please provide path to input position file, path to transaction file & path to the folder where output should be generated as arguments");
			return;
		}
		
		EndOfDayPositionServiceImpl service = new EndOfDayPositionServiceImpl();
		service.writeEndOfDayPositionFile(service.calculateEndOfDayForInstrument(args[0], args[1]), args[2]);
		
	}

}
