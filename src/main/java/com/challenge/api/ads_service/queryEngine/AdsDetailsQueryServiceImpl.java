package com.challenge.api.ads_service.queryEngine;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.challenge.api.ads_service.facts.AdsDetails;
import com.challenge.api.ads_service.facts.Country;

public class AdsDetailsQueryServiceImpl extends AbstractAdsDetailsQueryService {

	
	/**
	 * Default constuctor for AdsDetailsQueryServiceImpl.
	 */
	public AdsDetailsQueryServiceImpl() {
		super();
	}
	/**
	 * The insert method validates the AdsDetails object and save the object to the in memory cache.
	 */
	@Override
	public boolean insert(AdsDetails clazz) {
		if(clazz==null)
			return false;
		try {
			if(validate(clazz)) {
				inMemoryStore.save(clazz);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * The fetch method searches the in memory cache to load the adsDetails by 
	 * targetCountries,targetDemographics.
	 * @return Collection<AdsDetails>
	 */
	@Override
	public Set<AdsDetails> fetch(Set<Country> targetCountries, Set<String> targetDemographics) {
		logger.info(String.format("Inside the fetch with param targetCountries:%s ,targetDemographics: %s ",targetCountries,targetDemographics ));
		Set<AdsDetails> response = new TreeSet<AdsDetails>();
		try {
			if(targetCountries ==null) {
				targetCountries=new HashSet<Country>();
			}
			if(targetDemographics ==null) {
				targetDemographics=new HashSet<String>();
			}
			response.addAll(fetch(true, targetCountries, targetDemographics));
			response.addAll(fetch(false, targetCountries, targetDemographics));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * The fetch method searches the in memory cache to load the adsDetails by 
	 * priority,targetCountries,targetDemographics.
	 * @return Collection<AdsDetails>
	 */
	@Override
	public Set<AdsDetails> fetch(boolean priority, Set<Country> targetCountries, Set<String> targetDemographics) {
		logger.info(String.format("Inside the fetch with param priority:%b,targetCountries:%s ,targetDemographics: %s ",priority,targetCountries,targetDemographics ));
		Set<AdsDetails> response = new TreeSet<AdsDetails>();
		try {
			if(targetCountries ==null) {
				targetCountries=new HashSet<Country>();
			}
			if(targetDemographics ==null) {
				targetDemographics=new HashSet<String>();
			}
			response = inMemoryStore.search(priority, targetCountries, targetDemographics);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
}

