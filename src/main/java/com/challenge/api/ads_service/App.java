package com.challenge.api.ads_service;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.challenge.api.ads_service.facts.AdsDetails;
import com.challenge.api.ads_service.queryEngine.AdsDetailsInsertClient;
import com.challenge.api.ads_service.queryEngine.AdsDetailsLoader;
import com.challenge.api.ads_service.queryEngine.AdsDetailsQueryServiceImpl;
import com.challenge.api.ads_service.queryEngine.AdsDetailsSearchClient;
import com.challenge.api.ads_service.queryEngine.InMemoryDBTemplate;

/**
 * Hello world!
 *
 */
public class App 
{
	private static final String	num_Threads="1000";
	private static final String	threadSize="50";
	private AdsDetailsQueryServiceImpl adsQService;
    public static void main( String[] args )
    {
    	App app = new App();
    	app.doOperation(num_Threads, threadSize);
	    	
	}
    
    public void doOperation(String num_Threads,String threadSize) {
    	ExecutorService executor = Executors.newFixedThreadPool(Integer.valueOf(threadSize));
    	adsQService=new AdsDetailsQueryServiceImpl();
    	for (int i = 0; i < Integer.valueOf(num_Threads); i++) {
    		Runnable insertJob = new AdsDetailsInsertClient();
    		Runnable searchJob = new AdsDetailsSearchClient();
            
            executor.execute(insertJob);
            executor.execute(searchJob);
    	}
    	executor.shutdown();
    	try {
            executor.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }    
    }
}
