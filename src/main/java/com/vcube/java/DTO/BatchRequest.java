package com.vcube.java.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class BatchRequest {

	private String batchName;
    private LocalDate startDate;
    private long courseId;
}
