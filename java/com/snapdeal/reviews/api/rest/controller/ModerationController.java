package com.snapdeal.reviews.api.rest.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snapdeal.base.transport.service.ITransportService.Protocol;
import com.snapdeal.reviews.commons.UriConstants;
import com.snapdeal.reviews.commons.config.ReviewConfiguration;
import com.snapdeal.reviews.commons.dto.ModerationReview;
import com.snapdeal.reviews.commons.dto.ModerationReviewListingPage;
import com.snapdeal.reviews.commons.dto.ReviewModerationRequest;
import com.snapdeal.reviews.commons.dto.Status;
import com.snapdeal.reviews.commons.dto.wrapper.CheckedInReviewResponse;
import com.snapdeal.reviews.commons.dto.wrapper.CheckoutReviewResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetCheckedOutCountResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetModerationReviewsCountResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetReviewListingPageForModerationResponse;
import com.snapdeal.reviews.commons.dto.wrapper.ModerationSummaryResponse;
import com.snapdeal.reviews.commons.dto.wrapper.RangeQueryAttributeRequest;
import com.snapdeal.reviews.commons.dto.wrapper.SearchReviewsRequest;
import com.snapdeal.reviews.commons.dto.wrapper.TermQueryAttributeRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateReviewOnCheckinRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateReviewOnCheckinResponse;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateReviewOnCheckoutRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateReviewOnCheckoutResponse;
import com.snapdeal.reviews.commons.pagination.Page;
import com.snapdeal.reviews.mappers.OrikaMapper;
import com.snapdeal.reviews.model.IndexField;
import com.snapdeal.reviews.model.RangeQueryAttribute;
import com.snapdeal.reviews.model.ReviewBo;
import com.snapdeal.reviews.model.ReviewCheckoutBo;
import com.snapdeal.reviews.model.ReviewListingBo;
import com.snapdeal.reviews.model.ReviewModerationBo;
import com.snapdeal.reviews.model.ReviewModerationSearchBo;
import com.snapdeal.reviews.model.TermQueryAttribute;
import com.snapdeal.reviews.service.ConfigurationService;
import com.snapdeal.reviews.service.ReviewModerationService;
import com.snapdeal.reviews.validator.ModerationValidator;
import com.snapdeal.reviews.validator.ReviewStatusModerationValidator;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;

@Api(value = "Moderation", description = "Moderation APIs")
@RestController
public class ModerationController {

	@Autowired
	private ModerationValidator validator;

	@Autowired
	private List<ReviewStatusModerationValidator> moderationValidators;

	@Autowired
	private ReviewModerationService reviewService;

	@Autowired
	private OrikaMapper mapper;

	@Autowired
	private ConfigurationService configurationService;

	@ApiOperation(value = "Checkout Reviews by reviewIds")
	@ApiResponse(code = 200, message = "Successfully Checked Out", response = UpdateReviewOnCheckoutResponse.class)
	@RequestMapping(value = UriConstants.Moderation.CHECKOUT_REVIEWS, method = RequestMethod.POST)
	public ResponseEntity<UpdateReviewOnCheckoutResponse> updateReviewOnCheckout(
			@Valid @RequestBody UpdateReviewOnCheckoutRequest request) {

		validator.validateCheckoutRequest(request);
		List<ReviewCheckoutBo> reviewCheckoutBos = new ArrayList<ReviewCheckoutBo>();
		for (String reviewId : request.getReviewIds()) {
			ReviewCheckoutBo checkoutBo = new ReviewCheckoutBo(reviewId, request.getModeratorId());
			reviewCheckoutBos.add(checkoutBo);
		}
		CheckoutReviewResponse checkoutReviewResponse = reviewService.checkoutReviews(reviewCheckoutBos);
		UpdateReviewOnCheckoutResponse response = new UpdateReviewOnCheckoutResponse();
		response.setCheckoutReviewResponse(checkoutReviewResponse);
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<UpdateReviewOnCheckoutResponse>(response, HttpStatus.OK);

	}

