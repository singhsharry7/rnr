package com.snapdeal.reviews.service.test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.snapdeal.reviews.commons.OpinionBo;
import com.snapdeal.reviews.commons.UserReviewsInfo;
import com.snapdeal.reviews.commons.dto.Status;
import com.snapdeal.reviews.commons.richdata.IRichData;
import com.snapdeal.reviews.commons.richdata.MultiSelect;
import com.snapdeal.reviews.commons.richdata.RichData;
import com.snapdeal.reviews.commons.richdata.RichDataSet;
import com.snapdeal.reviews.commons.richdata.SingleSelect;
import com.snapdeal.reviews.commons.richdata.Tag;
import com.snapdeal.reviews.model.ReviewBo;
import com.snapdeal.reviews.model.ReviewableObjectBo;
import com.snapdeal.reviews.repository.ReviewDao;
import com.snapdeal.reviews.repository.cassandra.ReviewUuid;
import com.snapdeal.reviews.repository.cassandra.ReviewUuid.InvalidReviewIdException;
import com.snapdeal.reviews.repository.cassandra.ReviewUuid.ReviewPartition;
import com.snapdeal.reviews.service.ReviewService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
		"classpath:reviews-service.xml", 
		"classpath:reviews-repository.xml",
		"classpath:reviews-event-client.xml",
		"classpath:reviews-cache.xml",
		"classpath:reviews-context.xml"
})
public class ReviewsDataLoader {

	@Autowired
	private ReviewService reviewService;
	
	@Resource(name="reviewDaoImpl")
	private ReviewDao reviewDao;
	
	@Test
	public void createReview(){
		ReviewBo reviewBo;
		ReviewableObjectBo reviewableObjectBo;
		UserReviewsInfo userReviewsInfo;
		
		reviewableObjectBo = new ReviewableObjectBo(String.valueOf("1001"), 0, 0);
		for (int j = 0; j < 1000; j++){
			reviewBo = new ReviewBo();
			reviewBo.setReviewableObjectBo(reviewableObjectBo);
			reviewBo.setHeadline(AlphaNumericUtil.getHeadline());
			reviewBo.setComments(AlphaNumericUtil.getComments());
			reviewBo.setRating((int) (Math.random()*5));
			reviewBo.setRecommended(OpinionBo.YES);
			reviewBo.setSelection(createSelection());
			reviewBo.setStatus(Status.APPROVED);
			reviewBo.setUserId(String.valueOf((int)(Math.random()*1000000)));
			reviewBo.setNickName(AlphaNumericUtil.getWord(10));
			
			reviewDao.createReview(reviewBo);
		}
		
	}
//	public void createReview(){
//		ReviewBo reviewBo;
//		ReviewableObjectBo reviewableObjectBo;
//		for(int i = 0; i < 10000; i++){
//			reviewableObjectBo = new ReviewableObjectBo(String.valueOf((int)(Math.random()*1000000)), 0, 0);
//			for (int j = 0; j < 1000; j++){
//				reviewBo = new ReviewBo();
//				reviewBo.setReviewableObjectBo(reviewableObjectBo);
//				reviewBo.setHeadline(AlphaNumericUtil.getHeadline());
//				reviewBo.setComments(AlphaNumericUtil.getComments());
//				reviewBo.setNickName(AlphaNumericUtil.getWord(10));
//				reviewBo.setRating((int) (Math.random()*5));
//				reviewBo.setRecommended(OpinionBo.fromId((int) Math.random()));
//				reviewBo.setSelection(createSelection());
//				reviewBo.setStatus(Status.APPROVED);
//				reviewBo.setUserId(String.valueOf((int)(Math.random()*1000000)));
//				reviewDao.createReview(reviewBo);
//			}
//		}
//	}
	
	private static RichDataSet createSelection() {
		SingleSelect price = new SingleSelect("price",null,new Tag("over-priced"));
		MultiSelect selectedPros = new MultiSelect("pros",null,new HashSet<Tag>( Arrays.asList(new Tag("email"))));
		MultiSelect selectedCons = new MultiSelect("cons",null,new HashSet<Tag>( Arrays.asList(new Tag("battery"))));
		RichDataSet selection = createRichDataSet(Arrays.asList((RichData)price,selectedPros,selectedCons));
		return selection;
	}
	private static  RichDataSet createRichDataSet(Collection<RichData> elements) {
		return new RichDataSet("root", new HashSet<IRichData>(elements ));
	}
	
	public void updateNickname(){
		reviewDao.updateNickName("014be0307587000000002329000003a4");
	}
	
	/*public static void main(String[] args) throws InvalidReviewIdException {
		ReviewUuid fromHex = ReviewUuid.fromHex("014bdad677040000000003e900000149");
		ReviewPartition reviewPartition = fromHex.getReviewPartition();
		System.out.println(reviewPartition.getObjectIdHash());
	}*/
}
