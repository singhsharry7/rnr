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
import com.snapdeal.reviews.client.api.IConfigurationClientService;
import com.snapdeal.reviews.client.base.lib.adapter.Constants;
import com.snapdeal.reviews.commons.UriConstants.Configuration;
import com.snapdeal.reviews.commons.dto.RejectionReasonResponse;
import com.snapdeal.reviews.commons.dto.ReportHierarchy;
import com.snapdeal.reviews.commons.dto.wrapper.CreateRejectionReasonResponse;
import com.snapdeal.reviews.commons.dto.wrapper.DeleteRejectionReasonRequest;
import com.snapdeal.reviews.commons.dto.wrapper.MailConfigrationRequest;
import com.snapdeal.reviews.commons.dto.wrapper.MailConfigrationResponse;
import com.snapdeal.reviews.commons.dto.wrapper.ProductConfigurationRequest;
import com.snapdeal.reviews.commons.dto.wrapper.ProductConfigurationResponse;
import com.snapdeal.reviews.commons.dto.wrapper.RejectionReasonsRequest;
import com.snapdeal.reviews.commons.dto.wrapper.ReviewConfigurationRequest;
import com.snapdeal.reviews.commons.dto.wrapper.ReviewConfigurationResponse;
import com.snapdeal.reviews.commons.dto.wrapper.ReviewQualityWeightageConfigurationRequest;
import com.snapdeal.reviews.commons.dto.wrapper.ReviewQualityWeightageConfigurationResponse;

@Service
public class ConfigurationClientServiceImpl implements IConfigurationClientService {

	@Autowired
	private ITransportService transportService;

	private String webServiceBaseUrl;

	private Map<String, String> headerMap = new HashMap<>();

	@PostConstruct
	public void init() {
		headerMap.put("Accept", "application/" + Constants.CUSTOM_JSON_PROTOCOL.toLowerCase());
		headerMap.put("Content-Type", "application/" + Constants.CUSTOM_JSON_PROTOCOL.toLowerCase());
		transportService.registerService("/api/service/reviews/configurations/", "reviewServiceConfigurations.");
	}

	public String getWebServiceBaseUrl() {
		return webServiceBaseUrl;
	}

	@Override
	public void setWebServiceBaseUrl(String webServiceBaseURL) {
		this.webServiceBaseUrl = webServiceBaseURL;

	}

	@Override
	public ReviewConfigurationResponse getReviewConfiguration() throws SnapdealWSException {
		return (ReviewConfigurationResponse) transportService.executeGetRequest(
				getWebServiceBaseUrl() + Configuration.GET_REVIEW_CONFIGURATION, new HashMap<String, String>(),
				headerMap, ReviewConfigurationResponse.class);
	}

	@Override
	public ServiceResponse setReviewConfiguration(ReviewConfigurationRequest request) throws SnapdealWSException {
		request.setRequestProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return (ServiceResponse) transportService.executeRequest(
				getWebServiceBaseUrl() + Configuration.SET_REVIEW_CONFIGURATION, request, headerMap,
				ServiceResponse.class);
	}

	@Override
	public ReviewQualityWeightageConfigurationResponse getReviewQualityWeightageConfiguration()
			throws SnapdealWSException {
		return (ReviewQualityWeightageConfigurationResponse) transportService.executeGetRequest(
				getWebServiceBaseUrl() + Configuration.GET_REVIEW_QUALITY_WEIGHTAGE_CONFIGURATION,
				new HashMap<String, String>(), headerMap, ReviewQualityWeightageConfigurationResponse.class);
	}

	@Override
	public ServiceResponse setReviewQualityWeightageConfiguration(ReviewQualityWeightageConfigurationRequest request)
			throws SnapdealWSException {
		request.setRequestProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return (ServiceResponse) transportService.executeRequest(
				getWebServiceBaseUrl() + Configuration.SET_REVIEW_QUALITY_WEIGHTAGE_CONFIGURATION, request, headerMap,
				ServiceResponse.class);
	}

