package com.snapdeal.reviews;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.testng.annotations.Test;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.reviews.client.api.IConfigurationClientService;
import com.snapdeal.reviews.client.api.ProductMappingService;
import com.snapdeal.reviews.client.api.ReviewClientService;
import com.snapdeal.reviews.client.factory.ReviewClientFactory;
import com.snapdeal.reviews.client.factory.ReviewClientFactory.ConfigurationParams;
import com.snapdeal.reviews.commons.OpinionBo;
import com.snapdeal.reviews.commons.UserReviewsInfo;
import com.snapdeal.reviews.commons.config.ReviewConfiguration;
import com.snapdeal.reviews.commons.dto.Review;
import com.snapdeal.reviews.commons.dto.ReviewResponse;
import com.snapdeal.reviews.commons.dto.Status;
import com.snapdeal.reviews.commons.dto.VoteType;
import com.snapdeal.reviews.commons.dto.wrapper.CreateFeedbackRequest;
import com.snapdeal.reviews.commons.dto.wrapper.CreateReviewRequest;
import com.snapdeal.reviews.commons.dto.wrapper.GetReviewResponse;
import com.snapdeal.reviews.commons.dto.wrapper.ReviewConfigurationResponse;
import com.snapdeal.reviews.configuration.ConfigurationTestUtils;

public class ReviewRatingTest extends ReviewsApiTest {

	private ExecutorService executorService = Executors.newFixedThreadPool(10); 
	
	String getDummyUser(int userNo){
		String user = String.valueOf(userNo).concat("@xyz.com");
		return user;
	}
	
	static int getRandom(int upperLimit){
		int random = RandomUtils.nextInt(upperLimit) + 1;
		return random;
	}
	
	@Test(threadPoolSize = 3, invocationCount = 10,  timeOut = 10000)
	public void createRatingAndReview() throws SnapdealWSException {
		for (int i=0 ; i<1 ; i++){
			ReviewTestUtils.dummyUser = getDummyUser(1);
			ReviewResponse rating = ReviewLifeCycleTest.createRating();
			int n = getRandom(30);
			for (int j=0; j< n; j++){
				ReviewLifeCycleTest.updateRating(getRandom(5), rating.getId());
			}
			ReviewLifeCycleTest.createReview();
		}
	}
	
	@org.junit.Test
	public void createReviewMultithreadedTest(){
		String userName = "abcde";
		for(int i=1; i< 11;i++){
			String user = userName + String.valueOf(i);
			System.out.println("user===================>"+user);
			for (int j=1;j< 11; j++){
				CreateReview createReview = new CreateReview(user);
				executorService.execute(createReview);
			}
		}
		executorService.shutdown();
		try {
			executorService.awaitTermination(1000, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Iterator<String> reviewIterator = reviewIds.iterator();
		while (reviewIterator.hasNext()){
			String reviewId = reviewIterator.next();
			Map<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("id", reviewId);
			try {
				GetReviewResponse review = getClient().getReview(queryParams);
				Review rvw = review.getReview();
				System.out.println("reviews generated" +rvw.getId());
				
			} catch (SnapdealWSException e) {
				System.out.println("Review Id with null fields" +reviewId);
			}
		}
	}
	
	private static AtomicInteger productId = new AtomicInteger(999900);
	
	private static List<String> reviewIds = new ArrayList<String>();
	
	private class CreateReview extends ReviewsApiTest implements Runnable{
		
		private String dummyUser;
		
		CreateReview(String user){
			this.dummyUser = user;
		}
		
		private ReviewResponse createReview(ReviewTestUtility reviewTestUtility) throws SnapdealWSException {
			CreateReviewRequest createReviewRequest = new CreateReviewRequest();
			createReviewRequest.setReviewRequest(reviewTestUtility.createDummyProductReview());
			ReviewResponse createReviewResponse = getClient().createReview(createReviewRequest).getReviewResponse();
			return createReviewResponse;
		}
		
		@Override
		public void run() {
				ReviewTestUtility reviewTestUtility = new ReviewTestUtility();
				reviewTestUtility.setDummyUser(dummyUser);
				reviewTestUtility.setProductId(productId.get());
				try {
					ReviewResponse createReview = createReview(reviewTestUtility);
					reviewIds.add(createReview.getId());
				} catch (SnapdealWSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	}

}

class ReviewTestUtility {

	public static final String hostName = "10.1.23.137:8080";
	
	// public static final String hostName = "10.1.28.14:8080";
	// public static final String hostName = "54.174.93.88:11160";

	public static IConfigurationClientService configurationClientService = ConfigurationTestUtils.getConfigurationClient();
	public String dummyUser;
	public String dummyProduct;
	public int productId;

	public String getDummyUser() {
		//dummyUser = UUID.randomUUID().toString();
		return dummyUser;
	}

	public String getDummyProduct() {
		dummyProduct = UUID.randomUUID().toString();
		return dummyProduct;
	}

	public void setDummyUser(String user){
		this.dummyUser= user;
	}
	
	public void setProductId(int productId){
		this.productId = productId;
	}
	public String getNickname(){
		return RandomStringUtils.randomAlphabetic(4) ;
	}
	
	public Review createRating() {
		Review rating = new Review();
		rating.setProductId(String.valueOf(this.productId));
		rating.setRating(4);
		// rating.setUserId(getDummyUser());
		return rating;
	}

	public ReviewClientService getReviewClient() {
		Map<ConfigurationParams, String> configParams = new HashMap<>();
		configParams.put(ConfigurationParams.BASE_URL, "http://" + hostName
				+ "/reviews-api");
		ReviewClientFactory.init(configParams);
		return ReviewClientFactory.getClient();
	}

	public ProductMappingService getMappingClient() {
		Map<ConfigurationParams, String> configParams = new HashMap<>();
		configParams.put(ConfigurationParams.BASE_URL, "http://"
				+ ReviewTestUtils.hostName + "/reviews-api");
		ReviewClientFactory.init(configParams);
		return ReviewClientFactory.getProductMappingClient();
	}

	public Review createDummyProductReview() throws SnapdealWSException {
		ReviewConfigurationResponse configuration = configurationClientService.getReviewConfiguration();
		ReviewConfiguration reviewConfiguration = configuration.getReviewConfiguration();
		Review dummyReview = createRating();

		UserReviewsInfo userReviewsInfo = new UserReviewsInfo();
		userReviewsInfo.setUserId(getDummyUser());
		userReviewsInfo.setCertifiedBuyer(true);
		userReviewsInfo.setNickName(getNickname());

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

	public CreateFeedbackRequest createFeedbackRequest(String reviewId) {
		CreateFeedbackRequest createFeedbackRequest = new CreateFeedbackRequest();
		createFeedbackRequest.setReviewId(reviewId);
		createFeedbackRequest.setUserId("1242");
		createFeedbackRequest.setVoteType(VoteType.DOWN);
		return createFeedbackRequest; 
	}
}
