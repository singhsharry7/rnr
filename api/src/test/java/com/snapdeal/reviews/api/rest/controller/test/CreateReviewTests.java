package com.snapdeal.reviews.api.rest.controller.test;

import java.util.HashSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.snapdeal.reviews.api.rest.controller.ReviewsController;
import com.snapdeal.reviews.commons.OpinionBo;
import com.snapdeal.reviews.commons.dto.ReviewRequest;
import com.snapdeal.reviews.commons.richdata.IRichData;
import com.snapdeal.reviews.commons.richdata.RichDataSet;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:reviews-service.xml", "classpath:reviews-repository.xml",
		"classpath:api-servlet.xml", "classpath:web.xml" })
public class CreateReviewTests {

	@Autowired
	ReviewsController controller;

	@Test
	public void testCreate() {
		ReviewRequest request = new ReviewRequest();
		//request.setCertifiedBuyer(true);
		request.setComments("A good product - comment");
		request.setHeadline("A good product");
		//request.setNickName("Ashwini");
		request.setProductId("1001");
		//request.setObjectOwnerId(0);
		request.setRating(5);
		request.setRecommended(OpinionBo.YES);
		//request.setUserId("123");
		//request.setUserSourceId(0);
		IRichData richData = new RichDataSet("richData", new HashSet<IRichData>());
		request.setSelection(richData);
		// controller.createReview(request);
	}
	

}
