package com.snapdeal.reviews;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.base.model.common.ServiceResponse;
import com.snapdeal.reviews.commons.dto.SampleReview;
import com.snapdeal.reviews.commons.dto.wrapper.CreateSampleReviewRequest;
import com.snapdeal.reviews.commons.dto.wrapper.GetLabelsResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetSampleReviewResponse;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateSampleReviewRequest;

public class SampleReviewTest extends ReviewsApiTest
{

    @Test
    public void create_sampleReview_valid_test() throws SnapdealWSException
    {

	createReview(getCreateSampleReviewRequest());

    }

    @Test
    public void create_sampleReview_inValid_label()
    {
	try
	{
	   CreateSampleReviewRequest createSampleReviewRequest = getCreateSampleReviewRequest();
	   createSampleReviewRequest.getSampleReview().setLabel(null);
	   ServiceResponse serviceResponse = getClient().createSampleReview(
		   createSampleReviewRequest);
	}
	catch (SnapdealWSException ex)
	{
	   System.out.println("Expected Exception for invalid request");
	   Assert.assertEquals(400 , ex.getWsErrorCode());
	}

    }

    @Test
    public void create_sampleReview_inValid_comment()
    {
	ServiceResponse serviceResponse = null;
	try
	{
	   CreateSampleReviewRequest createSampleReviewRequest = getCreateSampleReviewRequest();
	   createSampleReviewRequest.getSampleReview().setComments(null);
	   serviceResponse = getClient().createSampleReview(
		   createSampleReviewRequest);
	}
	catch (SnapdealWSException ex)
	{
	   System.out.println("Expected Exception for invalid request");
	   Assert.assertEquals(400 , ex.getWsErrorCode());
	}

	Assert.assertNull(serviceResponse);

    }

    @Test
    public void create_sampleReview_inValid_rating()
    {
	ServiceResponse serviceResponse = null;
	try
	{
	   CreateSampleReviewRequest createSampleReviewRequest = getCreateSampleReviewRequest();
	   createSampleReviewRequest.getSampleReview().setRating(null);
	   serviceResponse = getClient().createSampleReview(
		   createSampleReviewRequest);
	}
	catch (SnapdealWSException ex)
	{
	   System.out.println("Expected Exception for invalid request");
	   Assert.assertEquals(400 , ex.getWsErrorCode());
	}
	Assert.assertNull(serviceResponse);
    }

    @Test
    public void create_sampleReview_inValid_headLine()
    {
	ServiceResponse serviceResponse = null;
	try
	{
	   CreateSampleReviewRequest createSampleReviewRequest = getCreateSampleReviewRequest();
	   createSampleReviewRequest.getSampleReview().setHeadline(null);
	   serviceResponse = getClient().createSampleReview(
		   createSampleReviewRequest);
	}
	catch (SnapdealWSException ex)
	{
	   System.out.println("Expected Exception for invalid request");
	   Assert.assertEquals(400 , ex.getWsErrorCode());
	}
	Assert.assertNull(serviceResponse);
    }

    @Test
    public void get_sampleReview_valid_test()
    {
	GetSampleReviewResponse getSampleReviewResponse = null;
	String label = getLabel();
	CreateSampleReviewRequest createSampleReviewRequest = getCreateSampleReviewRequest();
	createSampleReviewRequest.getSampleReview().setLabel(label);
	try
	{
	   createReview(createSampleReviewRequest);
	   Map<String , String> getSampleReviewParams = new HashMap<String , String>();
	   getSampleReviewParams.put("label" , label);
	   getSampleReviewResponse = getClient().getSampleReview(
		   getSampleReviewParams);
	   Assert.assertNotNull(getSampleReviewResponse);
	   Assert.assertNotNull(getSampleReviewResponse.getSampleReview());
	}
	catch (SnapdealWSException ex)
	{
	   Assert.assertEquals(200 , ex.getWsErrorCode());
	   System.out.println("Expected Exception for invalid request");
	}

    }

    private void createReview(
	   CreateSampleReviewRequest createSampleReviewRequest)
    {
	ServiceResponse serviceResponse = null;
	try
	{
	     serviceResponse = getClient().createSampleReview(
		    createSampleReviewRequest);
	    Assert.assertNotNull(serviceResponse);

	}
	catch (SnapdealWSException ex)
	{
	   Assert.assertEquals(201 , ex.getWsErrorCode());
	   System.out.println("Expected Exception for invalid request");
	}
	 Assert.assertNotNull(serviceResponse);
    }

