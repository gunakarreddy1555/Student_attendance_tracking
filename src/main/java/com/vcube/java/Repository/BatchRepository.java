package com.vcube.java.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vcube.java.Entity.Batch;

public interface BatchRepository extends JpaRepository<Batch, Long> {

	@Query(value = "SELECT * FROM batches b WHERE b.course_id = :courseId AND b.status = 'Active'", nativeQuery = true)
    List<Batch> findActiveBatchesByCourseId(@Param("courseId") Long courseId);
	
	boolean existsByBatchnameAndCourse_Id(String batchname, Long courseId);
}
