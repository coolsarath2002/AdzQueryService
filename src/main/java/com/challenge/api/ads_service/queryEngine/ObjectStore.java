package com.challenge.api.ads_service.queryEngine;

import java.util.Set;

import com.challenge.api.ads_service.facts.AdsDetails;
import com.challenge.api.ads_service.facts.Country;

public interface ObjectStore<T> {

	public void save(T clazz);
	public Set<AdsDetails> search(boolean priority, Set<Country> targetCountries, Set<String> targetDemographics);

}
