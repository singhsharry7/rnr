package com.snapdeal.reviews.client.api.impl;

import static com.snapdeal.reviews.commons.UriConstants.CREATE_RATING;
import static com.snapdeal.reviews.commons.UriConstants.CREATE_RECOMMENDATION;
import static com.snapdeal.reviews.commons.UriConstants.CREATE_REVIEW;
import static com.snapdeal.reviews.commons.UriConstants.CREATE_SAMPLE_REVIEW;
import static com.snapdeal.reviews.commons.UriConstants.FEEDBACK;
import static com.snapdeal.reviews.commons.UriConstants.FETCH_MASTER_DATA;
import static com.snapdeal.reviews.commons.UriConstants.FLAG_REVIEW;
import static com.snapdeal.reviews.commons.UriConstants.GET_REVIEW;
import static com.snapdeal.reviews.commons.UriConstants.GET_SAMPLE_REVIEW;
import static com.snapdeal.reviews.commons.UriConstants.PRODUCT_REVIEW_STATS_SUMMARY;
import static com.snapdeal.reviews.commons.UriConstants.REVIEW_LISTING;
import static com.snapdeal.reviews.commons.UriConstants.REVIEW_STATS;
import static com.snapdeal.reviews.commons.UriConstants.SEARCH_REVIEW;
import static com.snapdeal.reviews.commons.UriConstants.UNFLAG_REVIEW;
import static com.snapdeal.reviews.commons.UriConstants.UPDATE_RATING;
import static com.snapdeal.reviews.commons.UriConstants.UPDATE_RECOMMENDATION;
import static com.snapdeal.reviews.commons.UriConstants.UPDATE_SAMPLE_REVIEW;
import static com.snapdeal.reviews.commons.UriConstants.SEARCH_REVIEW_KEYWORD;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.base.model.common.ServiceResponse;
import com.snapdeal.base.transport.service.ITransportService;
import com.snapdeal.base.transport.service.ITransportService.Protocol;
import com.snapdeal.reviews.client.api.ReviewClientService;
import com.snapdeal.reviews.client.base.lib.adapter.Constants;
import com.snapdeal.reviews.commons.UriConstants;
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

@Service
public class ReviewClientServiceImpl implements ReviewClientService {

	@Autowired
	private ITransportService transportService;

	private String webServiceBaseUrl;

	private Map<String, String> headerMap = new HashMap<>();

	@PostConstruct
	public void init() {
		headerMap.put("Accept", "application/" + Constants.CUSTOM_JSON_PROTOCOL.toLowerCase());
		headerMap.put("Content-Type", "application/" + Constants.CUSTOM_JSON_PROTOCOL.toLowerCase());
		transportService.registerService("/api/service/reviews/", "reviewService.");
	}

	@Override
	public GetReviewMasterDataResponse fetchMasterData(Map<String, String> queryParams) throws SnapdealWSException {
		return (GetReviewMasterDataResponse) transportService.executeGetRequest(getWebServiceBaseUrl() + FETCH_MASTER_DATA,
				queryParams != null ? queryParams : new HashMap<String, String>(), headerMap, GetReviewMasterDataResponse.class);
	}

	@Override
	public GetReviewForProductByUserResponse getReviewByProductAndUser(Map<String, String> queryParams) throws SnapdealWSException {
		return (GetReviewForProductByUserResponse) transportService.executeGetRequest(getWebServiceBaseUrl() + SEARCH_REVIEW,
				queryParams != null ? queryParams : new HashMap<String, String>(), headerMap, GetReviewForProductByUserResponse.class);
	}

	@Override
	public CreateReviewResponse createReview(CreateReviewRequest request) throws SnapdealWSException {
		request.setRequestProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return (CreateReviewResponse) transportService.executeRequest(getWebServiceBaseUrl() + CREATE_REVIEW, request, headerMap,
				CreateReviewResponse.class);
	}

	@Override
	public GetReviewListingPageResponse getReviewList(Map<String, String> queryParams) throws SnapdealWSException {
		return (GetReviewListingPageResponse) transportService.executeGetRequest(getWebServiceBaseUrl() + REVIEW_LISTING,
				queryParams != null ? queryParams : new HashMap<String, String>(), headerMap, GetReviewListingPageResponse.class);
	}

