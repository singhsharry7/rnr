package com.snapdeal.reviews.validator;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snapdeal.reviews.commons.ReportIdentifier;
import com.snapdeal.reviews.commons.dto.wrapper.SubscriptionIndicatorRequest;
import com.snapdeal.reviews.exception.ErrorCode;
import com.snapdeal.reviews.exception.client.InvalidRequestException;
import com.snapdeal.reviews.model.ReviewReportBo;

@Component
public class ReportValidator {

	@Autowired
	private ValidationUtils utils;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ReportValidator.class);

	public void validateReportRequest(ReviewReportBo reviewReportBo) {
		utils.assertNonNull("from", reviewReportBo.getFrom());
		utils.assertNonNull("to", reviewReportBo.getTo());
		Date from = reviewReportBo.getFrom();
		Date to = reviewReportBo.getTo();
		if (from.after(to)) {
			throw new InvalidRequestException(ErrorCode.INPUT_INVALID_PAYLOAD,
					"from or to date");
		}
		Date now = new Date();
		if (from.after(now)) {
			throw new InvalidRequestException(ErrorCode.INPUT_INVALID_PAYLOAD,
					"from or to date");
		}

	}

	public void checkSubscriptionRequest(
			SubscriptionIndicatorRequest subscriptionIndicatorRequest) {
		utils.assertNonNull("reportId",
				subscriptionIndicatorRequest.getReportId());
		utils.assertNonNull("subscriptionIndicator",
				subscriptionIndicatorRequest.getSubscriptionIndicator());
		utils.assertNonNull("emailId",
				subscriptionIndicatorRequest.getEmailId());
		utils.assertNonEmpty("reportId",
				subscriptionIndicatorRequest.getReportId());
		utils.assertNonEmpty("subscriptionIndicator",
				subscriptionIndicatorRequest.getSubscriptionIndicator());
		utils.assertNonEmpty("emailId",
				subscriptionIndicatorRequest.getEmailId());
		checkValidEmailId(subscriptionIndicatorRequest.getEmailId());
	}

	private void checkValidEmailId(String emailId) {
		if (!emailId
				.matches("([\\w-]+(?:\\.[\\w-]+)*)@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)")) {
			LOGGER.debug("Invalid field : " + emailId + " with value : "
					+ emailId);
			throw new InvalidRequestException(ErrorCode.INPUT_INVALID_PAYLOAD,
					"emailId");
		}
	}

	public void checkReportIdentifier(ReportIdentifier reportIdentifier) {
		if (reportIdentifier == null) {
			throw new InvalidRequestException(ErrorCode.INPUT_INVALID_PAYLOAD,
					"Report Identifier.");
		}
	}

}
