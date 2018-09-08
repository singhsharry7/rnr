
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
import com.snapdeal.reviews.client.api.ProfanityClientService;
import com.snapdeal.reviews.client.base.lib.adapter.Constants;
import com.snapdeal.reviews.commons.UriConstants.Profanity;
import com.snapdeal.reviews.commons.dto.wrapper.ProfaneWordsResponse;
import com.snapdeal.reviews.commons.dto.wrapper.ProfanityCheckRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateProfaneWordsRequest;

@Service
public class ProfanityClientServiceImpl implements ProfanityClientService{
	
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
	public ServiceResponse addWords(UpdateProfaneWordsRequest request) throws SnapdealWSException {
		request.setRequestProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return transportService.executeRequest(getWebServiceBaseUrl()+ Profanity.ADD_WORDS, request, headerMap, ServiceResponse.class);
	}

	@Override
	public ServiceResponse deleteWords(UpdateProfaneWordsRequest request) throws SnapdealWSException {
		request.setRequestProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return transportService.executeRequest(getWebServiceBaseUrl()+ Profanity.DELETE_WORDS, request, headerMap, ServiceResponse.class);
	}

	@Override
	public ProfaneWordsResponse getAllWords() throws SnapdealWSException {
		return (ProfaneWordsResponse) transportService.executeGetRequest(getWebServiceBaseUrl()+ Profanity.GET_ALL_WORDS, new HashMap<String, String>(), headerMap, ProfaneWordsResponse.class);
	}

	@Override
	public ProfaneWordsResponse getAllWordsForModeration() throws SnapdealWSException {
		return (ProfaneWordsResponse) transportService.executeGetRequest(getWebServiceBaseUrl()+ Profanity.GET_ALL_WORDS_MODERATION, new HashMap<String, String>(), headerMap, ProfaneWordsResponse.class);
	}

	@Override
	public ProfaneWordsResponse checkProfanityForModeration(ProfanityCheckRequest request) throws SnapdealWSException {
		request.setRequestProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return (ProfaneWordsResponse) transportService.executeRequest(getWebServiceBaseUrl()+ Profanity.CHECK_PROFANITY_MODERATION, request, headerMap, ProfaneWordsResponse.class);
	}

	@Override
	public ProfaneWordsResponse checkProfanity(ProfanityCheckRequest request) throws SnapdealWSException {
		request.setRequestProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return (ProfaneWordsResponse) transportService.executeRequest(getWebServiceBaseUrl()+ Profanity.CHECK_PROFANITY, request, headerMap, ProfaneWordsResponse.class);
	}

}
