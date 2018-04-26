package com.blackjade.publisher;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.blackjade.publisher.apis.CPublish;
import com.blackjade.publisher.apis.CPublishAns;
import com.blackjade.publisher.apis.ComStatus;


import org.junit.Test;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PublisherApplicationTests {

	private RestTemplate restTemplate;

	private String port = "8112";

	@Before
	public void setUp() throws Exception {
		restTemplate = new RestTemplate();
	}	
	
	@Test
	public void postPublish() {
		String url = "http://localhost:" + port + "/publish";
		CPublish pub = new CPublish();
		
		pub.setRequestid(UUID.randomUUID());
		pub.setClientid(12345);	// client 12345
		pub.setSide('S');		// sell
		pub.setPnsid(1);		// BTC		
		pub.setPnsgid(8);		// crypto currency group, 7 token group
		
		pub.setPrice(57000);	// RMB  
		pub.setQuant(1000000);	// 1.0 BTC 
		pub.setMin(5000);		// min 5000 RMB   
		pub.setMax(10000);		// max 10000 RMB
				
		CPublishAns result = restTemplate.postForObject(url, pub, CPublishAns.class);
		
		// a lot of assertions should be done		
		assertEquals(true, (result.getStatus()==ComStatus.PublishStatus.SUCCESS));
		
		// 
	}
	
	

	/*
	@Test
	public void get() throws Exception {

		String url = "http://localhost:" + port + "/add?a=1&b=2";

		String u = "";
		try {
			u = restTemplate.getForObject(url, String.class);
			System.err.println(u);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(u);

	}

	@Test
	public void post() {

		String url = "http://localhost:" + port + "/add";

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("a", 1);
		map.add("b", 5);

		String result = restTemplate.postForObject(url, map, String.class);

		System.out.println(result);
	}
	*/
	
}















