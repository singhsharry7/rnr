package com.snapdeal.reviews.validator;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snapdeal.reviews.commons.config.ConfigurationType;
import com.snapdeal.reviews.commons.config.MailConfiguration;
import com.snapdeal.reviews.commons.config.RejectionReasonConfiguration;
import com.snapdeal.reviews.commons.config.ReviewConfiguration;
import com.snapdeal.reviews.commons.config.ReviewQualityWeightageConfiguration;
import com.snapdeal.reviews.commons.dto.DeleteRejectionReason;
import com.snapdeal.reviews.exception.ErrorCode;
import com.snapdeal.reviews.exception.client.ForbiddenException;
import com.snapdeal.reviews.service.ConfigurationService;

@Component
public class ConfigurationInputValidator {

	@Autowired
	private ValidationUtils utils;

	@Autowired
	private ConfigurationService configurationService;

	public void validate(ReviewConfiguration request) {
		utils.assertPositive("defaultMinTitleSize",
				request.getDefaultMinTitleSize());
		utils.assertPositive("defaultMaxTitleSize",
				request.getDefaultMaxTitleSize());
		utils.assertPositive("defaultMinCommentSize",
				request.getDefaultMinCommentSize());
		utils.assertPositive("defaultMaxCommentSize",
				request.getDefaultMaxCommentSize());
		utils.assertPositive("defaultAutoCheckInTimeForTwentyReviews",
				request.getDefaultAutoCheckInTimeForTwentyReviews());
		utils.assertPositive("defaultAutoCheckInTimeForFiftyReviews",
				request.getDefaultAutoCheckInTimeForFiftyReviews());
		utils.assertPositive("defaultAutoCheckInTimeAboveFiftyReviews",
				request.getDefaultAutoCheckInTimeAboveFiftyReviews());
		utils.assertPositive("defaultPaginationSize",
				request.getDefaultPaginationSize());

		utils.assertRange("defaultMinTitleSize",
				request.getDefaultMinTitleSize(), 0,
				request.getDefaultMaxTitleSize());
		utils.assertRange("defaultMaxTitleSize",
				request.getDefaultMaxTitleSize(),
				request.getDefaultMinTitleSize() + 1, Integer.MAX_VALUE);
		utils.assertRange("defaultMinCommentSize",
				request.getDefaultMinCommentSize(), 0,
				request.getDefaultMaxCommentSize());
		utils.assertRange("defaultMaxCommentSize",
				request.getDefaultMaxCommentSize(),
				request.getDefaultMinCommentSize() + 1, Integer.MAX_VALUE);
		utils.assertRange(
				"Auto Check-in Time Sequence(T(20) <= T(50) <= T(50+))",
				request.getDefaultAutoCheckInTimeForFiftyReviews(),
				request.getDefaultAutoCheckInTimeForTwentyReviews(),
				request.getDefaultAutoCheckInTimeAboveFiftyReviews());

	}

	public void validate(ReviewQualityWeightageConfiguration request) {

		utils.assertRange("freshnessOfReviewWeightage", request
				.getFreshnessConfiguration().getWeightage(), 0, 1);
		utils.dynamicOverlapping(request.getFreshnessConfiguration()
				.getRangeConfiguration(), "value for freshness field");

		// utils.assertRange("getsFlaggedWeightage",
		// request.getGetsFlaggedWeightage(), 0, 1);

		utils.assertRange("helpfulVotesWeightage", request
				.getHelpFulVoteConfiguration().getWeightage(), 0, 1);
		utils.staticOverlappingForHelpfulVotes(request
				.getHelpFulVoteConfiguration().getStaticConfigurations(),
				"value for helpfullVote field");

		// utils.assertRange("localLanguageWeightage",
		// request.getLocalLanguageWeightage(), 0, 1);

		utils.assertRange("moderatorScoreWeightage", request
				.getModeratorConfiguration().getWeightage(), 0, 1);

		utils.assertRange("lengthOfReviewWeightage", request
				.getLengthOfReviewConfiguration().getWeightage(), 0, 1);
		utils.dynamicOverlapping(request.getLengthOfReviewConfiguration()
				.getRangeConfiguration(), "value for lengthOfReview field");

		utils.assertRange("typeOfReviewerWeightage", request
				.getTypeOfReviewerConfiguration().getWeightage(), 0, 1);
		utils.staticOverlappingForReviewer(request
				.getTypeOfReviewerConfiguration().getStaticConfigurations(),
				"value for typeOfReviewer field");

		utils.assertTrue("Total Weightage must be equal to 1",
				request.checkTotalWeightage());
	}

