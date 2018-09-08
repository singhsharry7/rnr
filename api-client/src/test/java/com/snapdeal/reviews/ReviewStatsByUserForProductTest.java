package com.snapdeal.reviews;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.reviews.commons.OpinionBo;
import com.snapdeal.reviews.commons.dto.ReviewStatusForProductByUser;

public class ReviewStatsByUserForProductTest extends ReviewsApiTest {
	private ReviewStatusForProductByUser reviewStatus;
	
	@Test
	public void testWhenNoReviewOrRating() throws SnapdealWSException{
		try{
		makeRequest(UUID.randomUUID().toString());
		}
		catch(SnapdealWSException s){
			Assert.assertEquals(404, s.getWsErrorCode());
		}
	}	
	@Test
	public void testWhenOnlyRating() throws SnapdealWSException{
		String ratingId = ReviewLifeCycleTest.createRating().getId();
		makeRequest(ReviewTestUtils.getDummyUser());
		
		Assert.assertEquals(ratingId, reviewStatus.getReviewId());
		Assert.assertNotEquals(0, reviewStatus.getRating());
		assertNoFullReview();
	}

	@Test
	public void testWhenFullReview() throws SnapdealWSException{
		String reviewId = ReviewLifeCycleTest.createReview().getId();
		makeRequest(ReviewTestUtils.getDummyUser());
		
		Assert.assertNotEquals(0, reviewStatus.getRating());
		Assert.assertEquals(reviewId, reviewStatus.getReviewId());
		Assert.assertEquals(true, reviewStatus.isHasMadeFullReview());
		Assert.assertNotEquals(OpinionBo.ABSTAIN, reviewStatus.getRecommendation());
	}
	
	private void assertNoFullReview() {
		Assert.assertEquals(false, reviewStatus.isHasMadeFullReview());
		Assert.assertEquals(OpinionBo.ABSTAIN, reviewStatus.getRecommendation());
	}
	private void makeRequest(String userId) throws SnapdealWSException {
		Map<String, String> getReviewByProductAndUserParams = new HashMap<String, String>();
		getReviewByProductAndUserParams.put("productId", ReviewTestUtils.getDummyProduct());
		getReviewByProductAndUserParams.put("userId", userId);
		reviewStatus = getClient().getReviewSummaryByProductAndUser(getReviewByProductAndUserParams).getReviewStatusForProductByUser();
	}
}
