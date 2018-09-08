package com.snapdeal.reviews.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.base.model.common.ServiceResponse;
import com.snapdeal.reviews.ReviewsApiTest;
import com.snapdeal.reviews.commons.config.ReviewConfiguration;
import com.snapdeal.reviews.commons.config.ReviewQualityWeightageConfiguration;
import com.snapdeal.reviews.commons.dto.DeleteRejectionReason;
import com.snapdeal.reviews.commons.dto.RejectionReason;
import com.snapdeal.reviews.commons.dto.RejectionReasonResponse;
import com.snapdeal.reviews.commons.dto.wrapper.CreateRejectionReasonResponse;
import com.snapdeal.reviews.commons.dto.wrapper.DeleteRejectionReasonRequest;
import com.snapdeal.reviews.commons.dto.wrapper.RejectionReasonsRequest;
import com.snapdeal.reviews.commons.dto.wrapper.ReviewConfigurationRequest;
import com.snapdeal.reviews.commons.dto.wrapper.ReviewConfigurationResponse;
import com.snapdeal.reviews.commons.pagination.ReviewListingSortType;

public class ConfigurationClientTest extends ReviewsApiTest {

	private ReviewConfiguration reviewConfiguration;

	private ReviewQualityWeightageConfiguration reviewQualityWeightageConfiguration;

	private Map<String , String> queryParams = new HashMap<String , String>();
	
	private ReviewConfiguration getTestDataReviewConfiguration() {
		ReviewConfiguration reviewConfiguration = new ReviewConfiguration();
		reviewConfiguration.setCreateReviewRecommendationMandatory(true);
		reviewConfiguration.setCreateReviewRecommendationVisibility(true);
		reviewConfiguration.setCreateReviewRichDataVisibilty(true);
		reviewConfiguration.setDefaultMinTitleSize(5);
		reviewConfiguration.setDefaultMaxTitleSize(20);
		reviewConfiguration.setDefaultMinCommentSize(20);
		reviewConfiguration.setDefaultMaxCommentSize(50);
		reviewConfiguration.setDefaultPaginationSize(500);
		reviewConfiguration
				.setDefaultReviewListingSortType(ReviewListingSortType.HELPFUL_DESC);

		return reviewConfiguration;
	}

