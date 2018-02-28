package com.challenge.api.ads_service.facts;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class AdsDetails implements Comparable<AdsDetails> {
	
	private int id;
	private boolean highPriority;
	private Set<Country> targetedCountries;
	private Set<String> targetDemographics;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isHighPriority() {
		return highPriority;
	}
	public void setHighPriority(boolean highPriority) {
		this.highPriority = highPriority;
	}
	public Set<Country> getTargetedCountries() {
		return targetedCountries;
	}
	public void setTargetedCountries(Set<Country> targetedCountries) {
		this.targetedCountries = targetedCountries;
	}
	public Set<String> getTargetDemographics() {
		return targetDemographics;
	}
	public void setTargetDemographics(Set<String> targetDemographics) {
		this.targetDemographics = targetDemographics;
	}
	
	@Override
	public int compareTo(AdsDetails obj) {
		if(id>obj.id){  
	        return 1;  
	    }else if(id<obj.id){  
	        return -1;  
	    } 
	    return 0;  
	}
	
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append(String.format("{ id:%d,", this.getId()));
		str.append(String.format(" highPriority:%s,", this.isHighPriority()));
		str.append(String.format(" targetedCountries:{%s},",StringUtils.join(this.getTargetedCountries(),",")));
		str.append(String.format(" targetDemographics:{%s}}", StringUtils.join(this.getTargetDemographics(),",")));
		return str.toString();
	}
	
	
	
	

}
