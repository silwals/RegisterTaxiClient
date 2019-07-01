package com.miamioh.registertaxiclient.service;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.miamioh.registertaxiclient.model.request.Taxi;

import lombok.extern.slf4j.Slf4j;
//41.892072635,-87.62887415700001

@Service
@Slf4j
public class RequestProducerService {
	
	@Autowired
    private KafkaTemplate<String, Taxi> kafkaTemplate;
	
	@Value(value = "${kafka.topic}")
    private String topicName;   
	@Value(value = "${kafka.taxi.next.topic}")
    private String deRegisterTopicName;
	
	private static final Logger log = LoggerFactory.getLogger(RequestProducerService.class);
	
	
	public void createAndSendRequestAsync(String input) {
		log.info("Inside Register Taxi Request Producer Service");
		String[] tokens = input.split(",");
		Taxi taxi = new Taxi();
		//String taxiId = UUID.randomUUID().toString();
		String taxiId = Integer.toString((new Random()).nextInt(100-10)+100);
		taxi.setTaxiId(taxiId);
		taxi.setLatitude(Double.valueOf(tokens[0]));
		taxi.setLongitude(Double.valueOf(tokens[1]));
		taxi.setNoOfPassenger(new AtomicInteger(0));
		log.info("Generated Taxi Object: "+taxi);
		ListenableFuture<SendResult<String, Taxi>> future = kafkaTemplate.send(topicName, taxi);
		StopWatch watch = new StopWatch();
		watch.start();
		future.addCallback(
				new ListenableFutureCallback<SendResult<String, Taxi>>() {

					@Override
					public void onSuccess(
							SendResult<String, Taxi> result) {
						log.info("Successfully published message to the kafka topic with key={} and offset={}",
								taxi, result.getRecordMetadata().offset());

						watch.stop();
						log.info("total time taken to send the message to topic :{}", taxi.getTaxiId(), 
								watch.getTotalTimeMillis());
					}

					@Override
					public void onFailure(Throwable ex) {
						log.error("Unable to publish message to the kafka topic with key={}",
								taxi, ex);

						watch.stop();
						log.info("total time taken to send the message to topic :", taxi.getTaxiId(), 
								watch.getTotalTimeMillis());
					}
				});
	}


	public void deregisterTaxiAsync(Taxi taxi) {
		log.info("Generated Taxi Object to Deregister: "+taxi.getTaxiId());
		ListenableFuture<SendResult<String, Taxi>> future = kafkaTemplate.send(deRegisterTopicName, taxi);
		StopWatch watch = new StopWatch();
		watch.start();
		future.addCallback(
				new ListenableFutureCallback<SendResult<String, Taxi>>() {

					@Override
					public void onSuccess(
							SendResult<String, Taxi> result) {
						log.info("Successfully published message to the kafka topic with key={} and offset={}",
								taxi, result.getRecordMetadata().offset());

						watch.stop();
						log.info("total time taken to send the message to topic :{}", taxi.getTaxiId(), 
								watch.getTotalTimeMillis());
					}

					@Override
					public void onFailure(Throwable ex) {
						log.error("Unable to publish message to the kafka topic with key={}",
								taxi, ex);

						watch.stop();
						log.info("total time taken to send the message to topic :", taxi.getTaxiId(), 
								watch.getTotalTimeMillis());
					}
				});
	}

}
