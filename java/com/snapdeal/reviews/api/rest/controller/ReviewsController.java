package com.snapdeal.reviews.api.rest.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.snapdeal.base.model.common.ServiceResponse;
import com.snapdeal.base.transport.service.ITransportService.Protocol;
import com.snapdeal.reviews.commons.Constants;
import com.snapdeal.reviews.commons.OpinionBo;
import com.snapdeal.reviews.commons.ProductLabel;
import com.snapdeal.reviews.commons.UriConstants;
import com.snapdeal.reviews.commons.config.ReviewConfiguration;
import com.snapdeal.reviews.commons.dto.CreateReviewFailureResponse;
import com.snapdeal.reviews.commons.dto.GetProductsReviewedByUsersResponse;
import com.snapdeal.reviews.commons.dto.HealthCheckResponse;
import com.snapdeal.reviews.commons.dto.ProductReviewStats;
import com.snapdeal.reviews.commons.dto.RecommendationRequest;
import com.snapdeal.reviews.commons.dto.ResponseDataType.ProductStats;
import com.snapdeal.reviews.commons.dto.Review;
import com.snapdeal.reviews.commons.dto.ReviewListingPage;
import com.snapdeal.reviews.commons.dto.ReviewMasterData;
import com.snapdeal.reviews.commons.dto.ReviewRequest;
import com.snapdeal.reviews.commons.dto.ReviewResponse;
import com.snapdeal.reviews.commons.dto.ReviewStatusForProductByUser;
import com.snapdeal.reviews.commons.dto.ReviewUpdateResponse;
import com.snapdeal.reviews.commons.dto.ReviewsDynamicContentResponse;
import com.snapdeal.reviews.commons.dto.SampleReview;
import com.snapdeal.reviews.commons.dto.Status;
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
import com.snapdeal.reviews.commons.dto.wrapper.GetReviewsForProductsByUserResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetSampleReviewResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetUserActionsOnReviewsResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetUserActivities;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateRatingRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateRecommendationRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateReviewResponse;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateSampleReviewRequest;
import com.snapdeal.reviews.commons.pagination.Page;
import com.snapdeal.reviews.commons.pagination.ReviewListingCriteria;
import com.snapdeal.reviews.commons.pagination.ReviewListingSortType;
import com.snapdeal.reviews.exception.ErrorCode;
import com.snapdeal.reviews.exception.ReviewException;
import com.snapdeal.reviews.exception.client.InvalidRequestException;
import com.snapdeal.reviews.mappers.OrikaMapper;
import com.snapdeal.reviews.model.IndexField;
import com.snapdeal.reviews.model.ReviewBo;
import com.snapdeal.reviews.model.ReviewListingBo;
import com.snapdeal.reviews.model.ReviewUserSearchBo;
import com.snapdeal.reviews.model.ReviewableObjectBo;
import com.snapdeal.reviews.model.SampleReviewBo;
import com.snapdeal.reviews.model.TermQueryAttribute;
import com.snapdeal.reviews.model.UserActivityOnReviews;
import com.snapdeal.reviews.service.ConfigurationService;
import com.snapdeal.reviews.service.ReviewModerationService;
import com.snapdeal.reviews.service.ReviewService;
import com.snapdeal.reviews.service.ReviewWriteService;
import com.snapdeal.reviews.service.ReviewableObjectService;
import com.snapdeal.reviews.validator.ReviewStatusModerationValidator;
import com.snapdeal.reviews.validator.ReviewValidator;
import com.snapdeal.reviews.validator.ValidationUtils;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;

