package com.snapdeal.reviews.client.api.impl;

import static com.snapdeal.reviews.commons.UriConstants.Moderation.CHECKED_OUT_COUNT;
import static com.snapdeal.reviews.commons.UriConstants.Moderation.CHECKIN_REVIEWS;
import static com.snapdeal.reviews.commons.UriConstants.Moderation.CHECKOUT_REVIEWS;
import static com.snapdeal.reviews.commons.UriConstants.Moderation.MODERATION_SUMMARY_BY_DATE;
import static com.snapdeal.reviews.commons.UriConstants.Moderation.REVIEWS_COUNT;
import static com.snapdeal.reviews.commons.UriConstants.Moderation.REVIEWS_SEARCH_MODERATION;
import static com.snapdeal.reviews.commons.UriConstants.Moderation.REVIEW_LISTING_MODERATION;
import static com.snapdeal.reviews.commons.UriConstants.Moderation.REVIEWS_PENDING_FOR_MODERATION_COUNT;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.base.transport.service.ITransportService;
import com.snapdeal.base.transport.service.ITransportService.Protocol;
import com.snapdeal.reviews.client.api.ReviewModerationClientService;
import com.snapdeal.reviews.client.base.lib.adapter.Constants;
import com.snapdeal.reviews.commons.dto.wrapper.GetCheckedOutCountResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetModerationReviewsCountResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetReviewListingPageForModerationResponse;
import com.snapdeal.reviews.commons.dto.wrapper.ModerationSummaryResponse;
import com.snapdeal.reviews.commons.dto.wrapper.SearchReviewsRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateReviewOnCheckinRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateReviewOnCheckinResponse;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateReviewOnCheckoutRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateReviewOnCheckoutResponse;

@Service
public class ReviewModerationClientServiceImpl implements
		ReviewModerationClientService {

	@Autowired
	private ITransportService transportService;

	private String webServiceBaseUrl;

	private Map<String, String> headerMap = new HashMap<>();

	@PostConstruct
	public void init() {
		headerMap.put("Accept",
				"application/" + Constants.CUSTOM_JSON_PROTOCOL.toLowerCase());
		headerMap.put("Content-Type", "application/"
				+ Constants.CUSTOM_JSON_PROTOCOL.toLowerCase());
		transportService.registerService("/reviews", "reviewService.");
	}

	@Override
	public GetReviewListingPageForModerationResponse getReviewList(
			Map<String, String> queryParams) throws SnapdealWSException {
		return (GetReviewListingPageForModerationResponse) transportService
				.executeGetRequest(getWebServiceBaseUrl()
						+ REVIEW_LISTING_MODERATION,
						queryParams != null ? queryParams
								: new HashMap<String, String>(), headerMap,
						GetReviewListingPageForModerationResponse.class);
	}

	@Override
	public UpdateReviewOnCheckoutResponse updateReviewOnCheckout(
			UpdateReviewOnCheckoutRequest request) throws SnapdealWSException {
		request.setRequestProtocol(Protocol
				.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return (UpdateReviewOnCheckoutResponse) transportService
				.executeRequest(getWebServiceBaseUrl() + CHECKOUT_REVIEWS,
						request, headerMap,
						UpdateReviewOnCheckoutResponse.class);
	}

	@Override
	public UpdateReviewOnCheckinResponse updateReviewOnCheckin(
			UpdateReviewOnCheckinRequest request) throws SnapdealWSException {
		request.setRequestProtocol(Protocol
				.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return (UpdateReviewOnCheckinResponse) transportService.executeRequest(
				getWebServiceBaseUrl() + CHECKIN_REVIEWS, request, headerMap,
				UpdateReviewOnCheckinResponse.class);
	}

	@Override
	public void setWebServiceBaseUrl(String webServiceBaseURL) {
		this.webServiceBaseUrl = webServiceBaseURL;
	}

	@Override
	public ModerationSummaryResponse getModerationConsoleSummary(
			Map<String, String> queryParams) throws SnapdealWSException {
		return (ModerationSummaryResponse) transportService.executeGetRequest(
				getWebServiceBaseUrl() + MODERATION_SUMMARY_BY_DATE,
				queryParams != null ? queryParams
						: new HashMap<String, String>(), headerMap,
				ModerationSummaryResponse.class);
	}

	@Override
	public GetModerationReviewsCountResponse getReviewsCountByStatus(
			Map<String, String> queryParams) throws SnapdealWSException {
		return (GetModerationReviewsCountResponse) transportService
				.executeGetRequest(getWebServiceBaseUrl() + REVIEWS_COUNT,
						queryParams != null ? queryParams
								: new HashMap<String, String>(), headerMap,
						GetModerationReviewsCountResponse.class);
	}

	@Override
	public GetModerationReviewsCountResponse getReviewsPendingForModerationCount()
			throws SnapdealWSException {
		return (GetModerationReviewsCountResponse) transportService
				.executeGetRequest(getWebServiceBaseUrl()
						+ REVIEWS_PENDING_FOR_MODERATION_COUNT,
						new HashMap<String, String>(), headerMap,
						GetModerationReviewsCountResponse.class);
	}

	@Override
	public GetCheckedOutCountResponse getCheckedOutReviewsCount(
			Map<String, String> queryParams) throws SnapdealWSException {
		return (GetCheckedOutCountResponse) transportService.executeGetRequest(
				getWebServiceBaseUrl() + CHECKED_OUT_COUNT,
				queryParams != null ? queryParams
						: new HashMap<String, String>(), headerMap,
				GetCheckedOutCountResponse.class);
	}

	public String getWebServiceBaseUrl() {
		return webServiceBaseUrl;
	}

	@Override
	public GetReviewListingPageForModerationResponse searchReviewsByAttributes(
			SearchReviewsRequest searchReviewsRequest)
			throws SnapdealWSException {
		searchReviewsRequest.setRequestProtocol(Protocol
				.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return (GetReviewListingPageForModerationResponse) transportService
				.executeRequest(getWebServiceBaseUrl()
						+ REVIEWS_SEARCH_MODERATION, searchReviewsRequest,
						headerMap,
						GetReviewListingPageForModerationResponse.class);
	}

}
