package com.snapdeal.reviews.client.api;

import com.snapdeal.base.exception.SnapdealWSException;
import com.snapdeal.base.model.common.ServiceResponse;
import com.snapdeal.reviews.commons.dto.wrapper.MappingCommonRequest;
import com.snapdeal.reviews.commons.dto.wrapper.MappingResponse;

public interface ProductMappingService extends ReviewWebClientService {

	MappingResponse addPogMappingOFParentAndChild(MappingCommonRequest request)
			throws SnapdealWSException;

	MappingResponse getAllProductsMapping() throws SnapdealWSException;

	ServiceResponse deleteMapping(MappingCommonRequest request)
			throws SnapdealWSException;
}
