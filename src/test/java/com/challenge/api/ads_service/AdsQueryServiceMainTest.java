package com.challenge.api.ads_service;


import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.challenge.api.ads_service.facts.AdsDetails;
import com.challenge.api.ads_service.facts.Country;
import com.challenge.api.ads_service.queryEngine.AdsDetailsInsertClient;
import com.challenge.api.ads_service.queryEngine.AdsDetailsQueryServiceImpl;
import com.challenge.api.ads_service.queryEngine.AdsDetailsSearchClient;
import com.challenge.api.ads_service.queryEngine.InMemoryDBTemplate;
import com.challenge.api.ads_service.queryEngine.MetaDataRandomizer;



@RunWith(BlockJUnit4ClassRunner.class)
public class AdsQueryServiceMainTest {
    
	AdsDetailsQueryServiceImpl adsQService = null;
	InMemoryDBTemplate inMemoryDBTemplate = null;
	
	/**
	 * The method populate a few AdsDetails and loads into in-memory cache 
	 */
	@Before
	public void prepare(){
		adsQService = new AdsDetailsQueryServiceImpl();
		inMemoryDBTemplate = InMemoryDBTemplate.getInstance();
		Set<Country> country1 = new HashSet<Country>();
		Set<Country> country2 = new HashSet<Country>();
		Set<Country> country3 = new HashSet<Country>();
		
		Set<String> preferenceWords1 = new HashSet<String>();
		Set<String> preferenceWords2 = new HashSet<String>();
		Set<String> preferenceWords3 = new HashSet<String>();
		
		country1.add(Country.AFGHANISTAN);
		country1.add(Country.ARGENTINA);
		country1.add(Country.JAPAN);
		
		preferenceWords1.add("male");
		preferenceWords1.add("toyota");
		preferenceWords1.add("esurance");
		
		AdsDetails ads1 = new AdsDetails();
		ads1.setId(1);
		ads1.setHighPriority(true);
		ads1.setTargetedCountries(country1);
		ads1.setTargetDemographics(preferenceWords1);
		
		
		
		adsQService.insert(ads1);
		
		country2.add(Country.AFGHANISTAN);
		country2.add(Country.US);
		country2.add(Country.AUSTRALIA);
		
		preferenceWords2.add("female");
		preferenceWords2.add("honda");
		preferenceWords2.add("esurance");
		
		AdsDetails ads2 = new AdsDetails();
		ads2.setId(2);
		ads2.setHighPriority(true);
		ads2.setTargetedCountries(country2);
		ads2.setTargetDemographics(preferenceWords2);
		
		adsQService.insert(ads2);
		
		country3.add(Country.AUSTRIA);
		country3.add(Country.KAZAKHSTAN);
		country3.add(Country.LEBANON);
		
		preferenceWords3.add("female");
		preferenceWords3.add("audi");
		preferenceWords3.add("esurance");
		
		AdsDetails ads3 = new AdsDetails();
		ads3.setId(3);
		ads3.setHighPriority(false);
		ads3.setTargetedCountries(country3);
		ads3.setTargetDemographics(preferenceWords3);
		
		adsQService.insert(ads3);
	}
	
	/**
	 * Test scenario : Insert ads details with all information: highPriority,list of targetCountries, list of Demographics
	 * Excepted:  Return true.
	 */
	@Test
	public void testInsertOperationAllParam() {
		Set<Country> country = new HashSet<Country>();
		Set<String> preference = new HashSet<String>();
		country.add(Country.KUWAIT);
		country.add(Country.KIRIBATI);
		country.add(Country.LAOS);
		
		preference.add("male");
		preference.add("bmw");
		preference.add("progressive");
		
		AdsDetails ads3 = new AdsDetails();
		ads3.setId(4);
		ads3.setHighPriority(false);
		ads3.setTargetedCountries(country);
		ads3.setTargetDemographics(preference);
		
		assertEquals(true,adsQService.insert(ads3));
		
	}
	
	/**
	 * Test scenario : Insert ads details with missing targetCountries
	 * Excepted: Validation failure.Data not inserted.Returns False;
	 */
	@Test
	public void testInsertOperationInValidParam() {
		Set<Country> country = new HashSet<Country>();
		Set<String> preference = new HashSet<String>();
		preference.add("male");
		preference.add("bmw");
		preference.add("progressive");
		
		AdsDetails ads3 = new AdsDetails();
		ads3.setId(4);
		ads3.setTargetDemographics(preference);
		
		assertEquals(false,adsQService.insert(ads3));
		
	}
	
	/**
	 * Test scenario : Insert ads details with missing targetCountries
	 * Excepted: Validation failure.Data not inserted.Returns False;
	 */
	@Test
	public void testInsertOperationInValidParam1() {
		Set<Country> country = new HashSet<Country>();
		country.add(Country.ANDORRA);
		Set<String> preference = new HashSet<String>();
		
		
		AdsDetails ads3 = new AdsDetails();
		ads3.setId(4);
		ads3.setTargetDemographics(preference);
		
		assertEquals(false,adsQService.insert(ads3));
		
	}
	
	
	/**
	 * Search Operation : high Priority,List of valid targeted countries, Single valid target demographics
       Excepted: Return list of ads matching all criteria.
	 */
	@Test
	public void testSearchOperationAllParam() {
		Set<Country> country = new HashSet<Country>();
		country.add(Country.AUSTRIA);
		country.add(Country.AUSTRALIA);
		Set<String> words = new HashSet<String>();
		words.add("female");
		Set<AdsDetails> ads= adsQService.fetch(false, country, words);
		assertEquals(1, ads.size(), 0);
	}
	
