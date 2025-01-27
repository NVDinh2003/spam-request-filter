package com.nvd.controller;


import com.nvd.annotation.RedisCheck;
import com.nvd.service.RateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import jakarta.servlet.http.HttpServletRequest;

@RestController
public class TestController {
    @Autowired
    private RateLimiterService rateLimiterService;

    @RedisCheck // Áp dụng kiểm tra Redis trước khi xử lý method này
    @GetMapping("/my-endpoint")
    public ResponseEntity<String> myEndpoint(HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        if (rateLimiterService.allowRequest(ipAddress, 10, 60)) { // Cho phép tối đa 10 request trong 1 phút từ cùng một IP
            // Xử lý request ở đây
            return ResponseEntity.ok("Request allowed");
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many requests");
        }
    }
}