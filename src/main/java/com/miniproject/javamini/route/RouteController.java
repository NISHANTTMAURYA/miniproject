package com.miniproject.javamini.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import org.json.JSONArray;

@RestController
public class RouteController {

    @Autowired
    private RouteService routeService;

    @Value("${openrouteservice.api.key}")
    private String apiKey;

    @GetMapping("/travel-time")
    public String getTravelTime(
            @RequestParam String startLat,
            @RequestParam String startLon,
            @RequestParam String endLat,
            @RequestParam String endLon) {

        // Print the current coordinates
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("<h3>Start Coordinates: </h3>")
                .append("<p>Latitude: ").append(startLat).append(", Longitude: ").append(startLon).append("</p>")
                .append("<h3>End Coordinates: </h3>")
                .append("<p>Latitude: ").append(endLat).append(", Longitude: ").append(endLon).append("</p>");

        // Optionally, if you want to keep the travel details as well, uncomment the following lines:

        responseBuilder.append(getTravelDetails("driving-car", startLat, startLon, endLat, endLon));
        responseBuilder.append(getTravelDetails("foot-walking", startLat, startLon, endLat, endLon));
        responseBuilder.append(getTravelDetails("cycling-regular", startLat, startLon, endLat, endLon));


        return responseBuilder.toString();
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
            return String.format("<h3>Error retrieving details for mode: %s</h3><p>%s</p>", mode, e.getMessage());
        }
    }

    private String processResponse(String response, String mode) {
        JSONObject jsonResponse = new JSONObject(response);

        // Check for errors in the response
        if (jsonResponse.has("error")) {
            JSONObject error = jsonResponse.getJSONObject("error");
            return String.format("<h3>Error for mode: %s</h3><p>Error Code: %s</p><p>Message: %s</p>",
                    mode, error.getInt("code"), error.getString("message"));
        }

        JSONArray features = jsonResponse.getJSONArray("features");
        JSONObject firstFeature = features.getJSONObject(0);
        JSONObject summary = firstFeature.getJSONObject("properties").getJSONArray("segments").getJSONObject(0);

        double duration = summary.getDouble("duration") / 60; // converting to minutes

        // Format the output for each mode
        return String.format("<h3>Mode: %s</h3><p>Duration: %.2f minutes</p>",
                mode, duration);
    }
}
