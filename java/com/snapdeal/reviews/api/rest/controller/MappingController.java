package com.snapdeal.reviews.api.rest.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.snapdeal.base.model.common.ServiceResponse;
import com.snapdeal.base.transport.service.ITransportService.Protocol;
import com.snapdeal.reviews.commons.UriConstants.MappingPath;
import com.snapdeal.reviews.commons.dto.ProductMapping;
import com.snapdeal.reviews.commons.dto.wrapper.MappingCommonRequest;
import com.snapdeal.reviews.commons.dto.wrapper.MappingResponse;
import com.snapdeal.reviews.mappers.OrikaMapper;
import com.snapdeal.reviews.model.ProductMappingBo;
import com.snapdeal.reviews.service.MappingService;
import com.snapdeal.reviews.validator.MappingValidator;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api(value = "Mapping-Settings", description = "Mapping Settings")
@RestController
public class MappingController {

	@Autowired
	private MappingValidator Validator;

	@Autowired
	private MappingService mappingService;

	@Autowired
	private OrikaMapper mapper;

	@ApiOperation(value = "Delete Existing Mapping")
	@RequestMapping(value = MappingPath.DELETE_MAPPING, method = RequestMethod.POST)
	public ResponseEntity<ServiceResponse> deleteMapping(
			@Valid @RequestBody MappingCommonRequest request) {
		Validator.validate(request.getCommonMapping());
		mappingService
				.deleteMapping(request.getCommonMapping().getChildPageID(),
						request.getCommonMapping().getParentPageID());
		ServiceResponse response = new ServiceResponse();
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);

	}

	@ApiOperation(value = "Load Mapping")
	@RequestMapping(value = MappingPath.MAPPING, method = RequestMethod.GET)
	public ResponseEntity<MappingResponse> getAllProductsMapping() {
		List<ProductMappingBo> productMappingsBo = mappingService.getMapping();
		MappingResponse response = new MappingResponse();
		response.setProductMappings(mapper.mapAsList(productMappingsBo,
				ProductMapping.class));
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<MappingResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Add New Mapping")
	@RequestMapping(value = MappingPath.MAPPING, method = RequestMethod.POST)
	public ResponseEntity<MappingResponse> addPogMappingOFParentAndChild(
			@Valid @RequestBody MappingCommonRequest request) {

		Validator.validate(request.getCommonMapping());
		ProductMappingBo productMappingBO = new ProductMappingBo();
		productMappingBO.setChildPageID(request.getCommonMapping()
				.getChildPageID());
		productMappingBO.setParentPageID(request.getCommonMapping()
				.getParentPageID());

		List<ProductMappingBo> productMappingsBo = mappingService
				.addMapping(productMappingBO);
		MappingResponse response = new MappingResponse();
		response.setProductMappings(mapper.mapAsList(productMappingsBo,
				ProductMapping.class));
		response.setProtocol(Protocol.PROTOCOL_JSON);
		return new ResponseEntity<MappingResponse>(response, HttpStatus.OK);
	}

}
