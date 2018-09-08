package com.snapdeal.reviews;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.reviews.commons.dto.wrapper.GetProductReviewStatsResponse;

public class ProductReviewStatsTest extends ReviewsApiTest {

	@Test
	public void getProductStats() throws SnapdealWSException {
		ReviewModerationTest.createReviewAndApproveIt();
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("productId", ReviewTestUtils.PRODUCT_ID);
		GetProductReviewStatsResponse reviewStats = getClient().getReviewStats(queryParams);
		assertNotNull(reviewStats);
	}

	@Test
	public void getProductStatsWithNonExistingProduct() {
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("productId", "NonExistingProduct");
		try {
			getClient().getReviewStats(queryParams);
		} catch (SnapdealWSException e) {
			assertEquals(404, e.getWsErrorCode());
			return;
		}
		fail();
	}

	@Test
	public void getProductStatsWithProductIdMissing() {
		Map<String, String> queryParams = new HashMap<>();
		try {
			getClient().getReviewStats(queryParams);
		} catch (SnapdealWSException e) {
			assertEquals(400, e.getWsErrorCode());
			return;
		}
		fail();
	}

	@Test
	public void getProductReviewStatsSummary() throws SnapdealWSException {
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("productId", ReviewTestUtils.PRODUCT_ID);
		GetProductReviewStatsResponse reviewStats = getClient().getReviewStatsSummary(queryParams);
		assertNotNull(reviewStats.getProductReviewStats().getAvgRating());
		assertNotNull(reviewStats.getProductReviewStats().getNoOfRatings());
		assertNotNull(reviewStats.getProductReviewStats().getTotalRecommendations());
		assertNotNull(reviewStats.getProductReviewStats().getNoOfReviews());
		assertNull(reviewStats.getProductReviewStats().getRichDataCumulate());
		assertNull(reviewStats.getProductReviewStats().getMostLikedNegativeReview());
		assertNull(reviewStats.getProductReviewStats().getMostLikedPositiveReview());
	}

	@Test
	public void getProductStatsSummaryWithNonExistingProduct() {
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("productId", "NonExistingProduct");
		try {
			getClient().getReviewStatsSummary(queryParams);
		} catch (SnapdealWSException e) {
			assertEquals(404, e.getWsErrorCode());
			return;
		}
		fail();
	}

	@Test
	public void getProductStatsSummaryWithProductIdMissing() {
		Map<String, String> queryParams = new HashMap<>();
		try {
			getClient().getReviewStatsSummary(queryParams);
		} catch (SnapdealWSException e) {
			assertEquals(400, e.getWsErrorCode());
			return;
		}
		fail();
	}

}
