package com.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.entity.Student;
import com.demo.repository.StudentRepository;
import com.demo.service.StudentService;

@Service("studentService")
@Transactional
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepository studentRepository;

	@Override
	public void save(Student student) {
		studentRepository.save(student);
	}

	@Override
	public void update(Student student) {
		studentRepository.update(student);
	}

	@Override
	public void delete(Student student) {
		studentRepository.delete(student);
	}

	@Override
	public Student findById(Integer id) {
		return studentRepository.findById(id);
	}

	@Override
	public List<Student> findAll() {
		return studentRepository.findAll();
	}

}
