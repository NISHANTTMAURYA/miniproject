package com.miniproject.javamini.route_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RouteManagementService {
    private final RouteRepository routeRepository;

    @Autowired
    public RouteManagementService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    public Route createRoute(Route route) {
        return routeRepository.save(route);
    }

    public Route updateRoute(Long id, String endLocation, String pincode, String modeOfTransport, String username) {
        Optional<Route> optionalRoute = routeRepository.findById(id);
        if (optionalRoute.isPresent()) {
            Route route = optionalRoute.get();
            route.setEndLocation(endLocation);
            route.setPincode(pincode);
            route.setModeOfTransport(modeOfTransport);
            route.setUsername(username);
            return routeRepository.save(route);
        }
        return null;
    }

    public boolean deleteRoute(Long id) {
        if (routeRepository.existsById(id)) {
            routeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}