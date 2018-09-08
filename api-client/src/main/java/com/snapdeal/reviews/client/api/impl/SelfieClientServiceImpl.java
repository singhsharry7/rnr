package com.snapdeal.reviews.client.api.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.base.transport.service.ITransportService;
import com.snapdeal.base.transport.service.ITransportService.Protocol;
import com.snapdeal.reviews.client.api.SelfieClientService;
import com.snapdeal.reviews.client.base.lib.adapter.Constants;
import com.snapdeal.reviews.commons.UriConstants;
import com.snapdeal.reviews.commons.dto.CreateSelfieRequest;
import com.snapdeal.reviews.commons.dto.DeleteSelfieRequest;
import com.snapdeal.reviews.commons.dto.DeleteSelfieResponse;
import com.snapdeal.reviews.commons.dto.LikeSelfieRequest;
import com.snapdeal.reviews.commons.dto.LikeSelfieResponse;
import com.snapdeal.reviews.commons.dto.ReportSelfieRequest;
import com.snapdeal.reviews.commons.dto.ReportSelfieResponse;
import com.snapdeal.reviews.commons.dto.wrapper.CreateSelfieResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetSelfieListingPageForModerationResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetSelfieListingPageResponse;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateSelfieOnCheckinRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateSelfieOnCheckinResponse;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateSelfieOnCheckoutRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateSelfieOnCheckoutResponse;

@Service
public class SelfieClientServiceImpl implements SelfieClientService {

	@Autowired
	private ITransportService transportService;

	private String webServiceBaseUrl;

	private Map<String, String> headerMap = new HashMap<>();

	@PostConstruct
	public void init() {
		headerMap.put("Accept", "application/" + Constants.CUSTOM_JSON_PROTOCOL.toLowerCase());
		headerMap.put("Content-Type", "application/" + Constants.CUSTOM_JSON_PROTOCOL.toLowerCase());
		transportService.registerService("/api/service/selfies/", "selfieService.");
	}

	public String getWebServiceBaseUrl() {
		return webServiceBaseUrl;
	}

	public void setWebServiceBaseUrl(String webServiceBaseUrl) {
		this.webServiceBaseUrl = webServiceBaseUrl;
	}

	@Override
	public GetSelfieListingPageResponse getSelfieList(Map<String, String> queryParams) throws SnapdealWSException {
		return (GetSelfieListingPageResponse) transportService.executeGetRequest(
				getWebServiceBaseUrl() + UriConstants.Selfie.SELFIE_LISTING,
				queryParams != null ? queryParams : new HashMap<String, String>(), headerMap,
				GetSelfieListingPageResponse.class);
	}

	@Override
	public UpdateSelfieOnCheckinResponse updateSelfieOnCheckin(UpdateSelfieOnCheckinRequest request)
			throws SnapdealWSException {
		request.setRequestProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		request.setResponseProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return (UpdateSelfieOnCheckinResponse) transportService.executeRequest(
				getWebServiceBaseUrl() + UriConstants.Selfie.CHECKIN_SELFIES, request, headerMap,
				UpdateSelfieOnCheckinResponse.class);
	}

	@Override
	public UpdateSelfieOnCheckoutResponse updateSelfieOnCheckout(UpdateSelfieOnCheckoutRequest request)
			throws SnapdealWSException {
		request.setRequestProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		request.setResponseProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return (UpdateSelfieOnCheckoutResponse) transportService.executeRequest(
				getWebServiceBaseUrl() + UriConstants.Selfie.CHECKOUT_SELFIES, request, headerMap,
				UpdateSelfieOnCheckoutResponse.class);
	}

	@Override
	public GetSelfieListingPageForModerationResponse getSelfieListModeration(Map<String, String> queryParams)
			throws SnapdealWSException {

		return (GetSelfieListingPageForModerationResponse) transportService.executeGetRequest(
				getWebServiceBaseUrl() + UriConstants.Selfie.SELFIE_LISTING_MODERATION,
				queryParams != null ? queryParams : new HashMap<String, String>(), headerMap,
				GetSelfieListingPageForModerationResponse.class);

	}

	@Override
	public DeleteSelfieResponse deleteSelfie(DeleteSelfieRequest request) throws SnapdealWSException {
		request.setRequestProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		request.setResponseProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return (DeleteSelfieResponse) transportService.executeRequest(
				getWebServiceBaseUrl() + UriConstants.Selfie.DELETE_SELFI, request, headerMap,
				DeleteSelfieResponse.class);
	}

	@Override
	public LikeSelfieResponse likeSelfie(LikeSelfieRequest request) throws SnapdealWSException {
		request.setRequestProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		request.setResponseProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return (LikeSelfieResponse) transportService.executeRequest(getWebServiceBaseUrl() + UriConstants.Selfie.LIKE_SELFIE, request, headerMap,
				LikeSelfieResponse.class);
	}

	@Override
	public ReportSelfieResponse reportSefie(ReportSelfieRequest request) throws SnapdealWSException {
		request.setRequestProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		request.setResponseProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return (ReportSelfieResponse) transportService.executeRequest(getWebServiceBaseUrl() + UriConstants.Selfie.REPORT_SELFIE, request, headerMap,
				ReportSelfieResponse.class);
	}
}
