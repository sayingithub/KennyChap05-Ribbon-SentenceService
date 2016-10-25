package com.example;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class SentenceController {

	@Autowired
	LoadBalancerClient loadBalancer;

	@RequestMapping("/sentence")
	public @ResponseBody String getSentence() {
		return getWord("word-services-subject") + " " + getWord("word-services-verb") + " "
				+ getWord("word-services-article") + " " + getWord("word-services-adjective") + " "
				+ getWord("word-services-noun") + ".";
	}

	public String getWord(String service) {

		ServiceInstance instance = loadBalancer.choose(service);
		
		System.out.println("Service Id	["+instance.getServiceId()+"]");
		System.out.println("URI 		["+instance.getUri()+"]");
		System.out.println("Host		["+instance.getHost()+"]");
		System.out.println("Port		["+instance.getPort()+"]");
		
		return (new RestTemplate()).getForObject(instance.getUri(), String.class);
	}
}
