package com.challenge.api.ads_service.queryEngine;

import java.util.Collection;
import java.util.Set;

import com.challenge.api.ads_service.facts.Country;

public interface QueryService<T> {

	boolean insert(T classz) ;
	Set<T> fetch(Set<Country> targetCountries,Set<String> targetDemographics);
	Set<T> fetch(boolean priority,Set<Country> targetCountries,Set<String> targetDemographics);
}
