package com.snapdeal.reviews.api.rest.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.snapdeal.reviews.commons.UriConstants.Selfie;
import com.snapdeal.reviews.commons.config.ConfigurationType;
import com.snapdeal.reviews.commons.config.SelfieGeneralConfiguration;
import com.snapdeal.reviews.commons.config.SelfieNetworkConfiguration;
import com.snapdeal.reviews.commons.config.SelfieProductConfiguration;
import com.snapdeal.reviews.commons.config.SelfieSourceConfiguration;
import com.snapdeal.reviews.commons.dto.DeleteSelfieRequest;
import com.snapdeal.reviews.commons.dto.DeleteSelfieResponse;
import com.snapdeal.reviews.commons.dto.ModerationSelfie;
import com.snapdeal.reviews.commons.dto.ModerationSelfieListingPage;
import com.snapdeal.reviews.commons.dto.ReportSelfieResponse;
import com.snapdeal.reviews.commons.dto.SelfieModerationRequest;
import com.snapdeal.reviews.commons.dto.SelfieStatus;
import com.snapdeal.reviews.commons.dto.UploadGlobalSelfieRequest;
import com.snapdeal.reviews.commons.dto.UploadGlobalSelfieResponse;
import com.snapdeal.reviews.commons.dto.wrapper.CheckedInSelfieResponse;
import com.snapdeal.reviews.commons.dto.wrapper.CheckoutSelfieResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetSelfieListingPageForModerationResponse;
import com.snapdeal.reviews.commons.dto.wrapper.RangeQueryAttributeRequest;
import com.snapdeal.reviews.commons.dto.wrapper.SearchSelfiesRequest;
import com.snapdeal.reviews.commons.dto.wrapper.SelfieGeneralConfigurationResponse;
import com.snapdeal.reviews.commons.dto.wrapper.SelfieGeneralConfigurationResquest;
import com.snapdeal.reviews.commons.dto.wrapper.SelfieNetworkConfigurationRequest;
import com.snapdeal.reviews.commons.dto.wrapper.SelfieNetworkConfigurationResponse;
import com.snapdeal.reviews.commons.dto.wrapper.SelfieProductConfigurationRequest;
import com.snapdeal.reviews.commons.dto.wrapper.SelfieProductConfigurationResponse;
import com.snapdeal.reviews.commons.dto.wrapper.SelfieProductStatusResponse;
import com.snapdeal.reviews.commons.dto.wrapper.SelfieSearchSummaryResponse;
import com.snapdeal.reviews.commons.dto.wrapper.SelfieSourceConfigurationResponse;
import com.snapdeal.reviews.commons.dto.wrapper.SelfieSourceConfigurationResquest;
import com.snapdeal.reviews.commons.dto.wrapper.TermQueryAttributeRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateSelfieOnCheckinRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateSelfieOnCheckinResponse;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateSelfieOnCheckoutRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateSelfieOnCheckoutResponse;
import com.snapdeal.reviews.commons.pagination.Page;
import com.snapdeal.reviews.commons.pagination.SelfieNetworkFlag;
import com.snapdeal.reviews.commons.pagination.SelfieSourceType;
import com.snapdeal.reviews.mappers.OrikaMapper;
import com.snapdeal.reviews.model.IndexField;
import com.snapdeal.reviews.model.ModerationSelfieListingBo;
import com.snapdeal.reviews.model.RangeQueryAttribute;
import com.snapdeal.reviews.model.SelfieCheckoutBo;
import com.snapdeal.reviews.model.SelfieIndex;
import com.snapdeal.reviews.model.SelfieModerationBo;
import com.snapdeal.reviews.model.SelfieModerationSearchBo;
import com.snapdeal.reviews.model.TermQueryAttribute;
import com.snapdeal.reviews.service.ConfigurationService;
import com.snapdeal.reviews.service.SelfieModerationService;
import com.snapdeal.reviews.service.SelfieService;
import com.snapdeal.reviews.validator.ConfigurationInputValidator;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;

