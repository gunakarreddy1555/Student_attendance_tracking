package com.vcube.java.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Batchworkingdays {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long batchworkingid;	
	private long days;
	private String month;
	private long examDays; // ✅ New field to track exam days
	@ManyToOne
	private Batch batch;
	
}
