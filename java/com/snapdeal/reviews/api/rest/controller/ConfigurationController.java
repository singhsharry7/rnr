package com.snapdeal.reviews.api.rest.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snapdeal.base.model.common.ServiceResponse;
import com.snapdeal.base.transport.service.ITransportService.Protocol;
import com.snapdeal.reviews.commons.UriConstants.Configuration;
import com.snapdeal.reviews.commons.config.ConfigurationType;
import com.snapdeal.reviews.commons.config.MailConfiguration;
import com.snapdeal.reviews.commons.config.ProductConfiguration;
import com.snapdeal.reviews.commons.config.RejectionReasonConfiguration;
import com.snapdeal.reviews.commons.config.RejectionReasonWrapper;
import com.snapdeal.reviews.commons.config.ReportHierarchyConfiguration;
import com.snapdeal.reviews.commons.config.ReviewConfiguration;
import com.snapdeal.reviews.commons.config.ReviewQualityWeightageConfiguration;
import com.snapdeal.reviews.commons.config.SelfieConfiguration;
import com.snapdeal.reviews.commons.config.SelfieGeneralConfiguration;
import com.snapdeal.reviews.commons.config.SelfieGlobalConfiguration;
import com.snapdeal.reviews.commons.config.SelfieNetworkConfiguration;
import com.snapdeal.reviews.commons.config.SelfieProductConfiguration;
import com.snapdeal.reviews.commons.config.SelfieSourceConfiguration;
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
import com.snapdeal.reviews.commons.dto.wrapper.SelfieConfigurationResponse;
import com.snapdeal.reviews.commons.dto.wrapper.SelfieGlobalConfigurationRequest;
import com.snapdeal.reviews.commons.dto.wrapper.SelfieGlobalConfigurationResponse;
import com.snapdeal.reviews.commons.pagination.SelfieNetworkFlag;
import com.snapdeal.reviews.commons.pagination.SelfieSourceType;
import com.snapdeal.reviews.service.ConfigurationService;
import com.snapdeal.reviews.validator.ConfigurationInputValidator;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Configuration-Settings", description = "Default Configurations Settings")
public class ConfigurationController extends BaseController {

	@Autowired
	private ConfigurationInputValidator configurationInputValidator;

	@Autowired
	private ConfigurationService configurationService;

