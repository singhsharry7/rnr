package com.snapdeal.reviews.client.api;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.reviews.commons.dto.CreateSelfieRequest;
import com.snapdeal.reviews.commons.dto.DeleteSelfieRequest;
import com.snapdeal.reviews.commons.dto.DeleteSelfieResponse;
import com.snapdeal.reviews.commons.dto.LikeSelfieRequest;
import com.snapdeal.reviews.commons.dto.LikeSelfieResponse;
import com.snapdeal.reviews.commons.dto.ReportSelfieRequest;
import com.snapdeal.reviews.commons.dto.ReportSelfieResponse;
import com.snapdeal.reviews.commons.dto.wrapper.CreateSelfieResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetSelfieListingPageForModerationResponse;
import com.snapdeal.reviews.commons.dto.wrapper.GetSelfieListingPageResponse;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateSelfieOnCheckinRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateSelfieOnCheckinResponse;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateSelfieOnCheckoutRequest;
import com.snapdeal.reviews.commons.dto.wrapper.UpdateSelfieOnCheckoutResponse;

public interface SelfieClientService extends ReviewWebClientService {

	GetSelfieListingPageResponse getSelfieList(Map<String, String> queryParams)
			throws SnapdealWSException;

	UpdateSelfieOnCheckinResponse updateSelfieOnCheckin(
			UpdateSelfieOnCheckinRequest request) throws SnapdealWSException;

	UpdateSelfieOnCheckoutResponse updateSelfieOnCheckout(
			UpdateSelfieOnCheckoutRequest request) throws SnapdealWSException;
	
	GetSelfieListingPageForModerationResponse getSelfieListModeration(Map<String, String> queryParams)
			throws SnapdealWSException;
	
	DeleteSelfieResponse deleteSelfie(@Valid @ModelAttribute DeleteSelfieRequest request) 
			throws SnapdealWSException;

	LikeSelfieResponse likeSelfie(LikeSelfieRequest request) 
			throws SnapdealWSException;
	
	ReportSelfieResponse reportSefie(ReportSelfieRequest request) 
			throws SnapdealWSException;
	
}
