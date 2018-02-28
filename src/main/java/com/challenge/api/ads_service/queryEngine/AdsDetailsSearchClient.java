package com.challenge.api.ads_service.queryEngine;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.challenge.api.ads_service.facts.AdsDetails;
import com.challenge.api.ads_service.facts.Country;

public class AdsDetailsSearchClient extends Thread implements Runnable{
	private static final Logger logger = LoggerFactory.getLogger(AdsDetailsSearchClient.class);
	private AdsDetailsQueryServiceImpl adsQService;
	private static Random rndm=new Random();
	
	/**
	 * Search Client for concurrently execution the search operation
	 */
	public AdsDetailsSearchClient() {
		adsQService = new AdsDetailsQueryServiceImpl();
	}
	
	/**
	 * Search Client for concurrently execution the search operation.
	 * The method passes either the priority,targetCountries and demographics as param
	 * The fetch method returns the Set<AdsDetails>;
	 */
	public void run(){
		String transactionId = UUID.randomUUID().toString();
		logger.info(String.format("PROCESSING|%s|Search",transactionId));
		Set<Country> targetCountries = new TreeSet<Country>();
		targetCountries.addAll((MetaDataRandomizer.getCountry(rndm.nextInt(2))));
		
		Set<String> targetDemographics = new HashSet<String>();
		targetDemographics.addAll((MetaDataRandomizer.getWords(rndm.nextInt(4))));
		targetDemographics.add(MetaDataRandomizer.getAge());
		targetDemographics.add(MetaDataRandomizer.getGender());
		
		Set<AdsDetails> response = adsQService.fetch(targetCountries, targetDemographics);
		long endtime = new Date().getTime();
		logger.info(String.format("PROCESSED|%s|Search",transactionId));
		printResponseAdsDetails(response);
		
    }
	
	/**
	 * Regular print statement for sysout the result from In-memory fetch.
	 */
	public void printResponseAdsDetails(Set<AdsDetails> response) {
		Iterator<AdsDetails> entries = response.iterator();
		while(entries.hasNext()) {
			AdsDetails pair = (AdsDetails)entries.next();
			logger.info("printResponseAdsDetails :: " +  pair.toString());
		}
	}
}
