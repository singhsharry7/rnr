package com.snapdeal.reviews.client.factory;

import java.util.Map;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.snapdeal.base.transport.serialization.service.ISerializationService;
import com.snapdeal.base.transport.service.ITransportService;
import com.snapdeal.base.transport.service.ITransportService.Protocol;
import com.snapdeal.reviews.client.api.IConfigurationClientService;
import com.snapdeal.reviews.client.api.ProductMappingService;
import com.snapdeal.reviews.client.api.ProfanityClientService;
import com.snapdeal.reviews.client.api.ReportsClientService;
import com.snapdeal.reviews.client.api.ReviewClientService;
import com.snapdeal.reviews.client.api.ReviewModerationClientService;
import com.snapdeal.reviews.client.api.SelfieClientService;
import com.snapdeal.reviews.client.api.UserClientService;
import com.snapdeal.reviews.client.base.lib.adapter.Constants;
import com.snapdeal.reviews.client.base.lib.adapter.DynamicEnumAdder;
import com.snapdeal.reviews.client.base.lib.adapter.ProtocolMapModifier;

public class ReviewClientFactory {

	private static final ReviewClientService CLIENT_SERVICE;
	private static final SelfieClientService SELFIE_CLIENT_SERVICE;
	private static final ReviewModerationClientService CLIENT_MODERATION_SERVICE;
	private static final ProfanityClientService CLIENT_PROFANITY_SERVICE;
	private static final IConfigurationClientService CLIENT_CONFIGURATION_SERVICE;
	private static final UserClientService USER_CLIENT_SERVICE;
	private static final ProductMappingService PRODUCT_MAPPING_SERVICE;
	private static final ReportsClientService REPORTS_CLIENT_SERVICE;

	static {
		DynamicEnumAdder.addEnum(Protocol.class, Constants.CUSTOM_JSON_PROTOCOL);
		AbstractApplicationContext context = new ClassPathXmlApplicationContext("classpath:review-api-client.xml");
		ITransportService transportService = context.getBean(ITransportService.class);
		ProtocolMapModifier.addCustomJsonProtocol(transportService, (ISerializationService) context.getBean("gsonSerializer"));
		ProtocolMapModifier.addProtocolPropertyToProtocolMapMapperEntry(transportService);
		ProtocolMapModifier.addProtocolToHeadersMapperEntry(transportService);
		ProtocolMapModifier.addmediaTypeToProtocolMap(transportService);
		CLIENT_SERVICE = context.getBean(ReviewClientService.class);
		SELFIE_CLIENT_SERVICE = context.getBean(SelfieClientService.class);
		CLIENT_MODERATION_SERVICE = context.getBean(ReviewModerationClientService.class);
		CLIENT_PROFANITY_SERVICE = context.getBean(ProfanityClientService.class);
		CLIENT_CONFIGURATION_SERVICE = context.getBean(IConfigurationClientService.class);
		USER_CLIENT_SERVICE = context.getBean(UserClientService.class);
		PRODUCT_MAPPING_SERVICE = context.getBean(ProductMappingService.class);
		REPORTS_CLIENT_SERVICE = context.getBean(ReportsClientService.class);
	}

	public static void init(Map<ConfigurationParams, String> configParams) {
		CLIENT_SERVICE.setWebServiceBaseUrl(configParams.get(ConfigurationParams.BASE_URL));
		SELFIE_CLIENT_SERVICE.setWebServiceBaseUrl(configParams.get(ConfigurationParams.BASE_URL));
		CLIENT_MODERATION_SERVICE.setWebServiceBaseUrl(configParams.get(ConfigurationParams.BASE_URL));
		CLIENT_PROFANITY_SERVICE.setWebServiceBaseUrl(configParams.get(ConfigurationParams.BASE_URL));
		CLIENT_CONFIGURATION_SERVICE.setWebServiceBaseUrl(configParams.get(ConfigurationParams.BASE_URL));
		USER_CLIENT_SERVICE.setWebServiceBaseUrl(configParams.get(ConfigurationParams.BASE_URL));
		PRODUCT_MAPPING_SERVICE.setWebServiceBaseUrl(configParams.get(ConfigurationParams.BASE_URL));
		REPORTS_CLIENT_SERVICE.setWebServiceBaseUrl(configParams.get(ConfigurationParams.BASE_URL));
	}

	public static ReviewClientService getClient() {
		return CLIENT_SERVICE;
	}
	
	public static SelfieClientService getSelfieClient() {
		return SELFIE_CLIENT_SERVICE;
	}

	public static ReviewModerationClientService getModerationClient() {
		return CLIENT_MODERATION_SERVICE;
	}

	public static ProfanityClientService getProfanityClient() {
		return CLIENT_PROFANITY_SERVICE;
	}
	
	public static IConfigurationClientService getConfigurationClient(){
		return CLIENT_CONFIGURATION_SERVICE;
	}
	
	public static UserClientService getUserClient(){
		return USER_CLIENT_SERVICE;
	}
	
	public enum ConfigurationParams {
		BASE_URL;
	}
	public static ProductMappingService getProductMappingClient()
	{
		return PRODUCT_MAPPING_SERVICE;
	}
	public static ReportsClientService getReportsClientService()
	{
		return REPORTS_CLIENT_SERVICE;
	}
}
