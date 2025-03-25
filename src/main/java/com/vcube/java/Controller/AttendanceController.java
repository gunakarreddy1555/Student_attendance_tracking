package com.vcube.java.Controller;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.vcube.java.DTO.AttendanceRequest;
import com.vcube.java.Entity.Attendance;
import com.vcube.java.Entity.Batch;
import com.vcube.java.Entity.Batchworkingdays;
import com.vcube.java.Entity.Student;
import com.vcube.java.Repository.BatchRepository;
import com.vcube.java.Repository.Batchworkingdaysrepo;
import com.vcube.java.Service.AttendanceService;
import com.vcube.java.Service.StudentService;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

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

	@GetMapping("/takeattendance/{id}")
	public String takeAttendance(@PathVariable long id, Model model) {
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		Optional<Batch> byId = repo.findById(id);
		Batch batch = byId.get();
		List<Student> students = studentservice.getActiveStudents(id);

		System.out.println(students);
		model.addAttribute("students", students);
		model.addAttribute("batch", batch);
		return "take-attendance";
	}

//	@GetMapping("/attendance/get/{batchId}")
//	@ResponseBody
//	public List<Attendance> getAttendance(@PathVariable Long batchId, @RequestParam String date) {
////		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
//		List<Attendance> attendance = attendanceService.getAttendanceByBatchAndDate(batchId, date);
//
//		return attendance;
//	}
////	save attendance 
//
	@PostMapping("/submit")
	public ResponseEntity<Map<String, Object>> submitAttendance(@RequestParam Long batchId,
			@RequestParam boolean attendanceTaken, @RequestParam boolean examAttendance,
			@RequestBody List<AttendanceRequest> attendanceData) {

		Map<String, Object> response = new HashMap<>();

		Batch batch = batchrepo.findById(batchId).orElseThrow(() -> new RuntimeException("Batch not found"));
		// ✅ Check if attendanceData is empty
		if (attendanceData == null || attendanceData.isEmpty()) {
			response.put("success", false);
			response.put("message", "Attendance data cannot be empty.");
			response.put("course", batch.getCourse().getId());
			return ResponseEntity.badRequest().body(response);
		}

		LocalDate date = attendanceData.get(0).getDate();
		String monthName = date.format(DateTimeFormatter.ofPattern("MMMM"));

		if (attendanceTaken) {
			Optional<List<Batchworkingdays>> batchIdAndMonth = batchworkingdaysrepo.findByBatchIdAndMonth(batchId,
					monthName);

			if (batchIdAndMonth.isPresent() && !batchIdAndMonth.get().isEmpty()) {
				Batchworkingdays batchworkingdays = batchIdAndMonth.get().get(0);
				batchworkingdays.setDays(batchworkingdays.getDays() + 1);
				batchworkingdaysrepo.save(batchworkingdays);
			} else {
				Batchworkingdays batchworkingdays = new Batchworkingdays();
				batchworkingdays.setBatch(batch);
				batchworkingdays.setDays(1);
				batchworkingdays.setMonth(monthName);
				batchworkingdaysrepo.save(batchworkingdays);
			}
		}

		try {
			int savedCount = 0; // ✅ Track saved attendance records
			for (AttendanceRequest data : attendanceData) {
				Student student = studentservice.findById(data.getStudentId());
				boolean alreadyMarked = attendanceService.checkisprentornotbyday(student.getId(), date);

				if (!alreadyMarked && data.isPresent()) {
					Attendance attendance = new Attendance();
					attendance.setStudent(student);
					attendance.setDate(data.getDate());
					attendance.setStatus("P");
					attendanceService.save(attendance);
					savedCount++; // ✅ Increment saved count
				}
			}

			response.put("success", true);
			response.put("message", "Attendance saved successfully!");
			response.put("savedCount", savedCount); // ✅ Add count to response
			response.put("course", batch.getCourse().getId());
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "Something went wrong: " + e.getMessage());
			response.put("course", batch.getCourse().getId());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@PostMapping("/show")
	public String showAttendance(@RequestParam("batchId") Long batchId, @RequestParam("type") String type,
			@RequestParam("value") String value, Model model) {
		Batch batch = batchrepo.findById(batchId).get();
		System.out.println(type);
		if (type.equals("day")) {
			LocalDate date = LocalDate.parse(value);
			
			model.addAttribute("attendanceByday", attendanceService.findbyAttendanceByday(batchId, date));
			model.addAttribute("date", date);
			model.addAttribute("batch", batch);
			return "show-attendance-by-day";

		} else if (type.equals("month")) {
			Map<Student, Long> monthlyperformance = new HashMap<>();
			YearMonth yearMonth = YearMonth.parse(value);
			String monthName = yearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
			List<Student> students = studentservice.getActiveStudents(batchId);
			for (Student s : students) {

				monthlyperformance.put(s, attendanceService.getmonthlyattendancebymonth(s.getId(),
						yearMonth.getMonthValue(), yearMonth.getYear()));
			}
			model.addAttribute("monthlyperformance",monthlyperformance);
			List<Batchworkingdays> list = batchworkingdaysrepo.findByBatchIdAndMonth(batchId, monthName).get();
			model.addAttribute("batchworkingdays",
		    batchworkingdaysrepo.findByBatchIdAndMonth(batchId, monthName).get().get(0));
			model.addAttribute("yearMonth", yearMonth);
			model.addAttribute("batch",batch);
             return "show-Monthly-bybatch-month";
		}
		return "index.html";

	}
	@GetMapping("/viewdetails/{id}")
	public String viewStudentDetails(@PathVariable long id, @RequestParam String month, Model model) {
		YearMonth yearMonth = YearMonth.parse(month);
		Student student = studentservice.findById(id);
		List<Attendance> attendanceRecords = attendanceService.getStudentAttendanceByMonth(id, yearMonth.getMonthValue(), yearMonth.getYear());
		model.addAttribute("student", student);
		model.addAttribute("studnetmonthperformance", attendanceRecords);
	    return "Studentmonthsheet";
	}
//	@GetMapping("/viewAttendance/{id}")
//	public String getBatchAttendanceByMonth(@PathVariable long id, @RequestParam String month, Model model) {
//	    try {
//	        String[] parts = month.split("-");
//	        int year = Integer.parseInt(parts[0]);
//	        int m = Integer.parseInt(parts[1]);
//	        Map<Student, Long> monthlyperformance = new HashMap<>();
//              
//	        // Fetch batch information separately
//	        Optional<Batch> batchOpt = batchrepo.findById(id);
//	        String batchName = batchOpt.map(Batch::getBatchName).orElse("Unknown Batch");
//
//	        // Fetch students in the batch
//	        List<Student> students = studentservice.getStudentsByBatchId(id);
//	        for (Student s : students) {
//	        	
//	            monthlyperformance.put(s, attendanceService.getmonthlyattendancebymonth(s.getId(), m, year));
//	        }
//
//	        YearMonth ym = YearMonth.of(year, m);
//	        String monthName = ym.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
//
//	        Optional<List<Batchworkingdays>> batchIdAndMonth = batchworkingdaysrepo.findByBatchIdAndMonth(id, monthName);
//	        boolean noRecords = batchIdAndMonth.isEmpty() || batchIdAndMonth.get().isEmpty();
//
//	        model.addAttribute("monthlyperformance", monthlyperformance);
//	        model.addAttribute("noRecords", noRecords);
//	        model.addAttribute("batchworkingdays", noRecords ? 0 : batchIdAndMonth.get().get(0).getDays());
//	        model.addAttribute("batchname", batchName); // Always display batch name
//	        model.addAttribute("monthname", monthName);
//	        model.addAttribute("year", year);
//	        model.addAttribute("yearandmonth", month);
//
//	        if (noRecords) {
//	            model.addAttribute("noRecordsMessage", "No attendance records found for this month.");
//	        }
//
//	        return "show-Monthly-bybatch-month";
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        return "error-page";
//	    }
//	}
//
//	@GetMapping("/viewStudentAttendance/{id}")
//	public String getStudentAttendance(@PathVariable Long id, @RequestParam String month,Model model) {
//		try {
//			String[] parts = month.split("-");
//			int year = Integer.parseInt(parts[0]);
//			int m = Integer.parseInt(parts[1]);
//			System.out.println("-----------specific student attendence-------");
//			System.out.println(m+"-----"+year);
//			Student student = studentservice.getStudentById(id);
//			List<Attendance> attendanceRecords = attendanceService.getStudentAttendanceByMonth(id, m, year);
//			model.addAttribute("student", student);
//			model.addAttribute("studnetmonthperformance", attendanceRecords);
//
//			return "Studentmonthsheet";
//		} catch (Exception e) {
//			return  "error";
//		}
//	}
//
////	@GetMapping("/getmonthlysheetbybatch")
////	public String getbatchmothlyattendance(@RequestParam) {
////		
////	}
//	// @GetMapping("/download")
////    public ResponseEntity<byte[]> downloadAttendance() throws IOException {
////        Workbook workbook = new XSSFWorkbook();
////        Sheet sheet = workbook.createSheet("Attendance");
////
////        Row header = sheet.createRow(0);
////        header.cre	ateCell(0).setCellValue("Student ID");
////        header.createCell(1).setCellValue("Name");
////        header.createCell(2).setCellValue("Status");
////
////        List<Attendance> records = AttendanceRepository.findAll();
////        int rowNum = 1;
////        for (Attendance record : records) {
////            Row row = sheet.createRow(rowNum++);
////            row.createCell(0).setCellValue(record.getStudent().getStudentid());
////            row.createCell(1).setCellValue(record.getStudent().getName());
////            row.createCell(2).setCellValue(record.getStatus());
////        }
////
////        byte[] excelData = workbook.getBytes();
////        workbook.close();
////
////        return ResponseEntity.ok()
////                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attendance.xlsx")
////                .contentType(MediaType.APPLICATION_OCTET_STREAM)
////                .body(excelData);
////    }@PostMapping("/attendance/submit")

}