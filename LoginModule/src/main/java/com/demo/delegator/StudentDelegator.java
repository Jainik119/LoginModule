package com.demo.delegator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.entity.Student;
import com.demo.service.StudentService;

@Component
public class StudentDelegator {

	@Autowired
	private StudentService studentService;
	
	public void save(Student student) {
		studentService.save(student);
	}

	public void update(Student student) {
		studentService.update(student);
	}

	public void delete(Student student) {
		studentService.delete(student);
	}

	public Student findById(Integer id) {
		return studentService.findById(id);
	}

	public List<Student> findAll() {
		return studentService.findAll();
	}
}
