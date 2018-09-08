package com.snapdeal.reviews;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.reviews.client.api.ReviewClientService;
import com.snapdeal.reviews.client.api.ReviewModerationClientService;
import com.snapdeal.reviews.client.factory.ReviewClientFactory;
import com.snapdeal.reviews.client.factory.ReviewClientFactory.ConfigurationParams;
import com.snapdeal.reviews.commons.OpinionBo;
import com.snapdeal.reviews.commons.UserReviewsInfo;
import com.snapdeal.reviews.commons.dto.Review;
import com.snapdeal.reviews.commons.dto.ReviewResponse;
import com.snapdeal.reviews.commons.dto.Status;
import com.snapdeal.reviews.commons.dto.wrapper.CreateReviewRequest;

public class AppTest {

	public static void main(String[] args) throws SnapdealWSException {
		// AbstractApplicationContext context = new
		// ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		// ReviewClientService client =
		// context.getBean(ReviewClientServiceImpl.class);

		Map<ConfigurationParams, String> configParams = new HashMap<>();
		configParams.put(ConfigurationParams.BASE_URL, "http://10.1.28.14:8080/reviews-api");
		ReviewClientFactory.init(configParams);
		ReviewClientService client = ReviewClientFactory.getClient();
		//For moderation client
		ReviewModerationClientService moderationClient = ReviewClientFactory.getModerationClient();

		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("productId", "1001");
		client.getReviewStats(queryParams);

		System.exit(0);
		// client.setWebServiceBaseUrl("http://127.0.0.1:8080/reviews-api");
		// Map<String, String> queryParams = new HashMap<String, String>();
		// queryParams.put("productId", "1001");
		// queryParams.put("offset", "0");
		// try {
		// //GetReviewListingPageForModerationResponse reviewListForModeration =
		// client.getReviewListForModeration(queryParams);
		// //System.out.println(reviewListForModeration);
		// } catch (SnapdealWSException e) {
		// e.printStackTrace();
		// }

		Review dummyReview = new Review();
		UserReviewsInfo userReviewsInfo = new UserReviewsInfo();
		userReviewsInfo.setUserId("101");
		userReviewsInfo.setNickName("shakespeare");
		userReviewsInfo.setCertifiedBuyer(true);
		
		dummyReview.setUserReviewsInfo(userReviewsInfo);
		dummyReview.setRating(4);
		dummyReview.setProductId("22222");
		dummyReview.setRating(4);
		//dummyReview.setUserId("55555");
		//dummyReview.setCertifiedBuyer(true);
		dummyReview
				.setComments("A detailed write up of what the user's opinion A detailed write up of what the user's opinionA detailed write up of what the user's opinionA detailed write up of what the user's opiniop of what the user's opiniop of what the user's opinion");
		dummyReview.setCreatedAt(new Date(1423115040470L).getTime());
		dummyReview.setDownCount(1);
		dummyReview.setHeadline("Short heading of the review");
		//dummyReview.setNickName("shakespeare");
		dummyReview.setRecommended(OpinionBo.YES);
		dummyReview.setUpCount(15);
		//dummyReview.setUserId("101");
		dummyReview.setStatus(Status.CREATED);

		CreateReviewRequest createReviewRequest = new CreateReviewRequest();
		createReviewRequest.setReviewRequest(dummyReview);
		ReviewResponse createReviewResponse = client.createReview(createReviewRequest).getReviewResponse();

		// Assert.assertEquals(createRatingResponse.getId(),
		// createReviewResponse.getId());

		System.out.println();
	}

	public static void createReview() {

	}
}