package com.snapdeal.reviews.service.test;

public class AlphaNumericUtil {
	
	private static char[] ALPHA_NUMBERIC;
	
	static {
		ALPHA_NUMBERIC = new char[26];
		for(int i = 0; i < 26; i++){
			ALPHA_NUMBERIC[i] = (char)(i+97); 
		}
//		for(int i = 26; i < 52; i++){
//			ALPHA_NUMBERIC[i] = (char)(i-26+65); 
//		}
//		for(int i = 52; i < 62; i++){
//			ALPHA_NUMBERIC[i] = Character.forDigit(i - 52, 10); 
//		}
	}
	
	public static String getWord(int length) {
		StringBuilder builder = new StringBuilder(length);
		for(int i = 0; i < length; i++){
			builder.append(ALPHA_NUMBERIC[(int) (Math.random() * ALPHA_NUMBERIC.length)]);
		}
		return builder.toString();
	}
	
	public static String getSentence(int[] meta){
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < meta.length; i++){
			builder.append(" ");
			for(int j = 0; j < meta[i]; j++){
				builder.append(ALPHA_NUMBERIC[(int) (Math.random() * ALPHA_NUMBERIC.length)]);
			}
		}
		return builder.substring(1);
	}
	
	public static String getSentence(int noOfWords) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < noOfWords; i++){
			builder.append(" ");
			builder.append(getWord((int)(Math.random() * 15)));
		}
		return builder.substring(1);
	}
	
	public static String getHeadline(){
		return getSentence((int)(Math.random()*15) + 2);
	}
	
	public static  String getComments(){
		return getSentence((int)(Math.random()*200) + 2);
	}
	
	public static void main(String[] args) {
		System.out.println(getHeadline());
	}
}
