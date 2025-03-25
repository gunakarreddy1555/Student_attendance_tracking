package com.vcube.java.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vcube.java.Entity.Course;

@Repository
public interface CourseRepo extends JpaRepository<Course,Long>{

}