	@ApiOperation(value = "Set Configuration settings for reviews")
	@RequestMapping(value = Configuration.SET_REVIEW_CONFIGURATION, method = RequestMethod.POST)
	public ResponseEntity<ServiceResponse> setReviewConfigurationSettings(
			@Valid @RequestBody ReviewConfigurationRequest request) {
		ReviewConfiguration oldReviewConfiguration = (ReviewConfiguration) configurationService
				.getConfiguration(ConfigurationType.REVIEW_CONFIG);
		oldReviewConfiguration.merge(request.reviewConfiguration);
		configurationService.setConfiguration(ConfigurationType.REVIEW_CONFIG,
				oldReviewConfiguration);
		ServiceResponse response = new ServiceResponse();
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Get Configuration settings for reviews")
	@RequestMapping(value = Configuration.GET_REVIEW_CONFIGURATION, method = RequestMethod.GET)
	public ResponseEntity<ReviewConfigurationResponse> getReviewConfiguration() {
		ReviewConfigurationResponse response = new ReviewConfigurationResponse();
		ReviewConfiguration reviewConfiguration = configurationService
				.getConfiguration(ConfigurationType.REVIEW_CONFIG);
		response.setReviewConfiguration(reviewConfiguration);
		return new ResponseEntity<ReviewConfigurationResponse>(response,
				HttpStatus.OK);
	}
	
	@ApiOperation(value="Set Turn on or off Product Reviews")
	@RequestMapping(value=Configuration.PRODUCT_CONFIG,method=RequestMethod.POST)
	public ResponseEntity<ServiceResponse> setProductConfiguration(@Valid @RequestBody ProductConfigurationRequest request){
		configurationService.setConfiguration(request.getProductConfiguration());
		ServiceResponse response = new ServiceResponse();
		response.setProtocol(Protocol.PROTOCOL_JSON);
		response.setCode(HttpStatus.OK.toString());
		return new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value="Get Turn on or off Product Reviews")
	@RequestMapping(value=Configuration.GET_PRODUCT_CONFIG,method=RequestMethod.GET)
	public ResponseEntity<ProductConfigurationResponse>getProductConfiguration(@RequestParam String productId){
		ProductConfigurationResponse response=new ProductConfigurationResponse();
		//response.setProductConfiguration(productConfiguration);
		ProductConfiguration productConfiguration=new ProductConfiguration();
		productConfiguration.setProductId(productId);
		productConfiguration.setProductWriteFlowDisabled(configurationService.getProductConfiguration(productId));
		response.setProductConfiguration(productConfiguration);
		response.setCode(HttpStatus.OK.toString());
		return new ResponseEntity<ProductConfigurationResponse>(response,HttpStatus.OK);
	
	}
	
	@ApiOperation(value = "Get Default Configuration settings for reviews")
	@RequestMapping(value = Configuration.GET_DEFAULT_REVIEW_CONFIGURATION, method = RequestMethod.GET)
	public ResponseEntity<ReviewConfigurationResponse> getDefaultReviewConfiguration() {
		ReviewConfigurationResponse response = new ReviewConfigurationResponse();
		ReviewConfiguration reviewConfiguration = configurationService
				.getDefaultConfiguration(ConfigurationType.REVIEW_CONFIG);
		response.setReviewConfiguration(reviewConfiguration);
		return new ResponseEntity<ReviewConfigurationResponse>(response,
				HttpStatus.OK);
	}

	@ApiOperation(value = "Set Quality Weightage Configuration settings for reviews")
	@RequestMapping(value = Configuration.SET_REVIEW_QUALITY_WEIGHTAGE_CONFIGURATION, method = RequestMethod.POST)
	public ResponseEntity<ServiceResponse> setReviewQualityWeightageConfiguration(
			@Valid @RequestBody ReviewQualityWeightageConfigurationRequest request) {
		configurationInputValidator.validate(request
				.getReviewQualityWeightageConfiguration());
		configurationService.setConfiguration(
				ConfigurationType.REVIEW_QUALITY_WEIGHTAGE_CONFIG,
				request.getReviewQualityWeightageConfiguration());
		ServiceResponse response = new ServiceResponse();

		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Get Quality Weightage Configuration settings for reviews")
	@RequestMapping(value = Configuration.GET_REVIEW_QUALITY_WEIGHTAGE_CONFIGURATION, method = RequestMethod.GET)
	public ResponseEntity<ReviewQualityWeightageConfigurationResponse> getReviewQualityWeightageConfiguration() {
		ReviewQualityWeightageConfigurationResponse response = new ReviewQualityWeightageConfigurationResponse();
		ReviewQualityWeightageConfiguration reviewQualityWeightageConfiguration = configurationService
				.getConfiguration(ConfigurationType.REVIEW_QUALITY_WEIGHTAGE_CONFIG);
		response.setReviewQualityWeightageConfiguration(reviewQualityWeightageConfiguration);
		return new ResponseEntity<ReviewQualityWeightageConfigurationResponse>(
				response, HttpStatus.OK);
	}

	@ApiOperation(value = "Get Default Quality Weightage Configuration settings for reviews")
	@RequestMapping(value = Configuration.GET_DEFAULT_REVIEW_QUALITY_WEIGHTAGE_CONFIGURATION, method = RequestMethod.GET)
	public ResponseEntity<ReviewQualityWeightageConfigurationResponse> getDefaultReviewQualityWeightageConfiguration() {
		ReviewQualityWeightageConfigurationResponse response = new ReviewQualityWeightageConfigurationResponse();
		ReviewQualityWeightageConfiguration reviewQualityWeightageConfiguration = configurationService
				.getDefaultConfiguration(ConfigurationType.REVIEW_QUALITY_WEIGHTAGE_CONFIG);
		response.setReviewQualityWeightageConfiguration(reviewQualityWeightageConfiguration);
		return new ResponseEntity<ReviewQualityWeightageConfigurationResponse>(
				response, HttpStatus.OK);
	}

	@ApiOperation(value = "Get Configuration settings for mail")
	@RequestMapping(value = Configuration.GET_MAIL_CONFIGURATION, method = RequestMethod.GET)
	public ResponseEntity<MailConfigrationResponse> getMailConfigration() {
		MailConfigrationResponse response = new MailConfigrationResponse();
		MailConfiguration mailConfigration = configurationService
				.getConfiguration(ConfigurationType.MAIL_CONFIG);
		response.setMailConfiguration(mailConfigration);
		return new ResponseEntity<MailConfigrationResponse>(response,
				HttpStatus.OK);

	}

	@ApiOperation(value = "Set mail Configuration settings for reviews")
	@RequestMapping(value = Configuration.SET_MAIL_CONFIGURATION, method = RequestMethod.POST)
	public ResponseEntity<ServiceResponse> setMailConfigration(
			@Valid @RequestBody MailConfigrationRequest request) {
		MailConfiguration oldMailConfiguration = (MailConfiguration) configurationService
				.getConfiguration(ConfigurationType.MAIL_CONFIG);
		oldMailConfiguration.mergeMailConfigration(request
				.getMailConfiguration());
		configurationInputValidator.validate(oldMailConfiguration);
		configurationService.setConfiguration(ConfigurationType.MAIL_CONFIG,
				oldMailConfiguration);
		ServiceResponse response = new ServiceResponse();
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Get Default Configuration settings for mail")
	@RequestMapping(value = Configuration.GET_DEFAULT_MAIL_CONFIGURATION, method = RequestMethod.GET)
	public ResponseEntity<MailConfigrationResponse> getDefaultMailConfigration() {
		MailConfigrationResponse response = new MailConfigrationResponse();
		MailConfiguration mailConfiguration = configurationService
				.getDefaultConfiguration(ConfigurationType.MAIL_CONFIG);
		response.setMailConfiguration(mailConfiguration);
		return new ResponseEntity<MailConfigrationResponse>(response,
				HttpStatus.OK);
	}

	@ApiOperation(value = "Get Rejection Reasons")
	@RequestMapping(value = Configuration.REJECTION_REASONS, method = RequestMethod.GET)
	public ResponseEntity<RejectionReasonResponse> getRejectionReasons(
			@RequestParam(value = "is_customer_facing", required = false) Boolean isCustomerFacing) {
		RejectionReasonResponse response = new RejectionReasonResponse();
		RejectionReasonConfiguration rejectionReasonConfig = configurationService
				.getConfiguration(ConfigurationType.REJECTION_REASON_CONFIG);
		if (null != rejectionReasonConfig)
			response.setRejectionReasons(rejectionReasonConfig
					.getRejectionReasons(isCustomerFacing));
		else
			response.setRejectionReasons(null);
		return new ResponseEntity<RejectionReasonResponse>(response,
				HttpStatus.OK);
	}

	@ApiOperation(value = "Add Rejection Reasons")
	@RequestMapping(value = Configuration.REJECTION_REASONS, method = RequestMethod.POST)
	public ResponseEntity<CreateRejectionReasonResponse> addRejectionReasons(
			@RequestBody @Valid RejectionReasonsRequest request) {
		RejectionReasonConfiguration rejectionReasonConfiguration = configurationService
				.getConfiguration(ConfigurationType.REJECTION_REASON_CONFIG);
		if (null == rejectionReasonConfiguration) {
			rejectionReasonConfiguration = new RejectionReasonConfiguration();
		}

		request.getRejectionReasonRequest()
				.setRejectionReason(
						request.getRejectionReasonRequest()
								.getRejectionReason().trim());
		configurationInputValidator.checkRejectionReasonIfexists(request
				.getRejectionReasonRequest().getRejectionReason());
		RejectionReasonWrapper rejectionReason = rejectionReasonConfiguration
				.addRejectionReason(request.getRejectionReasonRequest(), true);
		configurationService.setConfiguration(
				ConfigurationType.REJECTION_REASON_CONFIG,
				rejectionReasonConfiguration);

		CreateRejectionReasonResponse response = new CreateRejectionReasonResponse();
		response.setRejectionReason(rejectionReason);
		return new ResponseEntity<CreateRejectionReasonResponse>(response,
				HttpStatus.OK);
	}

	@ApiOperation(value = "Delete Rejection Reasons")
	@RequestMapping(value = Configuration.DELETE_REJECTION_REASONS, method = RequestMethod.POST)
	public ResponseEntity<ServiceResponse> deleteRejectionReasons(
			@RequestBody @Valid DeleteRejectionReasonRequest request) {
		configurationInputValidator.validateRejectionReasonUUID(request
				.getDeleteRejectionReason());
		RejectionReasonConfiguration rejectionReasonConfiguration = configurationService
				.getConfiguration(ConfigurationType.REJECTION_REASON_CONFIG);
		if (null == rejectionReasonConfiguration) {
			rejectionReasonConfiguration = new RejectionReasonConfiguration();
		}
		rejectionReasonConfiguration.deleteRejectionReason(request
				.getDeleteRejectionReason().getRejectionReasonUUID());
		configurationService.setConfiguration(
				ConfigurationType.REJECTION_REASON_CONFIG,
				rejectionReasonConfiguration);

		ServiceResponse response = new ServiceResponse();
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Get hierarchy for reports.")
	@RequestMapping(value = Configuration.GET_REPORT_HIERARCHY, method = RequestMethod.GET)
	public ResponseEntity<ReportHierarchy> getReportHierarchy() {
		ReportHierarchy response = new ReportHierarchy();
		ReportHierarchyConfiguration model = configurationService
				.getReportHierarchy(ConfigurationType.REPORT_HIERARCHY_CONFIG);
		response.setReportHierarchyConfiguration(model);
		response.setCode(HttpStatus.OK.toString());
		return new ResponseEntity<ReportHierarchy>(response, HttpStatus.OK);
	}
	

	@ApiOperation(value = "Get Configuration settings for selfie")
	@RequestMapping(value = Configuration.GET_SELFIE_CONFIGURATION, method = RequestMethod.GET)
	public ResponseEntity<SelfieConfigurationResponse> getSelfieConfiguration(
			@RequestParam("sourceType") SelfieSourceType sourceType,
			@RequestParam("networkType") SelfieNetworkFlag networkType,
			@RequestParam(value = "productId", defaultValue = "home") String productId,
			@RequestParam(value = "categoryId", defaultValue = "home") String categoryId) {
		SelfieConfigurationResponse response = new SelfieConfigurationResponse();
		SelfieGeneralConfiguration selfieGeneralConfiguration = (SelfieGeneralConfiguration) configurationService
				.getConfiguration(ConfigurationType.SELFIE_CONFIG_GENERAL);
		SelfieNetworkConfiguration selfieNetworkConfiguration = (SelfieNetworkConfiguration) configurationService
				.getConfiguration(SelfieNetworkConfiguration.getNetworkConfigurationType(networkType));
		SelfieSourceConfiguration selfieSourceConfiguration = (SelfieSourceConfiguration) configurationService
				.getConfiguration(SelfieSourceConfiguration.getSourceConfigurationType(sourceType));
		SelfieProductConfiguration selfieProductConfiguration = (SelfieProductConfiguration) configurationService
				.getConfiguration(ConfigurationType.SELFIE_CONFIG_PRODUCT);
		Boolean porductCategoryFlag = selfieProductConfiguration
				.isProductSelfieEnabled(productId, categoryId);
		SelfieConfiguration selfieConfiguration = new SelfieConfiguration();
		selfieConfiguration.merge(selfieGeneralConfiguration,
				selfieSourceConfiguration, selfieNetworkConfiguration,
				porductCategoryFlag);
		response.setSelfieConfiguration(selfieConfiguration);
		return new ResponseEntity<SelfieConfigurationResponse>(response,
				HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get Global Configuration settings for selfie")
	@RequestMapping(value = Configuration.GET_GLOBAL_SELFIE_CONFIGURATION, method = RequestMethod.GET)
	public ResponseEntity<SelfieGlobalConfigurationResponse> getGlobalConfiguration(){
		SelfieGlobalConfigurationResponse response = new SelfieGlobalConfigurationResponse();
		SelfieGlobalConfiguration selfieGlobalConfiguration = (SelfieGlobalConfiguration)configurationService
				.getConfiguration(ConfigurationType.SELFIE_CONFIG_GLOBAL);
		SelfieProductConfiguration selfieProductConfiguration = (SelfieProductConfiguration) configurationService
				.getConfiguration(ConfigurationType.SELFIE_CONFIG_PRODUCT);
		response.setSelfieGlobalConfiguration(selfieGlobalConfiguration);
		response.setSelfieCategoryDisableList(selfieProductConfiguration.getSelfieCategoryDisableList());
		return new ResponseEntity<SelfieGlobalConfigurationResponse>(response,
				HttpStatus.OK);
	}
	
	@ApiOperation(value = "Set Global Configuration settings for selfie")
	@RequestMapping(value = Configuration.GET_GLOBAL_SELFIE_CONFIGURATION, method = RequestMethod.POST)
	public ResponseEntity<ServiceResponse> setGlobalConfiguration(
			@Valid @RequestBody SelfieGlobalConfigurationRequest request){
		SelfieGlobalConfiguration selfieGlobalConfiguration = request.getSelfieGlobalConfiguration();
		SelfieGlobalConfiguration oldSelfieGlobalConfiguration = (SelfieGlobalConfiguration)configurationService.
				getConfiguration(ConfigurationType.SELFIE_CONFIG_GLOBAL);
		oldSelfieGlobalConfiguration.merge(selfieGlobalConfiguration);
		configurationService.setConfiguration(ConfigurationType.SELFIE_CONFIG_GLOBAL, oldSelfieGlobalConfiguration);
		ServiceResponse response = new ServiceResponse();
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);
		
	}
}
