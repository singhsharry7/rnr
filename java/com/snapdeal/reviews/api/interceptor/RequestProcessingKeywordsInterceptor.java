package com.snapdeal.reviews.api.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class RequestProcessingKeywordsInterceptor extends HandlerInterceptorAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(RequestProcessingKeywordsInterceptor.class);

	private static final String PRODUCT_ID = "productId";
	private static final String KEYWORDS = "keywords";
	private static final String USER_ID = "userId";
	private static final String RATING = "rating";
	private static final String SORTTYPE = "sortType";
	private static final String STATUS = "status";

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		final String productId = request.getParameter(PRODUCT_ID);
		final String keywords = request.getParameter(KEYWORDS);
		final String userId = request.getParameter(USER_ID);
		final String rating = StringUtils.equals(request.getParameter(RATING),"0") ? "ALL" : request.getParameter(RATING);
		final String sortType = request.getParameter(SORTTYPE) == null ? "HELPFUL_DESC"
				: request.getParameter(SORTTYPE);
		final String status = request.getParameter(STATUS);
		LOGGER.info("ProductId ::" + productId + " Search Text ::" + keywords + " userId ::" + " Rating :: " + rating
				+ " SortType :: " + sortType + " Status :: " + status + (userId != null ? userId : "") + " ");
	}
}
