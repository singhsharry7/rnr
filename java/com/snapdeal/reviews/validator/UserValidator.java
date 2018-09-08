package com.snapdeal.reviews.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snapdeal.reviews.commons.dto.MailSubscriber;
import com.snapdeal.reviews.commons.dto.UserRequest;

@Component
public class UserValidator {
	
	@Autowired
	private ValidationUtils utils;
	
	public void validate(UserRequest user){
		user.setName(user.getName().trim());
		user.setPassword(user.getPassword().trim());
		utils.assertNonEmpty("name", user.getName());
		utils.assertNonEmpty("password", user.getPassword());
	}
	
	public void validateUserId(MailSubscriber subscriber){
		subscriber.setUserId(subscriber.getUserId().trim());
		utils.assertNonEmpty("userId",subscriber.getUserId());
	}
	
}
