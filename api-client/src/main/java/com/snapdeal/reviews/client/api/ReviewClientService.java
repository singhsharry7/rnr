package com.snapdeal.reviews.client.api;

import java.util.Map;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.base.model.common.ServiceResponse;
import com.snapdeal.reviews.commons.dto.GetProductsReviewedByUsersResponse;
import com.snapdeal.reviews.commons.dto.GetProductsReviewsByUsersResponse;
import com.snapdeal.reviews.commons.dto.ReviewsDynamicContentResponse;
import com.snapdeal.reviews.commons.dto.wrapper.CreateFeedbackRequest;
import com.snapdeal.reviews.commons.dto.wrapper.CreateRatingRequest;
import com.snapdeal.reviews.commons.dto.wrapper.CreateRecommendationRequest;
import com.snapdeal.reviews.commons.dto.wrapper.CreateReviewRequest;
import com.snapdeal.reviews.commons.dto.wrapper.CreateReviewResponse;
import com.snapdeal.reviews.commons.dto.wrapper.CreateSampleReviewRequest;
import com.snapdeal.reviews.commons.dto.wrapper.FlagReviewRequest;
import com.snapdeal.reviews.commons.dto.wrapper.GetLabelsResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetProductReviewStatsResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetReviewForProductByKeywordResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetReviewForProductByUserResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetReviewListingPageResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetReviewMasterDataResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetReviewResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetReviewStatusForProductByUserResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetSampleReviewResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetUserActionsOnReviewsResponse;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateRatingRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateRecommendationRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateReviewResponse;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateSampleReviewRequest;

public interface ReviewClientService extends ReviewWebClientService {

	CreateReviewResponse createReview(CreateReviewRequest request) throws SnapdealWSException;

	ServiceResponse postFeedback(CreateFeedbackRequest request) throws SnapdealWSException;

	CreateReviewResponse createRating(CreateRatingRequest request) throws SnapdealWSException;

	GetReviewMasterDataResponse fetchMasterData(Map<String, String> queryParams) throws SnapdealWSException;

	GetReviewForProductByUserResponse getReviewByProductAndUser(Map<String, String> queryParams) throws SnapdealWSException;

	GetReviewStatusForProductByUserResponse getReviewSummaryByProductAndUser(Map<String, String> queryParams) throws SnapdealWSException;
	
	GetReviewListingPageResponse getReviewList(Map<String, String> queryParams) throws SnapdealWSException;

	GetProductReviewStatsResponse getReviewStats(Map<String, String> queryParams) throws SnapdealWSException;
	
	GetProductReviewStatsResponse getReviewStatsSummary(Map<String, String> queryParams) throws SnapdealWSException;

	GetReviewResponse getReview(Map<String, String> queryParams) throws SnapdealWSException;
	
	ServiceResponse createFlag(FlagReviewRequest request) throws SnapdealWSException;

	ServiceResponse removeFlag(FlagReviewRequest request) throws SnapdealWSException;
	
	GetProductsReviewedByUsersResponse getProductsReviewedByUser(Map<String,String> queryParams) throws SnapdealWSException;
	
	GetProductsReviewsByUsersResponse getProductsReviewsByUser(Map<String,String> queryParams) throws SnapdealWSException;
	
	UpdateReviewResponse updateRating(UpdateRatingRequest request) throws SnapdealWSException;
	
	@Deprecated
	GetUserActionsOnReviewsResponse getUserActions(Map<String,String> queryParams) throws SnapdealWSException;
	
	UpdateReviewResponse updateRecommendation( UpdateRecommendationRequest request) throws SnapdealWSException;
	
	CreateReviewResponse createRecommendation(CreateRecommendationRequest request)throws SnapdealWSException;
	
	ServiceResponse createSampleReview(CreateSampleReviewRequest request)throws SnapdealWSException;
	
	ServiceResponse updateSampleReview(UpdateSampleReviewRequest request) throws SnapdealWSException;
	
	GetSampleReviewResponse getSampleReview(Map<String, String> queryParams)throws SnapdealWSException ;
	
	GetLabelsResponse getProductLabels()throws SnapdealWSException;

	ReviewsDynamicContentResponse getDynamicReviewsContent(
			Map<String, String> queryParams) throws SnapdealWSException;
	
	GetReviewForProductByKeywordResponse getReviewByProductAndKeyword(Map<String, String> queryParams) throws SnapdealWSException;
	
	GetReviewListingPageResponse getAllReviews(Map<String, String> queryParams) throws SnapdealWSException;
}
