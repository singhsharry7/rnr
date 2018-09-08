package com.snapdeal.reviews;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.reviews.client.api.IConfigurationClientService;
import com.snapdeal.reviews.client.api.ProductMappingService;
import com.snapdeal.reviews.client.api.ReviewClientService;
import com.snapdeal.reviews.client.api.SelfieClientService;
import com.snapdeal.reviews.client.factory.ReviewClientFactory;
import com.snapdeal.reviews.client.factory.ReviewClientFactory.ConfigurationParams;
import com.snapdeal.reviews.commons.OpinionBo;
import com.snapdeal.reviews.commons.UserReviewsInfo;
import com.snapdeal.reviews.commons.config.ReviewConfiguration;
import com.snapdeal.reviews.commons.dto.Review;
import com.snapdeal.reviews.commons.dto.Status;
import com.snapdeal.reviews.commons.dto.VoteType;
import com.snapdeal.reviews.commons.dto.wrapper.CreateFeedbackRequest;
import com.snapdeal.reviews.commons.dto.wrapper.ReviewConfigurationResponse;
import com.snapdeal.reviews.configuration.ConfigurationTestUtils;

public class ReviewTestUtils {

	public static final String hostName = "127.0.0.1:8080";
	
	// public static final String hostName = "10.1.28.14:8080";
	// public static final String hostName = "54.174.93.88:11160";

	public static IConfigurationClientService configurationClientService = ConfigurationTestUtils.getConfigurationClient();
	public static String dummyUser;
	public static String dummyProduct;
	public static String PRODUCT_ID = "16005";

	public static String getDummyUser() {
		dummyUser = UUID.randomUUID().toString();
		return dummyUser;
	}

	public static String getDummyProduct() {
		dummyProduct = UUID.randomUUID().toString();
		return dummyProduct;
	}

	public static Review createRating() {
		Review rating = new Review();
		rating.setProductId(PRODUCT_ID);
		rating.setRating(4);
		// rating.setUserId(getDummyUser());
		return rating;
	}

	public static ReviewClientService getReviewClient() {
		Map<ConfigurationParams, String> configParams = new HashMap<>();
		configParams.put(ConfigurationParams.BASE_URL, "http://" + hostName
				+ "/reviews-api");
		ReviewClientFactory.init(configParams);
		return ReviewClientFactory.getClient();
	}
	
	public static SelfieClientService getSelfieClient() {
		Map<ConfigurationParams, String> configParams = new HashMap<>();
		configParams.put(ConfigurationParams.BASE_URL, "http://" + hostName
				+ "/reviews-api");
		ReviewClientFactory.init(configParams);
		return ReviewClientFactory.getSelfieClient();
	}

	public static ProductMappingService getMappingClient() {
		Map<ConfigurationParams, String> configParams = new HashMap<>();
		configParams.put(ConfigurationParams.BASE_URL, "http://"
				+ ReviewTestUtils.hostName + "/reviews-api");
		ReviewClientFactory.init(configParams);
		return ReviewClientFactory.getProductMappingClient();
	}

	public static Review createDummyProductReview() throws SnapdealWSException {
		ReviewConfigurationResponse configuration = configurationClientService.getReviewConfiguration();
		ReviewConfiguration reviewConfiguration = configuration.getReviewConfiguration();
		Review dummyReview = createRating();

		UserReviewsInfo userReviewsInfo = new UserReviewsInfo();
		userReviewsInfo.setUserId(getDummyUser());
		userReviewsInfo.setCertifiedBuyer(true);
		userReviewsInfo.setNickName("Test Nickname");

		dummyReview.setUserReviewsInfo(userReviewsInfo);
		dummyReview.setComments(RandomStringUtils.randomAlphabetic(reviewConfiguration.getDefaultMinCommentSize()+1));
		dummyReview.setCreatedAt((new Date()).getTime());
		dummyReview.setDownCount(1);
		dummyReview.setHeadline(RandomStringUtils.randomAlphabetic(reviewConfiguration.getDefaultMinTitleSize()+1));
		dummyReview.setRecommended(OpinionBo.YES);
		//dummyReview.setSelection(RichDataTest.createSelection());
		dummyReview.setUpCount(15);
		dummyReview.setStatus(Status.CREATED);

		return dummyReview;
	}

	public static CreateFeedbackRequest createFeedbackRequest(String reviewId) {
		CreateFeedbackRequest createFeedbackRequest = new CreateFeedbackRequest();
		createFeedbackRequest.setReviewId(reviewId);
		createFeedbackRequest.setUserId("1242");
		createFeedbackRequest.setVoteType(VoteType.DOWN);
		return createFeedbackRequest; 
	}
}