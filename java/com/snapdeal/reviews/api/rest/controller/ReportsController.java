package com.snapdeal.reviews.api.rest.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snapdeal.base.model.common.ServiceResponse;
import com.snapdeal.base.transport.service.ITransportService.Protocol;
import com.snapdeal.reviews.commons.ReportIdentifier;
import com.snapdeal.reviews.commons.ReportType;
import com.snapdeal.reviews.commons.UriConstants;
import com.snapdeal.reviews.commons.dto.ReportsResponse;
import com.snapdeal.reviews.commons.dto.SubscriberReportResponse;
import com.snapdeal.reviews.commons.dto.TotalCountResponse;
import com.snapdeal.reviews.commons.dto.TotalFloatResponse;
import com.snapdeal.reviews.commons.dto.wrapper.SubscriptionIndicatorRequest;
import com.snapdeal.reviews.commons.reports.TableModel;
import com.snapdeal.reviews.model.ReportSubscriberBo;
import com.snapdeal.reviews.model.ReviewReportBo;
import com.snapdeal.reviews.service.ReportsService;
import com.snapdeal.reviews.validator.ReportValidator;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;

@Api(value = "Reports", description = "Reporting APIs")
@RestController
public class ReportsController {

	@Autowired
	private ReportsService reportService;

	@Autowired
	private ReportValidator validator;

