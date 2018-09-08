package com.snapdeal.reviews.configuration;

import java.util.HashMap;
import java.util.Map;

import com.snapdeal.reviews.ReviewTestUtils;
import com.snapdeal.reviews.client.api.IConfigurationClientService;
import com.snapdeal.reviews.client.factory.ReviewClientFactory;
import com.snapdeal.reviews.client.factory.ReviewClientFactory.ConfigurationParams;

public class ConfigurationTestUtils {
	
	public static IConfigurationClientService getConfigurationClient() {
		Map<ConfigurationParams, String> configParams = new HashMap<>();
		configParams.put(ConfigurationParams.BASE_URL, "http://" + ReviewTestUtils.hostName + "/reviews-api");
		ReviewClientFactory.init(configParams);
		return ReviewClientFactory.getConfigurationClient();
	}
}
