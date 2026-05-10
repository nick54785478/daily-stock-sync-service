package com.example.demo.iface.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.iface.dto.NifiTestResource;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/test")
public class TestController {

	@GetMapping("/{id}")
	public NifiTestResource getById(@PathVariable String id, @RequestParam Map<String, Object> query) {
		System.out.println(new NifiTestResource("200", "GET", "這是一個 Get 方法", query));
		return new NifiTestResource("200", "GET", "這是一個 Get 方法", query);
	}
}