    @Test
    public void get_sampleReview_invalid_test()
    {
	GetSampleReviewResponse getSampleReviewResponse = null;
	try
	{
	   Map<String , String> getSampleReviewParams = new HashMap<String , String>();
	   getSampleReviewParams.put("label" ,
		   RandomStringUtils.randomAlphabetic(10));
	   getSampleReviewResponse = getClient().getSampleReview(
		   getSampleReviewParams);
	   Assert.assertNotNull(getSampleReviewResponse);
	}
	catch (SnapdealWSException ex)
	{
	   System.out.println("Expected Exception for invalid request");
	   Assert.assertEquals(404 , ex.getWsErrorCode());
	}

    }

    @Test
    public void update_sampleReview_valid_test()
    {

	String label = getLabel();
	CreateSampleReviewRequest createSampleReviewRequest = getCreateSampleReviewRequest();
	createSampleReviewRequest.getSampleReview().setLabel(label);
	createReview(createSampleReviewRequest);
	SampleReview sampleReview = getSampleReviewByLabel(label);
	String nickName = RandomStringUtils.randomAlphabetic(10);
	UpdateSampleReviewRequest updateSampleReviewRequest = getUpdateSampleReviewRequest();
	updateSampleReviewRequest.setSampleReview(sampleReview);
	updateSampleReviewRequest.getSampleReview().setNickName(nickName);
	updateReview(updateSampleReviewRequest);
	sampleReview = getSampleReviewByLabel(label);
	Assert.assertEquals(nickName , sampleReview.getNickName());

    }

    private void updateReview(
	   UpdateSampleReviewRequest updateSampleReviewRequest)
    {
	try
	{
	   ServiceResponse serviceResponse = getClient().updateSampleReview(
		   updateSampleReviewRequest);
	   Assert.assertNotNull(serviceResponse);
	}
	catch (SnapdealWSException ex)
	{
	   Assert.assertEquals(200 , ex.getWsErrorCode());
	   System.out.println("Expected Exception for invalid request");
	}
    }

    @Test
    public void update_sampleReview_invalid_test()
    {
	ServiceResponse serviceResponse = null;
	try
	{
	   UpdateSampleReviewRequest updateSampleReviewRequest = getUpdateSampleReviewRequest();
	   updateSampleReviewRequest.getSampleReview().setNickName(null);
	   serviceResponse = getClient().updateSampleReview(
		   updateSampleReviewRequest);
	   Assert.assertNotNull(serviceResponse);

	}
	catch (SnapdealWSException ex)
	{
	   System.out.println("Expected Exception for invalid request");
	   Assert.assertEquals(400 , ex.getWsErrorCode());
	}

	Assert.assertNull(serviceResponse);
    }

    private SampleReview getSampleReviewByLabel(String label)
    {

	Map<String , String> getSampleReviewParams = new HashMap<String , String>();
	getSampleReviewParams.put("label" , label);
	GetSampleReviewResponse getSampleReviewResponse = null;
	try
	{
	   getSampleReviewResponse = getClient().getSampleReview(
		   getSampleReviewParams);
	}
	catch (SnapdealWSException e)
	{
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	}
	Assert.assertNotNull(getSampleReviewResponse);
	SampleReview sampleReview = getSampleReviewResponse.getSampleReview();
	Assert.assertNotNull(sampleReview);
	return sampleReview;
    }

    private CreateSampleReviewRequest getCreateSampleReviewRequest()
    {
	CreateSampleReviewRequest createSampleReviewRequest = new CreateSampleReviewRequest();
	createSampleReviewRequest.setSampleReview(getSampleReview());
	return createSampleReviewRequest;
    }

    private UpdateSampleReviewRequest getUpdateSampleReviewRequest()
    {
	UpdateSampleReviewRequest updateSampleReviewRequest = new UpdateSampleReviewRequest();
	updateSampleReviewRequest.setSampleReview(getSampleReview());
	return updateSampleReviewRequest;
    }

    private SampleReview getSampleReview()
    {
	SampleReview sampleReview = new SampleReview();
	sampleReview.setComments(RandomStringUtils.randomAlphabetic(45));
	sampleReview.setCreatedAt(new Date().getTime());
	sampleReview.setHeadline(RandomStringUtils.randomAlphabetic(25));
	sampleReview.setLabel(getLabel());
	sampleReview.setNickName(RandomStringUtils.randomAlphabetic(8));
	sampleReview.setRating(3);
	return sampleReview;
    }

    @Test
    public void getProductLabels_Test()
    {
	GetLabelsResponse getLabelsResponse;
	List<String> labelList = null;
	try
	{
	   getLabelsResponse = getClient().getProductLabels();
	   labelList = getLabelsResponse.getProductLabel().getLabelList();
	}
	catch (SnapdealWSException e)
	{
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	}

    }

    public String getLabel()
    {

	return RandomStringUtils.randomAlphabetic(15);

    }
}

