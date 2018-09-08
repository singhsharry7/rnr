package com.snapdeal.reviews.client.api;

import java.util.Map;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.base.model.common.ServiceResponse;
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

public interface IConfigurationClientService extends ReviewWebClientService {

	ReviewConfigurationResponse getReviewConfiguration() throws SnapdealWSException;
	
	ServiceResponse setReviewConfiguration(ReviewConfigurationRequest request) throws SnapdealWSException;

	ReviewConfigurationResponse getDefaultReviewConfiguration() throws SnapdealWSException;
	
	ReviewQualityWeightageConfigurationResponse getReviewQualityWeightageConfiguration() throws SnapdealWSException;
	
	ServiceResponse setReviewQualityWeightageConfiguration(ReviewQualityWeightageConfigurationRequest request) throws  SnapdealWSException;
	
	MailConfigrationResponse  getMailConfiguration() throws SnapdealWSException;
	
	MailConfigrationResponse getDefaultMailConfiguration() throws SnapdealWSException;
	
	ServiceResponse setMailConfiguration(MailConfigrationRequest request) throws  SnapdealWSException;
	
	ReviewQualityWeightageConfigurationResponse getDefaultQualityWeightageConfigurationResponse() throws SnapdealWSException;

	RejectionReasonResponse getRejectionReasons(Map<String, String> queryParams) throws SnapdealWSException;

	CreateRejectionReasonResponse addRejectionReasons(RejectionReasonsRequest request)
			throws SnapdealWSException;

	ServiceResponse deleteRejectionReasons(DeleteRejectionReasonRequest request)
			throws SnapdealWSException;
	
	ReportHierarchy getReportHierarchy() throws SnapdealWSException;
	
	
	ServiceResponse setProductConfiguration(ProductConfigurationRequest request)throws SnapdealWSException;

	ProductConfigurationResponse getProductConfigurationResponse(Map<String, String> queryParams)
			throws SnapdealWSException;
	
}