	/*
	 * private ReviewQualityWeightageConfiguration
	 * getTestDataReviewQualityWeightageConfiguration() {
	 * ReviewQualityWeightageConfiguration reviewQualityWeightageConfiguration =
	 * new ReviewQualityWeightageConfiguration();
	 * reviewQualityWeightageConfiguration
	 * .getFreshnessConfiguration().setWeightage(0.2f);
	 * reviewQualityWeightageConfiguration.setGetsFlaggedWeightage(0.05f);
	 * reviewQualityWeightageConfiguration
	 * .getHelpFulVoteConfiguration().setWeightage(0.1f);getAllProductsReviewedByUser
	 * reviewQualityWeightageConfiguration
	 * .getModeratorConfiguration().setWeightage(.03f);;
	 * reviewQualityWeightageConfiguration
	 * .getLengthOfReviewConfiguration().setWeightage(.5f);;
	 * reviewQualityWeightageConfiguration
	 * .getTypeOfReviewerConfiguration().setWeightage(.1f);;
	 * reviewQualityWeightageConfiguration.setLocalLanguageWeightage(0.1f);
	 * 
	 * return reviewQualityWeightageConfiguration; }
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testSetConfigurationSettings() throws SnapdealWSException {
		ReviewConfigurationRequest reviewConfigurationRequest = new ReviewConfigurationRequest();
		reviewConfiguration = getTestDataReviewConfiguration();
		reviewConfigurationRequest.setReviewConfiguration(reviewConfiguration);
		ServiceResponse serviceResponse = (ServiceResponse) getConfigurationClient()
				.setReviewConfiguration(reviewConfigurationRequest);
		Assert.assertEquals(true, serviceResponse.isSuccessful());
	}

	@Test
	public void testGetConfigurationSettings() throws SnapdealWSException {
		ReviewConfiguration expected = getTestDataReviewConfiguration();
		ReviewConfigurationResponse reviewConfigurationResponse = getConfigurationClient()
				.getReviewConfiguration();
		ReviewConfiguration actual = reviewConfigurationResponse
				.getReviewConfiguration();

		Assert.assertEquals(expected.getDefaultReviewListingSortType().name(),
				actual.getDefaultReviewListingSortType().name());
		Assert.assertEquals(expected.getDefaultMinTitleSize(),
				actual.getDefaultMinTitleSize());
		Assert.assertEquals(expected.getDefaultMaxTitleSize(),
				actual.getDefaultMaxTitleSize());
		Assert.assertEquals(expected.getDefaultMinCommentSize(),
				actual.getDefaultMinCommentSize());
		Assert.assertEquals(expected.getDefaultMaxCommentSize(),
				actual.getDefaultMaxCommentSize());
		Assert.assertEquals(expected.getDefaultPaginationSize(),
				actual.getDefaultPaginationSize());
		Assert.assertEquals(expected.getCreateReviewRecommendationMandatory(),
				actual.getCreateReviewRecommendationMandatory());
		Assert.assertEquals(expected.getCreateReviewRecommendationVisibility(),
				actual.getCreateReviewRecommendationVisibility());
		Assert.assertEquals(expected.getCreateReviewRichDataVisibilty(),
				actual.getCreateReviewRichDataVisibilty());
	}

	// Negative Scenario for set configuration APIs

	// maxTitleSize less than minTitleSize
	@Test
	public void testSetConfigurationSettingsMaxGreaterThanMinTitleSize()
			throws SnapdealWSException {
		ReviewConfigurationRequest reviewConfigurationRequest = new ReviewConfigurationRequest();
		reviewConfiguration = getTestDataReviewConfiguration();
		reviewConfiguration.setDefaultMinTitleSize(15);
		reviewConfiguration.setDefaultMaxTitleSize(10);
		reviewConfigurationRequest.setReviewConfiguration(reviewConfiguration);
		try {
			getConfigurationClient().setReviewConfiguration(
					reviewConfigurationRequest);
		} catch (Exception e) {
			Assert.assertTrue(e.getCause().getMessage()
					.contains("INPUT_INVALID_PAYLOAD"));
		}
	}

	// Negative Input
	@Test
	public void testSetConfigurationSettingsWithNegativeInput()
			throws SnapdealWSException {
		ReviewConfigurationRequest reviewConfigurationRequest = new ReviewConfigurationRequest();
		reviewConfiguration = getTestDataReviewConfiguration();
		reviewConfiguration.setDefaultPaginationSize(-1);
		reviewConfigurationRequest.setReviewConfiguration(reviewConfiguration);
		try {
			getConfigurationClient().setReviewConfiguration(
					reviewConfigurationRequest);
		} catch (Exception e) {
			Assert.assertTrue(e.getCause().getMessage()
					.contains("INPUT_INVALID_PAYLOAD"));
		}
	}

	/*
	 * Test Cases For Review Quality Weightage Configuration Settings
	 */