	@ApiOperation(value = "Review Summary report")
	@ApiResponse(code = 200, message = "Successfully retrieved summary report", response = ReportsResponse.class)
	@RequestMapping(value = UriConstants.Reports.SUMMARY_REPORT, method = RequestMethod.GET)
	public ResponseEntity<ReportsResponse> getReviewSummary(
			@RequestParam(value = "from", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
			@RequestParam(value = "to", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
			@RequestParam(value = "type", required = true) ReportType reportType) {

		ReviewReportBo reviewReportBo = new ReviewReportBo(from, to, reportType);
		validator.validateReportRequest(reviewReportBo);
		TableModel model = reportService.getReviewSummaryReport(to, from,
				reportType);
		ReportsResponse response = new ReportsResponse();
		response.setModel(model);
		response.setProtocol(Protocol.PROTOCOL_JSON);

		return new ResponseEntity<ReportsResponse>(response, HttpStatus.OK);

	}

	@ApiOperation(value = "All reviews report")
	@ApiResponse(code = 200, message = "Successfully retrieved all reviews report", response = ReportsResponse.class)
	@RequestMapping(value = UriConstants.Reports.REVIEWS_REPORT, method = RequestMethod.GET)
	public ResponseEntity<ReportsResponse> getAllReviews(
			@RequestParam(value = "from", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
			@RequestParam(value = "to", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
			@RequestParam(value = "offSet", required = false, defaultValue = "0") int offSet,
			@RequestParam(value = "limit", required = false) Integer limit) {
		ReviewReportBo reviewReportBo = new ReviewReportBo(from, to);
		reviewReportBo.setOffSet(offSet);
		reviewReportBo.setLimit(limit);
		validator.validateReportRequest(reviewReportBo);
		TableModel model = reportService.getAllReviews(reviewReportBo);
		ReportsResponse response = new ReportsResponse();
		response.setModel(model);
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<ReportsResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "All ratings report")
	@ApiResponse(code = 200, message = "Successfully retrieved all ratings report", response = ReportsResponse.class)
	@RequestMapping(value = UriConstants.Reports.RATINGS_REPORT, method = RequestMethod.GET)
	public ResponseEntity<ReportsResponse> getRatingsReport(
			@RequestParam(value = "from", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
			@RequestParam(value = "to", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
			@RequestParam(value = "type", required = true) ReportType reportType) {
		ReviewReportBo reviewReportBo = new ReviewReportBo(from, to);
		reviewReportBo.setType(reportType);
		validator.validateReportRequest(reviewReportBo);
		TableModel model = reportService.getRatingsReport(reviewReportBo);
		ReportsResponse response = new ReportsResponse();
		response.setModel(model);
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<ReportsResponse>(response, HttpStatus.OK);

	}

	@ApiOperation(value = "All ratings report till date")
	@ApiResponse(code = 200, message = "Successfully retrieved all ratings report till date ", response = ReportsResponse.class)
	@RequestMapping(value = UriConstants.Reports.RATINGS_REPORT_TILL_DATE, method = RequestMethod.GET)
	public ResponseEntity<ReportsResponse> getRatingsReportsTillDate(
			@RequestParam(value = "from", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
			@RequestParam(value = "to", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {
		ReviewReportBo reviewReportBo = new ReviewReportBo(from, to);
		validator.validateReportRequest(reviewReportBo);
		TableModel model = reportService
				.getRatingsReportTillDate(reviewReportBo);
		ReportsResponse response = new ReportsResponse();
		response.setModel(model);
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<ReportsResponse>(response, HttpStatus.OK);

	}

	@ApiOperation(value = "Set of subscribed emails for a report type", position = 2)
	@ApiResponse(code = 200, message = "Successfully retrieved emails for a report id", response = SubscriberReportResponse.class)
	@RequestMapping(value = UriConstants.Reports.SUBSCRIBER_REPORT_FOR_REPORTID, method = RequestMethod.GET)
	public ResponseEntity<SubscriberReportResponse> getSubscribedEmailsByReportId(
			@RequestParam("id") ReportIdentifier reportId) {
		validator.checkReportIdentifier(reportId);
		ReportSubscriberBo model = reportService
				.getSubscriberReportForReportId(reportId);

		SubscriberReportResponse response = new SubscriberReportResponse();
		response.setSubscribedEmails(model.getSubscribedEmailId());
		response.setReport_id(model.getReport_id());
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<SubscriberReportResponse>(response,
				HttpStatus.OK);
	}

	@ApiOperation(value = "Number of reviews moderated  by individual moderators")
	@ApiResponse(code = 200, message = "Successfully retrieved Number of reviews moderated  by individual moderators", response = ReportsResponse.class)
	@RequestMapping(value = UriConstants.Reports.MODERATOR_BREAK_UP_STATUS, method = RequestMethod.GET)
	public ResponseEntity<ReportsResponse> getNumberOfReviewsModeratedBreakup(
			@RequestParam(value = "from", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
			@RequestParam(value = "to", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
			@RequestParam(value = "type", required = true) ReportType reportType) {
		ReviewReportBo reviewReportBo = new ReviewReportBo(from, to);
		validator.validateReportRequest(reviewReportBo);
		TableModel model = reportService
				.getNumberOfReviewsModeratedBreakupStatus(to, from, reportType);
		ReportsResponse response = new ReportsResponse();
		response.setModel(model);
		response.setProtocol(Protocol.PROTOCOL_JSON);

		return new ResponseEntity<ReportsResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Total number of reviews moderated  by a moderator over a given period")
	@ApiResponse(code = 200, message = "Successfully retrieved total reviews moderated by the moderator", response = ReportsResponse.class)
	@RequestMapping(value = UriConstants.Reports.REVIEWS_COUNT_BY_MODERATOR, method = RequestMethod.GET)
	public ResponseEntity<ReportsResponse> getReviewsCountByModerator(
			@RequestParam(value = "from", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
			@RequestParam(value = "to", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
			@RequestParam(value = "id", required = true) String moderatorId,
			@RequestParam(value = "type", required = true) ReportType reportType) {
		TableModel model = reportService.getReviewsCountByModerator(from, to,
				reportType, moderatorId);
		ReviewReportBo reviewReport = new ReviewReportBo(from, to);
		validator.validateReportRequest(reviewReport);
		ReportsResponse response = new ReportsResponse();
		response.setModel(model);
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<ReportsResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Moderation 90% TAT by individual moderators")
	@RequestMapping(value = UriConstants.Reports.MODERATION_TAT_REPORT, method = RequestMethod.GET)
	public ResponseEntity<ReportsResponse> getModeratorsTATReport(
			@RequestParam(value = "from", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
			@RequestParam(value = "to", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
			@RequestParam(value = "type", required = true) ReportType reportType) {
		ReviewReportBo reportBo = new ReviewReportBo(from, to);
		reportBo.setType(reportType);
		validator.validateReportRequest(reportBo);
		TableModel model = reportService.getModerationTATReport(reportBo);
		ReportsResponse response = new ReportsResponse();
		response.setModel(model);
		response.setProtocol(Protocol.PROTOCOL_JSON);

		return new ResponseEntity<ReportsResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Total Number of reviews for date range")
	@ApiResponse(code = 200, message = "Successfully retrieved Number of reviews for given date Range", response = TotalCountResponse.class)
	@RequestMapping(value = UriConstants.Reports.TOTAL_REVIEWS_COUNT, method = RequestMethod.GET)
	public ResponseEntity<TotalCountResponse> getTotalReviewsCountForDateRange(
			@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
			@RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {
		ReviewReportBo reportBo = new ReviewReportBo(from, to);
		validator.validateReportRequest(reportBo);
		TotalCountResponse response = new TotalCountResponse();
		response.setCount(reportService.getTotalReviewCountForDateRange(from,
				to));
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<TotalCountResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Total Number of Ratings for date range")
	@ApiResponse(code = 200, message = "Successfully retrieved Number of ratings for given date Range", response = TotalCountResponse.class)
	@RequestMapping(value = UriConstants.Reports.TOTAL_RATINGS_COUNT, method = RequestMethod.GET)
	public ResponseEntity<TotalCountResponse> getTotalRatingsCountForDateRange(
			@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
			@RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {
		ReviewReportBo reportBo = new ReviewReportBo(from, to);
		validator.validateReportRequest(reportBo);
		TotalCountResponse response = new TotalCountResponse();
		response.setCount(reportService.getTotalRatingsCountForDateRange(from,
				to));
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<TotalCountResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Rejected Reviews Percentage")
	@ApiResponse(code = 200, message = "Successfully retrieved Rejected Reviews Percentage for given date Range", response = TotalFloatResponse.class)
	@RequestMapping(value = UriConstants.Reports.REJECTED_REVIEW_PERCENTAGE, method = RequestMethod.GET)
	public ResponseEntity<TotalFloatResponse> getRejectedReviewPercentageForDateRange(
			@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
			@RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {
		ReviewReportBo reportBo = new ReviewReportBo(from, to);
		validator.validateReportRequest(reportBo);
		TotalFloatResponse response = new TotalFloatResponse();
		response.setValue(reportService
				.getRejectedReviewPercentageForDateRange(from, to));
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<TotalFloatResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Quality Score Average")
	@ApiResponse(code = 200, message = "Successfully retrieved Quality Average Score for given date Range", response = TotalFloatResponse.class)
	@RequestMapping(value = UriConstants.Reports.QUALITY_SCORE_AVERAGE, method = RequestMethod.GET)
	public ResponseEntity<TotalFloatResponse> getAverageQualityScore(
			@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
			@RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {
		ReviewReportBo reportBo = new ReviewReportBo(from, to);
		validator.validateReportRequest(reportBo);
		TotalFloatResponse response = new TotalFloatResponse();
		response.setValue(reportService.getAverageQualityScoreForDateRange(
				from, to));
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<TotalFloatResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Moderation Tat Average")
	@ApiResponse(code = 200, message = "Successfully retrieved Moderation Tat Average for given date Range", response = TotalFloatResponse.class)
	@RequestMapping(value = UriConstants.Reports.MODERATION_TAT_AVERAGE, method = RequestMethod.GET)
	public ResponseEntity<TotalFloatResponse> getAverageModerationTat(
			@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
			@RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {
		ReviewReportBo reportBo = new ReviewReportBo(from, to);
		validator.validateReportRequest(reportBo);
		TotalFloatResponse response = new TotalFloatResponse();
		response.setValue(reportService.getAverageModerationTat(from, to));
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<TotalFloatResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Reports Graph reviews per date")
	@ApiResponse(code = 200, message = "Successfully Reports Graph reviews for given date Range", response = ReportsResponse.class)
	@RequestMapping(value = UriConstants.Reports.REVIEW_COUNT_BY_DATE, method = RequestMethod.GET)
	public ResponseEntity<ReportsResponse> getReviewCountByDate(
			@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
			@RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {
		ReviewReportBo reportBo = new ReviewReportBo(from, to);
		validator.validateReportRequest(reportBo);
		TableModel model = reportService.getReviewCountByDate(from, to);
		ReportsResponse response = new ReportsResponse();
		response.setModel(model);
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<ReportsResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "unique review data report")
	@ApiResponse(code = 200, message = "Successfully retrieved unique review data report", response = ReportsResponse.class)
	@RequestMapping(value = UriConstants.Reports.UNIQUE_REVIEW_DATA_REPORT, method = RequestMethod.GET)
	public ResponseEntity<ReportsResponse> getUniqueReviewDataReport(
			@RequestParam(value = "from", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
			@RequestParam(value = "to", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {
		ReviewReportBo reviewReportBo = new ReviewReportBo(from, to);
		validator.validateReportRequest(reviewReportBo);
		TableModel model = reportService.getUniqueReviewDataReport(from, to,
				ReportType.DAILY);
		ReportsResponse response = new ReportsResponse();
		response.setModel(model);
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<ReportsResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Manage Subscription for a report id.", position = 13)
	@ApiResponse(code = 200, message = "Successfully updated the subscription email list for a report id")
	@RequestMapping(value = UriConstants.Reports.UPDATE_SUBSCRIBER_LIST_FOR_REPORTID, method = RequestMethod.PUT)
	public ResponseEntity<ServiceResponse> updateSubscribedEmailsByReportId(
			@RequestBody SubscriptionIndicatorRequest subscriptionIndicatorRequest) {
		validator.checkSubscriptionRequest(subscriptionIndicatorRequest);
		reportService.updateSubscriptionDetailsForReportId(
				subscriptionIndicatorRequest.getReportId(),
				subscriptionIndicatorRequest.getEmailId(),
				subscriptionIndicatorRequest.getSubscriptionIndicator());
		ServiceResponse response = new ServiceResponse();
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Reviews count segregated by rejection reasons report.", position = 14)
	@RequestMapping(value = UriConstants.Reports.REVIEWS_COUNT_BREAKDOWN_BY_REJECTION_REASONS, method = RequestMethod.GET)
	public ResponseEntity<ReportsResponse> getReviewsCountBreakdownByRejectionReasons(
			@RequestParam(value = "from", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
			@RequestParam(value = "to", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
			@RequestParam(value = "type", required = true) ReportType reportType) {
		ReviewReportBo reportBo = new ReviewReportBo(from, to);
		reportBo.setType(reportType);
		validator.validateReportRequest(reportBo);
		TableModel model = reportService.getRejectedReviewsReport(reportBo);
		ReportsResponse response = new ReportsResponse();
		response.setModel(model);
		response.setProtocol(Protocol.PROTOCOL_JSON);
		response.setCode(HttpStatus.OK.toString());
		return new ResponseEntity<ReportsResponse>(response, HttpStatus.OK);
	}
	
}