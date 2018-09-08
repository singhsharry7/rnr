
package com.snapdeal.reviews;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.reviews.commons.dto.wrapper.GetReviewMasterDataResponse;

public class FetchMasterDataTest extends ReviewsApiTest{
	@Test
	public void testFetch() throws SnapdealWSException{
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("productId", "1001");
		GetReviewMasterDataResponse fetchMasterData = getClient().fetchMasterData(queryParams);
		Assert.assertNotNull(fetchMasterData.getReviewMasterData().getRichData());
	}
	
	@Ignore
	@Test(expected = SnapdealWSException.class)
	public void testFetchForInvalidProduct() throws SnapdealWSException{
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("productId", "1002");
		GetReviewMasterDataResponse fetchMasterData = getClient().fetchMasterData(queryParams);
		Assert.assertNotNull(fetchMasterData.getReviewMasterData().getRichData());
	}
}
