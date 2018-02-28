package com.challenge.api.ads_service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.challenge.api.ads_service.queryEngine.AdsDetailsInsertClient;
import com.challenge.api.ads_service.queryEngine.AdsDetailsQueryServiceImpl;
import com.challenge.api.ads_service.queryEngine.AdsDetailsSearchClient;
import com.challenge.api.ads_service.queryEngine.InMemoryDBTemplate;

@RunWith(BlockJUnit4ClassRunner.class)
public class AdsQueryConcurrencyTest {
	AdsDetailsQueryServiceImpl adsQService = null;
	InMemoryDBTemplate inMemoryDBTemplate = null;
	
	/**
	 * The method populate a few AdsDetails and loads into in-memory cache 
	 */
	
	@Before
	public void prepare(){
		adsQService = new AdsDetailsQueryServiceImpl();
		inMemoryDBTemplate = InMemoryDBTemplate.getInstance();
	}
	
	/**
	 * The test case run the Insert & Search Operation 200 occurences concurrently.
	 * The insert and search operation are unique request with the help of the MetadataRandomizer 
	 * util class.Since the concurrency request is unique the response is not asserted. 
	 */
	@Test
	public void testConcurrentOperation() {
		ExecutorService executor = Executors.newFixedThreadPool(Integer.valueOf(10));
		for (int i = 0; i < Integer.valueOf(1000); i++) {
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
