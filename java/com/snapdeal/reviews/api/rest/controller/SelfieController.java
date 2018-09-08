package com.snapdeal.reviews.api.rest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.elasticsearch.common.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.snapdeal.base.transport.service.ITransportService.Protocol;
import com.snapdeal.reviews.commons.Constants;
import com.snapdeal.reviews.commons.UriConstants.Selfie;
import com.snapdeal.reviews.commons.dto.CreateSelfieRequest;
import com.snapdeal.reviews.commons.dto.GetSelfieResponse;
import com.snapdeal.reviews.commons.dto.ImageType;
import com.snapdeal.reviews.commons.dto.LikeSelfieRequest;
import com.snapdeal.reviews.commons.dto.LikeSelfieResponse;
import com.snapdeal.reviews.commons.dto.PlatformImageSize;
import com.snapdeal.reviews.commons.dto.ReportSelfieRequest;
import com.snapdeal.reviews.commons.dto.ReportSelfieResponse;
import com.snapdeal.reviews.commons.dto.SelfieResponse;
import com.snapdeal.reviews.commons.dto.wrapper.CreateReviewResponse;
import com.snapdeal.reviews.commons.dto.wrapper.CreateSelfieResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetSelfieListingPageResponse;
import com.snapdeal.reviews.commons.dto.wrapper.SelfieCloudinaryDeleteResponse;
import com.snapdeal.reviews.commons.dto.wrapper.SelfieImageCloudinaryDeleteResponse;
import com.snapdeal.reviews.commons.dto.wrapper.SelfieImageUploadResponse;
import com.snapdeal.reviews.commons.dto.wrapper.SelfieUploadResponse;
import com.snapdeal.reviews.commons.pagination.Page;
import com.snapdeal.reviews.commons.pagination.SelfieListingCriteria;
import com.snapdeal.reviews.commons.pagination.SelfieListingSortType;
import com.snapdeal.reviews.exception.ErrorCode;
import com.snapdeal.reviews.exception.client.ForbiddenException;
import com.snapdeal.reviews.mappers.OrikaMapper;
import com.snapdeal.reviews.model.ReviewableObjectBo;
import com.snapdeal.reviews.model.SelfieBo;
import com.snapdeal.reviews.model.SelfieBoResponse;
import com.snapdeal.reviews.model.SelfieListingBo;
import com.snapdeal.reviews.model.SelfieUserBo;
import com.snapdeal.reviews.service.SelfieService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;

