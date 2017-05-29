package com.demo.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.demo.entity.Student;
import com.demo.repository.StudentRepository;

@Repository("studentRepository")
public class StudentRepositoryImpl implements StudentRepository{

	@Autowired
	private SessionFactory sessionFactory;
	
	private Session getSession(){
		return sessionFactory.getCurrentSession();
	}
	
	
	@Override
	public void save(Student student) {
			getSession().save(student);
	}

	@Override
	public void update(Student student) {
		getSession().update(student);
	}

	@Override
	public void delete(Student student) {
		getSession().delete(student);
	}

	@Override
	public Student findById(Integer id) {
		Criteria criteria =  getSession().createCriteria(Student.class);
		criteria.add(Restrictions.eq("id", id));
		Student s = (Student) criteria.uniqueResult();
		return s;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Student> findAll() {
		Criteria criteria =  getSession().createCriteria(Student.class);
		return criteria.list();
	}

}
