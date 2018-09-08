package com.snapdeal.reviews.api.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;

import scala.Some;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import com.snapdeal.base.model.common.ServiceResponse;
import com.wordnik.swagger.model.ApiInfo;
import com.wordnik.swagger.model.ResponseMessage;

@Configuration
@EnableSwagger()
public class SwaggerConfig {

	private SpringSwaggerConfig springSwaggerConfig;

	/**
	 * Required to autowire SpringSwaggerConfig
	 */
	@Autowired
	public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
		this.springSwaggerConfig = springSwaggerConfig;
	}

	/**
	 * Every SwaggerSpringMvcPlugin bean is picked up by the swagger-mvc
	 * framework - allowing for multiple swagger groups i.e. same code base
	 * multiple swagger resource listings.
	 */
	@Bean
	public SwaggerSpringMvcPlugin customImplementation() {
		return new SwaggerSpringMvcPlugin(this.springSwaggerConfig).apiInfo(apiInfo()).includePatterns(".*reviews.*", ".*selfie.*")
				.ignoredParameterTypes(HttpStatus.class).genericModelSubstitutes(ResponseEntity.class)
				.globalResponseMessage(RequestMethod.GET, getResponseMessagesForFailedRequests())
				.globalResponseMessage(RequestMethod.POST, getResponseMessagesForFailedRequests())
				.globalResponseMessage(RequestMethod.PUT, getResponseMessagesForFailedRequests()).build();
	}

	private ApiInfo apiInfo() {
		ApiInfo apiInfo = new ApiInfo("Reviews Platform", "Functionalities provided by the Review System to the end user", "about:blank",
				"TechBLR.reviews.dev <TechBLR.reviews.dev@snapdeal.com>", "My Apps API Licence Type", "about:blank");
		return apiInfo;
	}

	private List<ResponseMessage> getResponseMessagesForFailedRequests() {
		List<ResponseMessage> messages = new ArrayList<>();
		messages.add(getResponseMessageForFailedRequests(HttpStatus.BAD_REQUEST));
		messages.add(getResponseMessageForFailedRequests(HttpStatus.UNAUTHORIZED));
		messages.add(getResponseMessageForFailedRequests(HttpStatus.FORBIDDEN));
		messages.add(getResponseMessageForFailedRequests(HttpStatus.NOT_FOUND));
		messages.add(getResponseMessageForFailedRequests(HttpStatus.INTERNAL_SERVER_ERROR));
		return messages;
	}

	private ResponseMessage getResponseMessageForFailedRequests(HttpStatus httpStatus) {
		ServiceResponse response = new ServiceResponse();
		response.setCode("Error Code");
		response.setMessage("Error message description");
		return new ResponseMessage(httpStatus.value(), httpStatus.getReasonPhrase(), new Some<String>("ServiceResponse"));
	}
}
