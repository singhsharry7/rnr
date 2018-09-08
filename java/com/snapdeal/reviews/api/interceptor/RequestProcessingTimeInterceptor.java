package com.snapdeal.reviews.api.interceptor;

import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class RequestProcessingTimeInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(RequestProcessingTimeInterceptor.class);

	private static AtomicLong totalResponseTime = new AtomicLong();
	
	private static AtomicLong noOfRequest = new AtomicLong();
	
	private static AtomicLong totalNoRequest = new AtomicLong();
	
	static{
		new Thread(){
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(5000);
						logger.warn(
								String.valueOf(totalNoRequest.get()) 
								+ "," + 
								String.valueOf(totalResponseTime.get()/noOfRequest.get()));
						totalResponseTime.set(0);
						noOfRequest.set(0);
					} catch (Exception e) {
					}
				}
			}
		}.start();
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		long startTime = System.currentTimeMillis();
		request.setAttribute("REVIEWS_API_RESPONSE_TIME", startTime);
		noOfRequest.incrementAndGet();
		totalNoRequest.incrementAndGet();
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		long startTime = (Long) request.getAttribute("REVIEWS_API_RESPONSE_TIME");
		long responseTime = System.currentTimeMillis() - startTime;
		
		totalResponseTime.addAndGet(responseTime);
		
		response.addHeader("api-response-time", String.valueOf(responseTime));
	}
}
