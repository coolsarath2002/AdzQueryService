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

AbstractAdsDetailsQueryService.java/AdsDetailsQueryServiceImpl.java - Expose service operation/endpoint.Implementation begin for the AdsQueryService in AdsDetailsQueryServiceImpl.
AdsDetailsInsertClient.java - Insert client which creates new AdsDetails.The class extends Threads and the Ads are created unique using the MetadataRandomizer util.
AdsDetailsSearchClient.java - Search client which creates new AdsDetails.The class extends Threads and the Ads are created unique using the MetadataRandomizer util.
InMemoryDBTemplate.java - This class is a singleton and holds the in-memory cache for the Ads.We have choosen to use concurrentHashMap,CopyOnWriteArraySet for support concurrency.Trie datastructure is used to matching search for key matching the search criteria. 
MetaDataRandomizer - This utils is used for creating valid random data for creating the targetCountries and prority. The application contains a external file which contains a list of preference key words which can be used for generating unique targetdemographics.



Supported/Use cases:

All the below use cases must support concurrently.

Search Operation : high Priority,List of valid targeted countries, Single valid target demographics
       Excepted: Return list of ads matching all criteria. 
Search Operation : high Priority,List of valid targeted countries, Multiple matching target demographics
       Excepted: Return list of ads matching all criteria. 
Search Operation : high Priority, List of in-valid targeted countries, Subset of valid target demographics
        Excepted: Empty Ads returned. 
Search Operation : low Priority, List of valid targeted countries, Subset of in-valid target demographics
        Excepted: Empty Ads returned.
Search Operation : low Priority, List of valid targeted countries, partial matching target demographics
        Excepted: Empty Ads returned.
Search Operation : low Priority, List of valid targeted countries
        Excepted: Return all ads for the targeted countries.
Search Operation : low Priority,  Multiple matching target demographics
        Excepted: Return all ads for the countries with matching demographics.
Search Operation : low Priority,   List of valid targeted countries, Subset of valid target demographics
        Excepted: Maintain the ad counter and check if the ads published less than 10 times.
Search Operation : Check the ads counter if it reached the threshold.
        Excepted: Remove the ads from the result set if the ads published more than 10 times .


Insert Operation :  Insert ads details with all information: highPriority,list of targetCountries, list of Demographics
  Excepted:  Return ids for the inserted Ads.
Insert Operation :  Insert ads details with missing targetCountries
  Excepted: Validation failure.Data not inserted.
Insert Operation :  Insert ads details with missing demographics
  Excepted: Validation failure.Data not inserted.