@Api(value = "Reviews", description = "Reviews")
@RestController
public class ReviewsController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReviewsController.class);

	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private ReviewWriteService reviewWriteService;
	
	@Autowired
	private ReviewModerationService reviewModerationService;

	@Autowired
	private ReviewableObjectService reviewableObjectService;

	@Autowired
	private OrikaMapper mapper;

	@Autowired
	private ReviewValidator validator;

	@Autowired
	private List<ReviewStatusModerationValidator> moderationValidators;

	@Autowired
	private ValidationUtils validationUtils;
	
	@Autowired
	private ConfigurationService configurationService;
	
	@ApiOperation(value = "Fetch Master Data", position = 2)
	@RequestMapping(value = UriConstants.FETCH_MASTER_DATA, method = RequestMethod.GET)
	@ApiResponse(code = 200, message = "Fetched the master data", response = GetReviewMasterDataResponse.class)
	public ResponseEntity<GetReviewMasterDataResponse> getMasterData(@RequestParam("productId") String productId) {

		ReviewableObjectBo reviewableObjectBo = new ReviewableObjectBo(productId, Constants.DEFAULT_OBJECT_TYPE, Constants.DEFAULT_OBJECT_OWNER_ID);

		ReviewConfiguration  reviewConfiguration = configurationService.getReviewConfiguration();
		ReviewMasterData masterData;
		if(null != reviewConfiguration 
				&& null != reviewConfiguration.getCreateReviewRichDataVisibilty() 
				&& reviewConfiguration.getCreateReviewRichDataVisibilty()){
			masterData = reviewService.getMasterData(reviewableObjectBo);
		}else{
			masterData =  null;
		}
				
		GetReviewMasterDataResponse masterDataResponse = new GetReviewMasterDataResponse();
		masterDataResponse.setReviewMasterData(masterData);
		masterDataResponse.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<GetReviewMasterDataResponse>(masterDataResponse, HttpStatus.OK);
	}

	@ApiOperation(value = "Get review posted by a user on a given product", position = 2)
	@RequestMapping(value = UriConstants.SEARCH_REVIEW, method = RequestMethod.GET)
	@ApiResponse(code = 200, message = "Fetched review by user on product", response = GetReviewForProductByUserResponse.class)
	public ResponseEntity<GetReviewForProductByUserResponse> getReviewForProductByUser(@RequestParam("productId") String productId,
			@RequestParam("userId") String userId) {
		ReviewableObjectBo reviewableObjectBo = new ReviewableObjectBo(productId, Constants.DEFAULT_OBJECT_TYPE, Constants.DEFAULT_OBJECT_OWNER_ID);

		ReviewBo currentReviewBo = reviewService.getReview(reviewableObjectBo, userId);
		Review currentReview = mapper.map(currentReviewBo, Review.class);
		GetReviewForProductByUserResponse response = new GetReviewForProductByUserResponse();
		response.setReview(currentReview);
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<GetReviewForProductByUserResponse>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get all reviews posted by a user for a set of products", position = 2)
	@RequestMapping(value = UriConstants.SEARCH_REVIEWS_BY_USER, method = RequestMethod.GET)
	@ApiResponse(code = 200, message = "Fetched reviews by a user on a set of products", response = GetReviewForProductByUserResponse.class)
	public ResponseEntity<GetReviewsForProductsByUserResponse> getReviewsForProductsByUser(
			@RequestParam("userId") String userId, @RequestParam("productIds") List<String> productIds) {
		List<String> uniqueProductIds = new ArrayList<>(new LinkedHashSet<>(productIds));

		LOGGER.info("Retrieving all reviews for userId: " + userId + " for products: " + uniqueProductIds.toString());
		List<ReviewableObjectBo> reviewableObjectBos = new ArrayList<ReviewableObjectBo>();
		for (String productId : uniqueProductIds) {
			reviewableObjectBos.add(new ReviewableObjectBo(productId, Constants.DEFAULT_OBJECT_TYPE,
					Constants.DEFAULT_OBJECT_OWNER_ID));
		}

		List<ReviewBo> reviewBos = reviewService.getReview(reviewableObjectBos, userId);
		List<Review> reviews = new ArrayList<Review>();

		for (ReviewBo reviewBo : reviewBos) {
			reviewBo.setUserId(null);
			reviews.add(mapper.map(reviewBo, Review.class));
		}	

		GetReviewsForProductsByUserResponse response = new GetReviewsForProductsByUserResponse();
		response.setReviews(reviews);
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<GetReviewsForProductsByUserResponse>(response, HttpStatus.OK);
	}	

	@ApiOperation(value = "Create a Review", position = 3)
	@ApiResponse(code = 200, message = "Successfully created a review", response = CreateReviewResponse.class)
	@RequestMapping(value = UriConstants.CREATE_REVIEW, method = RequestMethod.POST)
	public ResponseEntity<CreateReviewResponse> createReview(@Valid @RequestBody CreateReviewRequest request, UriComponentsBuilder uriBuilder) {
		ReviewRequest createReviewRequest = request.getReviewRequest();
		validator.validate(createReviewRequest);
		
		if(null == createReviewRequest.getRecommended()){
			createReviewRequest.setRecommended(OpinionBo.ABSTAIN);
		}
		
		CreateReviewResponse cReviewResponse = new CreateReviewResponse();
		Set<String> profaneWords = validator.checkProfanity(createReviewRequest);
		if(!profaneWords.isEmpty()){
			cReviewResponse.setFailureResponse(new CreateReviewFailureResponse(profaneWords));
			return new ResponseEntity<CreateReviewResponse>(cReviewResponse, HttpStatus.BAD_REQUEST);
		}
		
		ReviewBo reviewBo = new ReviewBo();
		reviewBo = mapper.map(createReviewRequest, ReviewBo.class);
		ReviewBo response = reviewWriteService.createReview(reviewBo);
		ReviewResponse reviewResponse = new ReviewResponse();
		reviewResponse.setCreatedAt(response.getCreatedAt().getTime());
		reviewResponse.setId(response.getId());
		cReviewResponse.setReviewResponse(reviewResponse);
		cReviewResponse.setProtocol(Protocol.PROTOCOL_JSON);
		HttpHeaders headers = new HttpHeaders();
		setLocationHeader(headers, buildURIComponent(uriBuilder, UriConstants.SEARCH_REVIEW, reviewResponse.getId()));
		return new ResponseEntity<CreateReviewResponse>(cReviewResponse, headers, HttpStatus.OK);
	}

	

	@ApiOperation(value = "Retrieve reviews for an object", position = 6)
	@ApiResponse(code = 200, message = "Successfully retrieved reviews", response = GetReviewListingPageResponse.class)
	@RequestMapping(value = UriConstants.REVIEW_LISTING, method = RequestMethod.GET)
	public ResponseEntity<GetReviewListingPageResponse> getReviewListing(
			@RequestParam("productId") String productId,
			@RequestParam(value = "queryType", required = false) ReviewListingSortType sortType,
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "rating", required = false, defaultValue="0") int rating,
			@ApiParam(allowableValues = "range[0,infinity]") @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
			@ApiParam(allowableValues = "range[1,infinity]") @RequestParam(value = "limit", required = false) Integer limit) {

		ReviewConfiguration reviewConfiguration = configurationService.getReviewConfiguration();
		if(null == limit) {
			limit = reviewConfiguration.getDefaultPaginationSize();
		}
		if(null == sortType){
			sortType = reviewConfiguration.getDefaultReviewListingSortType();	
		}
		
		Page page = new Page(offset, limit);

		validator.validatePagination(page);
		validationUtils.assertRange("rating", rating, 0, 5);

		ReviewableObjectBo reviewableObjectBo = new ReviewableObjectBo(productId, Constants.DEFAULT_OBJECT_TYPE, Constants.DEFAULT_OBJECT_OWNER_ID);

		ReviewListingCriteria reviewListingCriteria = new ReviewListingCriteria();
		reviewListingCriteria.setUserId(userId);
		reviewListingCriteria.setPage(page);
		reviewListingCriteria.setSortType(sortType);
		reviewListingCriteria.setRating(rating);

		ReviewListingPage reviewListingPage = new ReviewListingPage();
		ReviewListingBo reviewListingBo = reviewService.search(reviewableObjectBo, reviewListingCriteria);
		List<ReviewBo> reviewBos = reviewListingBo.getReviewBos();
		reviewListingPage.setOffset(offset);
		reviewListingPage.setLimit(limit);
		reviewListingPage.setSize(reviewBos.size());
		reviewListingPage.setTotal(reviewListingBo.getTotal());
		reviewListingPage.setReviews(mapper.mapAsList(reviewBos, Review.class));
		
		GetReviewListingPageResponse response = new GetReviewListingPageResponse();
		response.setReviewListingPage(reviewListingPage);
		response.setProtocol(Protocol.PROTOCOL_JSON);

		return new ResponseEntity<GetReviewListingPageResponse>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Retrieve reviews with multiple ratings for an object", position = 25)
	@ApiResponse(code = 200, message = "Successfully retrieved reviews", response = GetReviewListingPageResponse.class)
	@RequestMapping(value = UriConstants.REVIEW_LISTING_WITH_MULTIPLE_RATINGS, method = RequestMethod.GET)
	public ResponseEntity<GetReviewListingPageResponse> getReviewListingWithMultipleRatings(
			@RequestParam("productId") String productId,
			@RequestParam(value = "queryType", required = false) ReviewListingSortType sortType,
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "ratings", required = false, defaultValue="0") String ratings,
			@ApiParam(allowableValues = "range[0,infinity]") @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
			@ApiParam(allowableValues = "range[1,infinity]") @RequestParam(value = "limit", required = false) Integer limit) {

		ReviewConfiguration reviewConfiguration = configurationService.getReviewConfiguration();
		if(null == limit) {
			limit = reviewConfiguration.getDefaultPaginationSize();
		}
		if(null == sortType){
			sortType = reviewConfiguration.getDefaultReviewListingSortType();	
		}
		
		Page page = new Page(offset, limit);

		validator.validatePagination(page);
		final List<Integer> ratingsList = new ArrayList<Integer>();
		for(final String rating : ratings.split(",")){
			try{
				final int rate =  Integer.parseInt(rating);
				if(ratingsList.contains(rate)){
					throw new InvalidRequestException(ErrorCode.INPUT_INVALID_PAYLOAD,
							"rating");
				}
				ratingsList.add(rate);
			}catch(NumberFormatException ne){
				throw new InvalidRequestException(ErrorCode.INPUT_INVALID_PAYLOAD,
						"rating");
			}
		}
		validationUtils.assertRange("rating", ratingsList, 0, 5);

		ReviewableObjectBo reviewableObjectBo = new ReviewableObjectBo(productId, Constants.DEFAULT_OBJECT_TYPE, Constants.DEFAULT_OBJECT_OWNER_ID);

		ReviewListingCriteria reviewListingCriteria = new ReviewListingCriteria();
		reviewListingCriteria.setUserId(userId);
		reviewListingCriteria.setPage(page);
		reviewListingCriteria.setSortType(sortType);
		reviewListingCriteria.setRatingList(ratingsList);

		ReviewListingPage reviewListingPage = new ReviewListingPage();
		ReviewListingBo reviewListingBo = reviewService.search(reviewableObjectBo, reviewListingCriteria);
		List<ReviewBo> reviewBos = reviewListingBo.getReviewBos();
		reviewListingPage.setOffset(offset);
		reviewListingPage.setLimit(limit);
		reviewListingPage.setSize(reviewBos.size());
		reviewListingPage.setTotal(reviewListingBo.getTotal());
		reviewListingPage.setReviews(mapper.mapAsList(reviewBos, Review.class));
		
		GetReviewListingPageResponse response = new GetReviewListingPageResponse();
		response.setReviewListingPage(reviewListingPage);
		response.setProtocol(Protocol.PROTOCOL_JSON);

		return new ResponseEntity<GetReviewListingPageResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Post a Feedback", position = 7)
	@ApiResponse(code = 200, message = "Successfully retrieved reviews")
	@RequestMapping(value = UriConstants.FEEDBACK, method = RequestMethod.POST)
	public ResponseEntity<ServiceResponse> createFeedback(@Valid @RequestBody CreateFeedbackRequest request) {
		reviewWriteService.createFeedback(request.getReviewId(), request.getUserId(), request.getVoteType());
		ServiceResponse response = new ServiceResponse();
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Fetch a review by id", position = 9)
	@RequestMapping(value = UriConstants.GET_REVIEW, method = RequestMethod.GET)
	public ResponseEntity<GetReviewResponse> getReview(@RequestParam("id") String reviewId) {
		Review review = mapper.map(reviewService.getReview(reviewId), Review.class);
		LOGGER.debug("Sending Response");
		GetReviewResponse response = new GetReviewResponse();
		response.setReview(review);
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<GetReviewResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Review Statistics", position = 8)
	@RequestMapping(value = UriConstants.REVIEW_STATS, method = RequestMethod.GET)
	public ResponseEntity<GetProductReviewStatsResponse> getReviewStats(
			@RequestParam("productId") String productId
			//,@ApiParam(required = false, value = "Response type. Valid value - minimal") @RequestParam(value = "responseType", required = false) ProductStats responseType,
			//@ApiParam(required = false, value = "Comma separated list of field names to be included in the response") @RequestParam(value = "select", required = false) String selectors)
			)
			throws ReviewException {
		GetProductReviewStatsResponse response = getProductStatsResponse(productId);
		return new ResponseEntity<GetProductReviewStatsResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Product Review Statistics Summary", position = 14)
	@RequestMapping(value = UriConstants.PRODUCT_REVIEW_STATS_SUMMARY, method = RequestMethod.GET)
	public ResponseEntity<GetProductReviewStatsResponse> getReviewStatsSummary(@RequestParam("productId")  String productId) throws ReviewException {
		GetProductReviewStatsResponse response = getProductStatsResponse(productId);
		HttpHeaders headers = addSelectorHeader(ProductStats.getFieldNames(ProductStats.MINIMAL));
		return new ResponseEntity<GetProductReviewStatsResponse>(response, headers, HttpStatus.OK);
	}

	private HttpHeaders addSelectorHeader(String selectors) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.QUERY_SELECTOR, selectors);
		return headers;
	}

	@ApiOperation(value = "Post a Rating", position = 1)
	@RequestMapping(value = UriConstants.CREATE_RATING, method = RequestMethod.POST)
	public ResponseEntity<CreateReviewResponse> createRating(@Valid @RequestBody CreateRatingRequest request) {
		ReviewBo reviewBo = mapper.map(request.getRating(), ReviewBo.class);

		LOGGER.debug("Sending Response");
		validator.validateRating(request.getRating());
		ReviewBo response = reviewWriteService.createRating(reviewBo);
		ReviewResponse reviewResponse = new ReviewResponse();
		reviewResponse.setCreatedAt(response.getCreatedAt().getTime());
		reviewResponse.setId(response.getId());
		CreateReviewResponse cResponse = new CreateReviewResponse();
		cResponse.setReviewResponse(reviewResponse);
		cResponse.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<CreateReviewResponse>(cResponse, HttpStatus.OK);
	}

	private <S, D> D mapAndGet(S src, Class<D> destinationClass) {
		return mapper.map(src, destinationClass);
	}



	@ApiOperation(value = "Flag a Review", position = 12)
	@ApiResponse(code = 200, message = "Successfully flagged")
	@RequestMapping(value = UriConstants.FLAG_REVIEW, method = RequestMethod.POST)
	public ResponseEntity<ServiceResponse> flagReview(@Valid @RequestBody FlagReviewRequest request) {
		reviewWriteService.createFlag(request.getReviewId(), request.getUserId(), request.getFlagType());
		ServiceResponse response = new ServiceResponse();
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "UnFlag a Review", position = 13)
	@ApiResponse(code = 200, message = "Successfully unflagged")
	@RequestMapping(value = UriConstants.UNFLAG_REVIEW, method = RequestMethod.POST)
	public ResponseEntity<ServiceResponse> unFlagReview(@Valid @RequestBody FlagReviewRequest request) {
		reviewWriteService.removeFlag(request.getReviewId(), request.getUserId(), request.getFlagType());
		ServiceResponse response = new ServiceResponse();
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Update Rating", position = 14)
	@RequestMapping(value = UriConstants.UPDATE_RATING, method = RequestMethod.POST)
	public ResponseEntity<UpdateReviewResponse> updateRating(@Valid @RequestBody UpdateRatingRequest request) {
		ReviewBo response = reviewWriteService.updateRating(request.getRatingUpdateRequest().getReviewId(), request.getRatingUpdateRequest().getRating(), request.getRatingUpdateRequest().getUserId());
		ReviewUpdateResponse reviewResponse = new ReviewUpdateResponse();
		/*if(response.getModifiedAt() != null){
			reviewResponse.setModifiedAt(response.getModifiedAt().getTime());
		}*/
		reviewResponse.setId(response.getId());
		UpdateReviewResponse cResponse = new UpdateReviewResponse();
		cResponse.setResponse(reviewResponse);
		cResponse.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<UpdateReviewResponse>(cResponse, HttpStatus.OK);
	}

	@ApiOperation(value = "Get the status of the review posted by a user on a given product", position = 14)
	@RequestMapping(value = UriConstants.SEARCH_REVIEW_SUMMARY, method = RequestMethod.GET)
	@ApiResponse(code = 200, message = "Fetched review status by user on product", response = GetReviewStatusForProductByUserResponse.class)
	public ResponseEntity<GetReviewStatusForProductByUserResponse> getReviewStatusForProductByUser(@RequestParam("productId") String productId,
			@RequestParam("userId") String userId) {
		ReviewableObjectBo reviewableObjectBo = new ReviewableObjectBo(productId, Constants.DEFAULT_OBJECT_TYPE, Constants.DEFAULT_OBJECT_OWNER_ID);

		ReviewStatusForProductByUser reviewStatus = reviewService.getReviewStatusForProductByUser(reviewableObjectBo, userId);
		GetReviewStatusForProductByUserResponse response = new GetReviewStatusForProductByUserResponse();
		response.setReviewStatusForProductByUser(reviewStatus);
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<GetReviewStatusForProductByUserResponse>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get user actions on set of reviews", position = 15)
	@RequestMapping(value = UriConstants.USER_ACTIONS, method = RequestMethod.GET)
	@ApiResponse(code = 200, message = "Fetched user actions on reviews by user ", response = GetUserActionsOnReviewsResponse.class)
	public ResponseEntity<GetUserActionsOnReviewsResponse> getUserActionOnReviews(
			@RequestParam("userId") String userId, @RequestParam("id") String[] reviewIds) {
		
		UserActivityOnReviews userActions = reviewService.getUserActions(userId, reviewIds);
		
		List<GetUserActivities> userActivities = mapper.mapAsList(
				userActions.getUserActions(), GetUserActivities.class);
		
		GetUserActionsOnReviewsResponse response = new GetUserActionsOnReviewsResponse();
		response.setUserActions(userActivities);
		response.setUserId(userId);
		response.setProtocol(Protocol.PROTOCOL_JSON);
		
		return new ResponseEntity<GetUserActionsOnReviewsResponse>(response, HttpStatus.OK);
	}
	private GetProductReviewStatsResponse getProductStatsResponse(String productId) {
		ProductReviewStats reviewStats = mapAndGet(
				reviewableObjectService.getObjectReviewStats(new ReviewableObjectBo(productId, Constants.DEFAULT_OBJECT_TYPE, 0)),
				ProductReviewStats.class);
		GetProductReviewStatsResponse response = new GetProductReviewStatsResponse();
		response.setProductReviewStats(reviewStats);
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return response;
	}
	
	@ApiOperation(value = "Get all product ids for which given users have written reivew", position = 17)
	@RequestMapping(value = UriConstants.PRODUCTS_REVIEWED_BY_USER_SUMMARY, method = RequestMethod.GET)
	@ApiResponse(code = 200, message = "Fetched all products for which users have written review ", 
					response = GetProductsReviewedByUsersResponse.class)
	public ResponseEntity<GetProductsReviewedByUsersResponse> getProductIdsByUser(
			@RequestParam("userIds") String[] userIds,
			@RequestParam(value = "status", defaultValue = "CREATED") Status[] status) {
		
		Map<String,List<String>> productsReviewedByUsers = reviewService.getAllProductsReviewedByUser(userIds, Arrays.asList(status));
		
		GetProductsReviewedByUsersResponse response = new GetProductsReviewedByUsersResponse();
		response.setProductsReviewedByUsers(productsReviewedByUsers);
		response.setProtocol(Protocol.PROTOCOL_JSON);
		
		return new ResponseEntity<GetProductsReviewedByUsersResponse>(response, HttpStatus.OK);
}
	@ApiOperation(value = "Update Recommendation", position = 16)
	@RequestMapping(value = UriConstants.UPDATE_RECOMMENDATION, method = RequestMethod.POST)
	@ApiResponse(code = 200, message = "Successfully Recommended")
	public ResponseEntity<UpdateReviewResponse> updateRecommendation(@Valid @RequestBody UpdateRecommendationRequest request) {	    
	    validator.validateRecommendation(request.getRecommendationRequest());
	    
	    RecommendationRequest recommendationRequest = request.getRecommendationRequest();
	    ReviewBo review = new ReviewBo();
	    String productId = recommendationRequest.getProductId();
	    ReviewableObjectBo reviewableObjectBo = new ReviewableObjectBo(productId);
		review.setReviewableObjectBo(reviewableObjectBo);
		review.setUserId(recommendationRequest.getUserId());
		review.setRecommended(recommendationRequest.getIsRecommended());
		ReviewBo response = reviewWriteService.updateRecommendation(review);
		ReviewUpdateResponse reviewResponse = new ReviewUpdateResponse();
		/*if(response.getModifiedAt()!=null)
		{
		    reviewResponse.setModifiedAt(response.getModifiedAt().getTime());   
		}*/
		reviewResponse.setId(response.getId());
		UpdateReviewResponse cResponse = new UpdateReviewResponse();
		cResponse.setResponse(reviewResponse);
		cResponse.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<UpdateReviewResponse>(cResponse, HttpStatus.OK);
	}

	@ApiOperation(value = "Create Recommendation", position = 18)
	@RequestMapping(value = UriConstants.CREATE_RECOMMENDATION, method = RequestMethod.POST)
	@ApiResponse(code = 200, message = "Recommendation Created")
	public ResponseEntity<CreateReviewResponse> createRecommendation(@Valid @RequestBody CreateRecommendationRequest request) {	    
	    validator.validateRecommendation(request.getRecommendationRequest());
	    RecommendationRequest recommendationRequest = request.getRecommendationRequest();
	    ReviewBo review = new ReviewBo();
	    String productId = recommendationRequest.getProductId();
	    ReviewableObjectBo reviewableObjectBo = new ReviewableObjectBo(productId);
		review.setReviewableObjectBo(reviewableObjectBo);
		review.setUserId(recommendationRequest.getUserId());
		review.setRecommended(recommendationRequest.getIsRecommended());
		
		ReviewBo response = reviewWriteService.createRecommendation(review);		
		CreateReviewResponse cReviewResponse = new CreateReviewResponse();			
		ReviewResponse reviewResponse = new ReviewResponse();
		reviewResponse.setCreatedAt(response.getCreatedAt().getTime());
		reviewResponse.setId(response.getId());
		cReviewResponse.setReviewResponse(reviewResponse);
		cReviewResponse.setProtocol(Protocol.PROTOCOL_JSON);		
		return new ResponseEntity<CreateReviewResponse>(cReviewResponse, HttpStatus.OK);
	}

	
	@ApiOperation(value = "Create a Sample Review", position = 19)
	@ApiResponse(code = 200, message = "Successfully created a review", response = ServiceResponse.class)
	@RequestMapping(value = UriConstants.CREATE_SAMPLE_REVIEW, method = RequestMethod.POST)
	public ResponseEntity<ServiceResponse> createSampleReview(@Valid @RequestBody CreateSampleReviewRequest request) {
	    	SampleReview sampleReview = request.getSampleReview();  
		validator.validate(sampleReview);		
		SampleReviewBo sampleReviewBo = null;		
		sampleReviewBo = mapper.map(sampleReview, SampleReviewBo.class);
		sampleReviewBo.setCreatedAt(new Date(sampleReview.getCreatedAt()));
		reviewWriteService.createSampleReview(sampleReviewBo);
		ServiceResponse response = new ServiceResponse();
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);
	}
	

	@ApiOperation(value = "Update a Sample Review", position = 20)
	@ApiResponse(code = 200, message = "Successfully updated a review", response = ServiceResponse.class)
	@RequestMapping(value = UriConstants.UPDATE_SAMPLE_REVIEW, method = RequestMethod.POST)
	public ResponseEntity<ServiceResponse> updateSampleReview(@Valid @RequestBody UpdateSampleReviewRequest request) {	    
	    	SampleReview sampleReview = request.getSampleReview();  
		validator.validate(sampleReview);		
		SampleReviewBo sampleReviewBo = null;		
		sampleReviewBo = mapper.map(sampleReview, SampleReviewBo.class);
		sampleReviewBo.setCreatedAt(new Date(sampleReview.getCreatedAt()));
		reviewWriteService.updateSampleReview(sampleReviewBo);
		ServiceResponse response = new ServiceResponse();
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Fetch a sample review by label", position = 21)
	@RequestMapping(value = UriConstants.GET_SAMPLE_REVIEW, method = RequestMethod.GET)
	public ResponseEntity<GetSampleReviewResponse> getSampleReview(@RequestParam("label") String label) {	  
	    	validationUtils.assertNonNull("label" , label);	    
		SampleReviewBo sampleReviewBo = new SampleReviewBo();	
		sampleReviewBo.setLabel(label.trim());
		sampleReviewBo= reviewService.getSampleReview(sampleReviewBo);
		SampleReview sampleReview =  mapper.map(sampleReviewBo, SampleReview.class);
		sampleReview.setCreatedAt(sampleReviewBo.getCreatedAt().getTime());
		GetSampleReviewResponse response = new GetSampleReviewResponse();
		response.setSampleReview(sampleReview);
		response.setProtocol(Protocol.PROTOCOL_JSON);		
		return new ResponseEntity<GetSampleReviewResponse>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Fetch all product labels", position = 22)
	@RequestMapping(value = UriConstants.GET_PRODUCT_LABELS, method = RequestMethod.GET)
	public ResponseEntity<GetLabelsResponse> getProductLabels() {
	    	List<String> labelList = null;
	    	labelList= reviewService.getProductLabelList();		
		GetLabelsResponse response = new GetLabelsResponse();
		ProductLabel productLabel = new ProductLabel();
		productLabel.setLabelList(labelList);
		response.setProductLabel(productLabel);
		response.setProtocol(Protocol.PROTOCOL_JSON);		
		return new ResponseEntity<GetLabelsResponse>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Health Check API", position = 22)
	@RequestMapping(value = UriConstants.HEALTH_CHECK, method = RequestMethod.GET)
	public ResponseEntity<HealthCheckResponse> healthCheck() {
		HealthCheckResponse response = new HealthCheckResponse();
		response.setVersion("9.1");
		response.setStatus("Up and Running");
		return new ResponseEntity<HealthCheckResponse>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get all the dynamic content for reviews", position = 23)
	@RequestMapping(value = UriConstants.REVIEW_DYNAMIC_ATTRIBUTES, method = RequestMethod.GET)
	public ResponseEntity<ReviewsDynamicContentResponse> getDynamicReviewContent(
			@RequestParam("userId") String userId, @RequestParam("id") String[] reviewIds) {
		List<ReviewBo> reviewBos = reviewService.getDynamicReviewsContent(userId, reviewIds);
		ReviewsDynamicContentResponse response = new ReviewsDynamicContentResponse(mapper.mapAsList(reviewBos, Review.class));
		return new ResponseEntity<ReviewsDynamicContentResponse>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Search on Reviews by keywords", position = 24)
	@ApiResponse(code = 200, message = "Successfully search reviews", response = GetReviewForProductByKeywordResponse.class)
	@RequestMapping(value = UriConstants.SEARCH_REVIEW_KEYWORD, method = RequestMethod.GET)
	public ResponseEntity<GetReviewForProductByKeywordResponse> getReviewForProductByKeyword(
			@RequestParam(value = "productId", required = true)String productId,
			@RequestParam(value = "sortType", required = false, defaultValue = "HELPFUL_DESC") ReviewListingSortType sortType,
			@RequestParam(value = "status", required = false, defaultValue = "APPROVED") Status status, 
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "rating", required = false, defaultValue = "0") int rating,
			@ApiParam(allowableValues = "range[0,infinity]") @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
			@ApiParam(allowableValues = "range[1,infinity]") @RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam("keywords") String keywords) {
		ReviewConfiguration reviewConfiguration = configurationService.getReviewConfiguration();
		ReviewListingPage reviewListingPage = new ReviewListingPage();

		if (null == limit) {
			limit = reviewConfiguration.getDefaultPaginationSize();
		}
		if (null == sortType) {
			sortType = reviewConfiguration.getDefaultReviewListingSortType();
		}

		Page page = new Page(offset, limit);
		validator.validatePagination(page);
		validationUtils.assertRange("rating", rating, 0, 5);
		validator.validateSearchFlow(reviewConfiguration);
		
		List<String> atrributeValueProduct = new ArrayList<String>();
		atrributeValueProduct.add(productId);
		
		List<TermQueryAttribute> termQueryMustAttributesList = new ArrayList<TermQueryAttribute>();
		TermQueryAttribute termQueryAttributeProductId = new TermQueryAttribute();
		termQueryAttributeProductId.setField(IndexField.PRODUCT_ID);
		termQueryAttributeProductId.setValue(atrributeValueProduct);
		termQueryMustAttributesList.add(termQueryAttributeProductId);
		
		if (rating > 0 && rating <= 5) {
			List<String> atrributeValueRating = new ArrayList<String>();
			atrributeValueRating.add(Integer.toString(rating));
			TermQueryAttribute termQueryAttributeRating = new TermQueryAttribute();
			termQueryAttributeRating.setField(IndexField.RATING);
			termQueryAttributeRating.setValue(atrributeValueRating);
			termQueryMustAttributesList.add(termQueryAttributeRating);
		}
		
		List<String> atrributeValueStatus = new ArrayList<String>();
		atrributeValueStatus.add(status.name());
		TermQueryAttribute termQueryAttributeStatus = new TermQueryAttribute();
		termQueryAttributeStatus.setField(IndexField.STATUS);
		termQueryAttributeStatus.setValue(atrributeValueStatus);
		termQueryMustAttributesList.add(termQueryAttributeStatus);
		
		ReviewUserSearchBo reviewUserSearchBo = new ReviewUserSearchBo();
		reviewUserSearchBo.setKeywords(keywords);
		reviewUserSearchBo.setPage(page);
		reviewUserSearchBo.setSortType(sortType);
		reviewUserSearchBo.setTermQueryMustAttributes(termQueryMustAttributesList);

		ReviewListingBo reviewsSearch = reviewModerationService.reviewsSearchByKeywords(reviewUserSearchBo);
		List<ReviewBo> reviewBos = reviewsSearch.getReviewBos();

		reviewListingPage.setOffset(offset);
		reviewListingPage.setLimit(limit);
		reviewListingPage.setSize(reviewBos.size());
		reviewListingPage.setTotal(reviewsSearch.getTotal());
		reviewListingPage.setReviews(mapper.mapAsList(reviewBos, Review.class));

		GetReviewForProductByKeywordResponse response = new GetReviewForProductByKeywordResponse();
		response.setReviewListingPage(reviewListingPage);
		response.setProtocol(Protocol.PROTOCOL_JSON);

		return new ResponseEntity<GetReviewForProductByKeywordResponse>(response, HttpStatus.OK);

	}
	
	
}
