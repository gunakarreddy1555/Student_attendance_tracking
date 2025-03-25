package com.vcube.java.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vcube.java.DTO.BatchRequest;
import com.vcube.java.Entity.Batch;
import com.vcube.java.Service.BatchService;
import com.vcube.java.Service.CourseService;

@Controller
@RequestMapping("/batch")
public class Batchcontroller {

	@Autowired
	private CourseService courseservice;
	
	@Autowired
	private BatchService batchservcie;
	@GetMapping("/createBatch")
	public String getBatchRegistration(@RequestParam("courseId") Long courseId, Model model) {
		
	    model.addAttribute("course", courseservice.FindCourseByid(courseId));
	   
	    return "batchregisteration";
	}

	
	@PostMapping("/addBatch")
	@ResponseBody
	public Map<String, Object> addBatchAjax(@RequestBody BatchRequest batchreq) {
	    Map<String, Object> response = new HashMap<>();
	    try {	
	        Batch batch = new Batch();
	        batch.setBatchname(batchreq.getBatchName());
	        batch.setStartdate(batchreq.getStartDate());
	        batch.setCourse(courseservice.FindCourseByid(batchreq.getCourseId()));
	        batchservcie.save(batch);
	        response.put("status", "success");
	        response.put("courseId", batchreq.getCourseId());
	        System.out.println(response);
	        System.out.println("BBBfffffffffffRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR");
	        return response;
	    } catch (Exception e) {
	        response.put("status", "fail");
	        
	        return response;
	    }
	}


	@GetMapping("/getbatchesbycourse/{id}")
	public String GetBatchesByCourse(@PathVariable long id,Model model) {	
		List<Batch> activeBatchesBycourse = batchservcie.getListOfActiveBatchesBycourse(id);
		System.out.println(activeBatchesBycourse);
		model.addAttribute("batches", activeBatchesBycourse);
		model.addAttribute("course", courseservice.FindCourseByid(id));
		return "batch-home";
		
	}
	 @GetMapping("/checkBatchName")
	    public ResponseEntity<Map<String, Boolean>> checkBatchName(@RequestParam String batchName, @RequestParam Long courseId) {
	        boolean exists = batchservcie.existsByBatchNameAndCourseId(batchName, courseId);
	        Map<String, Boolean> response = new HashMap<>();
	        response.put("exists", exists);
	        return ResponseEntity.ok(response);
	    }
	
}
