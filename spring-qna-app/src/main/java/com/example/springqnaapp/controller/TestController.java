package com.example.springqnaapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
	@GetMapping("/")
	public ResponseEntity<String> index() {
		return ResponseEntity.ok("HelloWorld!");
	}
}