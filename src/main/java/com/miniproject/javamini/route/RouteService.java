package com.miniproject.javamini.route;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Service
public class RouteService {

    @Value("${openrouteservice.api.key}")
    private String apiKey; // Ensure this is a field in your class

    private static final String BASE_URL = "https://api.openrouteservice.org/v2/directions/driving-car";

    public String getTravelTime(String startLat, String startLon, String endLat, String endLon) {
        RestTemplate restTemplate = new RestTemplate();

        // Construct the URL
        String url = String.format("%s?start=%s,%s&end=%s,%s", BASE_URL, startLon, startLat, endLon, endLat);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", apiKey);
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/geo+json");

        // Create an HttpEntity with the headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Send the GET request with headers
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return response.getBody(); // Return the response body
    }
}
