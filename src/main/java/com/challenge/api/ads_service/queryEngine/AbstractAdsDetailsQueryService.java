package com.challenge.api.ads_service.queryEngine;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.challenge.api.ads_service.facts.AdsDetails;

public abstract class AbstractAdsDetailsQueryService implements QueryService<AdsDetails> {

	protected static final Logger logger = LoggerFactory.getLogger(AbstractAdsDetailsQueryService.class);
	protected ObjectStore<AdsDetails> inMemoryStore;
	
	/**
	 * The Default constructor which initialize the inMemoryStore
	 */
	public AbstractAdsDetailsQueryService() {
		super();
		inMemoryStore = new InMemoryObjectStore();
	}
	
	/**
	 * The validation method which validates the domain object 
	 * @return boolean
	 */
	
	public boolean validate(AdsDetails objectToSave) {
		if (objectToSave == null)
			return false;
		if(objectToSave.getTargetDemographics()==null || objectToSave.getTargetDemographics().isEmpty())
			return false;
		if(objectToSave.getTargetedCountries()==null || objectToSave.getTargetedCountries().isEmpty())
			return false;
		
		return true;
	}
	
	
	
	
}
