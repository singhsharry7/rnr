package com.snapdeal.reviews;

import org.junit.Test;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.reviews.client.api.ReviewClientService;
import com.snapdeal.reviews.commons.dto.wrapper.CreateRatingRequest;
import com.snapdeal.reviews.commons.dto.wrapper.CreateReviewResponse;

public class ReviewCreationTest {
	@Test
	public void testCreateRating() throws SnapdealWSException {
		ReviewClientService client = ReviewTestUtils.getReviewClient();
		CreateRatingRequest createRatingRequest = new CreateRatingRequest();
		//createRatingRequest.setRating(ReviewTestUtils.createRating());
		
		CreateReviewResponse createRating;
		try {
			createRating = client.createRating(createRatingRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Assert.assertNotNull(createRating.getReviewResponse().getId());

	}
	
}
