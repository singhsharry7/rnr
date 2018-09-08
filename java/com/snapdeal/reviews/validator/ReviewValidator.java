package com.snapdeal.reviews.validator;

import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.snapdeal.profanity.editor.ProfanityEditor;
import com.snapdeal.reviews.commons.ProfanityFilterType;
import com.snapdeal.reviews.commons.config.ReviewConfiguration;
import com.snapdeal.reviews.commons.dto.Rating;
import com.snapdeal.reviews.commons.dto.RecommendationRequest;
import com.snapdeal.reviews.commons.dto.ReviewRequest;
import com.snapdeal.reviews.commons.dto.SampleReview;
import com.snapdeal.reviews.commons.pagination.Page;
import com.snapdeal.reviews.exception.ErrorCode;
import com.snapdeal.reviews.exception.client.ForbiddenException;
import com.snapdeal.reviews.service.ConfigurationService;

@Component
public class ReviewValidator {

	@Autowired
	private ValidationUtils utils;

	@Autowired
	private ConfigurationService configurationService;
	
	@Autowired
	List<ModerationValidator> list;

	@Autowired
	private ProfanityEditor profanityEditor;

	private ReviewConfiguration reviewConfiguration;
	
	private boolean productWriteFlowDisabled;
	
	private Boolean globalWriteFlowDisabled = null;
	
	public void validate(ReviewRequest request) {
		request.setHeadline(request.getHeadline().trim());
		request.getUserReviewsInfo().setNickName(request.getUserReviewsInfo().getNickName().trim());
		request.setComments(request.getComments().trim());
		request.setProductId(request.getProductId().trim());
		checkWriteFlow(request.getProductId());
		utils.assertNonEmpty("productId", request.getProductId());
		if ((null != reviewConfiguration)) {
			utils.assertLength("headline", request.getHeadline(),
					reviewConfiguration.getDefaultMinTitleSize(),
					reviewConfiguration.getDefaultMaxTitleSize());
			
			utils.assertLength("comments", request.getComments(),
					reviewConfiguration.getDefaultMinCommentSize(),
					reviewConfiguration.getDefaultMaxCommentSize());
			
			if (null != reviewConfiguration.getCreateReviewRecommendationMandatory()
					&& reviewConfiguration.getCreateReviewRecommendationMandatory()){
				utils.assertNonNull("recommended", request.isRecommended());
			}
		}
	}
	
	public void validate(SampleReview request) {
		ReviewConfiguration reviewConfiguration = configurationService.getReviewConfiguration();
		
		utils.assertNonNull("Comments", request.getComments());
		utils.assertNonNull("CreatedAt", request.getCreatedAt());
		utils.assertNonNull("HeadLine", request.getHeadline());
		utils.assertNonNull("Label", request.getLabel());
		utils.assertNonNull("NickName", request.getNickName());
		utils.assertNonNull("Rating", request.getRating());			
		request.setHeadline(request.getHeadline().trim());
		request.setNickName(request.getNickName().trim());
		request.setComments(request.getComments().trim());
		if ((null != reviewConfiguration)) {
			utils.assertLength("headline", request.getHeadline(),
					reviewConfiguration.getDefaultMinTitleSize(),
					reviewConfiguration.getDefaultMaxTitleSize());
			
			utils.assertLength("comments", request.getComments(),
					reviewConfiguration.getDefaultMinCommentSize(),
					reviewConfiguration.getDefaultMaxCommentSize());		
		}
	}

	public Set<String> checkProfanity(ReviewRequest request) {
		Set<String> profaneWords = profanityEditor.checkProfanity(
				request.getComments(), ProfanityFilterType.REVIEW_CREATION);
		profaneWords.addAll(profanityEditor.checkProfanity(
				request.getHeadline(), ProfanityFilterType.REVIEW_CREATION));
		return profaneWords;
	}

	public void validateRating(Rating rating) {
		checkWriteFlow(rating.getProductId());
		utils.assertNonNull("rating", rating.getRating());
		rating.setProductId(rating.getProductId().trim());
		rating.setUserId(rating.getUserId().trim());
		utils.assertNonEmpty("userId", rating.getUserId());
		utils.assertNonEmpty("productId", rating.getProductId());
	}

	public void validatePagination(Page page) {
		utils.assertPositive("offset", page.getOffset());
		utils.assertPositive("limit", page.getLimit());
	}
	
	public void validateRecommendation(RecommendationRequest request) {		
		checkWriteFlow(request.getProductId());
		utils.assertNonNull("productId", request.getProductId());
		utils.assertNonNull("UserId", request.getUserId());
		request.setProductId(request.getProductId().trim());
		request.setUserId(request.getUserId().trim());
		utils.assertRecommendValue(request.getIsRecommended());
	}
	
	private void checkWriteFlow(String productId){
		reviewConfiguration = configurationService.getReviewConfiguration();
		productWriteFlowDisabled = configurationService.getProductConfiguration(productId);
		globalWriteFlowDisabled = reviewConfiguration.getGlobalWriteFlowDisabled();
		
		if(( globalWriteFlowDisabled != null 
				&& globalWriteFlowDisabled.booleanValue()) 
				|| productWriteFlowDisabled){
			throw new ForbiddenException(ErrorCode.FORBIDDEN_ERROR, "Write Flows is off");
		}
	}	
	
	public void validateSearchFlow(ReviewConfiguration reviewConfiguration){
		final Boolean isSearchDisabled = reviewConfiguration.getIsReviewSearchDisabled();
		if(null != isSearchDisabled && isSearchDisabled){
			throw new ForbiddenException(ErrorCode.FORBIDDEN_ERROR, "Review-Search Flow is off");
		}
	}
}
