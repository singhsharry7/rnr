package com.snapdeal.reviews.client.codec;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import com.snapdeal.base.exception.SerializationException;
import com.snapdeal.base.exception.SerializationException.SerializationErrorCode;
import com.snapdeal.base.transport.serialization.service.ISerializationService;
import com.snapdeal.reviews.commons.json.JsonCodec;

public class GsonSerializer implements ISerializationService {

	private JsonCodec gsonCodec;

	public byte[] doSerialize(Class<? extends Object> classType, Object obj) throws SerializationException {
		return gsonCodec.toJson(obj).getBytes(Charset.forName("UTF-8"));
	}

	@Override
	public Object doDeserialize(byte[] data, Class<? extends Object> classType) throws SerializationException {
		try {
			return gsonCodec.fromJson(new String(data, "UTF-8"), classType);
		} catch (UnsupportedEncodingException e) {
			throw new SerializationException(SerializationErrorCode.JSON_DESERIALIZATION_EXCEPTION, e.getMessage());
		}
	}

	@Override
	public Object doDeserialize(InputStream inputStream, Class<? extends Object> clazz) throws SerializationException {
		return gsonCodec.fromJson(new InputStreamReader(inputStream), clazz);
	}

	@Override
	public Object doDeSerialize(String json, Class<? extends Object> classType) throws SerializationException {
		return gsonCodec.fromJson(json, classType);
	}

	@Override
	public String doSerialize(Object object) throws SerializationException {
		return gsonCodec.toJson(object);
	}

	public JsonCodec getGsonCodec() {
		return gsonCodec;
	}

	public void setGsonCodec(JsonCodec gsonCodec) {
		this.gsonCodec = gsonCodec;
	}

}
