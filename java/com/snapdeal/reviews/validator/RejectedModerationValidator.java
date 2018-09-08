package com.snapdeal.reviews.validator;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snapdeal.reviews.commons.config.ConfigurationType;
import com.snapdeal.reviews.commons.config.RejectionReasonConfiguration;
import com.snapdeal.reviews.commons.dto.Status;
import com.snapdeal.reviews.exception.ErrorCode;
import com.snapdeal.reviews.exception.client.InvalidRequestException;
import com.snapdeal.reviews.model.ReviewModerationBo;
import com.snapdeal.reviews.service.ConfigurationService;

@Component
public class RejectedModerationValidator implements ReviewStatusModerationValidator {

	@Autowired
	private ConfigurationService configurationService;
	
	@Autowired
	private ValidationUtils utils;
	
	@Override
	public void validate(ReviewModerationBo request) {
		RejectionReasonConfiguration rejectionReasonConfiguration = configurationService.getConfiguration(ConfigurationType.REJECTION_REASON_CONFIG);
		if(null != rejectionReasonConfiguration){
			if (request.getReviewRejectionReasonID() == null) {
				throw new InvalidRequestException(ErrorCode.INPUT_INVALID_PAYLOAD,
						"Rejected Review Should have Rejected Reason");
			}else{
				utils.assertUUID(request.getReviewRejectionReasonID(), "rejectionReasonID should be UUID");
				if(!rejectionReasonConfiguration.containsRejectionReasonUUID(UUID.fromString(request.getReviewRejectionReasonID())))
					throw new InvalidRequestException(ErrorCode.INPUT_INVALID_PAYLOAD,
						"Rejection Reason");
			}
		}
	}

	@Override
	public Status getCheckInStatus() {
		return Status.REJECTED;
	}

}
