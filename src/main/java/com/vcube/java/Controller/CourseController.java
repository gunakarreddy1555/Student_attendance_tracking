package com.vcube.java.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vcube.java.Entity.Course;
import com.vcube.java.Service.CourseService;

@Controller
@RequestMapping("/course")
public class CourseController {

	@Autowired
	private CourseService courseservice;
	@GetMapping("/")
	public String getfirstpage(Model model) {
		List<Course> allcourses = courseservice.getAllcourses();
		System.out.println("--------------------------------------------");
		System.out.println(allcourses);
		model.addAttribute("courses",allcourses);
		return "index";
	}
	@GetMapping("/register")
	public String getCourseRegisteration() {
		return "courseregisteration";
	}
	
	
}
