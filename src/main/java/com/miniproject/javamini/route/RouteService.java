package com.miniproject.javamini.route;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.TimeUnit;

@Service
public class RouteService {

    @Value("${openrouteservice.api.key}")
    private String apiKey;

    private static final String BASE_URL = "https://api.openrouteservice.org/v2/directions/driving-car";
    private final Cache<String, String> routeCache;

    public RouteService() {
        this.routeCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();
    }

    public String getTravelTime(String startLat, String startLon, String endLat, String endLon) {
        String cacheKey = String.format("%s-%s-%s-%s", startLat, startLon, endLat, endLon);
        
        String cachedResult = routeCache.getIfPresent(cacheKey);
        if (cachedResult != null) {
            return cachedResult;
        }

        // Construct the URL
        String url = String.format("%s?start=%s,%s&end=%s,%s", BASE_URL, startLon, startLat, endLon, endLat);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", apiKey);
        headers.set("Accept", "application/json");

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            String result = response.getBody();
            
            // Cache the result
            routeCache.put(cacheKey, result);
            
            return result;
        } catch (Exception e) {
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }
}
