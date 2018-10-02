1. Project created using Java 8
2. Google gson library, Junit & hamcrest-core jars should be in your classpath.
3. To run the application, pass arguments to CalculateEndOfDayService class. The arguments are complete path to position file, complete path to transaction file & path to the folder where your
want output to be generated.
For example,
java CalculateEndOfDayService C:\Users\Wendigo\Desktop\UBS_CodingAssignment\Input_StartOfDay_Positions.txt C:\Users\Wendigo\Desktop\UBS_CodingAssignment\1537277231233_Input_Transactions.txt C:\Users\Wendigo\Desktop\UBS_CodingAssignment\Output
4. Before you run Junit, please modify the values for these variable inputPositionFile, inputInvalidPositionFile, inputTransactionFile, outputFilePath to point to the files in resources folder.
5. To run junit, run the CalculateEndOfDayServiceTest class. This will output all the method names that are currently being run and a summary at end of total test cases ran and failures if any.