	@Override
	public ServiceResponse postFeedback(CreateFeedbackRequest request) throws SnapdealWSException {
		request.setRequestProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return transportService.executeRequest(getWebServiceBaseUrl() + FEEDBACK, request, headerMap, ServiceResponse.class);
	}

	@Override
	public GetProductReviewStatsResponse getReviewStats(Map<String, String> queryParams) throws SnapdealWSException {
		return (GetProductReviewStatsResponse) transportService.executeGetRequest(getWebServiceBaseUrl() + REVIEW_STATS,
				queryParams != null ? queryParams : new HashMap<String, String>(), headerMap, GetProductReviewStatsResponse.class);
	}

	@Override
	public GetProductReviewStatsResponse getReviewStatsSummary(Map<String, String> queryParams) throws SnapdealWSException {
		queryParams = queryParams != null ? queryParams : new HashMap<String, String>();
		return (GetProductReviewStatsResponse) transportService.executeGetRequest(getWebServiceBaseUrl() + PRODUCT_REVIEW_STATS_SUMMARY, queryParams,
				headerMap, GetProductReviewStatsResponse.class);
	}

	@Override
	public GetReviewResponse getReview(Map<String, String> queryParams) throws SnapdealWSException {
		return (GetReviewResponse) transportService.executeGetRequest(getWebServiceBaseUrl() + GET_REVIEW, queryParams != null ? queryParams
				: new HashMap<String, String>(), headerMap, GetReviewResponse.class);
	}

	@Override
	public CreateReviewResponse createRating(CreateRatingRequest request) throws SnapdealWSException {
		request.setRequestProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return (CreateReviewResponse) transportService.executeRequest(getWebServiceBaseUrl() + CREATE_RATING, request, headerMap,
				CreateReviewResponse.class);
	}

	public String getWebServiceBaseUrl() {
		return webServiceBaseUrl;
	}

	public void setWebServiceBaseUrl(String webServiceBaseUrl) {
		this.webServiceBaseUrl = webServiceBaseUrl;
	}

	@Override
	public ServiceResponse createFlag(FlagReviewRequest request) throws SnapdealWSException {
		request.setRequestProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return (ServiceResponse) transportService.executeRequest(getWebServiceBaseUrl() + FLAG_REVIEW, request, headerMap, ServiceResponse.class);
	}

	@Override
	public ServiceResponse removeFlag(FlagReviewRequest request) throws SnapdealWSException {
		request.setRequestProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return (ServiceResponse) transportService.executeRequest(getWebServiceBaseUrl() + UNFLAG_REVIEW, request, headerMap, ServiceResponse.class);
	}

	@Override
	public GetReviewStatusForProductByUserResponse getReviewSummaryByProductAndUser(Map<String, String> queryParams) throws SnapdealWSException {
		return (GetReviewStatusForProductByUserResponse) transportService.executeGetRequest(getWebServiceBaseUrl()
				+ UriConstants.SEARCH_REVIEW_SUMMARY, queryParams != null ? queryParams : new HashMap<String, String>(), headerMap,
				GetReviewStatusForProductByUserResponse.class);
	}
	
	@Override
	public GetProductsReviewedByUsersResponse getProductsReviewedByUser(Map<String, String> queryParams) throws SnapdealWSException {
		return (GetProductsReviewedByUsersResponse)transportService.executeGetRequest(
				getWebServiceBaseUrl()+ UriConstants.PRODUCTS_REVIEWED_BY_USER_SUMMARY,
				queryParams, headerMap, GetProductsReviewedByUsersResponse.class);
	}

	@Override
	public UpdateReviewResponse updateRating(UpdateRatingRequest request) throws SnapdealWSException {
		request.setRequestProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return (UpdateReviewResponse) transportService.executeRequest(getWebServiceBaseUrl() + UPDATE_RATING, request, headerMap, UpdateReviewResponse.class);
	}

	@Override
	public GetUserActionsOnReviewsResponse getUserActions(Map<String, String> queryParams) throws SnapdealWSException {
		queryParams = queryParams != null ? queryParams : new HashMap<String, String>();
		return (GetUserActionsOnReviewsResponse)transportService.executeGetRequest(getWebServiceBaseUrl()
				+ UriConstants.USER_ACTIONS, queryParams, headerMap,
						GetUserActionsOnReviewsResponse.class);
	}
	
