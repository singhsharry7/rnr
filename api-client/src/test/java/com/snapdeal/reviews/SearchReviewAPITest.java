package com.snapdeal.reviews;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.reviews.commons.dto.ModerationSearchField;
import com.snapdeal.reviews.commons.dto.wrapper.GetReviewListingPageForModerationResponse;
import com.snapdeal.reviews.commons.dto.wrapper.RangeQueryAttributeRequest;
import com.snapdeal.reviews.commons.dto.wrapper.SearchReviewsRequest;
import com.snapdeal.reviews.commons.dto.wrapper.TermQueryAttributeRequest;
import com.snapdeal.reviews.commons.pagination.Page;

public class SearchReviewAPITest extends ReviewsApiTest {
	@Test
	public void searchReviewByStatusTest() throws SnapdealWSException {
		SearchReviewsRequest searchReviewsRequest = new SearchReviewsRequest();
		searchReviewsRequest.setModeratorId("123");
		searchReviewsRequest.setPage(new Page(0, 1));
		List<TermQueryAttributeRequest> termQueryAttributeRequests = new ArrayList<TermQueryAttributeRequest>();
		/*TermQueryAttributeRequest queryAttributeRequest1 = new TermQueryAttributeRequest();
		queryAttributeRequest1.setSearchField(ModerationSearchField.USER_ID);
		List<String> value1 = new ArrayList<String>();
		value1.add("08e1fe0e-ef79-436b-a4ba-13bffd5a80f1");
		queryAttributeRequest1.setValue(value1);
		termQueryAttributeRequests.add(queryAttributeRequest1);*/
		TermQueryAttributeRequest queryAttributeRequest = new TermQueryAttributeRequest();
		queryAttributeRequest.setSearchField(ModerationSearchField.STATUS);
		List<String> value = new ArrayList<String>();
		value.add("APPROVED");
		queryAttributeRequest.setValue(value);
		termQueryAttributeRequests.add(queryAttributeRequest);
		searchReviewsRequest.setTermQueryAttributeRequests(termQueryAttributeRequests);
		
		List<RangeQueryAttributeRequest> rangeQueryAttributeRequests = new ArrayList<RangeQueryAttributeRequest>();
		RangeQueryAttributeRequest rangeAttributeRequest = new RangeQueryAttributeRequest();
		rangeAttributeRequest.setSearchField(ModerationSearchField.CREATED_DATE);
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -2);
		rangeAttributeRequest.setFrom(calendar.getTimeInMillis()+"");
		rangeAttributeRequest.setTo(date.getTime()+"");
		rangeQueryAttributeRequests.add(rangeAttributeRequest);
		
		searchReviewsRequest.setRangeQueryAttributeRequests(rangeQueryAttributeRequests);
		GetReviewListingPageForModerationResponse searchReviewsByAttributes = getModerationClient().searchReviewsByAttributes(searchReviewsRequest);
		System.out.println(searchReviewsByAttributes.getModerationReviewListingPage().getModerationReviews());
	}
}