	public void validateRejectionReasonUUID(DeleteRejectionReason request) {
		utils.assertUUID(request.getRejectionReasonUUID(),
				"Rejection reason ID should be UUID");
		RejectionReasonConfiguration rejectionReasonConfiguration = configurationService
				.getConfiguration(ConfigurationType.REJECTION_REASON_CONFIG);
		UUID reasonUUID = UUID.fromString(request.getRejectionReasonUUID());
		if (null == rejectionReasonConfiguration) {
			throw new ForbiddenException(ErrorCode.FORBIDDEN_ERROR,
					"Rejection Reason Not Exists");
		}
		if (null != rejectionReasonConfiguration
				&& !rejectionReasonConfiguration
						.containsRejectionReasonUUID(reasonUUID)) {
			throw new ForbiddenException(ErrorCode.FORBIDDEN_ERROR,
					"Rejection Reason Not Exists");
		}
	}

	public void checkRejectionReasonIfexists(String rejectionReason) {
		RejectionReasonConfiguration rejectionReasonConfiguration = configurationService
				.getConfiguration(ConfigurationType.REJECTION_REASON_CONFIG);
		if (null != rejectionReasonConfiguration
				&& rejectionReasonConfiguration
						.containsRejectionReason(rejectionReason)) {
			throw new ForbiddenException(ErrorCode.FORBIDDEN_ERROR,
					"Rejection Reason Already Exists");
		}
	}

	public void validate(MailConfiguration request) {
		utils.assertPositive(
				"numberofpreviousoderacktask for firstreviewrequestemail",
				request.getFirstReviewRequestEmailConfiguration()
						.getNumberOfPreviousOrders());
		utils.assertPositive(
				"numberofpreviousoderacktask for leaderboardupdateemail",
				request.getLeaderboardUpdateEmailConfiguration()
						.getNumberOfPreviousOrders());
		utils.assertPositive(
				"numberofpreviousoderacktask for rejectedreviewemail", request
						.getRejectedReviewEmailConfiguration()
						.getNumberOfPreviousOrders());
		utils.assertPositive(
				"numberofpreviousoderacktask for reviewacknowledgementemail",
				request.getReviewAcknowledgementEmailConfiguration()
						.getNumberOfPreviousOrders());
		utils.assertPositive(
				"numberofpreviousoderacktask for reviewhelpfulemail", request
						.getReviewHelpfulEmailConfiguration()
						.getNumberOfPreviousOrders());
		utils.assertPositive("numberofupvote for reviewhelpfulemail", request
				.getReviewHelpfulEmailConfiguration().getNumberOfUpVote());
		utils.assertPositive(
				"numberofpreviousoderacktask for secondReviewrequestemail",
				request.getSecondReviewRequestEmailConfiguration()
						.getNumberOfPreviousOrders());
		utils.assertPositive(
				"numberofpreviousoderacktask for welcometoleaderboardemail",
				request.getWelcomeToLeaderboardEmailConfiguration()
						.getNumberOfPreviousOrders());
		utils.assertPositive(
				"numberofpreviousoderacktask for reviewapprovedemail", request
						.getReviewApprovedEmailConfiguration()
						.getNumberOfPreviousOrders());
		utils.assertPositive("ndaysafterdelievery for firstreviewrequestemail",
				request.getFirstReviewRequestEmailConfiguration()
						.getNDaysAfterDelivery());
		utils.assertPositive(
				"ndaysafterdelievery for secondReviewrequestemail", request
						.getSecondReviewRequestEmailConfiguration()
						.getNDaysAfterDelivery());

	}
}
