package com.snapdeal.reviews;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.base.transport.service.ITransportService.Protocol;
import com.snapdeal.reviews.client.api.impl.SelfieClientServiceImpl;
import com.snapdeal.reviews.commons.UriConstants;
import com.snapdeal.reviews.commons.dto.CreateSelfieRequest;
import com.snapdeal.reviews.commons.dto.DeleteSelfieRequest;
import com.snapdeal.reviews.commons.dto.DeleteSelfieResponse;
import com.snapdeal.reviews.commons.dto.ImageType;
import com.snapdeal.reviews.commons.dto.LikeSelfieRequest;
import com.snapdeal.reviews.commons.dto.LikeSelfieResponse;
import com.snapdeal.reviews.commons.dto.LikeType;
import com.snapdeal.reviews.commons.dto.SelfieModerationRequest;
import com.snapdeal.reviews.commons.dto.SelfieRequest;
import com.snapdeal.reviews.commons.dto.SelfieStatus;
import com.snapdeal.reviews.commons.dto.UpdateSelfieModerationRequest;
import com.snapdeal.reviews.commons.dto.wrapper.CreateSelfieResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetSelfieListingPageResponse;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateSelfieOnCheckinRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateSelfieOnCheckinResponse;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateSelfieOnCheckoutRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateSelfieOnCheckoutResponse;

public class SelfieListingTest extends ReviewsApiTest {

	ObjectMapper mapper = new ObjectMapper();
	
	@Test
	public void testCreateSelfie() throws Exception{
		Assert.assertNotNull(createSelfi());
	}
	
	@Test
	public void testLikeSelfie() throws SnapdealWSException{
		Assert.assertEquals("200",testLikeSelfie("01511a01d26000000845c0e2af501a35","abhi"));
	}
	
	private String testLikeSelfie(String selfieId,String userId) throws SnapdealWSException{
		LikeSelfieRequest request = new LikeSelfieRequest();
		request.setSelfieId(selfieId);
		request.setUserId(userId);
		request.setLikeType(LikeType.DISLIKE);
		LikeSelfieResponse response= getSelfieClient().likeSelfie(request);
		return response.getCode();
	}
	
	private String createSelfi() throws Exception{
		CreateSelfieRequest createSelfieRequest = new CreateSelfieRequest();
		SelfieRequest selfiRequest = new SelfieRequest();
		selfiRequest.setCaption("Test Caption");
		selfiRequest.setCertifiedBuyer(true);
		selfiRequest.setImageType(ImageType.SELFIE);
		selfiRequest.setProductId("123");
		selfiRequest.setUserId("1001");
		selfiRequest.setNickname("avin");
		createSelfieRequest.setSelfiRequest(selfiRequest);
		CreateSelfieResponse response = sendCreateSelfieRequest(createSelfieRequest, "babydiaper.jpg");
		return response.getSelfieResponse().getId();
	}
	
	@Test
	public void testSelfieListingForNegativeOffset() throws SnapdealWSException{
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("offset", "-1");
		getSelfieClient().getSelfieList(queryParams);
		System.out.println();
	}
	
	@Test
	public void testSelfieListingForNegativeLimit() throws SnapdealWSException{
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("limit", "-1");
		getSelfieClient().getSelfieList(queryParams);
		System.out.println();
	}
	
	@Test
	public void testSelfieListingForInvalidQueryType() throws SnapdealWSException{
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("queryType", "sadsad");
		getSelfieClient().getSelfieList(queryParams);
		System.out.println();
	}
	
	@Test
	public void testDeleteSelfie() throws Exception{
		String selfieId = createSelfi();
		DeleteSelfieRequest request = new DeleteSelfieRequest();
		request.setSelfieId(selfieId);
		DeleteSelfieResponse response = getSelfieClient().deleteSelfie(request);
		Assert.assertEquals(true, response.isDeleted());
	}
	
