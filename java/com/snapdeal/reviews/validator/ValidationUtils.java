package com.snapdeal.reviews.validator;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.snapdeal.reviews.commons.OpinionBo;
import com.snapdeal.reviews.commons.config.BuyerType;
import com.snapdeal.reviews.commons.config.EmailTemplate;
import com.snapdeal.reviews.commons.config.RangeConfigration;
import com.snapdeal.reviews.commons.config.ReviewerStaticConfiguration;
import com.snapdeal.reviews.commons.config.VoteLabel;
import com.snapdeal.reviews.commons.config.VoteStaticConfiguration;
import com.snapdeal.reviews.exception.ErrorCode;
import com.snapdeal.reviews.exception.client.InvalidRequestException;

@Component
public class ValidationUtils {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ValidationUtils.class);

	public void assertNonNull(String fieldName, Object value) {
		if (value == null || value.toString().trim().isEmpty()) {
			LOGGER.debug("Invalid NonNull field : " + fieldName
					+ " with value : " + value);
			throw new InvalidRequestException(ErrorCode.INPUT_INVALID_PAYLOAD,
					fieldName);
		}
	}

	public void validate(EmailTemplate emailTemplate, String fieldName) {
		if (emailTemplate.getIsTaskEnabled() == null
				|| emailTemplate.getEmailSubject() == null
				|| (emailTemplate.getNumberOfPreviousOrders() == null || emailTemplate
						.getNumberOfPreviousOrders() < 0)) {
			throw new InvalidRequestException(ErrorCode.INPUT_INVALID_PAYLOAD,
					fieldName);
		}
	}

	public void assertRange(String fieldName, int value, int min, int max) {
		if (value < min || value > max) {
			LOGGER.debug("Invalid Range field : " + fieldName + " with max : "
					+ max + ", min : " + min + ", value : " + value);
			throw new InvalidRequestException(ErrorCode.INPUT_INVALID_PAYLOAD,
					fieldName);
		}
	}
	
	public void assertRange(String fieldName, List<Integer> valueList, int min, int max) {
		for(final int value : valueList){
			if (value < min || value > max) {
				LOGGER.debug("Invalid Range field : " + fieldName + " with max : "
						+ max + ", min : " + min + ", value : " + value);
				throw new InvalidRequestException(ErrorCode.INPUT_INVALID_PAYLOAD,
						fieldName);
			}
		}
	}

	public void assertMaxSize(String fieldName, String value, int sizeToAssert) {
		if (value.length() > sizeToAssert) {
			LOGGER.debug("Invalid field : " + fieldName + " with value : "
					+ value + ", sizeToAssert : " + sizeToAssert);
			throw new InvalidRequestException(ErrorCode.INPUT_INVALID_PAYLOAD,
					fieldName);
		}
	}

	public void assertPositive(String fieldName, long value) {
		if (value < 0) {
			LOGGER.debug("Invalid field : " + fieldName + " with value : "
					+ value);
			throw new InvalidRequestException(ErrorCode.INPUT_INVALID_PAYLOAD,
					fieldName);
		}
	}

	public void assertMinSize(String fieldName, String value, int sizeToAssert) {
		if (value.length() < sizeToAssert) {
			LOGGER.debug("Invalid field : " + fieldName + " with value : "
					+ value + ", sizeToAssert : " + sizeToAssert);
			throw new InvalidRequestException(ErrorCode.INPUT_INVALID_PAYLOAD,
					fieldName);
		}
	}

	public void assertNonEmpty(String fieldName, Object value) {
		if (value.toString().trim().isEmpty()) {
			LOGGER.debug("Invalid field : " + fieldName + " with value : "
					+ value);
			throw new InvalidRequestException(ErrorCode.INPUT_INVALID_PAYLOAD,
					fieldName);
		}
	}

	public void assertLength(String fieldName, String value, int min, int max) {
		assertMinSize(fieldName, value, min);
		assertMaxSize(fieldName, value, max);
	}

	public void assertUUID(String uuid, String fieldName) {
		try {
			UUID.fromString(uuid);
		} catch (Exception e) {
			throw new InvalidRequestException(ErrorCode.INPUT_INVALID_PAYLOAD,
					fieldName);
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Enum<T>> T validate(String fieldName, String value, T en) {
		try {
			return (T) T.valueOf(en.getClass(), value);
		} catch (IllegalArgumentException e) {
			throw new InvalidRequestException(ErrorCode.INPUT_INVALID_PAYLOAD,
					fieldName);
		}
	}

	public void assertRecommendValue(OpinionBo recommended) {
		if ((null == recommended)
				|| (!(recommended.getId() == OpinionBo.YES.getId() || recommended
						.getId() == OpinionBo.NO.getId()))) {
			LOGGER.debug("Invalid field : recommended with value : "
					+ recommended);
			throw new InvalidRequestException(ErrorCode.INPUT_INVALID_PAYLOAD,
					"Recommended should be YES or NO");
		}

	}

	public void assertTrue(String message, boolean value) {
		if (true != value) {
			throw new InvalidRequestException(ErrorCode.INPUT_INVALID_PAYLOAD,
					message);
		}
	}

	public void assertRange(String fieldName, Float value, float min, float max) {
		isNullOrNot(value, fieldName);
		if (value < min || value > max) {
			LOGGER.debug("Invalid Range field : " + fieldName + " with max : "
					+ max + ", min : " + min + ", value : " + value);
			throw new InvalidRequestException(ErrorCode.INPUT_INVALID_PAYLOAD,
					fieldName);
		}
	}

	public void isNullOrNot(Object obj, String fieldName) {
		if (obj == null) {
			LOGGER.debug("Invalid  : " + fieldName);
			throw new InvalidRequestException(ErrorCode.INPUT_INVALID_PAYLOAD,
					fieldName);
		}
	}

	public void dynamicOverlapping(List<RangeConfigration> rangeConfiguration,
			String fieldName) {
		isNullOrNot(rangeConfiguration, fieldName);
		int temp = -1;

		for (RangeConfigration rc : rangeConfiguration) {
			isNullOrNot(rc, fieldName);
			Integer startValue = rc.getStartValue();
			isNullOrNot(startValue, fieldName);
			Integer endValue = rc.getEndValue();
			isNullOrNot(endValue, fieldName);
			if ((temp + 1) != startValue || startValue > endValue) {
				LOGGER.debug("Invalid Point Range : " + fieldName);
				throw new InvalidRequestException(
						ErrorCode.INPUT_INVALID_PAYLOAD, fieldName);
			}
			assertNonNull(fieldName, rc.getPoints());
			assertRange(fieldName, rc.getPoints(), 0, 10);
			temp = endValue;
		}
	}

	private void isCertified(ReviewerStaticConfiguration sc,String fieldName) {
		isNullOrNot(sc, fieldName);
		if (BuyerType.CERTIFIED == sc.getLabel())
		{
			assertNonNull(fieldName, sc.getPoints());
			assertRange(fieldName, sc.getPoints(), 0, 10);
		}
		else {
			LOGGER.debug("Invalid Label: " + sc.getLabel());
			throw new InvalidRequestException(
					ErrorCode.INPUT_INVALID_PAYLOAD, "certified");
		}
	}

	private void isNonCertified(ReviewerStaticConfiguration sc,String fieldName) {
		isNullOrNot(sc, fieldName);
		if (BuyerType.NON_CERTIFIED == sc.getLabel())
		{
			assertNonNull(fieldName, sc.getPoints());
			assertRange(fieldName, sc.getPoints(), 0, 10);
		}
		else {
			LOGGER.debug("Invalid Label: " + sc.getLabel());
			throw new InvalidRequestException(
					ErrorCode.INPUT_INVALID_PAYLOAD, "non certified");
		}
	}

	public void staticOverlappingForReviewer(
			List<ReviewerStaticConfiguration> staticConfiguration,
			String fieldName) {
		isNullOrNot(staticConfiguration, fieldName);
		if(staticConfiguration.size()==2)
		{
			isCertified(staticConfiguration.get(0), fieldName);
			isNonCertified(staticConfiguration.get(1), fieldName);
		}
	else {
				LOGGER.debug("Invalid Label: " + fieldName);
				throw new InvalidRequestException(
						ErrorCode.INPUT_INVALID_PAYLOAD, fieldName);
			}
		
		}

	public void staticOverlappingForHelpfulVotes(
			List<VoteStaticConfiguration> staticConfiguration, String fieldName) {
		for (VoteStaticConfiguration sc : staticConfiguration) {
			isNullOrNot(sc, fieldName);
			if (VoteLabel.EVERY_UP_VOTE == sc.getLabel()) {
				assertNonNull(fieldName, sc.getPoints());
				assertRange(fieldName, sc.getPoints(), 0, 10);
			} else {
				LOGGER.debug("Invalid Label: " + sc.getLabel());
				throw new InvalidRequestException(
						ErrorCode.INPUT_INVALID_PAYLOAD, fieldName);
			}

		}
	}
}
