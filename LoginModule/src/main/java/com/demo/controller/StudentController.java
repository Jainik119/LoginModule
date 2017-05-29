package com.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demo.delegator.LoginDelegator;
import com.demo.delegator.StudentDelegator;
import com.demo.entity.Student;
import com.demo.entity.UserMaster;
import com.demo.entity.rest.RestLoginBO;
import com.demo.entity.rest.RestStudentBO;
import com.demo.entity.rest.RestStudentDeleteBO;
import com.demo.entity.rest.RestStudentFindBO;
import com.demo.entity.rest.RestStudentListBO;

@RestController
@RequestMapping(value = "/student")
public class StudentController {

	@Autowired
	private StudentDelegator studentDelegator;
	
	@Autowired
	private LoginDelegator loginDelegator;
	
	@RequestMapping(value = "/delete", method=RequestMethod.POST, headers="Accept=application/json", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> delete(@RequestHeader(value="auth-token") String userAgent,@RequestBody RestStudentDeleteBO bo){
		Map<String, Object> data =new HashMap<>();
		RestLoginBO restToken = loginDelegator.parseToken(userAgent);
		if (restToken != null) {
			if (restToken.getUsername().equals(bo.getUsername())) {
				UserMaster user = loginDelegator.findUserById(restToken.getUsername());
				if (user.getRole().equalsIgnoreCase("admin")) {
					Student s = studentDelegator.findById(Integer.parseInt(bo.getStudent_id()));
					if (s != null) {
						studentDelegator.delete(s);
						data.put("response", true);
						data.put("message", "delete successfully...!!");
					}else{
						data.put("response", false);
						data.put("message", "student not found");
					}
				}else{
					data.put("response", false);
					data.put("message", "unauthorized access...!!");
				}
			}else{
				data.put("response", false);
				data.put("message", "token match..!!");
			}
		}else{
			data.put("response", false);
			data.put("message", "token not found...!!");
		}
		return ResponseEntity.ok(data);
	}
	
	@RequestMapping(value = "/list", method=RequestMethod.POST, headers="Accept=application/json", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> list(@RequestHeader(value="auth-token") String userAgent,@RequestBody RestStudentListBO bo){
		Map<String, Object> data =new HashMap<>();
		RestLoginBO restToken = loginDelegator.parseToken(userAgent);
		if (restToken != null) {
			if (restToken.getUsername().equals(bo.getUsername())) {
				UserMaster user = loginDelegator.findUserById(restToken.getUsername());
				if (user.getRole().equalsIgnoreCase("admin") || user.getRole().equalsIgnoreCase("advisor")) {
					List<Student> s = studentDelegator.findAll();
					if (s != null) {
						data.put("response", true);
						data.put("list", s);
						data.put("message", "fatch the data successfully...!!");
					}else{
						data.put("response", false);
						data.put("message", "data not found");
					}
				}else{
					data.put("response", false);
					data.put("message", "unauthorized access...!!");
				}
			}else{
				data.put("response", false);
				data.put("message", "token match..!!");
			}
		}else{
			data.put("response", false);
			data.put("message", "token not found...!!");
		}
		return ResponseEntity.ok(data);
	}
	
	@RequestMapping(value = "/get", method=RequestMethod.POST, headers="Accept=application/json", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getData(@RequestHeader(value="auth-token") String userAgent,@RequestBody RestStudentFindBO bo){
		Map<String, Object> data =new HashMap<>();
		RestLoginBO restToken = loginDelegator.parseToken(userAgent);
		if (restToken != null) {
			if (restToken.getUsername().equals(bo.getUsername())) {
				UserMaster user = loginDelegator.findUserById(restToken.getUsername());
				if (user.getRole().equalsIgnoreCase("admin") || user.getRole().equalsIgnoreCase("advisor") || user.getRole().equalsIgnoreCase("student")) {
					Student s = studentDelegator.findById(Integer.parseInt(bo.getStudent_id()));
					if (s != null) {
						if (user.getUsername().equalsIgnoreCase(s.getUsername())) {
							data.put("response", true);
							data.put("data", s);
							data.put("message", "user found successfully...!!");
						}else{
							data.put("response", false);
							data.put("message", "woops somthing went wrong...!!");
						}
					}else{
						data.put("response", false);
						data.put("message", "student not found");
					}
				}else{
					data.put("response", false);
					data.put("message", "unauthorized access...!!");
				}
			}else{
				data.put("response", false);
				data.put("message", "token match..!!");
			}
		}else{
			data.put("response", false);
			data.put("message", "token not found...!!");
		}
		return ResponseEntity.ok(data);
	}
	
	@RequestMapping(value = "/save", method=RequestMethod.POST, headers="Accept=application/json", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> save(@RequestHeader(value="auth-token") String userAgent,@RequestBody RestStudentBO bo){
		Map<String, Object> data =new HashMap<>();
		RestLoginBO restToken = loginDelegator.parseToken(userAgent);
		if (restToken != null) {
			if (restToken.getUsername().equals(bo.getUsername())) {
				UserMaster user = loginDelegator.findUserById(restToken.getUsername());
				if (user.getRole().equalsIgnoreCase("admin") || user.getRole().equalsIgnoreCase("advisor")) {
					if (bo.getId() == null) {
						Student student = new Student();
						student.setName(bo.getName());
						student.setEmail(bo.getEmail());
						student.setCity(bo.getCity());
						studentDelegator.save(student);
						data.put("response", true);
						data.put("message", "save successfully...!!");
					}else{
						Student s = studentDelegator.findById(Integer.parseInt(bo.getId()));
						if (s != null) {
							s.setName(bo.getName());
							s.setEmail(bo.getEmail());
							s.setCity(bo.getCity());
							studentDelegator.update(s);
							data.put("response", true);
							data.put("message", "update successfully...!!");
						}else{
							data.put("response", false);
							data.put("message", "student not found");
						}
					}
				}else{
					data.put("response", false);
					data.put("message", "unauthorized access...!!");
				}
			}else{
				data.put("response", false);
				data.put("message", "token match..!!");
			}
		}else{
			data.put("response", false);
			data.put("message", "token not found...!!");
		}
		return ResponseEntity.ok(data);
	}
}
