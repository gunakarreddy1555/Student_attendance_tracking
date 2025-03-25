package com.vcube.java.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vcube.java.Entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long>{
	boolean existsByMobileWhatsApp(String mobileWhatsApp);
    boolean existsByEmail(String email);
    
    @Query(value = "SELECT * FROM student WHERE batch_id = :batchId AND LOWER(status) = 'active'",nativeQuery = true)
    List<Student> findActiveStudentsByBatchId(@Param("batchId") Long batchId);

    @Query(value = "SELECT * FROM student WHERE batch_id = :batchId AND LOWER(status) = 'inactive'", nativeQuery = true)
    List<Student> findInactiveStudentsByBatchId(@Param("batchId") Long batchId);

    // Get all students by batch ID
    @Query("SELECT s FROM Student s WHERE s.batch.id = :batchId")
    List<Student> findAllStudentsByBatchId(Long batchId);
}