	@Override
	public UpdateReviewResponse updateRecommendation( UpdateRecommendationRequest request) throws SnapdealWSException {
		request.setRequestProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return (UpdateReviewResponse) transportService.executeRequest(getWebServiceBaseUrl() + UPDATE_RECOMMENDATION, request, headerMap, UpdateReviewResponse.class);
	}

	@Override
        public CreateReviewResponse createRecommendation(
                CreateRecommendationRequest request) throws SnapdealWSException
        {
		request.setRequestProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return (CreateReviewResponse) transportService.executeRequest(getWebServiceBaseUrl() + CREATE_RECOMMENDATION, request, headerMap, CreateReviewResponse.class);
        }
	
	@Override
        public ServiceResponse createSampleReview(
        	CreateSampleReviewRequest request) throws SnapdealWSException
        {
	    request.setRequestProtocol(Protocol.PROTOCOL_JSON);
		return (ServiceResponse) transportService.executeRequest(getWebServiceBaseUrl() + CREATE_SAMPLE_REVIEW, request, headerMap, ServiceResponse.class);
        }

	@Override
        public ServiceResponse updateSampleReview(
                UpdateSampleReviewRequest request) throws SnapdealWSException
        {
	    request.setRequestProtocol(Protocol.PROTOCOL_JSON);
		return (ServiceResponse) transportService.executeRequest(getWebServiceBaseUrl() + UPDATE_SAMPLE_REVIEW, request, headerMap, ServiceResponse.class);
        }

	@Override
        public GetSampleReviewResponse getSampleReview(
                Map<String , String> queryParams)throws SnapdealWSException
        {
	    return (GetSampleReviewResponse) transportService.executeGetRequest(getWebServiceBaseUrl() + GET_SAMPLE_REVIEW, queryParams, headerMap, GetSampleReviewResponse.class);
	    
	    
        }

	@Override
        public GetLabelsResponse getProductLabels()
                throws SnapdealWSException
        {
	    return (GetLabelsResponse) transportService.executeGetRequest(getWebServiceBaseUrl() + GET_SAMPLE_REVIEW, new HashMap<String, String>(), headerMap, GetLabelsResponse.class);
	   
        }

	@Override
	public ReviewsDynamicContentResponse getDynamicReviewsContent(Map<String, String> queryParams) throws SnapdealWSException {
		queryParams = queryParams != null ? queryParams : new HashMap<String, String>();
		return (ReviewsDynamicContentResponse)transportService.executeGetRequest(getWebServiceBaseUrl()
				+ UriConstants.REVIEW_DYNAMIC_ATTRIBUTES, queryParams, headerMap,
				ReviewsDynamicContentResponse.class);
	}

	@Override
	public GetReviewForProductByKeywordResponse getReviewByProductAndKeyword(Map<String, String> queryParams) throws SnapdealWSException {
		return (GetReviewForProductByKeywordResponse) transportService.executeGetRequest(getWebServiceBaseUrl() + SEARCH_REVIEW_KEYWORD,
				queryParams != null ? queryParams : new HashMap<String, String>(), headerMap, GetReviewForProductByKeywordResponse.class);
	}
	
	@Override
	public GetReviewListingPageResponse getAllReviews(Map<String, String> queryParams) throws SnapdealWSException {
		return (GetReviewListingPageResponse) transportService.executeGetRequest(getWebServiceBaseUrl() + UriConstants.REVIEW_LISTING_WITH_MULTIPLE_RATINGS,
				queryParams != null ? queryParams : new HashMap<String, String>(), headerMap, GetReviewListingPageResponse.class);
	}

	@Override
	public GetProductsReviewsByUsersResponse getProductsReviewsByUser(Map<String, String> queryParams)
			throws SnapdealWSException {
		return (GetProductsReviewsByUsersResponse)transportService.executeGetRequest(
				getWebServiceBaseUrl()+ UriConstants.PRODUCTS_REVIEWED_BY_USER_SUMMARY,
				queryParams, headerMap, GetProductsReviewedByUsersResponse.class);
	}
}
