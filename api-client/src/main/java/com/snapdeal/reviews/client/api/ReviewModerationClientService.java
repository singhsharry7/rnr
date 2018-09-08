package com.snapdeal.reviews.client.api;

import java.util.Map;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.reviews.commons.dto.wrapper.GetCheckedOutCountResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetModerationReviewsCountResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetReviewListingPageForModerationResponse;
import com.snapdeal.reviews.commons.dto.wrapper.ModerationSummaryResponse;
import com.snapdeal.reviews.commons.dto.wrapper.SearchReviewsRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateReviewOnCheckinRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateReviewOnCheckinResponse;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateReviewOnCheckoutRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateReviewOnCheckoutResponse;

public interface ReviewModerationClientService extends ReviewWebClientService {

	public UpdateReviewOnCheckoutResponse updateReviewOnCheckout(UpdateReviewOnCheckoutRequest request)
			throws SnapdealWSException;

	public UpdateReviewOnCheckinResponse updateReviewOnCheckin(UpdateReviewOnCheckinRequest request)
			throws SnapdealWSException;

	public GetReviewListingPageForModerationResponse getReviewList(Map<String, String> queryParams)
			throws SnapdealWSException;

	public ModerationSummaryResponse getModerationConsoleSummary(Map<String, String> queryParams)
			throws SnapdealWSException;

	public GetModerationReviewsCountResponse getReviewsCountByStatus(Map<String, String> queryParams)
			throws SnapdealWSException;

	public GetReviewListingPageForModerationResponse searchReviewsByAttributes(
			SearchReviewsRequest searchReviewsRequest) throws SnapdealWSException;

	public GetCheckedOutCountResponse getCheckedOutReviewsCount(Map<String, String> queryParams) throws SnapdealWSException;

	public GetModerationReviewsCountResponse getReviewsPendingForModerationCount() throws SnapdealWSException;
}
