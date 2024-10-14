package com.miniproject.javamini.route_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@RestController
@RequestMapping("/route-management")
public class RouteManagementController {
    private static final Logger logger = LoggerFactory.getLogger(RouteManagementController.class);
    @Autowired
    private RouteManagementService routeManagementService;

    @GetMapping("/routes")
    public List<Route> getAllRoutes(@RequestParam(required = false) String username) {
        if (username != null && !username.isEmpty()) {
            return routeManagementService.getRoutesByUsername(username);
        }
        return routeManagementService.getAllRoutes();
    }

    @PostMapping("/routes")
    public Route createRoute(@RequestBody Route route) {
        return routeManagementService.createRoute(route);
    }

    @PutMapping("/routes/{id}")
    public Route updateRoute(@PathVariable Long id, @RequestBody Route route) {
        return routeManagementService.updateRoute(id, route);
    }

    @DeleteMapping("/routes")
    public void deleteRoutes(@RequestBody List<Long> ids) {
        routeManagementService.deleteRoutes(ids);
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllUsernames() {
        try {
            List<String> usernames = routeManagementService.getAllUsernames();
            return ResponseEntity.ok(usernames);
        } catch (Exception e) {
            logger.error("Error fetching usernames", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching usernames: " + e.getMessage());
        }
    }
    @PostMapping("/update-route-usernames")
    public ResponseEntity<String> updateRouteUsernames() {
        try {
            routeManagementService.updateRouteUsernames();
            return ResponseEntity.ok("Route usernames updated successfully");
        } catch (Exception e) {
            logger.error("Error updating route usernames", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating route usernames: " + e.getMessage());
        }
    }
}