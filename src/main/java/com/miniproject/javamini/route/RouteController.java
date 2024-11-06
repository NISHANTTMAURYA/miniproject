package com.miniproject.javamini.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.concurrent.CompletableFuture;

@RestController
public class RouteController {

    @Autowired
    private RouteService routeService;

    @Value("${openrouteservice.api.key}")
    private String apiKey;

    @GetMapping("/travel-time")
    public ResponseEntity<String> getTravelTime(
            @RequestParam String startLat,
            @RequestParam String startLon,
            @RequestParam String endLat,
            @RequestParam String endLon,
            @RequestParam(required = false) String mode) {

        try {
            JSONObject response = new JSONObject();
            JSONArray modes = new JSONArray();

            if (mode != null) {
                // Single mode request
                JSONObject modeDetails = new JSONObject(getTravelDetails(mode, startLat, startLon, endLat, endLon));
                return ResponseEntity.ok(modeDetails.toString());
            } else {
                // Parallel processing for all modes
                String[] transportModes = {"driving-car", "foot-walking", "cycling-regular"};
                List<CompletableFuture<JSONObject>> futures = Arrays.stream(transportModes)
                    .map(m -> CompletableFuture.supplyAsync(() -> 
                        new JSONObject(getTravelDetails(m, startLat, startLon, endLat, endLon))))
                    .collect(Collectors.toList());

                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
                
                for (CompletableFuture<JSONObject> future : futures) {
                    modes.put(future.get());
                }
            }

            response.put("modes", modes);
            return ResponseEntity.ok(response.toString());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private String getTravelDetails(String mode, String startLat, String startLon, String endLat, String endLon) {
        String apiUrl = String.format("https://api.openrouteservice.org/v2/directions/%s?start=%s,%s&end=%s,%s&api_key=%s",
                mode, startLon, startLat, endLon, endLat, apiKey);

        RestTemplate restTemplate = new RestTemplate();
        String response;

        try {
            response = restTemplate.getForObject(apiUrl, String.class);
            return processResponse(response, mode);
        } catch (Exception e) {
            return String.format("{\"error\": \"%s\"}", e.getMessage());
        }
    }

    private String processResponse(String response, String mode) {
        JSONObject jsonResponse = new JSONObject(response);

        if (jsonResponse.has("error")) {
            JSONObject error = jsonResponse.getJSONObject("error");
            return String.format("{\"error\": \"%s\"}", error.getString("message"));
        }

        JSONArray features = jsonResponse.getJSONArray("features");
        JSONObject firstFeature = features.getJSONObject(0);
        JSONObject summary = firstFeature.getJSONObject("properties").getJSONArray("segments").getJSONObject(0);

        double duration = summary.getDouble("duration") / 60; // converting to minutes
        double distance = summary.getDouble("distance") / 1000; // converting to kilometers

        // Format mode name for display
        String displayMode = mode.replace("-", " ");
        displayMode = displayMode.substring(0, 1).toUpperCase() + displayMode.substring(1);

        return String.format("{\"mode\": \"%s\", \"duration\": %.2f, \"distance\": %.2f}",
                displayMode, duration, distance);
    }
}
