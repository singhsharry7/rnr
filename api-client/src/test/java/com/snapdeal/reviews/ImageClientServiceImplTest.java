package com.snapdeal.reviews;

import java.io.IOException;

import org.junit.Test;

import com.snapdeal.reviews.client.api.impl.ImageClientServiceImpl;

public class ImageClientServiceImplTest {

	
	@Test
	public void testImageUploadForHttp() throws IOException{
		
		ImageClientServiceImpl obj=new ImageClientServiceImpl();
		
		String url=obj.uploadImage("http://static.guim.co.uk/sys-images/Guardian/About/General/2013/1/3/1357230493554/Man-Booker-Prize-judges-p-011.jpg","1234");
		
		
		System.out.println(url);
		
		
	}

	@Test
	public void testImageUploadForLocal() throws IOException{
		
		ImageClientServiceImpl obj=new ImageClientServiceImpl();
		
		String url=obj.uploadImage("/home/aman/Downloads/jacki.png","5678");
		
		
		System.out.println(url);
		
		
	}
	
	
	
	
}
