package com.miniproject.javamini.route_management;

import jakarta.persistence.*;

@Entity
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "END_LOCATION")
    private String endLocation;

    @Column(name = "MODE_OF_TRANSPORT")
    private String modeOfTransport;

    @Column(name = "PINCODE")
    private String pincode;

    @Column(name = "USERNAME")
    private String username;


    // Default constructor
    public Route() {
    }

    // Parameterized constructor
    public Route(String endLocation, String pincode, String modeOfTransport, String username) { // Change Long userId to String username
        this.endLocation = endLocation;
        this.pincode = pincode;
        this.modeOfTransport = modeOfTransport;
        this.username = username; // Use username
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getModeOfTransport() {
        return modeOfTransport;
    }

    public void setModeOfTransport(String modeOfTransport) {
        this.modeOfTransport = modeOfTransport;
    }

    public String getUsername() {
        return username; // Add getter for username
    }

    public void setUsername(String username) {
        this.username = username; // Add setter for username
    }
}
