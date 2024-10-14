package com.miniproject.javamini.route_management;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findByUsername(String username); // Optional: Add a method to find routes by username
}
