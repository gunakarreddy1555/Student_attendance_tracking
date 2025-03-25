package com.vcube.java.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vcube.java.Entity.Batchworkingdays;



@Repository
public interface Batchworkingdaysrepo extends JpaRepository<Batchworkingdays,Long>{

	@Query("FROM Batchworkingdays b WHERE b.batch.batchid = :batchId AND LOWER(b.month) = LOWER(:month)")
	Optional<List<Batchworkingdays>>findByBatchIdAndMonth(@Param("batchId") long batchId, @Param("month") String month);

}
