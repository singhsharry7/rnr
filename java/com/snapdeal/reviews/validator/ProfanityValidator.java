package com.snapdeal.reviews.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.snapdeal.reviews.commons.dto.ProfaneWordsRequest;
import com.snapdeal.reviews.exception.ErrorCode;
import com.snapdeal.reviews.exception.client.InvalidRequestException;

@Component
public class ProfanityValidator {

	public void validate(ProfaneWordsRequest request){
		List<String> words = new ArrayList<String>();
		for(String word : request.getWords()){
			word = word.trim().toLowerCase();
			if(word.isEmpty()){
				throw new InvalidRequestException(ErrorCode.INPUT_INVALID_PAYLOAD, "profane words to add");
			}
			words.add(word);
		}
		request.setWords(words);
	}

}
