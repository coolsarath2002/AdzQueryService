package com.challenge.api.ads_service.queryEngine;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import com.challenge.api.ads_service.facts.Country;

public class MetaDataRandomizer {
	private static final String fileName = "preferenceKeyWords.txt";
	private static final List<String> preferenceList = new ArrayList<String>();
	private static Random rndm=new Random();
	private static final int MIN_COUNTRY_CD = 1;
	private static final int MAX_COUNTRY_CD = 32;
	static {
		if(preferenceList.isEmpty())
			init();
	}
	enum Gender {
		MALE("male"),
		FEMALE("female");
		
		public String name;
		
		private Gender(String name) {
			this.name=name;
		}
		
		public String getName() {
			return this.name;
		}
	}
	
	public enum AdsPriority {
		HIGH(true,"high"),
		LOW(false,"low");
		
		private Boolean isHigh;
		private String value;
		
		private AdsPriority(Boolean isHigh,String value) {
			this.isHigh=isHigh;
			this.value=value;
		}
		
		public Boolean isHigh() {
			return this.isHigh;
		}
		public String getValue() {
			return this.value;
		}
	}
	
	/*public MetaDataRandomizer(){
		preferenceList = fromFile();
	}*/
	
	public static void init() {
		fromFile();
	}
	/*** Read from a file line by line
	 * @return
	 */	
	public static void fromFile() {
		if(preferenceList.isEmpty()) {
			try {
				ClassLoader classLoader = MetaDataRandomizer.class.getClassLoader();
				InputStreamReader inSource = new InputStreamReader(classLoader.getResourceAsStream(fileName));
				BufferedReader br = new BufferedReader(inSource);
				String addWrd = br.readLine(); 
				while(addWrd!=null && !addWrd.isEmpty()) {
					preferenceList.add(addWrd);
					addWrd=br.readLine();
				}
				br.close();
			}catch(IOException  ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**	 * Generate the random int by a specific range.
	 * @return
	 */
	private static int randomizeIntbyRange(int maxCountryCd, int minCountryCd) {
		Random random = new Random(Double.doubleToLongBits(Math.random()));
		return random.nextInt((maxCountryCd-minCountryCd) + 1) + minCountryCd;
	}
	
	/**
	 * Generate the random word from file loaded during start-up
	 * @return
	 */
	public static Set<String> getWords(int count) {
		Set<String> rdmWord = new TreeSet<String>();
		if(count==0) {
			return rdmWord;
		}
		rdmWord.add(getGender());
		rdmWord.add(getAge());
		for(int i=2;i<=count;i++) {
			rdmWord.add(getWord());
		}
		return rdmWord;
	}

	/**
	 * Generate the random word from file loaded during start-up
	 * @return
	 */
	private static String getWord() {
		return preferenceList.get(rndm.nextInt(preferenceList.size()));
	}
	
	/**
	 * Generate the gender based on random #
	 * @return
	 */
	public static String getGender() {
		String gender = rndm.nextInt(100) % 2 == 0 ? Gender.FEMALE.getName() : Gender.MALE.getName();
		return gender;
	}
	/**
	 * Generate a random age. for testing purpose the age is set to just 2 different values 
	 * @return
	 */
	public static String getAge() {
		String age = rndm.nextInt(100) % 2 == 0 ? "age 25-40" : "age 31-52";
		return age;
	}
	
	/**
	 * Generate a set of Country based on the input param.
	 * @return
	 */
	public static Set<Country> getCountry(int count) {
		Set<Country> rdmWord = new TreeSet<Country>();
		if(count==0) {
			return rdmWord;
		}
		for(int i=1;i<=count;i++) {
			rdmWord.add(getCountry());
		}
		return rdmWord;
		
	}

	/**
	 * Generate a random Country
	 * @return
	 */
	private static Country getCountry() {
		int countryId = randomizeIntbyRange(MAX_COUNTRY_CD,MIN_COUNTRY_CD);
		Country cnty = Country.get(countryId);
		return cnty;
	}
	/**
	 * Generate a random Priority
	 * @return
	 */
	public static AdsPriority getPriority() {
		AdsPriority priority = rndm.nextInt(100) % 2 == 0 ? AdsPriority.HIGH : AdsPriority.LOW;
		return priority;
	}

}


