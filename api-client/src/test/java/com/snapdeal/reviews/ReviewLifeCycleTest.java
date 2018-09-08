package com.snapdeal.reviews;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.base.model.common.ServiceResponse;
import com.snapdeal.reviews.commons.dto.Rating;
import com.snapdeal.reviews.commons.dto.RatingUpdateRequest;
import com.snapdeal.reviews.commons.dto.Review;
import com.snapdeal.reviews.commons.dto.ReviewResponse;
import com.snapdeal.reviews.commons.dto.wrapper.CreateRatingRequest;
import com.snapdeal.reviews.commons.dto.wrapper.CreateReviewRequest;
import com.snapdeal.reviews.commons.dto.wrapper.GetProductReviewStatsResponse;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateRatingRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateReviewResponse;

public class ReviewLifeCycleTest extends ReviewsApiTest {
	Review review;

	// create rating

	// fetch rating for user

	// get by id and product

	// create review, overwrite the existing id
	
	public static ReviewResponse createReview() throws SnapdealWSException {
		CreateReviewRequest createReviewRequest = new CreateReviewRequest();
		createReviewRequest.setReviewRequest(ReviewTestUtils.createDummyProductReview());
		ReviewResponse createReviewResponse = getClient().createReview(createReviewRequest).getReviewResponse();
		return createReviewResponse;
	}

	@Test
	public void testCreateRating() throws SnapdealWSException {
		// create rating
		ReviewResponse createRatingResponse = createRating();

		Assert.assertNotNull(createRatingResponse);

		System.out.println("Created rating");

		// fetch rating by id
		Map<String, String> fetchByReviewIdParams = new HashMap<String, String>();
		fetchByReviewIdParams.put("id", createRatingResponse.getId());
		review = getClient().getReview(fetchByReviewIdParams).getReview();
		Assert.assertEquals(createRatingResponse.getId(), review.getId());

		// fetch rating by user and product
		Map<String, String> getReviewByProductAndUserParams = new HashMap<String, String>();
		getReviewByProductAndUserParams.put("productId", ReviewTestUtils.getDummyProduct());
		getReviewByProductAndUserParams.put("userId", ReviewTestUtils.getDummyUser());
		review = getClient().getReviewByProductAndUser(getReviewByProductAndUserParams).getReview();
		Assert.assertEquals(createRatingResponse.getId(), review.getId());

		System.out.println("fetched rating");

		// create review, overrwrite the existing id

		ReviewResponse createReviewResponse = createReview();

		Assert.assertEquals(createRatingResponse.getId(), createReviewResponse.getId());
	}

	public static ReviewResponse createRating() throws SnapdealWSException {
		CreateRatingRequest createRatingRequest = new CreateRatingRequest();
		Rating rating = new Rating();
		rating.setProductId(ReviewTestUtils.getDummyProduct());
		rating.setRating(4);
		rating.setUserId(ReviewTestUtils.getDummyUser());
		createRatingRequest.setRating(rating);
		ReviewResponse createRatingResponse = getClient().createRating(createRatingRequest).getReviewResponse();
		return createRatingResponse;
	}

	public static UpdateReviewResponse updateRating(int rating, String reviewId) throws SnapdealWSException{
		UpdateRatingRequest updateRatingRequest = new UpdateRatingRequest();
		RatingUpdateRequest request = new RatingUpdateRequest();
		request.setRating(rating);
		request.setReviewId(reviewId);
		request.setUserId(ReviewTestUtils.getDummyUser());
		updateRatingRequest.setRatingUpdateRequest(request);
		UpdateReviewResponse updateRating = getClient().updateRating(updateRatingRequest);	
		return updateRating;
	}
	
	@Test
	public void feedbackTest() throws SnapdealWSException {
		//ReviewResponse createReview = createReview();
		ServiceResponse postFeedback = getClient().postFeedback(ReviewTestUtils.createFeedbackRequest("014c22b4682a00001f486e5b10ea6bac"));
	}

	@Test
	public void productStatsTest() throws SnapdealWSException {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("productId", ReviewTestUtils.PRODUCT_ID);
		GetProductReviewStatsResponse reviewStats = getClient().getReviewStats(queryParams);
		Assert.assertNotNull(reviewStats.getProductReviewStats());
	}

}
