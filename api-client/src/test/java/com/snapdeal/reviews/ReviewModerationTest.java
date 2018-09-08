package com.snapdeal.reviews;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.base.exception.TransportException;
import com.snapdeal.reviews.commons.dto.ReviewResponse;
import com.snapdeal.reviews.commons.dto.Status;
import com.snapdeal.reviews.commons.dto.wrapper.GetModerationReviewsCountResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetReviewResponse;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateReviewOnCheckinResponse;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateReviewOnCheckoutResponse;

public class ReviewModerationTest extends ReviewsApiTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void moderationLifeCycleApprovedTest() throws SnapdealWSException {
		List<String> reviewIds = new ArrayList<String>();
		ReviewResponse createReview = ReviewLifeCycleTest.createReview();
		// ReviewResponse createReview1 = ReviewLifeCycleTesst.createReview();
		reviewIds.add(createReview.getId());
		// reviewIds.add(createReview1.getId());
		UpdateReviewOnCheckoutResponse updateReviewOnCheckout = getModerationClient().updateReviewOnCheckout(
				ReviewModerationUtils.getUpdateReviewOnCheckoutRequest(reviewIds));
		getModerationClient().updateReviewOnCheckout(ReviewModerationUtils.getUpdateReviewOnCheckoutRequest(reviewIds));
		Assert.assertNotNull(updateReviewOnCheckout.getCheckoutReviewResponse().getCheckedOutReviews());
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("id", createReview.getId());
		GetReviewResponse review = getClient().getReview(queryParams);
		//Assert.assertEquals(review.getReview().getStatus(), Status.APPROVED);
		checkInTest(reviewIds, Status.APPROVED);
		
		review = getClient().getReview(queryParams);
		//Assert.assertEquals(review.getReview().getStatus(), Status.APPROVED);
	}

	public static void createReviewAndApproveIt() throws SnapdealWSException {
		List<String> reviewIds = new ArrayList<String>();
		ReviewResponse createReview = ReviewLifeCycleTest.createReview();
		reviewIds.add(createReview.getId());
		getModerationClient().updateReviewOnCheckout(ReviewModerationUtils.getUpdateReviewOnCheckoutRequest(reviewIds));
		checkInTest(reviewIds, Status.APPROVED);
	}

	public static void checkInTest(List<String> reviewIds, Status status) throws SnapdealWSException {
		UpdateReviewOnCheckinResponse updateReviewOnCheckin = getModerationClient().updateReviewOnCheckin(
				ReviewModerationUtils.getUpdateReviewOnCheckinRequest(reviewIds, status));
		Assert.assertNotNull(updateReviewOnCheckin.getCheckedInReviewResponse().getCheckedInReviews());
	}

	@Test
	public void moderationLifeCycleNegativeTest() throws SnapdealWSException {
		ReviewResponse createReview = ReviewLifeCycleTest.createReview();
		List<String> reviewIds = new ArrayList<String>();
		reviewIds.add(createReview.getId());

		UpdateReviewOnCheckoutResponse updateReviewOnCheckout = getModerationClient().updateReviewOnCheckout(
				ReviewModerationUtils.getUpdateReviewOnCheckoutRequest(reviewIds));

		Assert.assertNotNull(updateReviewOnCheckout.getCheckoutReviewResponse().getCheckedOutReviews());
		thrown.expect(TransportException.class);

		checkInTest(reviewIds, Status.APPROVED);

	}

	@Test
	public void moderationLifeCycleRejectedTest() throws SnapdealWSException {
		//ReviewResponse createReview = ReviewLifeCycleTest.createReview();
		List<String> reviewIds = new ArrayList<String>();
		reviewIds.add("014ca300bcd00000000003ea000002da");
		UpdateReviewOnCheckoutResponse updateReviewOnCheckout = getModerationClient().updateReviewOnCheckout(
				ReviewModerationUtils.getUpdateReviewOnCheckoutRequest(reviewIds));
		Assert.assertNotNull(updateReviewOnCheckout.getCheckoutReviewResponse().getCheckedOutReviews());
		checkInTest(reviewIds, Status.REJECTED);
	/*	Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("id", createReview.getId());
		GetReviewResponse review = getClient().getReview(queryParams);
		Assert.assertEquals(review.getReview().getStatus(), Status.REJECTED);*/
	}
	
	@Test
	public void getPendingReviewsCount() throws SnapdealWSException{
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("status", "CREATED");
		GetModerationReviewsCountResponse response = getModerationClient().getReviewsCountByStatus(queryParams);
		Assert.assertNotNull(response);
		assertEquals(new Long(5587), response.getTotalReviewsount());
		
	}

}
