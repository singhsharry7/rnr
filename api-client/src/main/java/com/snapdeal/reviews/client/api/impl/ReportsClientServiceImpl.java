package com.snapdeal.reviews.client.api.impl;

import static com.snapdeal.reviews.commons.UriConstants.Reports.MODERATOR_BREAK_UP_STATUS;
import static com.snapdeal.reviews.commons.UriConstants.Reports.SUMMARY_REPORT;
import static com.snapdeal.reviews.commons.UriConstants.Reports.UNIQUE_REVIEW_DATA_REPORT;
import static com.snapdeal.reviews.commons.UriConstants.Reports.REVIEWS_COUNT_BY_MODERATOR;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.snapdeal.reviews.commons.dto.TotalCountResponse;
import com.snapdeal.reviews.commons.dto.TotalFloatResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.base.model.common.ServiceResponse;
import com.snapdeal.base.transport.service.ITransportService;
import com.snapdeal.base.transport.service.ITransportService.Protocol;
import com.snapdeal.reviews.client.api.ReportsClientService;
import com.snapdeal.reviews.client.base.lib.adapter.Constants;
import com.snapdeal.reviews.commons.UriConstants.Reports;
import com.snapdeal.reviews.commons.dto.ReportsResponse;
import com.snapdeal.reviews.commons.dto.SubscriberReportResponse;
import com.snapdeal.reviews.commons.dto.wrapper.SubscriptionIndicatorRequest;

@Service
public class ReportsClientServiceImpl implements ReportsClientService {

	@Autowired
	private ITransportService transportService;

	private String webServiceBaseUrl;

	private Map<String, String> headerMap = new HashMap<>();

	@PostConstruct
	public void init() {
		headerMap.put("Accept",
				"application/" + Constants.CUSTOM_JSON_PROTOCOL.toLowerCase());
		headerMap.put("Content-Type", "application/"
				+ Constants.CUSTOM_JSON_PROTOCOL.toLowerCase());
		transportService.registerService("/api/service/reviews/",
				"reviewService.");
	}

	@Override
	public void setWebServiceBaseUrl(String webServiceBaseURL) {
		this.webServiceBaseUrl = webServiceBaseURL;
	}

	public String getWebServiceBaseUrl() {
		return webServiceBaseUrl;
	}

	@Override
	public ReportsResponse getReviewSummaryReport(
			Map<String, String> queryParams) throws SnapdealWSException {
		return (ReportsResponse) transportService.executeGetRequest(
				getWebServiceBaseUrl() + SUMMARY_REPORT,
				queryParams != null ? queryParams
						: new HashMap<String, String>(), headerMap,
				ReportsResponse.class);
	}

	@Override
	public ReportsResponse getNumberOfReviewsModeratedBreakupStatus(
			Map<String, String> queryParams) throws SnapdealWSException {
		return (ReportsResponse) transportService.executeGetRequest(
				getWebServiceBaseUrl() + MODERATOR_BREAK_UP_STATUS,
				queryParams != null ? queryParams
						: new HashMap<String, String>(), headerMap,
				ReportsResponse.class);
	}

	@Override
	public ReportsResponse getUniqueReviewReport(Map<String, String> queryParams)
			throws SnapdealWSException {
		return (ReportsResponse) transportService.executeGetRequest(
				getWebServiceBaseUrl() + UNIQUE_REVIEW_DATA_REPORT,
				queryParams != null ? queryParams
						: new HashMap<String, String>(), headerMap,
				ReportsResponse.class);
	}

	public ReportsResponse getModerationTATReport(
			Map<String, String> queryParams) throws SnapdealWSException {
		// TODO Auto-generated method stub
		return (ReportsResponse) transportService.executeGetRequest(
				getWebServiceBaseUrl() + Reports.MODERATION_TAT_REPORT,
				queryParams != null ? queryParams
						: new HashMap<String, String>(), headerMap,
				ReportsResponse.class);
	}

	@Override
	public ReportsResponse getAllReviews(Map<String, String> queryParams)
			throws SnapdealWSException {
		return (ReportsResponse) transportService.executeGetRequest(
				getWebServiceBaseUrl() + Reports.REVIEWS_REPORT,
				queryParams != null ? queryParams
						: new HashMap<String, String>(), headerMap,
				ReportsResponse.class);
	}

	@Override
	public ReportsResponse getRatingReport(Map<String, String> queryParams)
			throws SnapdealWSException {
		return (ReportsResponse) transportService.executeGetRequest(
				getWebServiceBaseUrl() + Reports.RATINGS_REPORT,
				queryParams != null ? queryParams
						: new HashMap<String, String>(), headerMap,
				ReportsResponse.class);
	}

