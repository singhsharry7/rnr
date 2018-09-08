package com.snapdeal.reviews;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.reviews.commons.dto.ReviewsDynamicContentResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetUserActionsOnReviewsResponse;

public class GetUserActionsTest extends ReviewsApiTest {
	@Test
	public void testGetUserActions_validUserIdAndReviewIds() throws SnapdealWSException{
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("userId", "varun1");
		queryParams.put("id", "014e24740730000000018a921e63b869,014e24753966000000018a92b81908d2");
		ReviewsDynamicContentResponse response = getClient().getDynamicReviewsContent(queryParams);
		//assertEquals("varun001", response.getUserId());
		assertEquals(response.getReviews().size(), 2);
	}
	@Test
	public void testGetUserActions_validUserIdAndInvalidReviewIds() throws SnapdealWSException{
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("userId", "varun1");
		queryParams.put("id", "129373251e8400000756b5b3030552f8,014c733a0de20000000ea28f0008df59");
		GetUserActionsOnReviewsResponse response = getClient().getUserActions(queryParams);
		assertEquals("varun001", response.getUserId());
		assertEquals(response.getUserActions().size(), 1);
	}
	@Test
	public void testGetUserActions_invalidUserIdAndValidReviewIds() throws SnapdealWSException{
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("userId", "abcas123");
		queryParams.put("id", "014c73251e8400000756b5b3030552f8,014c733a0de20000000ea28f0008df59");
		GetUserActionsOnReviewsResponse response = getClient().getUserActions(queryParams);
		assertEquals("abcas123", response.getUserId());
		assertEquals(response.getUserActions().size(), 2);
	}

}
