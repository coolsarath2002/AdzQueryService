package com.challenge.api.ads_service.queryEngine;

import java.util.Set;
import java.util.TreeSet;

import com.challenge.api.ads_service.facts.AdsDetails;
import com.challenge.api.ads_service.facts.Country;

public class InMemoryObjectStore implements ObjectStore<AdsDetails> {

	protected InMemoryDBTemplate inMemoryTemplate;
	
	/**
	 * Default constructor for InMemoryObjectStore
	 */
	public InMemoryObjectStore(){
		inMemoryTemplate = InMemoryDBTemplate.getInstance();
	}
	
	/** The method save the AdsDetails object into the inmemory cache.
	 * @param AdsDetails
	 * @return boolean
	 */
	@Override
	public void save(AdsDetails clazz){
		inMemoryTemplate.save(clazz);
	}
	/** The method validate if the properties are available in the search criteria.
	 * Based  on the available input param, then the respective operation is performed.
	 * if the target country is empty, then all the low/high ads are retrieved from the in-memory cache.
	 * @param priority
	 * @param targetCountries
	 * @param targetDemographics
	 * @return Set<AdsDetails>
	 * 
	 */
	@Override
	public Set<AdsDetails> search(boolean priority, Set<Country> targetCountries, Set<String> targetDemographics) {
		Set<AdsDetails> selectedAds=new TreeSet<AdsDetails>();
		String priorityString = priority ? MetaDataRandomizer.AdsPriority.HIGH.getValue() : MetaDataRandomizer.AdsPriority.LOW.getValue();;
		if(!targetCountries.isEmpty()) {
			for(Country cty:targetCountries) {
				inMemoryTemplate.find(priorityString, cty.getValue(), selectedAds);
			}
		}else {
			inMemoryTemplate.find(priorityString,"",selectedAds);
		}
		if(targetDemographics!=null && !targetDemographics.isEmpty()) {
			inMemoryTemplate.searchPreferenceTags(selectedAds,targetDemographics);
		}
		return selectedAds;
	}

}
