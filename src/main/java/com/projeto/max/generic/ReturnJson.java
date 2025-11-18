package com.projeto.max.generic;

import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ReturnJson {
    public static ResponseEntity<Map<String, Object>> response(Integer status, Object content, String message, boolean success){
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        response.put("content", content);
        response.put("status", status);
        response.put("time", LocalDateTime.now());
        return ResponseEntity.status(status).body(response);
    }

    public static ResponseEntity<Map<String, Object>> error(Integer status, Object content, String message){
        return response(status, content, message, false);
    }
    public static ResponseEntity<Map<String, Object>> errorGeral(Integer status, String message){
        return response(status, null, message, false);
    }

    public static ResponseEntity<Map<String, Object>> success(Integer status, Object content, String message){
        return response(status, content, message, true);
    }
}
