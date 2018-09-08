package com.snapdeal.reviews.client.api;

import java.util.Map;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.base.model.common.ServiceResponse;
import com.snapdeal.reviews.commons.dto.ReportsResponse;
import com.snapdeal.reviews.commons.dto.SubscriberReportResponse;
import com.snapdeal.reviews.commons.dto.TotalCountResponse;
import com.snapdeal.reviews.commons.dto.TotalFloatResponse;
import com.snapdeal.reviews.commons.dto.wrapper.SubscriptionIndicatorRequest;

public interface ReportsClientService extends ReviewWebClientService {

	ReportsResponse getReviewSummaryReport(Map<String, String> queryParams)
			throws SnapdealWSException;

	ReportsResponse getNumberOfReviewsModeratedBreakupStatus(
			Map<String, String> queryParams) throws SnapdealWSException;

	ReportsResponse getUniqueReviewReport(Map<String, String> queryParams)
			throws SnapdealWSException;

	ReportsResponse getModerationTATReport(Map<String, String> queryParams)
			throws SnapdealWSException;

	ReportsResponse getAllReviews(Map<String, String> queryParams)
			throws SnapdealWSException;

	ReportsResponse getRatingReport(Map<String, String> queryParams)
			throws SnapdealWSException;

	SubscriberReportResponse getSubscribedEmailsByReportId(
			Map<String, String> queryParams) throws SnapdealWSException;

	ServiceResponse updateSubscribedEmailsByReportId(
			SubscriptionIndicatorRequest subscriptionIndicatorRequest)
			throws SnapdealWSException;

    TotalCountResponse getTotalReviewsCountForDateRange(Map<String, String> queryParams)
            throws SnapdealWSException;

    TotalCountResponse getTotalRatingsCountForDateRange(Map<String, String> queryParams)
            throws SnapdealWSException;

    TotalFloatResponse getRejectedReviewPercentageForDateRange(Map<String, String> queryParams)
            throws SnapdealWSException;

    TotalFloatResponse getAverageQualityScore(Map<String, String> queryParams)
            throws SnapdealWSException;

    TotalFloatResponse getAverageModerationTat(Map<String, String> queryParams)
            throws SnapdealWSException;

    ReportsResponse getReviewCountByDate(Map<String, String> queryParams)
            throws SnapdealWSException;
    
    ReportsResponse getModeratorSummaryReport(
			Map<String, String> queryParams) throws SnapdealWSException;
    
    ReportsResponse getRatingsReportsTillDate(Map<String, String> queryParams)
			throws SnapdealWSException;
    
    ReportsResponse getReviewsCountBreakdownByRejectionReasons(Map<String, String> queryParams) 
    		throws SnapdealWSException;

}