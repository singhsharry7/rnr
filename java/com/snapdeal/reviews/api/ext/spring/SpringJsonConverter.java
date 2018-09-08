package com.snapdeal.reviews.api.ext.spring;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.google.gson.JsonElement;
import com.snapdeal.reviews.commons.Constants;
import com.snapdeal.reviews.commons.dto.wrapper.FieldSelectable;
import com.snapdeal.reviews.commons.json.JsonCodec;

public class SpringJsonConverter extends AbstractHttpMessageConverter<Object> implements GenericHttpMessageConverter<Object> {

	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	@Autowired
	private JsonCodec jsonCodec;

	public SpringJsonConverter() {
		super(new MediaType("application", "json", DEFAULT_CHARSET), new MediaType("application", "*+json", DEFAULT_CHARSET));
	}

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return canRead(mediaType);
	}

	@Override
	public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
		return canRead(mediaType);
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return canWrite(mediaType);
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		return readTypeToken(clazz, inputMessage);
	}

	@Override
	public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		return readTypeToken(type, inputMessage);
	}

	private Object readTypeToken(Type type, HttpInputMessage inputMessage) throws IOException {
		Reader json = new InputStreamReader(inputMessage.getBody(), getCharset(inputMessage.getHeaders()));
		try {
			return jsonCodec.fromJson(json, type);
		} catch (Exception ex) {
			throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
		}
	}

	@Override
	protected void writeInternal(Object o, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		HttpHeaders headers = outputMessage.getHeaders();
		Charset charset = getCharset(headers);
		try (OutputStreamWriter writer = new OutputStreamWriter(outputMessage.getBody(), charset)) {
			if (headers.containsKey(Constants.QUERY_SELECTOR) && o instanceof FieldSelectable) {
				excludeNotIncludedFields(o, headers.getFirst(Constants.QUERY_SELECTOR), writer);
			} else
				jsonCodec.toJson(o, writer);
		} catch (Exception ex) {
			throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
		} finally {
			headers.remove(Constants.QUERY_SELECTOR);
		}
	}

	private void excludeNotIncludedFields(Object o, String fieldsToInclude, OutputStreamWriter writer) {
		@SuppressWarnings("unchecked")
		FieldSelectable<? extends Object> fieldSelectable = (FieldSelectable<? extends Object>) o;
		JsonElement topLevelTree = jsonCodec.getJsonTree(o);
		JsonElement jsonElement = topLevelTree.getAsJsonObject().get(fieldSelectable.getFieldSelectableObjectName());
		jsonCodec.removeNotIncludedFields(jsonElement.getAsJsonObject(), fieldsToInclude);
		jsonCodec.toJson(topLevelTree, writer);
	}

	private Charset getCharset(HttpHeaders headers) {
		if (headers == null || headers.getContentType() == null || headers.getContentType().getCharSet() == null) {
			return DEFAULT_CHARSET;
		}
		return headers.getContentType().getCharSet();
	}

}
