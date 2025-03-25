package com.vcube.java.Controller;

import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vcube.java.Entity.Batchworkingdays;
import com.vcube.java.Entity.Student;
import com.vcube.java.Repository.BatchRepository;
import com.vcube.java.Repository.Batchworkingdaysrepo;
import com.vcube.java.Service.AttendanceService;
import com.vcube.java.Service.EmailService;
import com.vcube.java.Service.StudentService;


@Controller
public class MailController {

	@Autowired
	private StudentService studentservice;

	@Autowired
	private BatchRepository repo;

	@Autowired
	private AttendanceService attendanceService;

	@Autowired
	private Batchworkingdaysrepo batchworkingdaysrepo;

	@Autowired
	private BatchRepository batchrepo;
	
	@Autowired
	private EmailService emailservice;
	
	@PostMapping("/sendMail/{batchId}")
	public ResponseEntity<String> sendAttendanceEmails(@PathVariable long batchId, @RequestParam String month) {
		YearMonth yearMonth = YearMonth.parse(month);
		System.out.println("-----------mail controller-------");
		System.out.println(batchId);
		
		
		try {
		Map<Student, Long> monthlyperformance = new HashMap<>();

		// Fetch all students in the batch
		List<Student> students = studentservice.getActiveStudents(batchId);
		for (Student s : students) {

			monthlyperformance.put(s, attendanceService.getmonthlyattendancebymonth(s.getId(), yearMonth.getMonthValue(), yearMonth.getYear()));
		}

//        System.out.println(monthlyperformance);
		YearMonth ym = YearMonth.of(yearMonth.getYear(), yearMonth.getMonthValue());
		String monthName = ym.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

		Optional<List<Batchworkingdays>> batchIdAndMonth = batchworkingdaysrepo.findByBatchIdAndMonth(batchId,
				monthName);
		long totalLabs = batchIdAndMonth.get().get(0).getDays();
		for(Student s:monthlyperformance.keySet()) {
			Long studentattendedLabs = monthlyperformance.get(s);
			
			String email = s.getEmail();
			String name=s.getName();
			String subject="regarding ur monthly performance";
			String body="Dear " + name + ",\n\n" +
		               "You have attended " + studentattendedLabs + " lab sessions out of " + totalLabs + "  lab sessions.\n" +
		               "You have completed " + 2 + " weekly tests out of 4.\n\n" +
		               "VCube follows a structured process to ensure quality learning. If you are not interested in following the process, we won't be able to assist you further in placements.\n\n" +
		               "Best Regards,\n" +
		               "VCUBE Software Solutions";
			
			emailservice.sendEmail(email, subject, body);
			
		}
		return ResponseEntity.ok("Emails sent successfully!");
		}catch (Exception e) {
			return ResponseEntity.status(500).body("Failed to send emails.");
		}
    
	}
}
