package com.demo.repository;

import java.util.List;

import com.demo.entity.Student;

public interface StudentRepository {

	public void save(Student student);
	public void update(Student student);
	public void delete(Student student);
	public Student findById(Integer id);
	public List<Student> findAll();
}
