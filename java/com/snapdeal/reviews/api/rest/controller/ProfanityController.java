package com.snapdeal.reviews.api.rest.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.snapdeal.base.model.common.ServiceResponse;
import com.snapdeal.base.transport.service.ITransportService.Protocol;
import com.snapdeal.profanity.editor.ProfanityEditor;
import com.snapdeal.reviews.commons.ProfanityFilterType;
import com.snapdeal.reviews.commons.UriConstants;
import com.snapdeal.reviews.commons.dto.wrapper.ProfaneWordsResponse;
import com.snapdeal.reviews.commons.dto.wrapper.ProfanityCheckRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateProfaneWordsRequest;
import com.snapdeal.reviews.validator.ProfanityValidator;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Profanity-Editor", description = "Profanity Editor")
public class ProfanityController extends BaseController{
	
	@Autowired
	ProfanityEditor editor;
	
	@Autowired
	ProfanityValidator validator;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProfanityController.class);
	
	@ApiOperation(value = "Add new profane words", position = 4)
	@RequestMapping(value = UriConstants.Profanity.ADD_WORDS, method = RequestMethod.POST)
	public ResponseEntity<ServiceResponse> addWords(@RequestBody @Valid UpdateProfaneWordsRequest request){
		validator.validate(request.getRequest());
		editor.addWords(request.getRequest().getWords(), request.getRequest().getFilterType());
		ServiceResponse response = new ServiceResponse();
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Delete profane words", position = 5)
	@RequestMapping(value = UriConstants.Profanity.DELETE_WORDS, method = RequestMethod.POST)
	public ResponseEntity<ServiceResponse> deleteWords(@RequestBody @Valid UpdateProfaneWordsRequest request){
		validator.validate(request.getRequest());
		editor.deleteWords(request.getRequest().getWords(), request.getRequest().getFilterType());
		ServiceResponse response = new ServiceResponse();
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "List all profane words configured for review creation filter", position = 1)
	@RequestMapping(value = UriConstants.Profanity.GET_ALL_WORDS, method = RequestMethod.GET)
	public ResponseEntity<ProfaneWordsResponse> getAllWords(){
		ProfaneWordsResponse response = new ProfaneWordsResponse();
		response.setProfaneWords(editor.getAllWords(ProfanityFilterType.REVIEW_CREATION));
		return new ResponseEntity<ProfaneWordsResponse>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "List all profane words configured for moderation filter", position = 6)
	@RequestMapping(value = UriConstants.Profanity.GET_ALL_WORDS_MODERATION, method = RequestMethod.GET)
	public ResponseEntity<ProfaneWordsResponse> getAllWordsModeration(){
		ProfaneWordsResponse response = new ProfaneWordsResponse();
		response.setProfaneWords(editor.getAllWords(ProfanityFilterType.MODERATION));
		return new ResponseEntity<ProfaneWordsResponse>(response,HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "Provides the list of profane words in the given text", position = 2)
	@RequestMapping(value = UriConstants.Profanity.CHECK_PROFANITY_MODERATION, method = RequestMethod.POST)
	public ResponseEntity<ProfaneWordsResponse> checkProfanityForModeration(@RequestBody @Valid ProfanityCheckRequest request){
		ProfaneWordsResponse response = new ProfaneWordsResponse();
		response.setProfaneWords(editor.checkProfanity(request.getText(), ProfanityFilterType.MODERATION));
		return new ResponseEntity<ProfaneWordsResponse>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Provides the list of profane words in the given text", position = 3)
	@RequestMapping(value = UriConstants.Profanity.CHECK_PROFANITY, method = RequestMethod.POST)
	public ResponseEntity<ProfaneWordsResponse> checkProfanityForReviewCreation(@RequestBody @Valid ProfanityCheckRequest request){
		ProfaneWordsResponse response = new ProfaneWordsResponse();
		response.setProfaneWords(editor.checkProfanity(request.getText(), ProfanityFilterType.REVIEW_CREATION));
		return new ResponseEntity<ProfaneWordsResponse>(response, HttpStatus.OK);
	}
	
}
