package com.vcube.java.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String coursename;
	private String duration;
}
