package com.snapdeal.reviews.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snapdeal.reviews.commons.dto.wrapper.UpdateReviewOnCheckoutRequest;
import com.snapdeal.reviews.commons.pagination.Page;

@Component
public class ModerationValidator {
	
	@Autowired
	private ValidationUtils utils;

	public void validateCheckoutRequest(UpdateReviewOnCheckoutRequest request) {
//		utils.assertUUID(request.getModeratorId(), "Moderator Id should be UUID");
		for (String reviewId : request.getReviewIds())
			utils.assertNonNull("reviewId", reviewId);

	}

/*	public void validateCheckinRequest(UpdateReviewOnCheckinRequest request) {
		utils.assertUUID(request.getModeratorId(), "Moderator Id should be UUID");
	}*/
	
	public void validatePagination(Page page) {
		utils.assertPositive("offset", page.getOffset());
		utils.assertPositive("limit", page.getLimit());
	}
}
