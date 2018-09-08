package com.snapdeal.reviews;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.reviews.commons.dto.Rating;
import com.snapdeal.reviews.commons.dto.RatingUpdateRequest;
import com.snapdeal.reviews.commons.dto.wrapper.CreateRatingRequest;
import com.snapdeal.reviews.commons.dto.wrapper.CreateReviewResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetReviewResponse;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateRatingRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateReviewResponse;

public class RatingTest extends ReviewsApiTest {
	
	private CreateRatingRequest ratingRequest;
	
	private UpdateRatingRequest updateRequest;
	
	@Before
	public void setupRatingRequest(){
		ratingRequest = new CreateRatingRequest();
		updateRequest = new UpdateRatingRequest();
	}
	@Test 
	public void createRating_valid_test() throws SnapdealWSException{
		Rating rating = getTestRatingObj();
		ratingRequest.setRating(rating);
		CreateReviewResponse ratingResponse = getClient().createRating(ratingRequest);
		Assert.assertNotNull(ratingResponse.getReviewResponse());
		Assert.assertNotNull(ratingResponse.getReviewResponse().getId());
		
	}

	@Test 
	public void createRating_invalidInput_zeroRating_test(){
		Rating rating = getTestRatingObj();
		rating.setRating(0);
		ratingRequest.setRating(rating);
		try {
			CreateReviewResponse response = getClient().createRating(ratingRequest);
		} catch (SnapdealWSException e) {
			Assert.assertEquals(e.getWsErrorCode(),400);
		}	
	}
	
	@Test 
	public void createRating_invalidInput_noUserId_test(){
		Rating rating = getTestRatingObj();
		rating.setUserId("");
		ratingRequest.setRating(rating);
		try {
			CreateReviewResponse response = getClient().createRating(ratingRequest);
		} catch (SnapdealWSException e) {
			Assert.assertEquals(e.getWsErrorCode(),400);
		}	
	}

	@Test 
	public void createRating_invalidInput_noProductId_test(){
		Rating rating = getTestRatingObj();
		rating.setProductId("");
		ratingRequest.setRating(rating);
		try {
			CreateReviewResponse response = getClient().createRating(ratingRequest);
		} catch (SnapdealWSException e) {
			Assert.assertEquals(e.getWsErrorCode(),400);
		}	
	}
	
	@Test 
	public void createRating_invalidInput_reviewAlreadyExists_test(){
		Rating rating = getTestRatingObj();
		rating.setUserId("EetkiH");
		ratingRequest.setRating(rating);
		CreateReviewResponse response;
		try {
			response = getClient().createRating(ratingRequest);
		} catch (SnapdealWSException e) {
			Assert.assertEquals(e.getWsErrorCode(),403);
		}
		rating = getTestRatingObj();
	}

	
// Update Rating Test cases : 
	
	@Test
	public void updateRating_valid_Test() throws SnapdealWSException{
		int newRating =3;
		int oldRating=5;
		String productId = RandomStringUtils.randomAlphabetic(10);
		String userId = RandomStringUtils.randomAlphabetic(6);
		Rating rating = getTestRatingObj();
		rating.setProductId(productId);
		rating.setRating(oldRating);
		rating.setUserId(userId);
		ratingRequest.setRating(rating);
		
		CreateReviewResponse ratingResponse = getClient().createRating(ratingRequest);
		Assert.assertNotNull(ratingResponse.getReviewResponse());
		Assert.assertNotNull(ratingResponse.getReviewResponse().getId());
		
		RatingUpdateRequest ratingUpdateRequest = new RatingUpdateRequest();
		ratingUpdateRequest.setRating(newRating);
		ratingUpdateRequest.setReviewId(ratingResponse.getReviewResponse().getId());
		ratingUpdateRequest.setUserId(userId);
		updateRequest.setRatingUpdateRequest(ratingUpdateRequest);
		
		UpdateReviewResponse updateResponse = getClient().updateRating(updateRequest);
		Assert.assertNotNull(updateResponse.getResponse());
		Assert.assertNotNull(updateResponse.getResponse().getId());
		
	}
	
	@Test
	public void updateRating_ReviewAlreadyExists_Test(){
		
		RatingUpdateRequest ratingUpdateRequest = new RatingUpdateRequest();
		ratingUpdateRequest.setRating(3);
		ratingUpdateRequest.setReviewId("014c7fa9e1b700004707d18d40b9204e");
		ratingUpdateRequest.setUserId("1");
		updateRequest.setRatingUpdateRequest(ratingUpdateRequest);
		
		UpdateReviewResponse updateResponse;
		try {
			updateResponse = getClient().updateRating(updateRequest);
		} catch (SnapdealWSException e) {
			Assert.assertEquals(e.getWsErrorCode(),403);
		}
	}
	
	@Test
	public void updateRating_invalidRating_Test(){
		
		RatingUpdateRequest ratingUpdateRequest = new RatingUpdateRequest();
		ratingUpdateRequest.setRating(0);
		ratingUpdateRequest.setReviewId("SomeRandomId");
		ratingUpdateRequest.setUserId("RandomUser");
		updateRequest.setRatingUpdateRequest(ratingUpdateRequest);
		
		UpdateReviewResponse updateResponse;
		try {
			updateResponse = getClient().updateRating(updateRequest);
		} catch (SnapdealWSException e) {
			Assert.assertEquals(e.getWsErrorCode(),400);
		}
	}
	
	@Test
	public void updateRating_invalidUser_Test(){
		
		RatingUpdateRequest ratingUpdateRequest = new RatingUpdateRequest();
		ratingUpdateRequest.setRating(3);
		ratingUpdateRequest.setReviewId("SomeRandomId");
		ratingUpdateRequest.setUserId("");
		updateRequest.setRatingUpdateRequest(ratingUpdateRequest);
		
		UpdateReviewResponse updateResponse;
		try {
			updateResponse = getClient().updateRating(updateRequest);
		} catch (SnapdealWSException e) {
			Assert.assertEquals(e.getWsErrorCode(),400);
		}
	}
	
	@Test
	public void updateRating_invalidReviewId_Test(){
		
		RatingUpdateRequest ratingUpdateRequest = new RatingUpdateRequest();
		ratingUpdateRequest.setRating(3);
		ratingUpdateRequest.setUserId("RandomUser");
		updateRequest.setRatingUpdateRequest(ratingUpdateRequest);
		
		UpdateReviewResponse updateResponse;
		try {
			updateResponse = getClient().updateRating(updateRequest);
		} catch (SnapdealWSException e) {
			Assert.assertEquals(e.getWsErrorCode(),400);
		}
	}	
	
	public Rating getTestRatingObj() {
		Rating rating = new Rating();
		rating.setProductId("1005");
		rating.setRating(3);
		rating.setUserId(RandomStringUtils.randomAlphabetic(6));
		return rating;
	}
	
}
