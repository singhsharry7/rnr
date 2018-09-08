package com.snapdeal.reviews;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.base.exception.TransportException;
import com.snapdeal.reviews.commons.dto.wrapper.GetReviewListingPageResponse;
import com.snapdeal.reviews.commons.pagination.ReviewListingSortType;

public class ReviewListingTest extends ReviewsApiTest {

	@Test(expected=TransportException.class)
	public void testReviewListingForNegativeOffset() throws SnapdealWSException{
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("offset", "-1");
		getClient().getReviewList(queryParams);
		System.out.println();
	}
	
	@Test(expected=TransportException.class)
	public void testReviewListingForNegativeLimit() throws SnapdealWSException{
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("limit", "-1");
		getClient().getReviewList(queryParams);
		System.out.println();
	}
	
	@Test(expected=TransportException.class)
	public void testReviewListingForInvalidQueryType() throws SnapdealWSException{
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("queryType", "sadsad");
		getClient().getReviewList(queryParams);
		System.out.println();
	}
	
	@Test
	public void testReviewListing() throws SnapdealWSException{
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("offset", "0");
		queryParams.put("limit", "20");
		queryParams.put("productId", "1002");
		GetReviewListingPageResponse reviewList = getClient().getReviewList(queryParams);
		System.out.println();
	}
}