	// HTTP POST request
	private CreateSelfieResponse sendCreateSelfieRequest(CreateSelfieRequest request, String imageNameInResource) throws Exception {

		SelfieRequest selfieRequest = request.getSelfiRequest();
		String url =  ((SelfieClientServiceImpl)getSelfieClient()).getWebServiceBaseUrl() +  UriConstants.Selfie.CREATE_SELFI;

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);

		// add header
		//post.setHeader("User-Agent", "");

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("selfiRequest.productId", selfieRequest.getProductId()));
		urlParameters.add(new BasicNameValuePair("selfiRequest.caption", selfieRequest.getCaption()));
		urlParameters.add(new BasicNameValuePair("selfiRequest.userId", selfieRequest.getUserId()));
		urlParameters.add(new BasicNameValuePair("selfiRequest.nickname", selfieRequest.getNickname()));
		urlParameters.add(new BasicNameValuePair("selfiRequest.certifiedBuyer", ""+selfieRequest.isCertifiedBuyer()));
		urlParameters.add(new BasicNameValuePair("selfiRequest.imageType", selfieRequest.getImageType().name()));
		

		MultipartEntity entity = new MultipartEntity();
		for(NameValuePair pair: urlParameters){
			entity.addPart(pair.getName(), new StringBody(pair.getValue()));
		}
		
		ClassLoader classLoader = getClass().getClassLoader();
		
		FileBody bin = new FileBody(new File(classLoader.getResource(imageNameInResource).getFile()));
		entity.addPart("image", bin);
		post.setEntity(entity);

		HttpResponse response = client.execute(post);
		
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + post.getEntity());
		System.out.println("Response Code : " + 
                                    response.getStatusLine().getStatusCode());
/*
		BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		*/
		CreateSelfieResponse createSelfieresponse = (CreateSelfieResponse)mapper.readValue(
				response.getEntity().getContent(), CreateSelfieResponse.class);
		
		return createSelfieresponse;
	}

		
	@Test
	public void testSelfieListing() throws SnapdealWSException{
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("offset", "0");
		queryParams.put("limit", "20");
		queryParams.put("productId", "111");
		GetSelfieListingPageResponse selfieList = getSelfieClient().getSelfieList(queryParams);
		System.out.println();
	}
	
	@Test
	public void testCheckOutCheckIn() throws Exception{
		String selfieId = createSelfi();
		testCheckOutSelfie(selfieId);
		testCheckinSelfie(selfieId);
	}
	
	private void testCheckOutSelfie(String selfieId) throws SnapdealWSException{
		UpdateSelfieOnCheckoutRequest request = new UpdateSelfieOnCheckoutRequest();
		request.setModeratorId("222");
		List<String> selfieIds = new ArrayList<>();
		selfieIds.add(selfieId);
		request.setSelfieIds(selfieIds);
		UpdateSelfieOnCheckoutResponse response = getSelfieClient().updateSelfieOnCheckout(request);
		Assert.assertEquals(1, response.getCheckoutSelfieResponse().getCheckedOutSelfies().size());
	}
	
	private void testCheckinSelfie(String selfieId) throws SnapdealWSException{
		UpdateSelfieOnCheckinRequest request = new UpdateSelfieOnCheckinRequest();
		request.setModeratorId("222");
		UpdateSelfieModerationRequest moderationRequest = new UpdateSelfieModerationRequest();
		List<SelfieModerationRequest> selfieList = new ArrayList<>();
		SelfieModerationRequest req = new SelfieModerationRequest();
		req.setSelfieId(selfieId);
		req.setStatus(SelfieStatus.APPROVED);
		req.setModeratorRating(4);
		selfieList.add(req);
		moderationRequest.setSelfieModerationRequests(selfieList);
		request.setSelfieModerationRequest(moderationRequest);
		UpdateSelfieOnCheckinResponse response = getSelfieClient().updateSelfieOnCheckin(request);
		Assert.assertEquals(1, response.getCheckedInSelfieResponse().getCheckedInSelfies().size());
	}
	
}