	/**
	 * Search Operation : List of valid targeted countries, Multiple matching target demographics
       Excepted: Return list of ads matching for both high & low priority for the criteria.
	 */
	@Test
	public void testSearchOperationPriorityasOptional() {
		Set<Country> country = new HashSet<Country>();
		country.add(Country.AUSTRIA);
		country.add(Country.AUSTRALIA);
		Set<String> words = new HashSet<String>();
		words.add("female");
		Set<AdsDetails> ads= adsQService.fetch(country, words);
		assertEquals(2, ads.size(), 0);
	}
	
	/**
	 * Search Operation : priority,targetDemographics .
       Excepted: Return list of ads matching all demographics for all countries.
	 */
	@Test
	public void testSearchOperationCountryasOptional() {
		Set<Country> country = new HashSet<Country>();
		
		Set<String> words = new HashSet<String>();
		words.add("female");
		Set<AdsDetails> ads= adsQService.fetch(country, words);
		assertEquals(2, ads.size(), 0);
	}
	
	/**
	 * Search Operation : targetDemographics .
       Excepted: Return list of ads matching all demographics for all countries and priority.
	 */
	@Test
	public void testSearchOperationDemographicsOnly() {
		Set<Country> country = new HashSet<Country>();
		
		Set<String> words = new HashSet<String>();
		words.add("female");
		words.add("esurance");
		Set<AdsDetails> ads= adsQService.fetch(country, words);
		assertEquals(2, ads.size(), 0);
	}
	
	/**
	 * Search Operation : low Priority, List of valid targeted countries, partial matching target demographics
        Excepted: Empty Ads returned.
	 */
	@Test
	public void testSearchOperationWithNonMatchingDemographics() {
		Set<Country> country = new HashSet<Country>();
		country.add(Country.AUSTRIA);
		country.add(Country.AUSTRALIA);
		Set<String> words = new HashSet<String>();
		words.add("female");
		words.add("toyota");
		Set<AdsDetails> ads= adsQService.fetch(false, country, words);
		assertEquals(0, ads.size(), 0);
	}
	
	/** Search Operation : low Priority, List of invalid targeted countries, matching target demographics
        Excepted: Empty Ads returned.
	 *  
	 */
	@Test
	public void testSearchOperationWithNonMatchingCountry() {
		Set<Country> country = new HashSet<Country>();
		country.add(Country.AZERBAIJAN);
		country.add(Country.CANADA);
		Set<String> words = new HashSet<String>();
		words.add("female");
		Set<AdsDetails> ads= adsQService.fetch(country, words);
 	}
	
	/**Search Operation : low Priority,   List of valid targeted countries, Subset of valid target demographics
        Excepted: Maintain the ad counter and check if the ads published less than 10 times.
	*/     
	@Test
	public void testSearchOperationCheckAdsServingCounter() {
		Set<Country> country = new HashSet<Country>();
		country.add(Country.AUSTRIA);
		country.add(Country.AUSTRALIA);
		Set<String> words = new HashSet<String>();
		words.add("female");
		for(int i =0;i<5;i++) {
			Set<AdsDetails> ads= adsQService.fetch(country, words);
		}
		assertEquals(5, inMemoryDBTemplate.getAdzCounter(2) ,0);
		assertEquals(5, inMemoryDBTemplate.getAdzCounter(3) ,0);
	}
	/**
	 * Search Operation : Check the ads counter if it reached the threshold.
        Excepted: Remove the ads from the result set if the ads published 10 times .
	 */
	@Test
	public void testSearchOperationMaxoutAdsServingCounter() {
		Set<Country> country = new HashSet<Country>();
		country.add(Country.AUSTRIA);
		country.add(Country.AUSTRALIA);
		Set<String> words = new HashSet<String>();
		words.add("female");
		Set<AdsDetails> ads = null;
		for(int i=0;i<=10;i++) {
			ads= adsQService.fetch(country, words);
		}
		assertEquals(10, inMemoryDBTemplate.getAdzCounter(2) ,0);
		assertEquals(10, inMemoryDBTemplate.getAdzCounter(3) ,0);
		assertEquals(0, ads.size() ,0);
	}
	
	@Test
	public void testSearchOperationConcurrencyAdsServingCounter() {
		
		ExecutorService executor = Executors.newFixedThreadPool(Integer.valueOf(10));
		for (int i = 0; i < Integer.valueOf(10); i++) {
			Runnable insertJob = new Runnable() {
				
				@Override
				public void run() {
					
					Set<Country> country = new HashSet<Country>();
					country.add(Country.AUSTRIA);
					country.add(Country.AUSTRALIA);
					Set<String> words = new HashSet<String>();
					words.add("female");
					Set<AdsDetails> ads = adsQService.fetch(country, words);
					System.out.println("Concurrent testing" + ads.toString());
				}
			};
	        
			executor.execute(insertJob);
		}
		executor.shutdown();
    	try {
            executor.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		
		assertEquals(10, inMemoryDBTemplate.getAdzCounter(2) ,0);
		assertEquals(10, inMemoryDBTemplate.getAdzCounter(3) ,0);
	}
}
