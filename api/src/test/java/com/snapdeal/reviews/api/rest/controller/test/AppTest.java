package com.snapdeal.reviews.api.rest.controller.test;

import java.util.Random;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.snapdeal.reviews.data.event.BaseReviewEvent;
import com.snapdeal.reviews.data.event.ReviewEventType;
import com.snapdeal.reviews.data.event.exception.EventException;
import com.snapdeal.reviews.event.generator.EventGenerator;
import com.snapdeal.reviews.event.generator.KafkaEventGenerator;

public class AppTest {

	public static void main(String[] args) throws EventException {
		Random random = new Random();
		try (AbstractApplicationContext appContext = new ClassPathXmlApplicationContext("classpath:reviews-repository.xml",
				"classpath:reviews-event-client.xml", "classpath:reviews-cache.xml")) {
			EventGenerator gen = appContext.getBean(KafkaEventGenerator.class);
			BaseReviewEvent event = new BaseReviewEvent();
			event.setEventType(ReviewEventType.REVIEWCREATED);
			event.setReviewId(String.valueOf(random.nextInt(100)));
			gen.send("review_events_ext", event);
			BaseReviewEvent event2 = new BaseReviewEvent();
			event2.setEventType(ReviewEventType.RATINGDELETED);
			event2.setReviewId(String.valueOf(random.nextInt(100)));
			gen.send("review_events_ext", event2);
		} finally {
		}
	}

}