	@Override
	public MailConfigrationResponse getMailConfiguration() throws SnapdealWSException {
		return (MailConfigrationResponse) transportService.executeGetRequest(
				getWebServiceBaseUrl() + Configuration.GET_MAIL_CONFIGURATION, new HashMap<String, String>(), headerMap,
				MailConfigrationResponse.class);
	}

	@Override
	public ServiceResponse setMailConfiguration(MailConfigrationRequest request) throws SnapdealWSException {
		request.setRequestProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return (ServiceResponse) transportService.executeRequest(
				getWebServiceBaseUrl() + Configuration.SET_MAIL_CONFIGURATION, request, headerMap,
				ServiceResponse.class);
	}

	public ReviewConfigurationResponse getDefaultReviewConfiguration() throws SnapdealWSException {
		return (ReviewConfigurationResponse) transportService.executeGetRequest(
				getWebServiceBaseUrl() + Configuration.GET_DEFAULT_REVIEW_CONFIGURATION, new HashMap<String, String>(),
				headerMap, ReviewConfigurationResponse.class);
	}

	@Override
	public ReviewQualityWeightageConfigurationResponse getDefaultQualityWeightageConfigurationResponse()
			throws SnapdealWSException {
		return (ReviewQualityWeightageConfigurationResponse) transportService.executeGetRequest(
				getWebServiceBaseUrl() + Configuration.GET_DEFAULT_REVIEW_QUALITY_WEIGHTAGE_CONFIGURATION,
				new HashMap<String, String>(), headerMap, ReviewQualityWeightageConfigurationResponse.class);
	}

	@Override
	public RejectionReasonResponse getRejectionReasons(Map<String, String> queryParams) throws SnapdealWSException {
		return (RejectionReasonResponse) transportService.executeGetRequest(
				getWebServiceBaseUrl() + Configuration.REJECTION_REASONS, queryParams, headerMap,
				RejectionReasonResponse.class);
	}

	@Override
	public CreateRejectionReasonResponse addRejectionReasons(RejectionReasonsRequest request)
			throws SnapdealWSException {
		request.setRequestProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return (CreateRejectionReasonResponse) transportService.executeRequest(
				getWebServiceBaseUrl() + Configuration.REJECTION_REASONS, request, headerMap,
				CreateRejectionReasonResponse.class);
	}

	@Override
	public ServiceResponse deleteRejectionReasons(DeleteRejectionReasonRequest request) throws SnapdealWSException {
		request.setRequestProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return (ServiceResponse) transportService.executeRequest(
				getWebServiceBaseUrl() + Configuration.DELETE_REJECTION_REASONS, request, headerMap,
				ServiceResponse.class);
	}

	@Override
	public MailConfigrationResponse getDefaultMailConfiguration() throws SnapdealWSException {
		return (MailConfigrationResponse) transportService.executeGetRequest(
				getWebServiceBaseUrl() + Configuration.GET_DEFAULT_MAIL_CONFIGURATION, new HashMap<String, String>(),
				headerMap, MailConfigrationResponse.class);
	}

	@Override
	public ReportHierarchy getReportHierarchy() throws SnapdealWSException {
		return (ReportHierarchy) transportService.executeGetRequest(
				getWebServiceBaseUrl() + Configuration.GET_REPORT_HIERARCHY, new HashMap<String, String>(), headerMap,
				ReportHierarchy.class);
	}

	@Override
	public ServiceResponse setProductConfiguration(ProductConfigurationRequest request) throws SnapdealWSException {
		request.setRequestProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return (ServiceResponse) transportService.executeRequest(getWebServiceBaseUrl() + Configuration.PRODUCT_CONFIG,
				request, headerMap, ServiceResponse.class);
	}

	@Override
	public ProductConfigurationResponse getProductConfigurationResponse(Map<String, String> queryParams)
			throws SnapdealWSException {
		return (ProductConfigurationResponse) transportService.executeGetRequest(
				getWebServiceBaseUrl() + Configuration.GET_PRODUCT_CONFIG, queryParams, headerMap,
				ProductConfigurationResponse.class);
	}

}
