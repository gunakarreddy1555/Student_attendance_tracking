package com.vcube.java.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Student {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    private String name;
    @Column(unique = true, nullable = false)
    private String mobileWhatsApp;
    private String alternateMobileNumber;
    @Column(unique = true, nullable = false)
    private String email;
     private String rollnumber;
    private String pg;
    private String pgStream;
    private String pgYOP;
    private Double pgPercentage;

    private String graduation;
    private String graduationStream;
    private String graduationYOP;
    private Double graduationPercentage;

    private String interOrDiploma;
    private String interOrDiplomaStream;
    private Double interOrDiplomaPercentage;
    private String interOrDiplomaYOP;

    private Double tenthPercentage;
    private String tenthYOP;

    private String internshipExperience; 
    private String internshipRoleDuration; 
    private Double fullTimeITExperience; 
    private String experiencedDomainRolePackage; 
    private String careerGap;
    private String status="Active";
    @Lob
    private byte[] resume;
    
    @Lob
    private byte[] photo;
    
    @ManyToOne
    private Batch batch;
}