	/*
	 * @SuppressWarnings("deprecation")
	 * 
	 * @Test public void testSetReviewQualityWeightageSettings() throws
	 * SnapdealWSException { reviewQualityWeightageConfiguration =
	 * getTestDataReviewQualityWeightageConfiguration();
	 * ReviewQualityWeightageConfigurationRequest request = new
	 * ReviewQualityWeightageConfigurationRequest();
	 * request.setReviewQualityWeightageConfiguration
	 * (reviewQualityWeightageConfiguration); ServiceResponse serviceResponse =
	 * (ServiceResponse) getConfigurationClient()
	 * .setReviewQualityWeightageConfiguration(request);
	 * Assert.assertEquals(true, serviceResponse.isSuccessful()); }
	 * 
	 * @Test public void testGetReviewQualityWeightageSettings() throws
	 * SnapdealWSException { ReviewQualityWeightageConfiguration expected =
	 * getTestDataReviewQualityWeightageConfiguration();
	 * ReviewQualityWeightageConfigurationResponse response =
	 * getConfigurationClient() .getReviewQualityWeightageConfiguration();
	 * ReviewQualityWeightageConfiguration actual = response
	 * .getReviewQualityWeightageConfiguration();
	 * 
	 * Assert.assertEquals(expected.getFreshnessConfiguration().getWeightage(),
	 * actual.getFreshnessConfiguration().getWeightage());
	 * Assert.assertEquals(expected.getGetsFlaggedWeightage(),
	 * actual.getGetsFlaggedWeightage());
	 * Assert.assertEquals(expected.getHelpFulVoteConfiguration
	 * ().getWeightage(), actual.getHelpFulVoteConfiguration().getWeightage());
	 * Assert.assertEquals(expected.getLocalLanguageWeightage(),
	 * actual.getLocalLanguageWeightage());
	 * Assert.assertEquals(expected.getModeratorConfiguration().getWeightage(),
	 * actual.getModeratorConfiguration().getWeightage());
	 * Assert.assertEquals(expected
	 * .getLengthOfReviewConfiguration().getWeightage(),
	 * actual.getLengthOfReviewConfiguration().getWeightage());
	 * Assert.assertEquals
	 * (expected.getTypeOfReviewerConfiguration().getWeightage(),
	 * actual.getTypeOfReviewerConfiguration().getWeightage()); }
	 * 
	 * // Negative Scenarios
	 * 
	 * // If total weightage exceeds 1
	 * 
	 * @Test public void testSetWeightageConfigurationWithWeightageExceedsOne(){
	 * ReviewQualityWeightageConfigurationRequest request = new
	 * ReviewQualityWeightageConfigurationRequest();
	 * reviewQualityWeightageConfiguration =
	 * getTestDataReviewQualityWeightageConfiguration();
	 * reviewQualityWeightageConfiguration
	 * .getFreshnessConfiguration().setWeightage(0.6f);;
	 * request.setReviewQualityWeightageConfiguration
	 * (reviewQualityWeightageConfiguration); try {
	 * getConfigurationClient().setReviewQualityWeightageConfiguration(request);
	 * } catch (Exception e) { Assert.assertTrue(e.getCause().getMessage()
	 * .contains("INPUT_INVALID_PAYLOAD")); } }
	 * 
	 * // If any input in negative
	 * 
	 * @Test public void testSetWeightageConfigurationWithNegativeInputs(){
	 * ReviewQualityWeightageConfigurationRequest request = new
	 * ReviewQualityWeightageConfigurationRequest();
	 * reviewQualityWeightageConfiguration =
	 * getTestDataReviewQualityWeightageConfiguration();
	 * reviewQualityWeightageConfiguration
	 * .getFreshnessConfiguration().setWeightage(-1f);;
	 * request.setReviewQualityWeightageConfiguration
	 * (reviewQualityWeightageConfiguration); try {
	 * getConfigurationClient().setReviewQualityWeightageConfiguration(request);
	 * } catch (Exception e) { Assert.assertTrue(e.getCause().getMessage()
	 * .contains("INPUT_INVALID_PAYLOAD")); } }
	 * 
	 * // If any input is greater than 1 /* @Test public void
	 * testSetWeightageConfigurationWithInputsGreaterThanOne(){
	 * ReviewQualityWeightageConfigurationRequest request = new
	 * ReviewQualityWeightageConfigurationRequest();
	 * reviewQualityWeightageConfiguration =
	 * getTestDataReviewQualityWeightageConfiguration();
	 * reviewQualityWeightageConfiguration
	 * .getFreshnessConfiguration().setWeightage(1.4f);
	 * request.setReviewQualityWeightageConfiguration
	 * (reviewQualityWeightageConfiguration); try {
	 * getConfigurationClient().setReviewQualityWeightageConfiguration(request);
	 * } catch (Exception e) { Assert.assertTrue(e.getCause().getMessage()
	 * .contains("INPUT_INVALID_PAYLOAD")); } }
	 */

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * Test Cases For Rejection Reasons
	 */
	public RejectionReason prepareRejectionReason() {
		RejectionReason rejectionReason = new RejectionReason();
		rejectionReason.setRejectionReason(RandomStringUtils
				.randomAlphabetic(20));
		return rejectionReason;
	}

	@Test
	public void testCreateRejectionReason() throws SnapdealWSException {
		RejectionReasonsRequest request = new RejectionReasonsRequest();
		request.setRejectionReasonRequest(prepareRejectionReason());
		CreateRejectionReasonResponse response = getConfigurationClient()
				.addRejectionReasons(request);
		Assert.assertEquals(request.getRejectionReasonRequest()
				.getRejectionReason(), response.getRejectionReason().getValue());
	}

