package com.vcube.java.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vcube.java.Entity.Attendance;



@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentIdAndDateBetween(Long studentId, LocalDate start, LocalDate end);

	List<Attendance> findByDateAndStudentBatchId(LocalDate selectedDate, Long batchId);
	
	 @Query("SELECT a FROM Attendance a " +
	           "JOIN a.student s " +
	           "JOIN s.batch b " +
	           "WHERE b.id = :batchId " +
	           "AND MONTH(a.date) = :month " +
	           "AND YEAR(a.date) = :year")
	    List<Attendance> findAttendanceByBatchAndMonth(
	            @Param("batchId") Long batchId,
	            @Param("month") int month,
	            @Param("year") int year);
	 
	 @Query("SELECT a.student.id, COUNT(a) FROM Attendance a " +
	           "WHERE MONTH(a.date) = :month AND YEAR(a.date) = :year " +
	           "GROUP BY a.student.id")
	    List<Object[]> getStudentAttendanceCountByMonth(@Param("month") int month, @Param("year") int year);
	    
	    @Query("SELECT COUNT(a) FROM Attendance a " +
	    	       "WHERE a.student.id = :studentId " +
	    	       "AND MONTH(a.date) = :month " +
	    	       "AND YEAR(a.date) = :year")
	    	Long getAttendanceCountByStudentAndMonth(@Param("studentId") Long studentId,
	    	                                         @Param("month") int month,
	    	                                         @Param("year") int year);

	    @Query(value = "SELECT * FROM attendance WHERE student_id = :studentId AND MONTH(date) = :month AND YEAR(date) = :year", nativeQuery = true)
	    List<Attendance> findAttendanceByStudentAndMonth(@Param("studentId") Long studentId, @Param("month") int month, @Param("year") int year);

	    @Query("SELECT COUNT(a) > 0 FROM Attendance a WHERE a.student.id = :studentId AND a.date = :date")
	    boolean existsByStudentAndDate(@Param("studentId") Long studentId, @Param("date") LocalDate date);
	    
	    @Query("SELECT a FROM Attendance a WHERE a.student.batch.id = :batchId AND a.date = :date")
	    List<Attendance> findByBatchIdAndDate(@Param("batchId") Long batchId, @Param("date") LocalDate date);

	}


