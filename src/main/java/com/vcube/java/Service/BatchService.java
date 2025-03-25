package com.vcube.java.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vcube.java.Entity.Batch;
import com.vcube.java.Repository.BatchRepository;

@Service
public class BatchService {

	@Autowired
	private BatchRepository batchrepo;
	public void saveBatch(Batch batch) {
		batchrepo.save(batch);
	}
	public void save(Batch batch) {
		batchrepo.save(batch);	
	}
	public List<Batch> getListOfActiveBatchesBycourse(long id){
		return batchrepo.findActiveBatchesByCourseId(id);
	}
	public Batch getBtachByid(long id) {
		return batchrepo.findById(id).get();
	}
	public boolean existsByBatchNameAndCourseId(String batchName, Long courseId) {
		return batchrepo.existsByBatchnameAndCourse_Id(batchName, courseId);
		
	}
}