@Api(value = "Selfie", description = "Selfie")
@RestController
public class SelfieController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(SelfieController.class);

	@Autowired
	private OrikaMapper mapper;

	@Autowired
	private SelfieService selfieService;

	
	@ApiOperation(value = "Create a Selfi", position = 3, consumes = "application/json")
	@ApiResponse(code = 200, message = "Successfully created a Selfi", response = CreateReviewResponse.class)
	@RequestMapping(value = Selfie.CREATE_SELFI, method = RequestMethod.POST
	/*
	 * , consumes = {MediaType . MULTIPART_FORM_DATA_VALUE ,
	 * "application/x-protobuf"}
	 */)
	public ResponseEntity<CreateSelfieResponse> createSelfie(@Valid @ModelAttribute CreateSelfieRequest request,
			@RequestParam("image") MultipartFile image) {
		logger.info("Creating selfie for user: " + request.getSelfiRequest().getUserId() + " for productId: "
				+ request.getSelfiRequest().getProductId()+" ImageType "+image.getContentType().toLowerCase());

		if(!selfieService.isImageFile(image))
		{
			logger.error("Create selfie failed for user: "+request.getSelfiRequest().getUserId()+" Error:"+ ErrorCode.INVALID_IMG_TYPE );
			throw new ForbiddenException(ErrorCode.INVALID_IMG_TYPE, "Invalid image");
		}
		String imageLink = selfieService.uploadSelfie(image);
		if (request.getSelfiRequest().getImageType() == null) {
			request.getSelfiRequest().setImageType(ImageType.SELFIE);
		}
		SelfieBo selfieBo = mapper.map(request.getSelfiRequest(), SelfieBo.class);
		selfieBo.setImage(imageLink);
		SelfieBo response = selfieService.createSelfie(selfieBo);
		SelfieResponse selfieResponse = new SelfieResponse(response.getId(), response.getCreatedAt().getTime());
		CreateSelfieResponse cResponse = new CreateSelfieResponse(selfieResponse);
		cResponse.setProtocol(Protocol.PROTOCOL_JSON);
		cResponse.setCode(HttpStatus.OK.toString());
		return new ResponseEntity<CreateSelfieResponse>(cResponse, null, HttpStatus.OK);
	}



	
	// Selfie Listing for Selfie Train
	@ApiOperation(value = "Retrieve selfies for an object", position = 4)
	@ApiResponse(code = 200, message = "Successfully retrieved selfies", response = GetSelfieListingPageResponse.class)
	@RequestMapping(value = Selfie.SELFIE_LISTING, method = RequestMethod.GET)
	public ResponseEntity<GetSelfieListingPageResponse> getSelfieListing(@RequestParam("productId") String productId,
			@RequestParam(value = "queryType", required = false, defaultValue = "RECENCY_DESC") SelfieListingSortType sortType,
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "deviceType", required = false) String deviceType,
			@RequestParam(value = "os", required = false) String os,
			@RequestParam(value = "network", required = false) String network,
			@RequestParam(value = "source", required = false) String source,

	@ApiParam(allowableValues = "range[0,infinity]") @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
			@ApiParam(allowableValues = "range[1,infinity]") @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit) {

		Page page = new Page(offset, limit);

		ReviewableObjectBo reviewableObjectBo = new ReviewableObjectBo(productId, Constants.DEFAULT_OBJECT_TYPE,
				Constants.DEFAULT_OBJECT_OWNER_ID);

		SelfieListingCriteria selfieListingCriteria = new SelfieListingCriteria();
		selfieListingCriteria.setUserId(userId);
		selfieListingCriteria.setPage(page);
		selfieListingCriteria.setSortType(sortType);
		logger.info("Listing all selfies for userId: " + userId + " with productId: " + productId + " device "
				+ deviceType + "network " + network + " os " + os + "source " + source);
		
		SelfieListingBo selfieListingBo = selfieService.search(reviewableObjectBo, selfieListingCriteria);

		List<SelfieBoResponse> selfieBosResponse = selfieListingBo.getSelfieBosResponse();
				
		GetSelfieListingPageResponse response = new GetSelfieListingPageResponse();
		response.setOffset(offset);
		response.setLimit(limit);
		response.setSize(selfieBosResponse.size());
		response.setCode(HttpStatus.OK.toString());
		response.setTotal(selfieListingBo.getTotal());
		response.setSelfies(mapper.mapAsList(selfieBosResponse, GetSelfieResponse.class));
		populateImageMap(response.getSelfies());
		if (StringUtils.isNotEmpty(userId)) {
			selfieService.getUserInfo(response.getSelfies(), userId);
		}
		// response.setSelfieListingPage(selfieListingPage);
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<GetSelfieListingPageResponse>(response, HttpStatus.OK);
	}

	private void populateImageMap(List<GetSelfieResponse> selfies) {
		if (selfies != null) {
			for (GetSelfieResponse selfie : selfies) {
				selfie.setImageMap(getImageMapFromImage(selfie.getImage()));
			}
		}
	}

	private Map<PlatformImageSize, String> getImageMapFromImage(String imageLink) {
		Map<PlatformImageSize, String> map = new HashMap<>();
		map.put(PlatformImageSize.SMALL, imageLink);
		map.put(PlatformImageSize.MEDIUM, imageLink);
		map.put(PlatformImageSize.LARGE, imageLink);
		return map;
	}

	@ApiOperation(value = "Retrieve selfies for an object", position = 4)
	@ApiResponse(code = 200, message = "Successfully retrieved selfies", response = GetSelfieListingPageResponse.class)
	@RequestMapping(value = Selfie.SELFIE_HOME_PAGE, method = RequestMethod.GET)
	public ResponseEntity<GetSelfieListingPageResponse> getSelfieHomePage(
			@RequestParam(value = "queryType", required = false, defaultValue = "RECENCY_DESC") SelfieListingSortType sortType,
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "deviceType", required = false) String deviceType,
			@RequestParam(value = "os", required = false) String os,
			@RequestParam(value = "network", required = false) String network,
			@RequestParam(value = "source", required = false) String source,

	@ApiParam(allowableValues = "range[0,infinity]") @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
			@ApiParam(allowableValues = "range[1,infinity]") @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit) {

		Page page = new Page(offset, limit);
				
		SelfieListingCriteria selfieListingCriteria = new SelfieListingCriteria();
		selfieListingCriteria.setUserId(userId);
		selfieListingCriteria.setPage(page);
		selfieListingCriteria.setSortType(sortType);
		logger.info("Listing Global selfies for userId: " + userId + " device " + deviceType + "network " + network
				+ " os " + os + "source " + source);
		SelfieListingBo selfieListingBo = selfieService.search(selfieListingCriteria);

		List<SelfieBoResponse> selfieBosResponse = selfieListingBo.getSelfieBosResponse();		
				
		GetSelfieListingPageResponse response = new GetSelfieListingPageResponse();
		response.setOffset(offset);
		response.setLimit(limit);
		response.setSize(selfieBosResponse.size());
		response.setCode(HttpStatus.OK.toString());
		response.setTotal(selfieListingBo.getTotal());
		response.setSelfies(mapper.mapAsList(selfieBosResponse, GetSelfieResponse.class));
		populateImageMap(response.getSelfies());
		if (StringUtils.isNotEmpty(userId)) {
			selfieService.getUserInfo(response.getSelfies(), userId);
		}
		// response.setSelfieListingPage(selfieListingPage);
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<GetSelfieListingPageResponse>(response, HttpStatus.OK);
	}

	/*@ApiOperation(value = "Upload Selfies")
	@ApiResponse(code = 200, message = "Successfully Uploaded", response = SelfieUploadResponse.class)
	@RequestMapping(value = Selfi.UPLOAD_SELFIE, method = RequestMethod.POST)*/
	public ResponseEntity<SelfieUploadResponse> uploadSelfieImage(@RequestParam("imageFile") MultipartFile file) {
		logger.info("Uploading Selfie");
		String imageLink = selfieService.uploadSelfie(file);
		SelfieImageUploadResponse selfieImageUploadResponse = new SelfieImageUploadResponse();
		Map<PlatformImageSize, String> imageMap = new HashMap<>();
		imageMap.put(PlatformImageSize.MEDIUM, imageLink);
		selfieImageUploadResponse.setImageToLinkMap(imageMap);
		SelfieUploadResponse response = new SelfieUploadResponse();
		response.setSelfieImageUploadResponse(selfieImageUploadResponse);
		return new ResponseEntity<SelfieUploadResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Like selfie", position = 5, consumes = "application/json")
	@ApiResponse(code = 200, message = "selfie liked/disliked", response = LikeSelfieResponse.class)
	@RequestMapping(value = Selfie.LIKE_SELFIE, method = RequestMethod.POST)
	public ResponseEntity<LikeSelfieResponse> likeSelfie(@RequestBody LikeSelfieRequest request,
			UriComponentsBuilder uriBuilder) {
		SelfieUserBo selfieuserBo = mapper.map(request, SelfieUserBo.class);
		selfieService.createSelfieUserFeedback(selfieuserBo, request.getSelfieId(), request.getLikeType());
		LikeSelfieResponse lResponse = new LikeSelfieResponse();
		lResponse.setCode(HttpStatus.OK.toString());
		lResponse.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<LikeSelfieResponse>(lResponse, null, HttpStatus.OK);
	}

	@ApiOperation(value = "Report selfie", position = 6, consumes = "application/json")
	@ApiResponse(code = 200, message = "selfie reported/unreported", response = ReportSelfieResponse.class)
	@RequestMapping(value = Selfie.REPORT_SELFIE, method = RequestMethod.POST)
	public ResponseEntity<ReportSelfieResponse> reportSelfie(@RequestBody ReportSelfieRequest request,
			UriComponentsBuilder uriBuilder) {
		SelfieUserBo selfieuserBo = mapper.map(request, SelfieUserBo.class);
		selfieService.createSelfieUserFeedback(selfieuserBo, request.getSelfieId(), request.getLikeType());
		ReportSelfieResponse lResponse = new ReportSelfieResponse();
		lResponse.setCode(HttpStatus.OK.toString());
		lResponse.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<ReportSelfieResponse>(lResponse, null, HttpStatus.OK);
	}

	
/*	@ApiOperation(value = "Delete Selfies from cloudinary")
	@ApiResponse(code = 200, message = "Successfully Deleted From Cloudinary", response = SelfieCloudinaryDeleteResponse.class)
	@RequestMapping(value = Selfi.DELETE_SELFIE_CLOUDINARY, method = RequestMethod.POST)*/
	public ResponseEntity<SelfieCloudinaryDeleteResponse> deleteSelfieImage(@RequestParam("imageurl") String url) {
		logger.info("Deleting Selfie");
		boolean deleteStatus = selfieService.deleteSelfieFromCloudinary(url);
		SelfieImageCloudinaryDeleteResponse selfieImageCloudinaryDeleteResponse = new SelfieImageCloudinaryDeleteResponse();
		selfieImageCloudinaryDeleteResponse.setDeleteStatus(deleteStatus);
		SelfieCloudinaryDeleteResponse response = new SelfieCloudinaryDeleteResponse();
		response.setSelfieImageCloudinaryDeleteResponse(selfieImageCloudinaryDeleteResponse);
		return new ResponseEntity<SelfieCloudinaryDeleteResponse>(response, HttpStatus.OK);

	}

}
