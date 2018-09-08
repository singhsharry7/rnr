package com.snapdeal.reviews.api.rest.controller;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snapdeal.reviews.commons.UriConstants;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;

@Api(value = "Logger", description = "Logger")
@RestController
public class LogController extends BaseController{

	@ApiOperation(value = "Update Log Level", position = 1)
	@ApiResponse(code = 200, message = "Successfully updated the log level")
	@RequestMapping(value = UriConstants.UPDATE_LOG_LEVEL, method = RequestMethod.PUT)
	public ResponseEntity<Boolean> updateLogLevel(@RequestBody String level){
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		Configuration config = ctx.getConfiguration();
		LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
		Level logLevel = Level.getLevel(level);
		loggerConfig.setLevel(logLevel);
		ctx.updateLoggers();
		return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
	}
}
