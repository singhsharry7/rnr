package com.snapdeal.reviews.client.api;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.base.model.common.ServiceResponse;
import com.snapdeal.reviews.commons.dto.wrapper.ProfaneWordsResponse;
import com.snapdeal.reviews.commons.dto.wrapper.ProfanityCheckRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateProfaneWordsRequest;

public interface ProfanityClientService extends ReviewWebClientService {
	
	ServiceResponse addWords(UpdateProfaneWordsRequest request) throws SnapdealWSException;
	
	ServiceResponse deleteWords(UpdateProfaneWordsRequest request) throws SnapdealWSException;
	
	ProfaneWordsResponse getAllWords() throws SnapdealWSException;
	
	ProfaneWordsResponse getAllWordsForModeration() throws SnapdealWSException;
	
	ProfaneWordsResponse checkProfanityForModeration(ProfanityCheckRequest request) throws SnapdealWSException;
	
	ProfaneWordsResponse checkProfanity(ProfanityCheckRequest request) throws SnapdealWSException;
	
}
