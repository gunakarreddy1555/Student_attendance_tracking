package com.vcube.java.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vcube.java.Entity.Student;
import com.vcube.java.Repository.StudentRepository;

@Service
public class StudentService {

	@Autowired
	private StudentRepository studentrepo;
	
	public void saveStudent(Student student) {
		studentrepo.save(student);
	}
	public boolean isMobileExists(String mobile) {
	     boolean app = studentrepo.existsByMobileWhatsApp(mobile);
	     System.out.println(app);
	     return app;
	}

	public boolean isEmailExists(String email) {
	    return studentrepo.existsByEmail(email);
	}

	public List<Student> getActiveStudents(Long batchId) {
        return studentrepo.findActiveStudentsByBatchId(batchId);
    }

    public List<Student> getInactiveStudents(Long batchId) {
        return studentrepo.findInactiveStudentsByBatchId(batchId);
    }

    public List<Student> getAllStudents(Long batchId) {
        return studentrepo.findAllStudentsByBatchId(batchId);
    }
    
    public Student findById(long id) {
    	return studentrepo.findById(id).get();
    }
	}
