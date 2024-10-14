package com.miniproject.javamini.route_management;

import com.miniproject.javamini.user_management.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.miniproject.javamini.user_management.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class RouteManagementService {

    private static final Logger logger = LoggerFactory.getLogger(RouteManagementService.class);

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    public List<Route> getRoutesByUsername(String username) {
        if (username == null || username.isEmpty()) {
            return getAllRoutes();
        }
        return routeRepository.findByUsername(username);
    }

    public Route createRoute(Route route) {
        // Check if the username is set
        if (route.getUsername() == null || route.getUsername().isEmpty()) {
            throw new RuntimeException("Username must be set when creating a route");
        }
        return routeRepository.save(route);
    }

    public Route updateRoute(Long id, Route updatedRoute) {
        Route existingRoute = routeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        existingRoute.setEndLocation(updatedRoute.getEndLocation());
        existingRoute.setPincode(updatedRoute.getPincode());
        existingRoute.setModeOfTransport(updatedRoute.getModeOfTransport());
        existingRoute.setUsername(updatedRoute.getUsername());

        return routeRepository.save(existingRoute);
    }

    public void deleteRoutes(List<Long> ids) {
        routeRepository.deleteAllById(ids);
    }

    public List<String> getAllUsernames() {
        try {
            logger.info("Fetching all usernames");
            List<String> usernames = userRepository.findAll().stream()
                    .map(User::getUsername)
                    .collect(Collectors.toList());

            logger.info("Found {} usernames", usernames.size());

            return usernames;
        } catch (Exception e) {
            logger.error("Error fetching usernames", e);
            throw new RuntimeException("Error fetching usernames: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void updateRouteUsernames() {
        List<Route> routes = routeRepository.findAll();
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new RuntimeException("No users found in the database");
        }

        for (Route route : routes) {
            if (route.getUsername() == null || route.getUsername().isEmpty()) {
                // Assign a random user to the route
                User randomUser = users.get(new Random().nextInt(users.size()));
                route.setUsername(randomUser.getUsername());
                routeRepository.save(route);
            }
        }
    }
}