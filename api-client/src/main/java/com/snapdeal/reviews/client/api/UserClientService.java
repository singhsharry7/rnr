package com.snapdeal.reviews.client.api;

import java.util.Map;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.base.model.common.ServiceResponse;
import com.snapdeal.reviews.commons.dto.wrapper.MailSubscriptionRequest;
import com.snapdeal.reviews.commons.dto.wrapper.MailSubscriptionResponse;
import com.snapdeal.reviews.commons.dto.wrapper.NicknameResponse;
import com.snapdeal.reviews.commons.dto.wrapper.UserReviewStatsPageResponse;

public interface UserClientService extends ReviewWebClientService  {

	ServiceResponse subscribeForMails(MailSubscriptionRequest request)
			throws SnapdealWSException;

	ServiceResponse unsubscribeFromMails(Map<String, String> queryParams)throws SnapdealWSException;
	
	MailSubscriptionResponse hasSubscribed(Map<String, String> queryParams) throws SnapdealWSException;

	NicknameResponse getNickname(Map<String, String> queryParams) throws SnapdealWSException;

	UserReviewStatsPageResponse getUserReviewResponse(Map<String, String> queryParams) throws SnapdealWSException;

}
