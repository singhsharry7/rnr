package com.snapdeal.reviews;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.reviews.commons.dto.Review;

public class GetReviewTest extends ReviewsApiTest {
	@Test
	public void testNonHexId() throws SnapdealWSException{
		try{
			getReviewById("dsfhb");
		}
		catch(SnapdealWSException s){
			Assert.assertEquals(400, s.getWsErrorCode());
		}
	}
	
	@Test
	public void testHexWithIncorrectSize() throws SnapdealWSException{
		try{
			getReviewById("128969ab");
		}
		catch(SnapdealWSException s){
			Assert.assertEquals(400, s.getWsErrorCode());
		}
	}
	
	@Test
	public void testValidButNonExistentHex() throws SnapdealWSException{
		try{
			getReviewById("00000000000000000000000000000000");
		}
		catch(SnapdealWSException s){
			Assert.assertEquals(404, s.getWsErrorCode());
		}
	}

	private Review getReviewById(String id) throws SnapdealWSException {
		Map<String, String> fetchByReviewIdParams = new HashMap<String, String>();
		fetchByReviewIdParams.put("id",id);
		return getClient().getReview(fetchByReviewIdParams).getReview();
	}
	
	@Test
	public void testExistingReview() throws SnapdealWSException{
		String id = ReviewLifeCycleTest.createReview().getId();
		Assert.assertEquals(id, getReviewById(id).getId());
	}
}
