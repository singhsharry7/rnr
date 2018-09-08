package com.snapdeal.reviews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.base.exception.TransportException;
import com.snapdeal.base.model.common.ServiceResponse;
import com.snapdeal.reviews.commons.dto.FlagType;
import com.snapdeal.reviews.commons.dto.Review;
import com.snapdeal.reviews.commons.dto.Status;
import com.snapdeal.reviews.commons.dto.wrapper.FlagReviewRequest;
import com.snapdeal.reviews.commons.dto.wrapper.GetReviewResponse;

public class ReviewSpamAbuseTest extends ReviewsApiTest {

	private Review review;
	private FlagReviewRequest flagRequest;
	private FlagReviewRequest unFlagRequest;
	private String reviewId;
	private String user;
	private Map<String, String> queryParams;
	private ArrayList<String> reviewIds;
	
	private ServiceResponse createFlag(FlagType flagType) throws SnapdealWSException{
		flagRequest.setFlagType(flagType);
		ServiceResponse serviceResponse = getClient().createFlag(flagRequest);
		return serviceResponse;		
	}
	
	private ServiceResponse removeFlag(FlagType flagType) throws SnapdealWSException{
		unFlagRequest.setFlagType(flagType);
		ServiceResponse serviceResponse = getClient().removeFlag(unFlagRequest);
		return serviceResponse;
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
		flagRequest = new FlagReviewRequest();
		unFlagRequest = new FlagReviewRequest();
		queryParams = new HashMap<String, String>();
		
		// creating a approved review
		reviewId = ReviewLifeCycleTest.createReview().getId();
		reviewIds = new ArrayList<String>();
		reviewIds.add(reviewId);
		getModerationClient().updateReviewOnCheckout(ReviewModerationUtils.getUpdateReviewOnCheckoutRequest(reviewIds));
		getModerationClient().updateReviewOnCheckin(ReviewModerationUtils.getUpdateReviewOnCheckinRequest(reviewIds, Status.APPROVED));
	
		// Initialisation for requests
		user = ReviewTestUtils.getDummyUser();
		flagRequest.setReviewId(reviewId);
		flagRequest.setUserId(user);
		unFlagRequest.setReviewId(reviewId);
		unFlagRequest.setUserId(user);
	}

	// Mark SPAM/ABUSE a review with valid requestBody
	
	@Test
	public void testSpamReview() throws SnapdealWSException{
		ServiceResponse serviceResponse = createFlag(FlagType.SPAM);
		queryParams.put("id", reviewId);
		GetReviewResponse getReviewResponse = getClient().getReview(queryParams);
		review = getReviewResponse.getReview();
		
		Assert.assertEquals(true, serviceResponse.isSuccessful());
//		Assert.assertEquals(true, review.getMarkedAsSpam().contains(user));
//		Assert.assertEquals(true, review.isFlagged());
	}
	
	@Test
	public void testAbuseReview() throws SnapdealWSException{
		ServiceResponse serviceResponse = createFlag(FlagType.ABUSE);
		queryParams.put("id", reviewId);
		GetReviewResponse getReviewResponse = getClient().getReview(queryParams);
		review = getReviewResponse.getReview();
		
		Assert.assertEquals(true, serviceResponse.isSuccessful());
//		Assert.assertEquals(true, review.getMarkedAsAbuse().contains(user));
//		Assert.assertEquals(true, review.isFlagged());
	}

	// Mark SPAM/ABUSE Negative Scenarios
	// 1) Marking SPAM/ABUSE with all fields empty
	@Test
	public void testCreateFlagWithEmptyFields() throws SnapdealWSException{
		flagRequest.setReviewId(null);
		flagRequest.setUserId(null);
		thrown.expect(TransportException.class);
		createFlag(FlagType.SPAM);
	}
	// 2) Marking SPAM/ABUSE to an unapproved review
	@Test
	public void testCreateFlagForAnUnapprovedReview() throws SnapdealWSException{
		
		getModerationClient().updateReviewOnCheckout(ReviewModerationUtils.getUpdateReviewOnCheckoutRequest(reviewIds));
		getModerationClient().updateReviewOnCheckin(ReviewModerationUtils.getUpdateReviewOnCheckinRequest(reviewIds, Status.REJECTED));
		
		try {
			createFlag(FlagType.SPAM);
		} catch (Exception e) {
			Assert.assertTrue(e.getCause().getMessage().contains("FORBIDDEN_ERROR"));
		}
	}
	// 3) Marking SPAM/ABUSE to an invalid review id
	@Test
	public void testCreateFlagForInvalidReviewId() throws SnapdealWSException{
		flagRequest.setReviewId("test99880011");
		try {
			createFlag(FlagType.SPAM);
		} catch (Exception e) {
			Assert.assertTrue(e.getCause().getMessage().contains("INPUT_INVALID_PAYLOAD"));
		}
	}
	
	// UnMark SPAM/ABUSE a review with valid requestBody
	
	@Test
	public void testUnSpamReview() throws SnapdealWSException{
		createFlag(FlagType.SPAM);
		ServiceResponse serviceResponse = removeFlag(FlagType.SPAM);
		queryParams.put("id", reviewId);
		GetReviewResponse getReviewResponse = getClient().getReview(queryParams);
		review = getReviewResponse.getReview();
		
		Assert.assertEquals(true, serviceResponse.isSuccessful());
//		Assert.assertEquals(false, review.getMarkedAsSpam().contains(user));
//		Assert.assertEquals(false, review.isFlagged());
	}
	
	@Test
	public void testUnAbuseReview() throws SnapdealWSException{
		createFlag(FlagType.ABUSE);
		ServiceResponse serviceResponse = removeFlag(FlagType.ABUSE);
		queryParams.put("id", reviewId);
		GetReviewResponse getReviewResponse = getClient().getReview(queryParams);
		review = getReviewResponse.getReview();
		
		Assert.assertEquals(true, serviceResponse.isSuccessful());
//		Assert.assertEquals(false, review.getMarkedAsAbuse().contains(user));
//		Assert.assertEquals(false, review.isFlagged());
	}
	
	//UnMark SPAM/ABUSE Negative Scenarios
	// 1) UnMark with empty fields
	@Test
	public void testRemoveFlagWithEmptyFields() throws SnapdealWSException{
		unFlagRequest.setReviewId(null);
		unFlagRequest.setUserId(null);
		thrown.expect(TransportException.class);
		removeFlag(FlagType.SPAM);
	}

	// 2) UnMark with invalid review
	@Test
	public void testRemoveFlagForInvalidReviewId() throws SnapdealWSException{
		unFlagRequest.setReviewId("test99880011");
		try {
			removeFlag(FlagType.SPAM);
		} catch (Exception e) {
			Assert.assertTrue(e.getCause().getMessage().contains("INPUT_INVALID_PAYLOAD"));
		}
	}
	// 3) UnMark with UnApproved review
	@Test
	public void testRemoveFlagForAnUnapprovedReview() throws SnapdealWSException{
		getModerationClient().updateReviewOnCheckout(ReviewModerationUtils.getUpdateReviewOnCheckoutRequest(reviewIds));
		getModerationClient().updateReviewOnCheckin(ReviewModerationUtils.getUpdateReviewOnCheckinRequest(reviewIds, Status.REJECTED));
		
		try {
			removeFlag(FlagType.SPAM);
		} catch (Exception e) {
			Assert.assertTrue(e.getCause().getMessage().contains("FORBIDDEN_ERROR"));
		}
	}
}
