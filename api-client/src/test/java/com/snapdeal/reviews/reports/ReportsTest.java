package com.snapdeal.reviews.reports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import org.springframework.util.Assert;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.reviews.ReviewTestUtils;
import com.snapdeal.reviews.ReviewsApiTest;
import com.snapdeal.reviews.commons.ReportIdentifier;
import com.snapdeal.reviews.commons.ReportType;
import com.snapdeal.reviews.commons.dto.ReportsResponse;
import com.snapdeal.reviews.commons.dto.ReviewModerationRequest;
import com.snapdeal.reviews.commons.dto.Status;
import com.snapdeal.reviews.commons.dto.SubscriberReportResponse;
import com.snapdeal.reviews.commons.dto.UpdateReviewModerationRequest;
import com.snapdeal.reviews.commons.dto.wrapper.CreateReviewRequest;
import com.snapdeal.reviews.commons.dto.wrapper.CreateReviewResponse;
import com.snapdeal.reviews.commons.dto.wrapper.SubscriptionIndicatorRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateReviewOnCheckinRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateReviewOnCheckoutRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateReviewOnCheckoutResponse;
import com.snapdeal.reviews.commons.json.JsonCodec;
import com.snapdeal.reviews.commons.reports.constants.SubscriptionIndicator;

public class ReportsTest extends ReviewsApiTest {

	private String moderatorId;

	private List<String> checkedOutReviewIds;

	private void createCheckedOutReviews(int noOfReviews)
			throws SnapdealWSException {
		List<String> reviewIds = createReviewsForTest(noOfReviews);
		UpdateReviewOnCheckoutRequest checkoutRequest = new UpdateReviewOnCheckoutRequest();
		checkoutRequest.setReviewIds(reviewIds);
		checkoutRequest.setModeratorId(moderatorId);

		UpdateReviewOnCheckoutResponse checkOutResponse = getModerationClient()
				.updateReviewOnCheckout(checkoutRequest);
		checkedOutReviewIds = checkOutResponse.getCheckoutReviewResponse()
				.getCheckedOutReviews();
	}

	private void updateReviewOnCheckIn(UpdateReviewOnCheckinRequest request)
			throws SnapdealWSException {
		getModerationClient().updateReviewOnCheckin(request);
	}

