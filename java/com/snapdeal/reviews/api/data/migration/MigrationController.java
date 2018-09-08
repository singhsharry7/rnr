package com.snapdeal.reviews.api.data.migration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.snapdeal.reviews.api.rest.controller.BaseController;
import com.snapdeal.reviews.commons.UriConstants;
import com.snapdeal.reviews.data.migration.ReviewMigrationRequest;
import com.snapdeal.reviews.data.migration.ReviewMigrationResponse;
import com.snapdeal.reviews.model.ReviewableObjectBo;
import com.snapdeal.reviews.service.AdministrationService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@ApiIgnore
@RestController
public class MigrationController extends BaseController{
	
	@Autowired
	private AdministrationService adminService;

	@ApiIgnore
	@RequestMapping(value = UriConstants.Migration.REVIEWS, method = RequestMethod.POST)
	public ResponseEntity<ReviewMigrationResponse> migrateReviews(@RequestBody @Valid ReviewMigrationRequest request){
		ReviewMigrationResponse response = adminService.migrateReviews(request.getProductId(), request.getReviews(),request.isCamsCallRequired(),request.getMigrationType(),request.isOnlyRating(),request.getIncrementalVersion());
		return new ResponseEntity<ReviewMigrationResponse>(response,HttpStatus.OK);
	}

	@ApiIgnore
	@RequestMapping(value = UriConstants.Migration.PRODUCT, method = RequestMethod.POST)
	public ResponseEntity<ReviewMigrationResponse> reloadReviews(@RequestBody @Valid String productId){
		ReviewMigrationResponse response = adminService.clearDataForProduct(new ReviewableObjectBo(productId));
		return new ResponseEntity<ReviewMigrationResponse>(response,HttpStatus.OK);
	}
	
	@ApiIgnore
	@RequestMapping(value = UriConstants.Migration.DELETEREVIEW, method = RequestMethod.DELETE)
	public ResponseEntity<ReviewMigrationResponse> deleteReview(@RequestParam("reviewId") @NotNull String reviewId){
		ReviewMigrationResponse response = adminService.deleteReview(reviewId);
		return new ResponseEntity<ReviewMigrationResponse>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value = UriConstants.Migration.RECALCULATE_STATS, method = RequestMethod.POST)
	public ResponseEntity<ReviewMigrationResponse> recalculateStats(@RequestBody @Valid String productId){
		ReviewMigrationResponse response = adminService.recalculateStatsForProduct(new ReviewableObjectBo(productId));
		return new ResponseEntity<ReviewMigrationResponse>(response, HttpStatus.OK);
	}
	
}
