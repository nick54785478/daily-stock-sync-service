package com.example.demo.iface.dto;

import java.util.Map;

public record NifiTestResource(String code, String method, String message, Map<String, Object> query) {

}
