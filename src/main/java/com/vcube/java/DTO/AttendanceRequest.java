package com.vcube.java.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AttendanceRequest {
    private long studentId;
    private LocalDate date;
    private boolean present;
}
