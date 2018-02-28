package com.challenge.api.ads_service.queryEngine;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.challenge.api.ads_service.facts.AdsDetails;
import com.challenge.api.ads_service.facts.Country;

public class InMemoryDBTemplate {
	private static final Logger logger = LoggerFactory.getLogger(InMemoryDBTemplate.class);
	private static InMemoryDBTemplate _instance;
	private Map<Integer, AdsDetails> dataCache = new ConcurrentHashMap<Integer, AdsDetails>();
	private Map<String, CopyOnWriteArraySet<Integer>> priorityCountryCache =new ConcurrentSkipListMap<String,CopyOnWriteArraySet<Integer>>();
	private Map<Integer, AtomicInteger> adsServiceCounter = new ConcurrentHashMap<Integer, AtomicInteger>();
	
	/**
	 * Call the getInstance() to instantiate the InMemoryDBTemplate.
	 */
	static {
		getInstance();
	}
	
	private InMemoryDBTemplate() {
		super();
	}
	
	/**
	 * To instantiate the InMemoryDBTemplate.
	 */
	public static InMemoryDBTemplate getInstance() {
		if(_instance==null) {
			_instance = new InMemoryDBTemplate();
		}
		return _instance;
	}
	
	
	/**
	 * The save methods does the following:
	 *  loads the AdsDetails object into the datacache Map.
	 *  Generate a key for the priorityCountry map based on priority and each Country in the collection.
	 *  Creates a CopyOnWriteArraySet in for each priorityCountry based.
	 *  The id of the AdsDetails is stored in the priorityCountry map.
	 *  Creates a counter for the ads served to 0.
	 * @param ads
	 */
	public void save(AdsDetails ads) {
		dataCache.put(ads.getId(), ads);
		String priorityString = ads.isHighPriority() ? MetaDataRandomizer.AdsPriority.HIGH.getValue() : MetaDataRandomizer.AdsPriority.LOW.getValue();
		//Iterate over each targetCountries and create a Map
		for(Country c:ads.getTargetedCountries()) {
			String newKey=String.format("%s-%s", priorityString,c.getValue()).toLowerCase().trim();
			CopyOnWriteArraySet<Integer>  idSet=  (CopyOnWriteArraySet<Integer>) priorityCountryCache.get(newKey);
			if(idSet==null) {
				idSet = new CopyOnWriteArraySet<Integer>();
			}
			idSet.add(ads.getId());
			priorityCountryCache.put(newKey, idSet);
			
		}
		adsServiceCounter.put(ads.getId(), new AtomicInteger(0));
	}
	
	
	/**
	 * The methods finds the set of ads id from the priorityCountryCache using the PatriciaTrie.
	 * Iterate over the selected Ads id's to get the Ads object from the datacache. 
	 * @param priorityStr
	 * @param targetCountry
	 * @param addToResponse
	 */
	public void find(String priorityStr, String targetCountry,Set<AdsDetails> addToResponse){
		String newKey=String.format("%s-%s", priorityStr,targetCountry).toLowerCase().trim();
		Trie<String,CopyOnWriteArraySet<Integer>> countryTrie = new PatriciaTrie(priorityCountryCache);
		SortedMap<String,CopyOnWriteArraySet<Integer>> resultSet =  countryTrie.prefixMap(newKey);
		if(resultSet!=null && !resultSet.isEmpty()) {
			Iterator<Map.Entry<String,CopyOnWriteArraySet<Integer>>> entries = resultSet.entrySet().iterator();
			while(entries.hasNext()) {
				Map.Entry<String,CopyOnWriteArraySet<Integer>> pair = (Map.Entry<String,CopyOnWriteArraySet<Integer>>)entries.next();
				logger.info(pair.getKey() + " - " + pair.getValue().toString());
			    CopyOnWriteArraySet<Integer> idsSet = pair.getValue();
			    for(Integer idx:idsSet) {
			    	if(dataCache.get(idx)!=null)
			    		addToResponse.add(dataCache.get(idx));
			    }
			}
		}
	}
	/**
	 * The methods get the set/list of targetDemographics and searches on teh matched Ads 
	 * from the find operation. The method further remove the unmatched ids from the addToResponse object.
	 * @param addToResponse
	 * @param targetDemographics
	 */
	public void searchPreferenceTags(Set<AdsDetails> addToResponse,Set<String> targetDemographics) {
		Iterator<AdsDetails> adzIterate = addToResponse.iterator();
		while(adzIterate.hasNext()) {
			AdsDetails adz = adzIterate.next(); 
			if(adz.getTargetDemographics().containsAll(targetDemographics)) {
				AtomicInteger atInt= adsServiceCounter.get(adz.getId());
				if(atInt.intValue() < 10) {
					atInt.set(atInt.incrementAndGet());
					adsServiceCounter.put(adz.getId(), atInt);
					continue;
				}
		    }
			adzIterate.remove();
		} 
	}
	
	/**
	 * Regular print method for sys.out the dataCache map.
	 */
	public void printAllSearch() {
		Iterator<Map.Entry<Integer, AdsDetails>> entries = dataCache.entrySet().iterator();
		while(entries.hasNext()) {
			Map.Entry<Integer, AdsDetails> pair = (Map.Entry<Integer, AdsDetails>)entries.next();
			logger.info(pair.getKey() + " - " + pair.getValue().toString());
		}
	}
	
	public int getAdzCounter(int index) {
		if(adsServiceCounter!=null && adsServiceCounter.containsKey(index)) {
			return adsServiceCounter.get(index).intValue();
		}
		return 0;
	}

}
