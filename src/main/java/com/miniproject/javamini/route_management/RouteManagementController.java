package com.miniproject.javamini.route_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/route-management")
public class RouteManagementController {
    private final RouteManagementService routeManagementService;

    @Autowired
    public RouteManagementController(RouteManagementService routeManagementService) {
        this.routeManagementService = routeManagementService;
    }

    @GetMapping("/routes")
    @ResponseBody
    public List<Route> getAllRoutes() {
        return routeManagementService.getAllRoutes();
    }

    @PostMapping("/routes")
    @ResponseBody
    public Route createRoute(
            @RequestParam String endLocation,
            @RequestParam String pincode,
            @RequestParam String modeOfTransport,
            @RequestParam String username) {
        Route route = new Route(endLocation, pincode, modeOfTransport, username);
        return routeManagementService.createRoute(route);
    }

    @PutMapping("/routes/{id}")
    @ResponseBody
    public ResponseEntity<Route> updateRoute(
            @PathVariable Long id,
            @RequestParam String endLocation,
            @RequestParam String pincode,
            @RequestParam String modeOfTransport,
            @RequestParam String username) {
        Route updatedRoute = routeManagementService.updateRoute(id, endLocation, pincode, modeOfTransport, username);
        if (updatedRoute != null) {
            return ResponseEntity.ok(updatedRoute);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/routes/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long id) {
        boolean deleted = routeManagementService.deleteRoute(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}