
package com.snapdeal.reviews;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.reviews.commons.OpinionBo;
import com.snapdeal.reviews.commons.UserReviewsInfo;
import com.snapdeal.reviews.commons.dto.RecommendationRequest;
import com.snapdeal.reviews.commons.dto.Review;
import com.snapdeal.reviews.commons.dto.ReviewResponse;
import com.snapdeal.reviews.commons.dto.Status;
import com.snapdeal.reviews.commons.dto.wrapper.CreateRatingRequest;
import com.snapdeal.reviews.commons.dto.wrapper.CreateReviewRequest;
import com.snapdeal.reviews.commons.dto.wrapper.GetReviewResponse;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateRatingRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateRecommendationRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateReviewResponse;

public class UpdateRecommendationTest extends ReviewsApiTest
{

    private static String PRODUCTID;
    private static String USERID;
    private static final int SUCCESSCODE = 200;

    // private final int BADREQUEST = 400;
    // private final int NOTFOUND = 404;

    @Before
    public void setupRequestIds()
    {
	USERID = RandomStringUtils.randomAlphabetic(10);
	PRODUCTID = RandomStringUtils.randomAlphabetic(10);
    }

    @Test
    public void updateRecommendationSuccessCase() throws SnapdealWSException
    {
	// creating review
	createReview();

	// updating the recommendation to YES
	UpdateRecommendationRequest updateRecommendationRequest = getRecommendedBean();
	UpdateReviewResponse response = getClient().updateRecommendation(
	        updateRecommendationRequest);
	assertSuccess(response);
	Map<String , String> queryParams = new HashMap<String , String>();
	queryParams.put("id" , response.getResponse().getId());
	GetReviewResponse review = getClient().getReview(queryParams);
	Assert.assertEquals(updateRecommendationRequest
	        .getRecommendationRequest().getIsRecommended() , review
	        .getReview().getRecommended());

    }

    @Test
    public void updateNotRecommendationSuccessCase() throws SnapdealWSException
    {
	// creating review
	createReview();
	UpdateReviewResponse response = null;
	UpdateRecommendationRequest updateRecommendationRequest = getNotRecommendedBean();
	response = getClient()
	        .updateRecommendation(updateRecommendationRequest);
	assertSuccess(response);
	Map<String , String> queryParams = new HashMap<String , String>();
	queryParams.put("id" , response.getResponse().getId());
	GetReviewResponse review = getClient().getReview(queryParams);
	Assert.assertEquals(updateRecommendationRequest
	        .getRecommendationRequest().getIsRecommended() , review
	        .getReview().getRecommended());

    }

    @Test
    public void updateRecommendationFailureCase()
    {
	// checking invalid request

	UpdateReviewResponse response = null;
	UpdateRecommendationRequest updateRecommendationRequest = getInvalidRequest();
	try
	{
	    response = getClient().updateRecommendation(
		    updateRecommendationRequest);
	}
	catch (Exception ex)
	{
	    System.out.println("Expected Exception for invalid request");
	}
	assertFailure(response);
    }

    public static UpdateRecommendationRequest getRecommendedBean()
    {
	UpdateRecommendationRequest updateRecommendationRequest = new UpdateRecommendationRequest();
	RecommendationRequest recommendationRequest = new RecommendationRequest();
	recommendationRequest.setProductId(PRODUCTID);
	recommendationRequest.setUserId(USERID);
	recommendationRequest.setIsRecommended(OpinionBo.YES);
	updateRecommendationRequest
	        .setRecommendationRequest(recommendationRequest);
	return updateRecommendationRequest;
    }

    public static UpdateRecommendationRequest getNotRecommendedBean()
    {
	UpdateRecommendationRequest updateRecommendationRequest = new UpdateRecommendationRequest();
	RecommendationRequest recommendationRequest = new RecommendationRequest();
	recommendationRequest.setProductId(PRODUCTID);
	recommendationRequest.setUserId(USERID);
	recommendationRequest.setIsRecommended(OpinionBo.NO);
	updateRecommendationRequest
	        .setRecommendationRequest(recommendationRequest);
	return updateRecommendationRequest;
    }

    public static void assertSuccess(UpdateReviewResponse response)
    {
	Assert.assertNotNull(response);
    }

    public static void assertFailure(UpdateReviewResponse response)
    {

	Assert.assertNull(response);
    }

    public static UpdateRecommendationRequest getInvalidRequest()
    {
	UpdateRecommendationRequest updateRecommendationRequest = new UpdateRecommendationRequest();
	RecommendationRequest recommendationRequest = new RecommendationRequest();
	recommendationRequest.setProductId(PRODUCTID);
	recommendationRequest.setUserId(USERID);
	// will not accept ABSTAIN
	recommendationRequest.setIsRecommended(OpinionBo.ABSTAIN);
	updateRecommendationRequest
	        .setRecommendationRequest(recommendationRequest);
	return updateRecommendationRequest;
    }

    public static void createReview() throws SnapdealWSException
    {
	Review dummyReview = new Review();
	UserReviewsInfo userReviewsInfo = new UserReviewsInfo();
	userReviewsInfo.setUserId(USERID);
	userReviewsInfo.setNickName("shakespeare");
	userReviewsInfo.setCertifiedBuyer(true);
	dummyReview.setUserReviewsInfo(userReviewsInfo);
	dummyReview.setRating(4);
	dummyReview.setProductId(PRODUCTID);
	dummyReview.setRating(4);
	dummyReview
	        .setComments("A detailed write up of what the user's opinion A detailed write up of what the user's opinionA detailed write up of what the user's opinionA detailed write up of what the user's opiniop of what the user's opiniop of what the user's opinion");
	dummyReview.setCreatedAt(new Date(1423115040470L).getTime());
	dummyReview.setDownCount(1);
	dummyReview.setHeadline("Short heading of the review");
	dummyReview.setRecommended(OpinionBo.ABSTAIN);
	dummyReview.setUpCount(15);
	dummyReview.setStatus(Status.CREATED);
	CreateReviewRequest createReviewRequest = new CreateReviewRequest();
	createReviewRequest.setReviewRequest(dummyReview);
	ReviewResponse createReviewResponse = null;
	try
	{
	    createReviewResponse = getClient()
		    .createReview(createReviewRequest).getReviewResponse();
	}
	catch (Exception ex)
	{
	    System.out
		    .println("Expected exception if the Review is already exists");
	}
	// Assert.assertEquals(String.valueOf(successcose) ,
	// String.valueOf(SUCCESSCODE));

    }
}
