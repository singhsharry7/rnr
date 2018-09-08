package com.snapdeal.reviews.mailers.subscription;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.base.model.common.ServiceResponse;
import com.snapdeal.reviews.ReviewsApiTest;
import com.snapdeal.reviews.commons.dto.MailSubscriber;
import com.snapdeal.reviews.commons.dto.Nickname;
import com.snapdeal.reviews.commons.dto.wrapper.MailSubscriptionRequest;
import com.snapdeal.reviews.commons.dto.wrapper.NicknameResponse;

public class MailSubscriptionTests extends ReviewsApiTest{
	
	public MailSubscriptionTests() {
		System.setProperty("config.location", "D:\\devel\\repository\\reviews_next_gen\\application\\profanity-editor\\src\\main\\resources\\");
	}
	
	@Test
	public void subscribeForMails() throws SnapdealWSException{
		MailSubscriptionRequest request = new MailSubscriptionRequest();
		request.setSubscriber(new MailSubscriber("test@gmail.com"));
		ServiceResponse response = getUserClient().subscribeForMails(request);
	}
	
	@Test
	public void getNicname() throws SnapdealWSException{
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("userId", "szdjkkjbc");
		NicknameResponse response = getUserClient().getNickname(queryParams);
		System.out.println(response.getNickname().getNickname());
	}

}
