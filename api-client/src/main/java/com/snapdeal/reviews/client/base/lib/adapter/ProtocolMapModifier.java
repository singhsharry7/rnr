package com.snapdeal.reviews.client.base.lib.adapter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.snapdeal.base.transport.serialization.service.ISerializationService;
import com.snapdeal.base.transport.service.ITransportService;
import com.snapdeal.base.transport.service.ITransportService.Protocol;
import com.snapdeal.base.transport.service.impl.TransportServiceImpl;

public class ProtocolMapModifier {

	public static void addCustomJsonProtocol(ITransportService transportService, ISerializationService serializationService) {
		Field field;
		try {
			field = TransportServiceImpl.class.getDeclaredField("protocolToSerializationServiceMap");
			field.setAccessible(true);
			Map<Protocol, ISerializationService> map = (Map<Protocol, ISerializationService>) field.get(transportService);
			map.put(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL), serializationService);
		} catch (Exception e) {
			throw new RuntimeException("Exception while initializing the Reviews API client");
		}
	}

	public static void addProtocolToHeadersMapperEntry(ITransportService transportService) {
		Field field;
		try {
			field = TransportServiceImpl.class.getDeclaredField("protocolToHeadersMap");
			field.setAccessible(true);
			Map<Protocol, Map<String, String>> map = (Map<Protocol, Map<String, String>>) field.get(transportService);
			map.put(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL), new HashMap<String, String>(new HashMap<String, String>(1)));
			map.get(Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL)).put("Content-Type",
					"application/" + Constants.CUSTOM_JSON_PROTOCOL.toLowerCase());
		} catch (Exception e) {
			throw new RuntimeException("Exception while initializing the Reviews API client");
		}
	}

	public static void addProtocolPropertyToProtocolMapMapperEntry(ITransportService transportService) {
		Field field;
		try {
			field = TransportServiceImpl.class.getDeclaredField("protocolPropertyToProtocolMap");
			field.setAccessible(true);
			Map<String, Protocol> map = (Map<String, Protocol>) field.get(transportService);
			map.put(Constants.CUSTOM_JSON_PROTOCOL.toLowerCase(), Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		} catch (Exception e) {
			throw new RuntimeException("Exception while initializing the Reviews API client");
		}
	}

	public static void addmediaTypeToProtocolMap(ITransportService transportService) {
		Field field;
		try {
			field = TransportServiceImpl.class.getDeclaredField("mediaTypeToProtocolMap");
			field.setAccessible(true);
			Map<String, Protocol> map = (Map<String, Protocol>) field.get(transportService);
			map.put("application/" + Constants.CUSTOM_JSON_PROTOCOL.toLowerCase(), Protocol.valueOf(Constants.CUSTOM_JSON_PROTOCOL));
		} catch (Exception e) {
			throw new RuntimeException("Exception while initializing the Reviews API client");
		}
	}
}
