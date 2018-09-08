package com.snapdeal.reviews;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.reviews.commons.dto.GetProductsReviewedByUsersResponse;

public class GetProductsReviewedByUserTest extends ReviewsApiTest {
	@Test
	public void testGetProductsReviewed_invalidUserId() throws SnapdealWSException{
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("userIds", "adsf123zc");
		GetProductsReviewedByUsersResponse response = getClient().getProductsReviewedByUser(queryParams);
		assertEquals(response.getProductsReviewedByUsers().size(), 1);
		assertEquals(response.getProductsReviewedByUsers().get("adsf123zc").size(),0);
	}
	@Test
	public void testGetProductsReviewed_blankUserId() throws SnapdealWSException{
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("userIds", " ");
		GetProductsReviewedByUsersResponse response = getClient().getProductsReviewedByUser(queryParams);
		assertEquals(response.getProductsReviewedByUsers().size(), 1);
		assertNull(response.getProductsReviewedByUsers().get(" "));
	}
	@Test
	public void testGetProductsReviewed_validUserId() throws SnapdealWSException{
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("userIds", "11varun");
		GetProductsReviewedByUsersResponse response = getClient().getProductsReviewedByUser(queryParams);
		assertEquals(response.getProductsReviewedByUsers().size(), 1);
		assertEquals(response.getProductsReviewedByUsers().get("11varun").size(),4);
	}
	
}