	@ApiOperation(value = "Checkin Reviews by reviewIds and Status")
	@ApiResponse(code = 200, message = "Successfully Checked In", response = UpdateReviewOnCheckinResponse.class)
	@RequestMapping(value = UriConstants.Moderation.CHECKIN_REVIEWS, method = RequestMethod.POST)
	public ResponseEntity<UpdateReviewOnCheckinResponse> updateReviewOnCheckin(
			@Valid @RequestBody UpdateReviewOnCheckinRequest request) {
//		validator.validateCheckinRequest(request);
		List<ReviewModerationBo> reviewModerationBos = new ArrayList<ReviewModerationBo>();
		for (ReviewModerationRequest moderationRequest : request.getReviewModerationRequest()
				.getReviewModerationRequests()) {
			ReviewModerationBo reviewModerationBo = mapper.map(moderationRequest, ReviewModerationBo.class);
			reviewModerationBo.setModeratorId(request.getModeratorId());
			for (ReviewStatusModerationValidator validator : moderationValidators) {
				if (validator.getCheckInStatus().equals(reviewModerationBo.getStatus())) {
					validator.validate(reviewModerationBo);
				}	
			}
			reviewModerationBos.add(reviewModerationBo);
		}
		CheckedInReviewResponse checkedInReviewResponse = reviewService.checkInReviews(request.getModeratorId(), reviewModerationBos);
		UpdateReviewOnCheckinResponse response = new UpdateReviewOnCheckinResponse();
		response.setCheckedInReviewResponse(checkedInReviewResponse);
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<UpdateReviewOnCheckinResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Retrieve reviews for moderation")
	@ApiResponse(code = 200, message = "Successfully retrieved reviews", response = GetReviewListingPageForModerationResponse.class)
	@RequestMapping(value = UriConstants.Moderation.REVIEW_LISTING_MODERATION, method = RequestMethod.GET)
	public ResponseEntity<GetReviewListingPageForModerationResponse> getReviewListing(
			@RequestParam(value = "status", defaultValue = "CREATED") Status status,
			@RequestParam(value = "moderatorId", required = false) String moderatorId,
			@RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
			@RequestParam(value = "limit", required = false) Integer limit) {

		ReviewConfiguration reviewConfiguration = configurationService.getReviewConfiguration();
		if (null == limit) {
			limit = reviewConfiguration.getDefaultPaginationSize();
		}

		Page page = new Page(offset, limit);

		validator.validatePagination(page);

		ReviewListingBo reviewListingBo = reviewService.searchForModeration(status, moderatorId, page);

		List<ReviewBo> reviewBos = reviewListingBo.getReviewBos();

		ModerationReviewListingPage moderationReviewListingPage = new ModerationReviewListingPage();
		moderationReviewListingPage.setOffset(offset);
		moderationReviewListingPage.setLimit(limit);
		moderationReviewListingPage.setSize(reviewBos.size());
		moderationReviewListingPage.setTotal(reviewListingBo.getTotal());
		moderationReviewListingPage.setModerationReviews(mapper.mapAsList(reviewBos, ModerationReview.class));
		GetReviewListingPageForModerationResponse response = new GetReviewListingPageForModerationResponse();
		response.setModerationReviewListingPage(moderationReviewListingPage);
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<GetReviewListingPageForModerationResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Get total reviews count by status")
	@ApiResponse(code = 200, message = "Successfully retrieved reviews count", response = GetModerationReviewsCountResponse.class)
	@RequestMapping(value = UriConstants.Moderation.REVIEWS_COUNT, method = RequestMethod.GET)
	public ResponseEntity<GetModerationReviewsCountResponse> getReviewsCountByStatus(
			@RequestParam(value = "status", required = true) Status status) {

		Long totalReviewsCount = reviewService.getReviewsCountByStatus(status);
		GetModerationReviewsCountResponse response = new GetModerationReviewsCountResponse();
		response.setTotalReviewsCount(totalReviewsCount);
		response.setProtocol(Protocol.PROTOCOL_JSON);

		return new ResponseEntity<GetModerationReviewsCountResponse>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get total reviews count pending for moderation")
	@ApiResponse(code = 200, message = "Successfully retrieved reviews count", response = GetModerationReviewsCountResponse.class)
	@RequestMapping(value = UriConstants.Moderation.REVIEWS_PENDING_FOR_MODERATION_COUNT, method = RequestMethod.GET)
	public ResponseEntity<GetModerationReviewsCountResponse> getReviewsPendingForModerationCount() {

		Long totalReviewsCount = reviewService.getReviewsPendingForModerationCount();
		GetModerationReviewsCountResponse response = new GetModerationReviewsCountResponse();
		response.setTotalReviewsCount(totalReviewsCount);
		response.setProtocol(Protocol.PROTOCOL_JSON);

		return new ResponseEntity<GetModerationReviewsCountResponse>(response, HttpStatus.OK);
	}

	
	
	
	@ApiOperation(value = "Get moderation summary by date")
	@ApiResponse(code = 200, message = "Successfully retrieved moderation summary", response = ModerationSummaryResponse.class)
	@RequestMapping(value = UriConstants.Moderation.MODERATION_SUMMARY_BY_DATE, method = RequestMethod.GET)
	public ResponseEntity<ModerationSummaryResponse> getModerationSummaryByDate(
			@RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
			@RequestParam(value = "id", required=false) String moderatorId) {
		// If date is null, set it to yesterdays date
		if (date == null) {
			date = new Date(System.currentTimeMillis() - 1000L * 60L * 60L * 24L);
		}
		ModerationSummaryResponse response = reviewService.getModerationSummary(date, moderatorId);

		response.setProtocol(Protocol.PROTOCOL_JSON);

		return new ResponseEntity<ModerationSummaryResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Get reviews by search parameters")
	@ApiResponse(code = 200, message = "Successfully retrieved moderation summary", response = ModerationSummaryResponse.class)
	@RequestMapping(value = UriConstants.Moderation.REVIEWS_SEARCH_MODERATION, method = RequestMethod.POST)
	public ResponseEntity<GetReviewListingPageForModerationResponse> searchReviewsByAttributes(
			@Valid @RequestBody SearchReviewsRequest searchReviewsRequest) {

		//ReviewConfiguration reviewConfiguration = configurationService.getReviewConfiguration();
		ReviewModerationSearchBo moderationSearchBo = createModerationSearchBo(searchReviewsRequest);
		ReviewListingBo reviewsSearch = reviewService.reviewsSearch(moderationSearchBo);

		List<ReviewBo> reviewBos = reviewsSearch.getReviewBos();

		ModerationReviewListingPage moderationReviewListingPage = new ModerationReviewListingPage();
		/*
		 * reviewListingPage.setOffset(offset); reviewListingPage.setLimit(limit);
		 */
		moderationReviewListingPage.setSize(reviewBos.size());
		moderationReviewListingPage.setTotal(reviewsSearch.getTotal());
		moderationReviewListingPage.setModerationReviews(mapper.mapAsList(reviewBos, ModerationReview.class));
		GetReviewListingPageForModerationResponse response = new GetReviewListingPageForModerationResponse();
		response.setModerationReviewListingPage(moderationReviewListingPage);
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<GetReviewListingPageForModerationResponse>(response, HttpStatus.OK);
	}

	ReviewModerationSearchBo createModerationSearchBo(SearchReviewsRequest searchReviewsRequest) {
		ReviewModerationSearchBo reviewModerationSearchBo = new ReviewModerationSearchBo();
		List<TermQueryAttributeRequest> termQueryAttributeRequests = searchReviewsRequest
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
		List<RangeQueryAttributeRequest> rangeQueryAttributeRequests = searchReviewsRequest
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
		reviewModerationSearchBo.setTermQueryAttributes(termQueryAttributes);
		reviewModerationSearchBo.setRangeQueryAttributes(rangeQueryAttributes);
		reviewModerationSearchBo.setPage(searchReviewsRequest.getPage());
		reviewModerationSearchBo.setModeratorId(searchReviewsRequest.getModeratorId());
		reviewModerationSearchBo.setIncludeRating(searchReviewsRequest.isIncludeRatings());

		return reviewModerationSearchBo;
	}
	
	@ApiOperation(value = "Get total reviews currently checked out by a moderator")
	@ApiResponse(code = 200, message = "Successfully retrieved reviews count", response = GetModerationReviewsCountResponse.class)
	@RequestMapping(value = UriConstants.Moderation.CHECKED_OUT_COUNT, method = RequestMethod.GET)
	public ResponseEntity<GetCheckedOutCountResponse> getCheckedOutReviewsCount(
			@RequestParam(value = "moderatorId", required = true) String moderatorId) {

		Long totalReviewsCount = reviewService.getCheckedOutCount(moderatorId);
		GetCheckedOutCountResponse response = new GetCheckedOutCountResponse();
		response.setCheckedOutCount(totalReviewsCount);
		response.setProtocol(Protocol.PROTOCOL_JSON);

		return new ResponseEntity<GetCheckedOutCountResponse>(response, HttpStatus.OK);
	}
	
}
