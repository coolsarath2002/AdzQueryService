package com.challenge.api.ads_service.queryEngine;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import com.challenge.api.ads_service.facts.AdsDetails;
import com.challenge.api.ads_service.facts.Country;

public class AdsDetailsLoader {
	
	private static Random rndm=new Random();
	
	public AdsDetailsLoader(){
		
	}
	
	public AdsDetails createAdsDetails() {
		AdsDetails ads = new AdsDetails();
		ads.setHighPriority(MetaDataRandomizer.getPriority().isHigh());
		ads.setTargetedCountries(MetaDataRandomizer.getCountry(rndm.nextInt(10)));
		ads.setTargetDemographics(MetaDataRandomizer.getWords(rndm.nextInt(30)));
		
		AdsDetailsQueryServiceImpl adsQService = new AdsDetailsQueryServiceImpl();
		adsQService.insert(ads);
		return ads;
	}
	
	public Set<AdsDetails> search() {
		AdsDetailsQueryServiceImpl adsQService = new AdsDetailsQueryServiceImpl();
		Set<Country> targetCountries = new TreeSet<Country>();
		targetCountries.add(Country.ALBANIA);
		targetCountries.add(Country.AUSTRALIA);
		targetCountries.add(Country.LIBYA);
		
		Set<String> targetDemographics = new HashSet<String>();
		targetDemographics.add("female");
		targetDemographics.add("toyota");
		
		return adsQService.fetch(false, targetCountries, targetDemographics);
	}
}
