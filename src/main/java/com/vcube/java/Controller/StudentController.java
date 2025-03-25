package com.vcube.java.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.vcube.java.DTO.StudentDTO;
import com.vcube.java.Entity.Batch;
import com.vcube.java.Entity.Student;
import com.vcube.java.Service.BatchService;
import com.vcube.java.Service.StudentService;

@Controller
@RequestMapping("/student")
public class StudentController {

	@Autowired
	private StudentService studentservice;
	@Autowired
	private BatchService batchservcie;
	@GetMapping("/")
	public String getfirstpage() {
		return "index";
	}

	@GetMapping("/Studentreg")
	public String addStudentForm(@RequestParam("batchId") Long batchId, Model model) {
	    System.out.println(batchId+"=========================");
	    model.addAttribute("batch", batchservcie.getBtachByid(batchId));  
	    return "studnetfirstregisteration"; 
	}
	
	public ResponseEntity<String> saveStudent(@ModelAttribute Student student, 
            @RequestParam MultipartFile photo, 
            @RequestParam MultipartFile resume)
	{
	    // save logic here
	    // Save photo & resume if required
	    // save student entity in DB
		System.out.println("----------------hellooo ------------");
	    return ResponseEntity.ok("Success");
	}

	@PostMapping("/registerStudent")
	@ResponseBody
	public String registerStudent(@RequestBody StudentDTO studentDTO) {
	    try {
	    	Student student = new Student();
	    	student.setEmail(studentDTO.getEmail());
	    	student.setMobileWhatsApp(studentDTO.getMobileWhatsApp());
	    	student.setName(studentDTO.getName());
	    	student.setAlternateMobileNumber(studentDTO.getAlternateMobileNumber()); 	 
	    	student.setBatch(batchservcie.getBtachByid(studentDTO.getBatchid()));
	    	student.setRollnumber(Assignrollnumber(studentDTO.getBatchid()));
	    	System.out.println(student);
	    	studentservice.saveStudent(student);
	        return "success";
	    } catch (Exception e) {
	    	Batch batch = batchservcie.getBtachByid(studentDTO.getBatchid());
	    	 Long nextNumber = batch.getLastStudentNumber() - 1;
	         batch.setLastStudentNumber(nextNumber);
	         batchservcie.save(batch);
	        return "error";
	    }
	}

	@GetMapping("/checkMobile")
    @ResponseBody
    public Map<String, Boolean> checkMobileExists(@RequestParam("mobile") String mobile) {
        boolean exists = studentservice.isMobileExists(mobile);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return response;
    }

    @GetMapping("/checkEmail")
    @ResponseBody
    public Map<String, Boolean> checkEmailExists(@RequestParam("email") String email) {
        boolean exists = studentservice.isEmailExists(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return response;
    }

	public String Assignrollnumber(long batchid) {
		Batch batch = batchservcie.getBtachByid(batchid);
		
        Long nextNumber = batch.getLastStudentNumber() + 1;
        batch.setLastStudentNumber(nextNumber);
     
        batchservcie.save(batch);
        String studentId = String.format("VCJFT-%s-%03d", batch.getBatchname(), nextNumber);
        return studentId;
	}
	
	@GetMapping("/activeStudents")
    public String activeStudents(@RequestParam Long batchId, Model model) {
	
        List<Student> activeStudents = studentservice.getActiveStudents(batchId);
        Batch batch = batchservcie.getBtachByid(batchId);
        model.addAttribute("Studentsbybatch", activeStudents);
        model.addAttribute("status", "activeStudents");
        model.addAttribute("batch", batch);
        return "show-students-by-batch"; // Thymeleaf page name
    }

    @GetMapping("/inactiveStudents")
    public String inactiveStudents(@RequestParam Long batchId, Model model) {
    
        List<Student> inactiveStudents = studentservice.getInactiveStudents(batchId);
        Batch batch = batchservcie.getBtachByid(batchId);
        model.addAttribute("Studentsbybatch", inactiveStudents);
        model.addAttribute("status", "inactiveStudents");
        model.addAttribute("batch", batch);
        return "show-students-by-batch"; 
        
        }

    @GetMapping("/viewStudents")
    public String allStudents(@RequestParam Long batchId, Model model) {
    	
        List<Student> allStudents = studentservice.getAllStudents(batchId);
        Batch batch = batchservcie.getBtachByid(batchId);
        model.addAttribute("Studentsbybatch", allStudents);
        model.addAttribute("status", "Students");
        model.addAttribute("batch", batch);
        return "show-students-by-batch"; 
    }
}
