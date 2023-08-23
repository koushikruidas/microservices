package com.kafka.messaging.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import com.kafka.messaging.model.User;

@RestController
public class KafkaController {

	@Autowired
	KafkaTemplate<String, User> kafkaTemplate;
	private String topic = "example";
	@PostMapping("/post/{name}")
	public String post(@PathVariable("name") String name) {
		kafkaTemplate.send(topic, new User(name,"hello@gmail.com"));
		return "Published Successfully";
	}
}
