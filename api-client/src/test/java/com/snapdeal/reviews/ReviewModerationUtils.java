package com.snapdeal.reviews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snapdeal.reviews.client.api.ProfanityClientService;
import com.snapdeal.reviews.client.api.ReportsClientService;
import com.snapdeal.reviews.client.api.ReviewModerationClientService;
import com.snapdeal.reviews.client.api.UserClientService;
import com.snapdeal.reviews.client.factory.ReviewClientFactory;
import com.snapdeal.reviews.client.factory.ReviewClientFactory.ConfigurationParams;
import com.snapdeal.reviews.commons.dto.ReviewModerationRequest;
import com.snapdeal.reviews.commons.dto.Status;
import com.snapdeal.reviews.commons.dto.UpdateReviewModerationRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateReviewOnCheckinRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateReviewOnCheckoutRequest;

public class ReviewModerationUtils {
	
	public static ReviewModerationClientService getReviewModerationClient() {
		Map<ConfigurationParams, String> configParams = new HashMap<>();
		configParams.put(ConfigurationParams.BASE_URL, "http://" + ReviewTestUtils.hostName + "/reviews-api");
		ReviewClientFactory.init(configParams);
		return ReviewClientFactory.getModerationClient();
	}
	
	public static ProfanityClientService getReviewProfanityClient() {
		Map<ConfigurationParams, String> configParams = new HashMap<>();
		configParams.put(ConfigurationParams.BASE_URL, "http://" + ReviewTestUtils.hostName + "/reviews-api");
		ReviewClientFactory.init(configParams);
		return ReviewClientFactory.getProfanityClient();
	}
	
	public static UserClientService getReviewUserClient() {
		Map<ConfigurationParams, String> configParams = new HashMap<>();
		configParams.put(ConfigurationParams.BASE_URL, "http://" + ReviewTestUtils.hostName + "/reviews-api");
		ReviewClientFactory.init(configParams);
		return ReviewClientFactory.getUserClient();
	}
	
	public static ReportsClientService getReportsClient() {
		Map<ConfigurationParams, String> configParams = new HashMap<>();
		configParams.put(ConfigurationParams.BASE_URL, "http://" + ReviewTestUtils.hostName + "/reviews-api");
		ReviewClientFactory.init(configParams);
		return ReviewClientFactory.getReportsClientService();
	}

	public static UpdateReviewOnCheckoutRequest getUpdateReviewOnCheckoutRequest(List<String> reviewIds) {
		UpdateReviewOnCheckoutRequest checkoutRequest = new UpdateReviewOnCheckoutRequest();
		checkoutRequest.setModeratorId("df1ef3f9-2492-438e-b069-bf87c1c57e91");
		checkoutRequest.setReviewIds(reviewIds);
		return checkoutRequest;
	}

	public static UpdateReviewOnCheckinRequest getUpdateReviewOnCheckinRequest(List<String> reviewIds, Status status) {
		UpdateReviewOnCheckinRequest checkinRequest = new UpdateReviewOnCheckinRequest();
		checkinRequest.setModeratorId("df1ef3f9-2492-438e-b069-bf87c1c57e91");
		List<ReviewModerationRequest> rMr = new ArrayList<ReviewModerationRequest>();
		UpdateReviewModerationRequest reviewModerationRequest = new UpdateReviewModerationRequest();
		for (String reviewId : reviewIds) {
			ReviewModerationRequest reModerationRequest = new ReviewModerationRequest();
			reModerationRequest.setReviewId(reviewId);
			reModerationRequest.setStatus(status);
			reModerationRequest.setModeratorRating(5);
			//reModerationRequest.setReviewRejectionReasonID(ReviewRejectionReason.COPYRIGHT_VIOLATION.name());
			rMr.add(reModerationRequest);
		}
		reviewModerationRequest.setReviewModerationRequests(rMr);
		checkinRequest.setReviewModerationRequest(reviewModerationRequest);
		return checkinRequest;
	}

}
