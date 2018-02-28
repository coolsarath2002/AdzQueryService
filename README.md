# AdzQueryService

Run the below command in command prompt
mvn install -DrunSuite=**/AdsDetailsAllTests.class -DfailIfNoTests=false

Run JUNIT from eclipse
AdsDetailsAllTests.java
The suite will run the 
- AdsQueryServiceMainTest first which has hard coded Ads inserted for assertion.
- AdsQueryConcurrencyTest is executed the concurrency testing. The order of exection is important for the assertion.

Run the Main Class
- Run the App.java which executes the insert and search in parallel.The number of thread can be configured in the App.java class.
   private static final String	num_Threads="1000";
	 private static final String	threadSize="50";
