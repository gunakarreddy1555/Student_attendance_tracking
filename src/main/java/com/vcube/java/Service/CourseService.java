package com.vcube.java.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vcube.java.Entity.Course;
import com.vcube.java.Repository.CourseRepo;

@Service
public class CourseService {

	@Autowired
	private CourseRepo courserepo;
	public List<Course> getAllcourses(){
		return courserepo.findAll();
	}
	public Course FindCourseByid(long courseId) {
		return courserepo.findById(courseId).get();
		
	}
}
