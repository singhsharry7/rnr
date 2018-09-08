package com.snapdeal.reviews.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snapdeal.reviews.commons.dto.MappingCommon;

@Component
public class MappingValidator {
	@Autowired
	private ValidationUtils utils;
	
	public void validate(MappingCommon request){
		utils.assertPositive("child pog id", request.getChildPageID());
		utils.assertPositive("parent pog id", request.getParentPageID());
	}
}

