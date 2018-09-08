package com.snapdeal.reviews.client.api.impl;

import com.snapdeal.reviews.client.api.ImageClientService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.Multipart;
import javax.swing.text.html.ImageView;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Api;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;

@Service
public class ImageClientServiceImpl implements ImageClientService {

	
	private String link;


	@SuppressWarnings("unchecked")
	@Override
	public String uploadImage(String filepath, String public_id)
			throws IOException {
		
		//Credentials and Transformation for cloudinary
		
		// TODO Auto-generated method stub
		Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name",
				"dyzv98rkm", "api_key", "669696846483369", "api_secret",
				"6NLM9Nb1i-622b04iOHGoeKYyEk"));
		
		Map<Object, Object> uploadResult = new HashMap<Object, Object>();

		Transformation transformation = new Transformation().width(400)
				.height(300).x(50).y(80).crop("limit").fetchFormat("png")
				.chain().flags("relative").width(0.5).background("green")
				.crop("thumb").gravity("face");

		Map options = ObjectUtils.asMap(
				"resource_type", "auto",
				"public_id", public_id,
		"transformation", transformation,
		"eager",Arrays.asList(new Transformation().width(0.2).crop("scale"),new Transformation().effect("hue:30"),
						new Transformation().effect("red")

				));
		
		//Check for URL path
		
		if(filepath.startsWith("http")||filepath.startsWith("https")){
			
			
			uploadResult = cloudinary.uploader().upload(filepath, options);

			for (Entry<Object, Object> entry : uploadResult.entrySet()) {

			//	System.out.println(entry.getKey() + "----->" + entry.getValue());

				if (entry.getKey().equals("url")) {
					link = (String) entry.getValue();
				}

			

		}
		}
		//Check for local path
		else{
	FileInputStream fileInputStream=null;
        
        File file = new File(filepath);
        
        byte[] bFile = new byte[(int) file.length()];
        
  
            //convert file into array of bytes
	    fileInputStream = new FileInputStream(file);
	    fileInputStream.read(bFile);
	    fileInputStream.close();
		

		System.out.println(file.getAbsolutePath());
		System.out.println(file.getPath());
		System.out.println("hello");
	System.out.println(file.getAbsolutePath());
		
		uploadResult = cloudinary.uploader().upload(bFile, options);

	
		System.out.println(uploadResult);

		for (Entry<Object, Object> entry : uploadResult.entrySet()) {

		//	System.out.println(entry.getKey() + "----->" + entry.getValue());

			if (entry.getKey().equals("url")) {
				link = (String) entry.getValue();
			}

		}
		}
		return link;

	
	}
	public static void main(String[] args) throws Exception {

	//	ImageView iv = new ImageView(getClass().getResource("/home/aman/Downloads/hello.png").toExternalForm());
		String filepath = "/home/aman/Downloads/jacki.png";
		String filepath2 = "https://upload.wikimedia.org/wikipedia/en/2/28/Deep_Fried_Man_portrait_-_real_name_Daniel_Friedman_-_South_African_Comedian.jpg";
		String public_id = "blank";
		ImageClientServiceImpl obj = new ImageClientServiceImpl();
		System.out.println(obj.uploadImage(filepath,public_id));

	}

}