	@Override
	public SubscriberReportResponse getSubscribedEmailsByReportId(
			Map<String, String> queryParams) throws SnapdealWSException {
		return (SubscriberReportResponse) transportService
				.executeGetRequest(getWebServiceBaseUrl()
						+ Reports.SUBSCRIBER_REPORT_FOR_REPORTID,
						queryParams != null ? queryParams
								: new HashMap<String, String>(), headerMap,
						SubscriberReportResponse.class);
	}

	@Override
	public ServiceResponse updateSubscribedEmailsByReportId(
			SubscriptionIndicatorRequest subscriptionIndicatorRequest)
			throws SnapdealWSException {
		subscriptionIndicatorRequest.setRequestProtocol(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		return (ServiceResponse) transportService.executePutRequest(
				getWebServiceBaseUrl()
						+ Reports.UPDATE_SUBSCRIBER_LIST_FOR_REPORTID,
				subscriptionIndicatorRequest, headerMap, ServiceResponse.class);
	}

    @Override
    public TotalCountResponse getTotalReviewsCountForDateRange(Map<String, String> queryParams) throws SnapdealWSException {
        return (TotalCountResponse) transportService
                .executeGetRequest(getWebServiceBaseUrl()
                                + Reports.TOTAL_REVIEWS_COUNT,
                        queryParams != null ? queryParams
                                : new HashMap<String, String>(), headerMap,
                        TotalCountResponse.class);
    }

    @Override
    public TotalCountResponse getTotalRatingsCountForDateRange(Map<String, String> queryParams) throws SnapdealWSException {
        return (TotalCountResponse) transportService
                .executeGetRequest(getWebServiceBaseUrl()
                                + Reports.TOTAL_RATINGS_COUNT,
                        queryParams != null ? queryParams
                                : new HashMap<String, String>(), headerMap,
                        TotalCountResponse.class);
    }

    @Override
    public TotalFloatResponse getRejectedReviewPercentageForDateRange(Map<String, String> queryParams) throws SnapdealWSException {
        return (TotalFloatResponse) transportService
                .executeGetRequest(getWebServiceBaseUrl()
                                + Reports.REJECTED_REVIEW_PERCENTAGE,
                        queryParams != null ? queryParams
                                : new HashMap<String, String>(), headerMap,
                        TotalFloatResponse.class);
    }

    @Override
    public TotalFloatResponse getAverageQualityScore(Map<String, String> queryParams) throws SnapdealWSException {
        return (TotalFloatResponse) transportService
                .executeGetRequest(getWebServiceBaseUrl()
                                + Reports.QUALITY_SCORE_AVERAGE,
                        queryParams != null ? queryParams
                                : new HashMap<String, String>(), headerMap,
                        TotalFloatResponse.class);
    }

    @Override
    public TotalFloatResponse getAverageModerationTat(Map<String, String> queryParams) throws SnapdealWSException {
        return (TotalFloatResponse) transportService
                .executeGetRequest(getWebServiceBaseUrl()
                                + Reports.MODERATION_TAT_AVERAGE,
                        queryParams != null ? queryParams
                                : new HashMap<String, String>(), headerMap,
                        TotalFloatResponse.class);
    }

    @Override
    public ReportsResponse getReviewCountByDate(Map<String, String> queryParams) throws SnapdealWSException {
        return (ReportsResponse) transportService
                .executeGetRequest(getWebServiceBaseUrl()
                                + Reports.REVIEW_COUNT_BY_DATE,
                        queryParams != null ? queryParams
                                : new HashMap<String, String>(), headerMap,
                        ReportsResponse.class);
    }
    
    @Override
	public ReportsResponse getModeratorSummaryReport(Map<String, String> queryParams) throws SnapdealWSException {
		return (ReportsResponse) transportService.executeGetRequest(
				getWebServiceBaseUrl() + REVIEWS_COUNT_BY_MODERATOR,
				queryParams != null ? queryParams
						: new HashMap<String, String>(), headerMap,
				ReportsResponse.class);
	}

	@Override
	public ReportsResponse getRatingsReportsTillDate(
			Map<String, String> queryParams) throws SnapdealWSException {
		return (ReportsResponse) transportService.executeGetRequest(
				getWebServiceBaseUrl() + Reports.RATINGS_REPORT_TILL_DATE,
				queryParams != null ? queryParams
						: new HashMap<String, String>(), headerMap,
				ReportsResponse.class);
	}

	@Override
	public ReportsResponse getReviewsCountBreakdownByRejectionReasons(
			Map<String, String> queryParams) throws SnapdealWSException {
		return (ReportsResponse) transportService.executeGetRequest(
				getWebServiceBaseUrl()
						+ Reports.REVIEWS_COUNT_BREAKDOWN_BY_REJECTION_REASONS,
				queryParams, headerMap, ReportsResponse.class);
	}

}
