package com.snapdeal.reviews.api.rest.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.snapdeal.base.model.common.ServiceResponse;
import com.snapdeal.base.transport.service.ITransportService.Protocol;
import com.snapdeal.reviews.commons.UriConstants;
import com.snapdeal.reviews.commons.dto.MailSubscriber;
import com.snapdeal.reviews.commons.dto.Nickname;
import com.snapdeal.reviews.commons.dto.User;
import com.snapdeal.reviews.commons.dto.UserRequest;
import com.snapdeal.reviews.commons.dto.UserResponse;
import com.snapdeal.reviews.commons.dto.wrapper.MailSubscriptionRequest;
import com.snapdeal.reviews.commons.dto.wrapper.MailSubscriptionResponse;
import com.snapdeal.reviews.commons.dto.wrapper.NicknameResponse;
import com.snapdeal.reviews.commons.dto.wrapper.UserReviewStatsPageResponse;
import com.snapdeal.reviews.exception.ErrorCode;
import com.snapdeal.reviews.exception.client.InvalidRequestException;
import com.snapdeal.reviews.mappers.OrikaMapper;
import com.snapdeal.reviews.model.RoleBo;
import com.snapdeal.reviews.model.UserBo;
import com.snapdeal.reviews.service.UserService;
import com.snapdeal.reviews.validator.UserValidator;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Users", description = "Apis for user information")
public class UserController{

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private OrikaMapper mapper;

	@Autowired
	private UserValidator validator;

	@ApiIgnore
	@ApiOperation(value = "Create a User", position = 2)
	@RequestMapping(value = UriConstants.User.CREATE, method = RequestMethod.POST)
	public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest user){
		validator.validate(user);
		UserBo userBo = mapper.map(user, UserBo.class);
		UserBo response = userService.create(userBo);
		UserResponse userResponse = new UserResponse();
		userResponse.setCreatedAt(response.getCreatedAt().getTime());
		userResponse.setUserId(response.getId());
		//		HttpHeaders headers = new HttpHeaders();
		//		setLocationHeader(headers, buildURIComponent(uriBuilder, UriConstants.GET_REVIEW, userResponse.getUserId()));
		LOGGER.debug("Sending Response");
		return new ResponseEntity<UserResponse>(userResponse, HttpStatus.CREATED);
	}

	@ApiIgnore
	@ApiOperation(value = "Login to the Dashboard", position = 1)
	@RequestMapping(value = UriConstants.User.LOGIN, method = RequestMethod.GET)
	public ResponseEntity login(@RequestParam(value = "id") String id, @RequestParam(value = "pwd") String password){
		userService.login(id, password);
		/**
		 * Where are we setting bad request status code
		 */
		return new ResponseEntity(HttpStatus.OK);
	}

	@ApiIgnore
	@ApiOperation(value = "Get all Users", position = 3)
	@RequestMapping(value = UriConstants.User.LIST_USERS, method = RequestMethod.GET)
	public ResponseEntity<List<User>> getAllUsers(){
		List<User> users = mapper.mapAsList(userService.getAllUsers(),User.class);
		return new ResponseEntity<List<User>>(users,HttpStatus.OK);
	}

	@ApiIgnore
	@ApiOperation(value = "Get all Roles", position = 4)
	@RequestMapping(value = UriConstants.User.LIST_ROLES, method = RequestMethod.GET)
	public ResponseEntity<List<RoleBo>> getAllRoles(){
		List<RoleBo> roles = userService.getAllRoles();
		return new ResponseEntity<List<RoleBo>>(roles,HttpStatus.OK);
	}

	@ApiOperation(value = "Subscribe for review emails")
	@RequestMapping(value = UriConstants.Mailers.SUBSCRIBE, method = RequestMethod.POST)
	public ResponseEntity<ServiceResponse> subscribe(@Valid @RequestBody MailSubscriptionRequest request){
		validator.validateUserId(request.getSubscriber());
		userService.subscribe(request.getSubscriber().getUserId());
		ServiceResponse response = new ServiceResponse();
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);
	}


	@ApiOperation(value = "Unsubscribe from review emails")
	@RequestMapping(value = UriConstants.Mailers.UNSUBSCRIBE, method = RequestMethod.GET)
	public ResponseEntity<ServiceResponse> unsubscribe(@RequestParam(value = "userId") String userId){
		userId = userId.trim();
		if(userId.isEmpty()){
			throw new InvalidRequestException(ErrorCode.INPUT_INVALID_PAYLOAD,"userId");
		}
		userService.unsubscribe(userId);
		ServiceResponse response = new ServiceResponse();
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Get if user has subscrbed or not")
	@RequestMapping(value = UriConstants.Mailers.SUBSCRIPTION, method = RequestMethod.GET)
	public ResponseEntity<MailSubscriptionResponse> hasSubscribed(@RequestParam(value = "userId") String userId){
		validator.validateUserId(new MailSubscriber(userId));
		MailSubscriptionResponse response = new MailSubscriptionResponse();
		response.setHasSubscribed(userService.hasSubscribed(userId));
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<MailSubscriptionResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Get nickname of a user")
	@RequestMapping(value = UriConstants.User.GET_NICKNAME, method = RequestMethod.GET)
	public ResponseEntity<NicknameResponse> getNickname(@RequestParam(value = "userId") String userId){
		validator.validateUserId(new MailSubscriber(userId));
		NicknameResponse response = new NicknameResponse();
		response.setNickname(new Nickname(userService.getNickName(userId)));
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<NicknameResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Get user review statistics")
	@RequestMapping(value = UriConstants.User.GET_USER_REVIEW_STATS, method = RequestMethod.GET)
	public ResponseEntity<UserReviewStatsPageResponse> getUserReviewStats(@RequestParam(value = "scrollId",required=false) String scrollId, @RequestParam(value = "offset",required=false, defaultValue="0") int offset ){
		UserReviewStatsPageResponse response = new UserReviewStatsPageResponse();
		response.setPage(userService.getUserReviewStats(scrollId, offset));
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<UserReviewStatsPageResponse>(response, HttpStatus.OK);
	}

}