	@Test
	public void testDeleteRejectionReason() throws SnapdealWSException {
		RejectionReasonsRequest request = new RejectionReasonsRequest();
		request.setRejectionReasonRequest(prepareRejectionReason());
		CreateRejectionReasonResponse response = getConfigurationClient()
				.addRejectionReasons(request);
		DeleteRejectionReasonRequest deleteRequest = new DeleteRejectionReasonRequest();
		DeleteRejectionReason deleteRejectionReason = new DeleteRejectionReason();
		deleteRejectionReason.setRejectionReasonUUID(response
				.getRejectionReason().getId().toString());
		ServiceResponse deletionResponse = getConfigurationClient()
				.deleteRejectionReasons(deleteRequest);

		Assert.assertTrue(deletionResponse.isSuccessful());
	}

	@Test
	public void testGetRejectionReasons() throws SnapdealWSException {
		RejectionReasonResponse response = getConfigurationClient()
				.getRejectionReasons(queryParams);
		Assert.assertTrue(response.isSuccessful());
	}

	// Negative Scenarios for Rejection Reasons

	// creating an already created review
	@Test
	public void testCreationOfAlreadyCreatedReason() throws SnapdealWSException {
		RejectionReasonsRequest request = new RejectionReasonsRequest();
		request.setRejectionReasonRequest(prepareRejectionReason());
		CreateRejectionReasonResponse response = getConfigurationClient()
				.addRejectionReasons(request);
		try {
			getConfigurationClient().addRejectionReasons(request);
		} catch (SnapdealWSException e) {
			Assert.assertEquals(403, e.getWsErrorCode());
		}
	}

	// Deletion with invalid reasonUUID
	@Test
	public void testDeletionWithInvalidReasonUUID() throws SnapdealWSException {
		DeleteRejectionReasonRequest deleteRequest = new DeleteRejectionReasonRequest();
		DeleteRejectionReason deleteRejectionReason = new DeleteRejectionReason();
		deleteRejectionReason.setRejectionReasonUUID("test1223");
		deleteRequest.setDeleteRejectionReason(deleteRejectionReason);
		try {
			getConfigurationClient().deleteRejectionReasons(deleteRequest);
		} catch (SnapdealWSException e) {
			Assert.assertEquals(400, e.getWsErrorCode());
		}
	}

	// Deletion with reason already deleted
	@Test
	public void testAlreadyDeletedReviewReasonUUID() throws SnapdealWSException {
		RejectionReasonsRequest request = new RejectionReasonsRequest();
		request.setRejectionReasonRequest(prepareRejectionReason());
		CreateRejectionReasonResponse response = getConfigurationClient()
				.addRejectionReasons(request);
		DeleteRejectionReasonRequest deleteRequest = new DeleteRejectionReasonRequest();
		DeleteRejectionReason deleteRejectionReason = new DeleteRejectionReason();
		deleteRejectionReason.setRejectionReasonUUID(response
				.getRejectionReason().getId().toString());
		deleteRequest.setDeleteRejectionReason(deleteRejectionReason);
		getConfigurationClient().deleteRejectionReasons(deleteRequest);
		try {
			getConfigurationClient().deleteRejectionReasons(deleteRequest);
		} catch (SnapdealWSException e) {
			Assert.assertEquals(403, e.getWsErrorCode());
		}

	}

	// Deletion with valid reason UUID but reason doesn't Exists
	@Test
	public void testForReasonNotExits() throws SnapdealWSException {
		String reasonUUID = UUID.randomUUID().toString();
		DeleteRejectionReasonRequest deleteRequest = new DeleteRejectionReasonRequest();
		DeleteRejectionReason deleteRejectionReason = new DeleteRejectionReason();
		deleteRejectionReason.setRejectionReasonUUID(reasonUUID);
		deleteRequest.setDeleteRejectionReason(deleteRejectionReason);
		try {
			getConfigurationClient().deleteRejectionReasons(deleteRequest);
		} catch (SnapdealWSException e) {
			Assert.assertEquals(403, e.getWsErrorCode());
		}
	}
}