	private void prepareTestDataModerationTatReport(
			List<String> checkedOutReviewIds, long timeForModeration)
			throws SnapdealWSException {
		UpdateReviewOnCheckinRequest checkinRequest = new UpdateReviewOnCheckinRequest();
		List<ReviewModerationRequest> reviewModerationRequests = new ArrayList<ReviewModerationRequest>();
		UpdateReviewModerationRequest updateReviewModerationRequest = new UpdateReviewModerationRequest();
		for (String checkedOutReviewId : checkedOutReviewIds) {
			ReviewModerationRequest moderationRequest = new ReviewModerationRequest();
			moderationRequest
					.setModeratorRating((RandomUtils.nextInt() % 5) + 1);
			moderationRequest.setReviewId(checkedOutReviewId);
			moderationRequest.setStatus(Status.APPROVED);
			reviewModerationRequests.add(moderationRequest);
		}
		updateReviewModerationRequest
				.setReviewModerationRequests(reviewModerationRequests);
		checkinRequest
				.setReviewModerationRequest(updateReviewModerationRequest);
		checkinRequest.setModeratorId(moderatorId);
		try {
			Thread.sleep(timeForModeration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		updateReviewOnCheckIn(checkinRequest);
	}

	private List<String> createReviewsForTest(int noOfReviews)
			throws SnapdealWSException {
		CreateReviewRequest request = new CreateReviewRequest();
		List<String> reviewIds = new ArrayList<String>();
		for (int i = 0; i < noOfReviews; i++) {
			request.setReviewRequest(ReviewTestUtils.createDummyProductReview());
			CreateReviewResponse response = getClient().createReview(request);
			reviewIds.add(response.getReviewResponse().getId());
		}
		return reviewIds;
	}

	@Test
	public void getSummaryReportTest() throws SnapdealWSException {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("from", "2015-05-04");
		queryParams.put("to", "2015-05-18");
		queryParams.put("type", "DAILY");
		ReportsResponse response = getReportsClient().getReviewSummaryReport(
				queryParams);
		// assertEquals(response.getUserActions().size(), 2);
		Assert.notNull(response);

	}

	@Test
	public void getNumberOfReviewsModeratedBreakupStatus()
			throws SnapdealWSException {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("from", "2015-05-04");
		queryParams.put("to", "2015-05-20");
		queryParams.put("type", "WEEKLY");
		ReportsResponse response = getReportsClient()
				.getNumberOfReviewsModeratedBreakupStatus(queryParams);
		// assertEquals(response.getUserActions().size(), 2);
		Assert.notNull(response);
	}

	@Test
	public void getAllReviewsTest() throws SnapdealWSException {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("from", "2015-04-04");
		queryParams.put("to", "2015-05-03");
		ReportsResponse response = getReportsClient()
				.getAllReviews(queryParams);
		// assertEquals(response.getUserActions().size(), 2);
		Assert.notNull(response);

	}

	private Map<String, String> queryParams() {
		Map<String, String> queryParams = new HashMap<String, String>();
		Calendar cal = Calendar.getInstance();
		Date from = cal.getTime();
		cal.add(Calendar.DATE, 1);
		Date to = cal.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String fromDate = sdf.format(from);
		String toDate = sdf.format(to);
		queryParams.put("from", fromDate);
		queryParams.put("to", toDate);
		queryParams.put("type", ReportType.DAILY.name());
		return queryParams;
	}

	@Test
	public void getModerationTatReport() throws SnapdealWSException {
		Map<String, String> queryParams = queryParams();
		int noOfReviews = 10;
		List<String> reviewIds = new ArrayList<String>();
		moderatorId = RandomStringUtils.randomAlphabetic(10) + "@snapdeal.com";
		System.out.println(moderatorId);
		for (int i = 0; i < noOfReviews; i++) {
			createCheckedOutReviews(1);
			reviewIds.add(checkedOutReviewIds.get(0));
			prepareTestDataModerationTatReport(reviewIds, ((i + 1) * 1000));
		}
		ReportsResponse reportsResponseTAT = getReportsClient()
				.getModerationTATReport(queryParams);
		System.out.println(JsonCodec.getInstance().toJson(reportsResponseTAT));
	}

	@Test
	public void getModerationTatForBulkCheckInCase() throws SnapdealWSException {
		Map<String, String> queryParams = queryParams();
		List<String> reviewIds = new ArrayList<String>();
		moderatorId = RandomStringUtils.randomAlphabetic(10) + "@snapdeal.com";
		System.out.println(moderatorId);
		createCheckedOutReviews(10);
		for (int i = 0; i < 10; i++) {
			reviewIds.add(checkedOutReviewIds.get(i));
			prepareTestDataModerationTatReport(reviewIds, ((i + 1) * 100));
		}
		ReportsResponse reportsResponseTAT = getReportsClient()
				.getModerationTATReport(queryParams);
		System.out.println(JsonCodec.getInstance().toJson(reportsResponseTAT));
	}

	@Test
	public void getAllRatingsTest() throws SnapdealWSException {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("from", "2015-04-04");
		queryParams.put("to", "2015-05-03");
		queryParams.put("type", ReportType.DAILY.toString());
		ReportsResponse response = getReportsClient().getRatingReport(
				queryParams);
		// assertEquals(response.getUserActions().size(), 2);
		Assert.notNull(response);

	}

	@Test
	public void getModeratorSummaryReport() throws SnapdealWSException {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("from", "2015-05-25");
		queryParams.put("to", "2015-05-25");
		queryParams.put("type", ReportType.DAILY.toString());
		queryParams.put("id", "7d3fca6f-b424-4435-b332-e86e9a4d3c1f");
		ReportsResponse response = getReportsClient()
				.getModeratorSummaryReport(queryParams);
		// assertEquals(response.getUserActions().size(), 2);
		Assert.notNull(response);

	}

	@Test
	public void getAllRatingsTillDateTest() throws SnapdealWSException {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("from", "2015-04-04");
		queryParams.put("to", "2015-05-03");
		ReportsResponse response = getReportsClient()
				.getRatingsReportsTillDate(queryParams);
		// assertEquals(response.getUserActions().size(), 2);
		Assert.notNull(response);

	}

	@Test
	public void getSubscriberDetailsByReportId() throws SnapdealWSException {
		Map<String, String> queryParams = new HashMap<String, String>();
		String reportId = "MODERATION_TAT_REPORT";
		queryParams.put("id", reportId);
		System.out.println(reportId);

		SubscriberReportResponse subscriberReportResponse = getReportsClient()
				.getSubscribedEmailsByReportId(queryParams);
		System.out.println(subscriberReportResponse.getSubscribedEmails());
	}

	@Test
	public void updateSubscriberDetailsByReportId() throws SnapdealWSException {
		SubscriptionIndicatorRequest subscriptionIndicatorRequest = new SubscriptionIndicatorRequest();
		subscriptionIndicatorRequest
				.setReportId(ReportIdentifier.MODERATION_TAT_REPORT);
		subscriptionIndicatorRequest
				.setSubscriptionIndicator(SubscriptionIndicator.SUBSCRIBE);
		subscriptionIndicatorRequest.setEmailId("apiTest@gmail.com");

		 getReportsClient().updateSubscribedEmailsByReportId(subscriptionIndicatorRequest);
	}
}
