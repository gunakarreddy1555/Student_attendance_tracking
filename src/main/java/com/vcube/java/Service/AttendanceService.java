package com.vcube.java.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vcube.java.Entity.Attendance;
import com.vcube.java.Repository.AttendanceRepository;
import com.vcube.java.Repository.StudentRepository;

@Service
public class AttendanceService {

	
    @Autowired
    private AttendanceRepository attendanceRepository;

//    @Autowired
//    private MonthlyAttendanceRepository monthlyAttendanceRepository;
     
    @Autowired
    private StudentRepository studentrepo;
//    @Transactional
//    public void processExcelFile(MultipartFile file, String batchName, String monthYear) throws IOException {
//        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
//            Sheet sheet = workbook.getSheetAt(0);
//            Iterator<Row> rowIterator = sheet.iterator();
//            rowIterator.next(); // Skip header row
//
//            // Calculate total working days (excluding Saturdays and Sundays)
//            int totalWorkingDays = getTotalWorkingDays(monthYear);
//            System.out.println(totalWorkingDays+"");
//            while (rowIterator.hasNext()) {
//                Row row = rowIterator.next();
//
//                Cell firstCell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
//                if (firstCell == null || firstCell.getCellType() == CellType.BLANK) {
//                    break;
//                }
//
//                String studentid = getCellValueAsString(firstCell);
//                System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
//                System.out.println(studentid);
//                Student byStudentid = studentrepo.findByStudentid(studentid);
//                int presentDays = 0;
//                int examDays = 0;
//
//                for (int i = 1; i < row.getLastCellNum(); i++) {
//                    Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
//                    if (cell != null) {
//                        String value = getCellValueAsString(cell).trim();
//                        if ("P".equalsIgnoreCase(value)) presentDays++;
//                        else if ("E".equalsIgnoreCase(value)) examDays++;
//                    }
//                }
//
//                MonthlyAttendance attendance = new MonthlyAttendance(
//                batchName, monthYear, totalWorkingDays, presentDays, examDays
//                );
//                attendance.setStudent(byStudentid);
//                System.out.println(attendance+"===================================================");
//                monthlyAttendanceRepository.save(attendance);
//            }
//        }
//    }
//
//    public List<MonthlyAttendance> getAttendanceByBatchAndMonth(String batch, String month) {
//        return monthlyAttendanceRepository.findByBatchNameAndMonth(batch, month);
//    }
//
//    private int getTotalWorkingDays(String monthYear) {
//        String[] parts = monthYear.split(" ");
//        int month = Month.valueOf(parts[0].toUpperCase()).getValue();
//        int year = Integer.parseInt(parts[1]);
//
//        LocalDate start = LocalDate.of(year, month, 1);
//        LocalDate end = start.with(TemporalAdjusters.lastDayOfMonth());
//        int count = 0;
//
//        while (!start.isAfter(end)) {
//            if (start.getDayOfWeek() != DayOfWeek.SATURDAY && start.getDayOfWeek() != DayOfWeek.SUNDAY) {
//                count++;
//            }
//            start = start.plusDays(1);
//        }
//        return count;
//    }
//
//    private String getCellValueAsString(Cell cell) {
//        if (cell == null) return "";
//        return switch (cell.getCellType()) {
//            case STRING -> cell.getStringCellValue();
//            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
//            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
//            case FORMULA -> cell.getCellFormula();
//            default -> "";
//        };
//    }
//
//	public List<Attendance> getAttendanceByBatchAndDate(Long batchId, String date) {
//		
//		return null;
//	}
// 
//	  public Map<Student, List<Attendance>> getAttendanceByBatchAndMonth(Long batchId, int month, int year) {
//	        List<Attendance> attendanceList = attendanceRepository.findAttendanceByBatchAndMonth(batchId, month, year);
//
//	        // Group attendance records by student and sort dates in ascending order
//	        return attendanceList.stream()
//	                .collect(Collectors.groupingBy(
//	                        Attendance::getStudent,
//	                        Collectors.collectingAndThen(
//	                                Collectors.toList(),
//	                                list -> list.stream()
//	                                            .sorted(Comparator.comparing(Attendance::getDate))
//	                                            .collect(Collectors.toList())
//	                        )
//	                ));
//	    }
	public void save(Attendance attendance) {
        attendanceRepository.save(attendance);	
	}
//	
	public long getmonthlyattendancebymonth(long id,int month,int year) {
		return attendanceRepository.getAttendanceCountByStudentAndMonth(id, month, year);
	}
	 public List<Attendance> getStudentAttendanceByMonth(Long studentId, int month, int year) {
	        return attendanceRepository.findAttendanceByStudentAndMonth(studentId, month, year);
	    }
	 public boolean checkisprentornotbyday(long id,LocalDate date) {
		return  attendanceRepository.existsByStudentAndDate(id, date);
	 }
	 
	 public List<Attendance> findbyAttendanceByday(long id,LocalDate date){
		return attendanceRepository.findByBatchIdAndDate(id, date);
	 }
	public List<Attendance> findAttendanceByBatchAndMonth(long batchid,int month ,int year) {
		return attendanceRepository.findAttendanceByBatchAndMonth(batchid,month,year);
		
	}
}
