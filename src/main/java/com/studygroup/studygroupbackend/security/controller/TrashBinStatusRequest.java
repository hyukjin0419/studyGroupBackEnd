package com.studygroup.studygroupbackend.security.controller;

import lombok.Data;

@Data
public class TrashBinStatusRequest {
    private String pickupZone;
    private double plasticKg;
    private double paperKg;
    private double generalKg;
    private String lastUpdated;
}
