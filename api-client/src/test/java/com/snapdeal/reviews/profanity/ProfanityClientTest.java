package com.snapdeal.reviews.profanity;

import org.junit.Test;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.reviews.ReviewsApiTest;
import com.snapdeal.reviews.commons.dto.wrapper.ProfaneWordsResponse;
import com.snapdeal.reviews.commons.dto.wrapper.ProfanityCheckRequest;

public class ProfanityClientTest extends ReviewsApiTest{
	
	public ProfanityClientTest() {
		System.setProperty("config.location", "D:\\devel\\repository\\reviews_next_gen\\application\\profanity-editor\\src\\main\\resources\\");
	}
	
	@Test
	public void testGetAllWords() throws SnapdealWSException{
		ProfaneWordsResponse words = getProfanityClient().getAllWords();
	}
	
	@Test
	public void testGetAllWordsModeration() throws SnapdealWSException{
		ProfaneWordsResponse words = getProfanityClient().getAllWordsForModeration();
		System.out.println(words);
	}
	
	@Test
	public void testProfanityCheck() throws SnapdealWSException{
		ProfanityCheckRequest request = new ProfanityCheckRequest();
		request.setText("asjdbfc adcnalks kadncjsadcc ksjdbcsd sbckjadsd");
		ProfaneWordsResponse words = getProfanityClient().checkProfanity(request);
	}
	
	@Test
	public void testProfanityCheckModeration() throws SnapdealWSException{
		ProfanityCheckRequest request = new ProfanityCheckRequest();
		request.setText("asjdbfc adcnalks kadncjsadcc ksjdbcsd sbckjadsd");
		ProfaneWordsResponse words = getProfanityClient().checkProfanityForModeration(request);
	}
	

}
