package com.challenge.api.ads_service.queryEngine;

import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.challenge.api.ads_service.facts.AdsDetails;


public class AdsDetailsInsertClient extends Thread implements Runnable{
	private static final Logger logger = LoggerFactory.getLogger(AdsDetailsInsertClient.class);
	private static Random rndm=new Random();
	private AdsDetailsQueryServiceImpl adsQService;
	private static AtomicInteger idCounter = new AtomicInteger(90000);
	
	/**
	 * Insert Client for concurrently execution the Insert operation
	 */
	public AdsDetailsInsertClient() {
		adsQService = new AdsDetailsQueryServiceImpl();
	}
	
	/**
	 * Insert Client for concurrently execution the Insert operation.
	 * The method creates a new AdsDetails object with unquie country and demographics
	 * The insert method returns a boolean;
	 */
	public void run()
    {
		String transactionId = UUID.randomUUID().toString();
		logger.info(String.format("PROCESSING|%s|Insert",transactionId));
		AdsDetails ads = new AdsDetails();
		ads.setId(idCounter.getAndIncrement());
		ads.setHighPriority(MetaDataRandomizer.getPriority().isHigh());
		ads.setTargetedCountries(MetaDataRandomizer.getCountry(rndm.nextInt(10)));
		ads.setTargetDemographics(MetaDataRandomizer.getWords(rndm.nextInt(30)));
		boolean isSuccess = adsQService.insert(ads);
		logger.info(String.format("New inserted AdsDetails  was :: %b , %s", isSuccess ,ads.toString()));
		logger.info(String.format("PROCESSED|%s|Insert",transactionId));
		
		
    }
}
