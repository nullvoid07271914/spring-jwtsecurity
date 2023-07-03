package com.src.jwtsecurity.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.src.jwtsecurity.api.body.StudentSubjectsInSemesterParams;
import com.src.jwtsecurity.dto.StudentSubjectsBySemester;
import com.src.jwtsecurity.query.StudentJpaQuery;

@RestController
@RequestMapping("/api")
public class StudentApiResource {

	private static final Logger logger = LoggerFactory.getLogger(StudentApiResource.class);

	@Autowired
	private StudentJpaQuery repo;

	@PostMapping("/student/subjects")
	public ResponseEntity<List<StudentSubjectsBySemester>> subjectsBySemester(
			@RequestBody StudentSubjectsInSemesterParams params) {
		String uriPath = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/subject/semester")
				.toUriString();
		logger.info("uriPath = {}", uriPath);

		return ResponseEntity.ok().body(repo.studentSubjectsBySemester(params));
	}
}