@Api(value = "selfie-admin", description = "Selfie Admin Settings")
@RestController
public class SelfieAdminController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(SelfieController.class);
	
	@Autowired
	private OrikaMapper mapper;

	@Autowired
	private SelfieService selfieService;

	@Autowired
	private SelfieModerationService selfieModerationService;

	
	@Autowired
	private ConfigurationInputValidator configurationInputValidator;

	@Autowired
	private ConfigurationService configurationService;

	@ApiOperation(value = "Set Network Configuration settings for selfie ")
	@RequestMapping(value = Selfie.SET_SELFIE_NETWORK_CONFIGURATION, method = RequestMethod.POST)
	public ResponseEntity<ServiceResponse> setSelfieNetworkConfiguration(
			@Valid @RequestBody SelfieNetworkConfigurationRequest request) {
		ConfigurationType configType = SelfieNetworkConfiguration.getNetworkConfigurationType(request.getNetworkType());
		SelfieNetworkConfiguration oldSelfieNetworkConfiguration = (SelfieNetworkConfiguration) configurationService
				.getConfiguration(configType);
		oldSelfieNetworkConfiguration.merge(request.selfieNetworkConfiguration);
		configurationService.setConfiguration(configType,
				oldSelfieNetworkConfiguration);
		ServiceResponse response = new ServiceResponse();
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Get Network Configuration settings for selfie")
	@RequestMapping(value = Selfie.GET_SELFIE_NETWORK_CONFIGURATION, method = RequestMethod.GET)
	public ResponseEntity<SelfieNetworkConfigurationResponse> getSelfieNetworkConfiguration(
			@RequestParam("networkType") SelfieNetworkFlag networkType) {
		SelfieNetworkConfigurationResponse response = new SelfieNetworkConfigurationResponse();
		SelfieNetworkConfiguration selfieNetworkConfiguration = (SelfieNetworkConfiguration) configurationService
				.getConfiguration(SelfieNetworkConfiguration.getNetworkConfigurationType(networkType));
		response.setSelfieNetworkConfiguration(selfieNetworkConfiguration);
		return new ResponseEntity<SelfieNetworkConfigurationResponse>(response,
				HttpStatus.OK);
	}

	@ApiOperation(value = "Set Source Configuration settings for selfie ")
	@RequestMapping(value = Selfie.SET_SELFIE_SOURCE_CONFIURATION, method = RequestMethod.POST)
	public ResponseEntity<ServiceResponse> setSelfieSourceConfiguration(
			@Valid @RequestBody SelfieSourceConfigurationResquest request) {
		ConfigurationType configType = SelfieSourceConfiguration.getSourceConfigurationType(request.getSourceType());
		SelfieSourceConfiguration oldSelfieSourceConfiguration = (SelfieSourceConfiguration) configurationService
				.getConfiguration(configType);
		oldSelfieSourceConfiguration.merge(request.selfieSourceConfiguration);
		configurationService.setConfiguration(configType,
				oldSelfieSourceConfiguration);
		ServiceResponse response = new ServiceResponse();
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Get Source Configuration settings for selfie")
	@RequestMapping(value = Selfie.GET_SELFIE_SOURCE_CONFIURATION, method = RequestMethod.GET)
	public ResponseEntity<SelfieSourceConfigurationResponse> getSelfieSourceConfiguration(
			@RequestParam("sourceType") SelfieSourceType sourceType) {
		SelfieSourceConfigurationResponse response = new SelfieSourceConfigurationResponse();
		SelfieSourceConfiguration selfieSourceConfiguration = (SelfieSourceConfiguration) configurationService
				.getConfiguration(SelfieSourceConfiguration.getSourceConfigurationType(sourceType));
		response.setSelfieSourceConfiguration(selfieSourceConfiguration);
		return new ResponseEntity<SelfieSourceConfigurationResponse>(response,
				HttpStatus.OK);
	}

	@ApiOperation(value = "Set General Configuration settings for selfie ")
	@RequestMapping(value = Selfie.SET_SELFIE_GENERAL_CONFIURATION, method = RequestMethod.POST)
	public ResponseEntity<ServiceResponse> setSelfieGeneralConfiguration(
			@Valid @RequestBody SelfieGeneralConfigurationResquest request) {
		ConfigurationType configType = ConfigurationType.SELFIE_CONFIG_GENERAL;
		SelfieGeneralConfiguration oldSelfieGeneralConfiguration = (SelfieGeneralConfiguration) configurationService
				.getConfiguration(configType);
		oldSelfieGeneralConfiguration.merge(request.selfieGeneralConfiguration);
		configurationService.setConfiguration(configType,
				oldSelfieGeneralConfiguration);
		ServiceResponse response = new ServiceResponse();
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Get General Configuration settings for selfie")
	@RequestMapping(value = Selfie.GET_SELFIE_GENERAL_CONFIURATION, method = RequestMethod.GET)
	public ResponseEntity<SelfieGeneralConfigurationResponse> getSelfieGeneralConfiguration() {
		SelfieGeneralConfigurationResponse response = new SelfieGeneralConfigurationResponse();
		SelfieGeneralConfiguration selfieGeneralConfiguration = (SelfieGeneralConfiguration) configurationService
				.getConfiguration(ConfigurationType.SELFIE_CONFIG_GENERAL);
		response.setSelfieGeneralConfiguration(selfieGeneralConfiguration);
		return new ResponseEntity<SelfieGeneralConfigurationResponse>(response,
				HttpStatus.OK);
	}

	@ApiOperation(value = "Set Product or Category Configuration settings for selfie ")
	@RequestMapping(value = Selfie.SET_SELFIE_PRODUCT_CONFIURATION, method = RequestMethod.POST)
	public ResponseEntity<ServiceResponse> setSelfieproductConfiguration(
			@Valid @RequestBody SelfieProductConfigurationRequest request) {
		ConfigurationType configType = ConfigurationType.SELFIE_CONFIG_PRODUCT;
		SelfieProductConfiguration oldSelfieProductConfiguration = (SelfieProductConfiguration) configurationService
				.getConfiguration(configType);
		oldSelfieProductConfiguration.updateProductObject(
				request.selfieProductConfiguration,
				request.selfieCategoryDisable, request.selfieProductDisable);
		configurationService.setConfiguration(configType,
				oldSelfieProductConfiguration);
		ServiceResponse response = new ServiceResponse();
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Get Product or Category Disabled List for selfie")
	@RequestMapping(value = Selfie.GET_SELFIE_PRODUCT_DISABLED_LIST, method = RequestMethod.GET)
	public ResponseEntity<SelfieProductConfigurationResponse> getSelfieProductListConfiguration() {
		SelfieProductConfigurationResponse response = new SelfieProductConfigurationResponse();
		SelfieProductConfiguration selfieProductConfiguration = (SelfieProductConfiguration) configurationService
				.getConfiguration(ConfigurationType.SELFIE_CONFIG_PRODUCT);
		response.setSelfieProductConfiguration(selfieProductConfiguration);
		return new ResponseEntity<SelfieProductConfigurationResponse>(response,
				HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get Selfie enabled for product")
	@RequestMapping(value = Selfie.GET_SELFIE_PRODUCT_DISABLED, method = RequestMethod.GET)
	public ResponseEntity<SelfieProductStatusResponse> getSelfieProductStatusConfiguration(
			@RequestParam(value = "productId", required = false) String productId,
			@RequestParam(value = "categoryId", required = false) String categoryId) {
		SelfieProductConfiguration selfieProductConfiguration =(SelfieProductConfiguration) configurationService
				.getConfiguration(ConfigurationType.SELFIE_CONFIG_PRODUCT);
		Boolean isSelfieEnabled = selfieProductConfiguration.isProductSelfieEnabled(productId, categoryId);
		SelfieProductStatusResponse response = new SelfieProductStatusResponse();
		response.setIsSelfieEnabled(isSelfieEnabled);
		return new ResponseEntity<SelfieProductStatusResponse>(response,
				HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get selfies by search parameters")
	@ApiResponse(code = 200, message = "Successfully retrieved search summary", response = SelfieSearchSummaryResponse.class)
	@RequestMapping(value = Selfie.SELFIES_SEARCH_MODERATION, method = RequestMethod.POST)
	public ResponseEntity<GetSelfieListingPageForModerationResponse> searchSelfiesByAttributes(
			@Valid @RequestBody SearchSelfiesRequest searchSelfiesRequest) {

		SelfieModerationSearchBo moderationSearchBo = createModerationSearchBo(searchSelfiesRequest);
		ModerationSelfieListingBo selfiesSearch = selfieModerationService.selfiesSearch(moderationSearchBo);

		List<SelfieIndex> selfieIndexes = selfiesSearch.getSelfieIndexes();

		ModerationSelfieListingPage moderationSelfieListingPage = new ModerationSelfieListingPage();
		/*
		 * selfieListingPage.setOffset(offset); selfieListingPage.setLimit(limit);
		 */
		moderationSelfieListingPage.setSize(selfieIndexes.size());
		moderationSelfieListingPage.setTotal(selfiesSearch.getTotal());
		moderationSelfieListingPage.setModerationSelfies(mapper.mapAsList(selfieIndexes, ModerationSelfie.class));
		GetSelfieListingPageForModerationResponse response = new GetSelfieListingPageForModerationResponse();
		response.setModerationSelfieListingPage(moderationSelfieListingPage);
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<GetSelfieListingPageForModerationResponse>(response, HttpStatus.OK);
	}
	
	SelfieModerationSearchBo createModerationSearchBo(SearchSelfiesRequest searchSelfiesRequest) {
		SelfieModerationSearchBo selfieModerationSearchBo = new SelfieModerationSearchBo();
		List<TermQueryAttributeRequest> termQueryAttributeRequests = searchSelfiesRequest
				.getTermQueryAttributeRequests();
		List<TermQueryAttribute> termQueryAttributes = new ArrayList<TermQueryAttribute>();
		if (termQueryAttributeRequests != null) {
			for (TermQueryAttributeRequest termQueryAttributeRequest : termQueryAttributeRequests) {
				TermQueryAttribute termQueryAttribute = new TermQueryAttribute();
				termQueryAttribute.setField(IndexField.getIndexFieldFromModerationSearchField(termQueryAttributeRequest
						.getSearchField()));
				termQueryAttribute.setValue(termQueryAttributeRequest.getValue());
				termQueryAttributes.add(termQueryAttribute);

			}
		}
		List<RangeQueryAttributeRequest> rangeQueryAttributeRequests = searchSelfiesRequest
				.getRangeQueryAttributeRequests();
		List<RangeQueryAttribute> rangeQueryAttributes = new ArrayList<RangeQueryAttribute>();
		if (rangeQueryAttributeRequests != null) {

			for (RangeQueryAttributeRequest rangeQueryAttributeRequest : rangeQueryAttributeRequests) {
				RangeQueryAttribute rangeQueryAttribute = new RangeQueryAttribute();
				rangeQueryAttribute.setField(IndexField
						.getIndexFieldFromModerationSearchField(rangeQueryAttributeRequest.getSearchField()));
				rangeQueryAttribute.setFrom(rangeQueryAttributeRequest.getFrom());
				rangeQueryAttribute.setTo(rangeQueryAttributeRequest.getTo());
				rangeQueryAttributes.add(rangeQueryAttribute);

			}
		}
		selfieModerationSearchBo.setTermQueryAttributes(termQueryAttributes);
		selfieModerationSearchBo.setRangeQueryAttributes(rangeQueryAttributes);
		selfieModerationSearchBo.setPage(searchSelfiesRequest.getPage());
		selfieModerationSearchBo.setModeratorId(searchSelfiesRequest.getModeratorId());

		return selfieModerationSearchBo;
	}
	
	@ApiOperation(value = "Checkout Selfies by selfieIds")
	@ApiResponse(code = 200, message = "Successfully Checked Out", response = UpdateSelfieOnCheckoutResponse.class)
	@RequestMapping(value = Selfie.CHECKOUT_SELFIES, method = RequestMethod.POST)
	public ResponseEntity<UpdateSelfieOnCheckoutResponse> updateSelfieOnCheckout(
			@Valid @RequestBody UpdateSelfieOnCheckoutRequest request) {

		// TODO Validate
		// validateCheckoutRequest(request);
		List<SelfieCheckoutBo> selfieCheckoutBos = new ArrayList<SelfieCheckoutBo>();
		for (String selfieId : request.getSelfieIds()) {
			SelfieCheckoutBo checkoutBo = new SelfieCheckoutBo(selfieId,
					request.getModeratorId());
			selfieCheckoutBos.add(checkoutBo);
		}
		CheckoutSelfieResponse checkoutSelfieResponse = selfieModerationService
				.checkoutSelfies(selfieCheckoutBos);
		UpdateSelfieOnCheckoutResponse response = new UpdateSelfieOnCheckoutResponse();
		response.setCheckoutSelfieResponse(checkoutSelfieResponse);
		response.setProtocol(Protocol.PROTOCOL_JSON);
		response.setCode(HttpStatus.OK.toString());
		return new ResponseEntity<UpdateSelfieOnCheckoutResponse>(response,
				HttpStatus.OK);
	}

	@ApiOperation(value = "Checkin Selfies by  selfieIds and status", consumes = "application/json")
	@ApiResponse(code = 200, message = "Successfully Checked In", response = UpdateSelfieOnCheckinResponse.class)
	@RequestMapping(value = Selfie.CHECKIN_SELFIES, method = RequestMethod.POST)
	public ResponseEntity<UpdateSelfieOnCheckinResponse> updateSelfieOnCheckin(
			@Valid @RequestBody UpdateSelfieOnCheckinRequest request) {

		// validator.validateCheckinRequest(request);
		List<SelfieModerationBo> selfieModerationBos = new ArrayList<SelfieModerationBo>();
		for (SelfieModerationRequest moderationRequest : request
				.getSelfieModerationRequest().getSelfieModerationRequests()) {

			SelfieModerationBo selfieModerationBo = mapper.map(
					moderationRequest, SelfieModerationBo.class);
			selfieModerationBo.setModeratorId(request.getModeratorId());

			/*
			 * for (SelfieStatusModerationValidator validator :
			 * selfieModerationValidators) { if
			 * (validator.getCheckInStatus().equals(selfieModerationBo.getStatus
			 * ())) { validator.validate(selfieModerationBo); } }
			 */

			selfieModerationBos.add(selfieModerationBo);
		}
		CheckedInSelfieResponse checkedInSelfieResponse = selfieModerationService
				.checkInSelfies(request.getModeratorId(), selfieModerationBos);
		UpdateSelfieOnCheckinResponse response = new UpdateSelfieOnCheckinResponse();
		response.setCheckedInSelfieResponse(checkedInSelfieResponse);
		response.setProtocol(Protocol.PROTOCOL_JSON);
		response.setCode(HttpStatus.OK.toString());
		return new ResponseEntity<UpdateSelfieOnCheckinResponse>(response,
				HttpStatus.OK);
	}
	
	@ApiOperation(value = "Delete a Selfi", position = 4, consumes = "application/json")
	@ApiResponse(code = 200, message = "Successfully marked as deleted", response = DeleteSelfieResponse.class)
	@RequestMapping(value = Selfie.DELETE_SELFI, method = RequestMethod.POST)
	public ResponseEntity<DeleteSelfieResponse> deleteSelfie(@Valid @RequestBody DeleteSelfieRequest request) {
		logger.info("Mark selfie for delete: " + request.getSelfieId());
		selfieService.deleteSelfie(request.getSelfieId());
		DeleteSelfieResponse response = new DeleteSelfieResponse();
		response.setDeleted(true);
		response.setSuccessful(true);
		response.setProtocol(Protocol.PROTOCOL_JSON);
		response.setCode(HttpStatus.OK.toString());/*
		 * , consumes = {MediaType . MULTIPART_FORM_DATA_VALUE ,
		 * "application/x-protobuf"}
		 */
		
		return new ResponseEntity<DeleteSelfieResponse>(response, null, HttpStatus.OK);
	}
	

	// Selfie Listing for Moderation Console
	@ApiOperation(value = "Retrieve selfies for moderation")
	@ApiResponse(code = 200, message = "Successfully retrieved selfies", response = GetSelfieListingPageForModerationResponse.class)
	@RequestMapping(value = Selfie.SELFIE_LISTING_MODERATION, method = RequestMethod.GET)
	public ResponseEntity<GetSelfieListingPageForModerationResponse> getSelfieListingModeration(
			@RequestParam(value = "status", defaultValue = "CREATED") SelfieStatus status,
			@RequestParam(value = "moderatorId", required = false) String moderatorId,
			@RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
			@RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit) {
		Page page = new Page(offset, limit);

		// validator.validatePagination(page);

		ModerationSelfieListingBo selfieListingBo = selfieModerationService.searchForModerationSelfie(status,
				moderatorId, page);

		List<SelfieIndex> selfieIndex = selfieListingBo.getSelfieIndexes();

		ModerationSelfieListingPage moderationSelfieListingPage = new ModerationSelfieListingPage();
		moderationSelfieListingPage.setOffset(offset);
		moderationSelfieListingPage.setLimit(limit);
		moderationSelfieListingPage.setSize(selfieIndex.size());
		moderationSelfieListingPage.setTotal(selfieListingBo.getTotal());
		moderationSelfieListingPage.setModerationSelfies(mapper.mapAsList(selfieIndex, ModerationSelfie.class));
		GetSelfieListingPageForModerationResponse response = new GetSelfieListingPageForModerationResponse();
		response.setModerationSelfieListingPage(moderationSelfieListingPage);
		response.setProtocol(Protocol.PROTOCOL_JSON);
		response.setCode(HttpStatus.OK.toString());
		return new ResponseEntity<GetSelfieListingPageForModerationResponse>(response, HttpStatus.OK);
	}
	
	// Approved Selfie Listing for Moderation Console
		@ApiOperation(value = "Retrieve selfies for Global moderation")
		@ApiResponse(code = 200, message = "Successfully retrieved Global selfies", response = GetSelfieListingPageForModerationResponse.class)
		@RequestMapping(value = Selfie.GLOBAL_SELFIE_LISTING_MODERATION, method = RequestMethod.GET)
		public ResponseEntity<GetSelfieListingPageForModerationResponse> getGlobalSelfieListingModeration(
				@RequestParam(value = "status", defaultValue = "APPROVED") SelfieStatus status,
				@RequestParam(value = "moderatorId", required = false) String moderatorId,
				@RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
				@RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit) {
			Page page = new Page(offset, limit);

			// validator.validatePagination(page);

			ModerationSelfieListingBo selfieListingBo = selfieModerationService.searchForGlobalModerationSelfie(status,
					moderatorId, page);

			List<SelfieIndex> selfieIndex = selfieListingBo.getSelfieIndexes();

			ModerationSelfieListingPage moderationSelfieListingPage = new ModerationSelfieListingPage();
			moderationSelfieListingPage.setOffset(offset);
			moderationSelfieListingPage.setLimit(limit);
			moderationSelfieListingPage.setSize(selfieIndex.size());
			moderationSelfieListingPage.setTotal(selfieListingBo.getTotal());
			moderationSelfieListingPage.setModerationSelfies(mapper.mapAsList(selfieIndex, ModerationSelfie.class));
			GetSelfieListingPageForModerationResponse response = new GetSelfieListingPageForModerationResponse();
			response.setModerationSelfieListingPage(moderationSelfieListingPage);
			response.setProtocol(Protocol.PROTOCOL_JSON);
			response.setCode(HttpStatus.OK.toString());
			return new ResponseEntity<GetSelfieListingPageForModerationResponse>(response, HttpStatus.OK);
		}
		
		@ApiOperation(value = "Upload Global selfie", position = 6, consumes = "application/json")
		@ApiResponse(code = 200, message = "Global Selfie Uploaded", response = ReportSelfieResponse.class)
		@RequestMapping(value = Selfie.UPLOAD_GLOBAL, method = RequestMethod.POST)
		public ResponseEntity<UploadGlobalSelfieResponse> uploadGlobalSelfies(
				@RequestBody UploadGlobalSelfieRequest request, UriComponentsBuilder uriBuilder) {
			boolean uploadResponse = selfieService.uploadGlobalSelfie(request.getSelfieIDs());
			UploadGlobalSelfieResponse response = new UploadGlobalSelfieResponse();
			response.setProtocol(Protocol.PROTOCOL_JSON);
			response.setSuccessful(uploadResponse);
			return new ResponseEntity<UploadGlobalSelfieResponse>(response, null, HttpStatus.OK);
		}

}
