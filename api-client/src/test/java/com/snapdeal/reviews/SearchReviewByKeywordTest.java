package com.snapdeal.reviews;


import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.reviews.commons.config.ReviewConfiguration;
import com.snapdeal.reviews.commons.dto.wrapper.GetReviewForProductByKeywordResponse;
import com.snapdeal.reviews.commons.dto.wrapper.ReviewConfigurationRequest;


public class SearchReviewByKeywordTest extends ReviewsApiTest{

	@Test
	public void testReviewSearchByKeyword() throws SnapdealWSException{
        
		ReviewConfigurationRequest request = new ReviewConfigurationRequest();
		ReviewConfiguration configuration = new ReviewConfiguration();
		configuration.setIsReviewSearchDisabled(false);
		request.setReviewConfiguration(configuration);
		getConfigurationClient().setReviewConfiguration(request);
		Map<String,String> requestMap = new HashMap<>();
		requestMap.put("productId", "1520618294");
		requestMap.put("sortType", "HELPFUL_DESC");
		requestMap.put("rating", "0");
		requestMap.put("limit", "10");
		requestMap.put("keywords", "quality");
		GetReviewForProductByKeywordResponse searchReviewsByKeywords = getClient().getReviewByProductAndKeyword(requestMap);
		System.out.println(searchReviewsByKeywords.getReviewListingPage().getReviews());
	}
}
  