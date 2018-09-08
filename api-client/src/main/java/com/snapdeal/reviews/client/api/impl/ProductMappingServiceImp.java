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
import com.snapdeal.reviews.client.api.ProductMappingService;
import com.snapdeal.reviews.client.base.lib.adapter.Constants;
import com.snapdeal.reviews.commons.UriConstants.MappingPath;
import com.snapdeal.reviews.commons.dto.wrapper.MappingCommonRequest;
import com.snapdeal.reviews.commons.dto.wrapper.MappingResponse;

@Service
public class ProductMappingServiceImp implements ProductMappingService {

	@Autowired
	private ITransportService transportService;

	private String webServiceBaseUrl;

	private Map<String, String> headerMap = new HashMap<>();

	@PostConstruct
	public void init() {
		headerMap.put("Accept", "application/" + Constants.CUSTOM_JSON_PROTOCOL.toLowerCase());
		headerMap.put("Content-Type", "application/" + Constants.CUSTOM_JSON_PROTOCOL.toLowerCase());
		transportService.registerService("/api/service/reviews/", "productPogIdMapping");
	}

	public String getWebServiceBaseUrl() {
		return webServiceBaseUrl;
	}

	@Override
	public void setWebServiceBaseUrl(String webServiceBaseURL) {
		this.webServiceBaseUrl = webServiceBaseURL;

	}
	@Override
	public MappingResponse addPogMappingOFParentAndChild(
			MappingCommonRequest request) throws SnapdealWSException {
		request.setRequestProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return (MappingResponse) transportService.executeRequest(getWebServiceBaseUrl()+ MappingPath.MAPPING, request, headerMap, MappingResponse.class);
	}

	@Override
	public MappingResponse getAllProductsMapping() throws SnapdealWSException {
		return (MappingResponse) transportService.executeGetRequest(getWebServiceBaseUrl()+ MappingPath.MAPPING, new HashMap<String, String>(), headerMap, MappingResponse.class);
		
	}

	@Override
	public ServiceResponse deleteMapping(MappingCommonRequest request)
			throws SnapdealWSException {
		request.setRequestProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return transportService.executeRequest(getWebServiceBaseUrl()+ MappingPath.DELETE_MAPPING, request, headerMap, ServiceResponse.class);
	}

}
