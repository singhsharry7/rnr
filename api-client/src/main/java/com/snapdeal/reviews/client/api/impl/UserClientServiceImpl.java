package com.snapdeal.reviews.client.api.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.base.model.common.ServiceResponse;
import com.snapdeal.base.transport.service.ITransportService;
import com.snapdeal.base.transport.service.ITransportService.Protocol;
import com.snapdeal.reviews.client.api.UserClientService;
import com.snapdeal.reviews.client.base.lib.adapter.Constants;
import com.snapdeal.reviews.commons.UriConstants.Mailers;
import com.snapdeal.reviews.commons.UriConstants.User;
import com.snapdeal.reviews.commons.dto.wrapper.MailSubscriptionRequest;
import com.snapdeal.reviews.commons.dto.wrapper.MailSubscriptionResponse;
import com.snapdeal.reviews.commons.dto.wrapper.NicknameResponse;
import com.snapdeal.reviews.commons.dto.wrapper.UserReviewStatsPageResponse;

@Service
public class UserClientServiceImpl implements UserClientService{
	
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

	public String getWebServiceBaseUrl() {
		return webServiceBaseUrl;
	}

	public void setWebServiceBaseUrl(String webServiceBaseUrl) {
		this.webServiceBaseUrl = webServiceBaseUrl;
	}
	
	@Override
	public ServiceResponse subscribeForMails(MailSubscriptionRequest request) throws SnapdealWSException {
		request.setRequestProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return transportService.executeRequest(getWebServiceBaseUrl()+ Mailers.SUBSCRIBE, request, headerMap, ServiceResponse.class);
	} 
	
	@Override
	public ServiceResponse unsubscribeFromMails(Map<String, String> queryParams) throws SnapdealWSException {
		return transportService.executeGetRequest(getWebServiceBaseUrl()+ Mailers.UNSUBSCRIBE, queryParams, headerMap, ServiceResponse.class);
	} 
	
	@Override
	
	public MailSubscriptionResponse hasSubscribed(Map<String, String> queryParams) throws SnapdealWSException {
		return (MailSubscriptionResponse) transportService.executeGetRequest(getWebServiceBaseUrl()+ Mailers.SUBSCRIPTION, queryParams, headerMap, MailSubscriptionResponse.class);
	}
	
	@Override
	public NicknameResponse getNickname(Map<String, String> queryParams) throws SnapdealWSException {
		return (NicknameResponse) transportService.executeGetRequest(getWebServiceBaseUrl()+ User.GET_NICKNAME, queryParams, headerMap, NicknameResponse.class);
	}
	
	@Override
	public UserReviewStatsPageResponse getUserReviewResponse(Map<String, String> queryParams) throws SnapdealWSException {
		return (UserReviewStatsPageResponse) transportService.executeGetRequest(getWebServiceBaseUrl()+ User.GET_USER_REVIEW_STATS, queryParams, headerMap, UserReviewStatsPageResponse.class);
	}

}
