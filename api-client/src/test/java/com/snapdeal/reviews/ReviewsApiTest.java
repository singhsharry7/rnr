package com.snapdeal.reviews;

import org.junit.BeforeClass;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.reviews.client.api.IConfigurationClientService;
import com.snapdeal.reviews.client.api.ProductMappingService;
import com.snapdeal.reviews.client.api.ProfanityClientService;
import com.snapdeal.reviews.client.api.ReportsClientService;
import com.snapdeal.reviews.client.api.ReviewClientService;
import com.snapdeal.reviews.client.api.ReviewModerationClientService;
import com.snapdeal.reviews.client.api.SelfieClientService;
import com.snapdeal.reviews.client.api.UserClientService;
import com.snapdeal.reviews.configuration.ConfigurationTestUtils;

public class ReviewsApiTest {
	private static ReviewClientService client;
	private static ReviewModerationClientService moderationClient;
	private static ProfanityClientService profanityClient;
	private static IConfigurationClientService configurationClient;
	private static UserClientService userClient;
	private static ProductMappingService productMappingClient;
	private static ReportsClientService reportsServiceClient;
	private static SelfieClientService selfieClientService;

	@BeforeClass
	public static void setup() throws SnapdealWSException {
		client = ReviewTestUtils.getReviewClient();
		selfieClientService = ReviewTestUtils.getSelfieClient();
		moderationClient = ReviewModerationUtils.getReviewModerationClient();
		profanityClient = ReviewModerationUtils.getReviewProfanityClient();
		configurationClient = ConfigurationTestUtils.getConfigurationClient();
		userClient = ReviewModerationUtils.getReviewUserClient();
		reportsServiceClient = ReviewModerationUtils.getReportsClient();
		productMappingClient = ReviewTestUtils.getMappingClient();
	}

	protected static ReviewClientService getClient() {
		return client;
	}
	
	protected static SelfieClientService getSelfieClient() {
		return selfieClientService;
	}

	protected static ReviewModerationClientService getModerationClient() {
		return moderationClient;
	}

	public static IConfigurationClientService getConfigurationClient() {
		return configurationClient;
	}

	public static ProfanityClientService getProfanityClient() {
		return profanityClient;
	}

	public static UserClientService getUserClient() {
		return userClient;
	}
	
	public static ReportsClientService getReportsClient() {
		return reportsServiceClient;
	}

	public static ProductMappingService getProductMappingClient() {
		return productMappingClient;
	}
}
