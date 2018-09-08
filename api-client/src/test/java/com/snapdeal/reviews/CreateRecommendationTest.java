
package com.snapdeal.reviews;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.reviews.commons.OpinionBo;
import com.snapdeal.reviews.commons.dto.RecommendationRequest;
import com.snapdeal.reviews.commons.dto.wrapper.CreateRecommendationRequest;
import com.snapdeal.reviews.commons.dto.wrapper.CreateReviewResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetReviewResponse;

public class CreateRecommendationTest extends ReviewsApiTest
{

    //private static final String PRODUCTID = "23424r2424";
    //private static final String USERID = "4234223421424";
    private static final int SUCCESSCODE = 200;
    //private static final String USERID_2 = "23421341234";

    @Test
    public void createRecommendation_valid_test() throws SnapdealWSException
    {
	// creating review

	CreateReviewResponse response = null;
	CreateRecommendationRequest createRecommendationRequest = getValidRecommendedBean();
	response = getClient()
	        .createRecommendation(createRecommendationRequest);
	assertSuccess(response);
	Map<String , String> queryParams = new HashMap<String , String>();
	queryParams.put("id" , response.getReviewResponse().getId());
	GetReviewResponse review = getClient().getReview(queryParams);
	Assert.assertEquals(createRecommendationRequest
	        .getRecommendationRequest().getIsRecommended() , review
	        .getReview().getRecommended());

    }

    @Test
    public void createRecommendation_invalid_recommendationValue_Test()
	    throws SnapdealWSException
    {

	CreateReviewResponse response = null;
	CreateRecommendationRequest createRecommendationRequest = getValidRecommendedBean();
	createRecommendationRequest.getRecommendationRequest()
	        .setIsRecommended(OpinionBo.ABSTAIN);
	try
	{
	    response = getClient().createRecommendation(
		    createRecommendationRequest);
	}
	catch (SnapdealWSException ex)
	{
	    System.out.println("Expected Exception for invalid request");
	    Assert.assertEquals(ex.getWsErrorCode() , 400);
	}
	Assert.assertNull(response);

    }

    @Test
    public void createRecommendation_invalid_userId_test()
	    throws SnapdealWSException
    {

	CreateReviewResponse response = null;
	CreateRecommendationRequest createRecommendationRequest = getValidRecommendedBean();
	createRecommendationRequest.getRecommendationRequest().setUserId(null);
	try
	{
	    response = getClient().createRecommendation(
		    createRecommendationRequest);
	}
	catch (SnapdealWSException ex)
	{
	    System.out.println("Expected Exception for invalid request");
	    Assert.assertEquals(ex.getWsErrorCode() , 400);
	}
	Assert.assertNull(response);

    }

    @Test
    public void createRecommendation_invalid_prodectId_test()
	    throws SnapdealWSException
    {

	CreateReviewResponse response = null;
	CreateRecommendationRequest createRecommendationRequest = getValidRecommendedBean();
	createRecommendationRequest.getRecommendationRequest().setProductId(
	        null);
	try
	{
	    response = getClient().createRecommendation(
		    createRecommendationRequest);
	}
	catch (SnapdealWSException ex)
	{
	    System.out.println("Expected Exception for invalid request");
	    Assert.assertEquals(ex.getWsErrorCode() , 400);
	}
	Assert.assertNull(response);

    }

    @Test
    public void createRecommendation_invalid_repeatCreate_test()
	    throws SnapdealWSException
    {
	// creating review

	CreateReviewResponse response = null;
	CreateRecommendationRequest createRecommendationRequest = getValidRecommendedBean();
	try
	{
	    response = getClient().createRecommendation(
		    createRecommendationRequest);
	    assertSuccess(response);
	    response = getClient().createRecommendation(
		    createRecommendationRequest);
	}
	catch (SnapdealWSException ex)
	{
	    System.out.println("Expected Exception for repeted request");
	    Assert.assertEquals(ex.getWsErrorCode() , 403);
	}

    }

    private static void assertSuccess(CreateReviewResponse response)
    {
	Assert.assertNotNull(response);
	Assert.assertNotNull(response.getReviewResponse());
	Assert.assertNotNull(response.getReviewResponse().getId());
    }

    private static CreateRecommendationRequest getValidRecommendedBean()
    {
	CreateRecommendationRequest createRecommendationRequest = new CreateRecommendationRequest();
	RecommendationRequest recommendationRequest = new RecommendationRequest();
	recommendationRequest.setProductId(RandomStringUtils.randomAlphabetic(10));
	recommendationRequest.setUserId(RandomStringUtils.randomAlphabetic(10));
	recommendationRequest.setIsRecommended(OpinionBo.NO);
	createRecommendationRequest
	        .setRecommendationRequest(recommendationRequest);
	return createRecommendationRequest;
    }
}
