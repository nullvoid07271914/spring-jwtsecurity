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

import com.src.jwtsecurity.api.body.SubjectsInSemesterParams;
import com.src.jwtsecurity.dto.SubjectBySemester;
import com.src.jwtsecurity.query.SubjectJpaQuery;

@RestController
@RequestMapping("/api")
public class SubjectApiResource {

	private static final Logger logger = LoggerFactory.getLogger(SubjectApiResource.class);

	@Autowired
	private SubjectJpaQuery query;

	@PostMapping("/subject/semester")
	public ResponseEntity<List<SubjectBySemester>> subjectsBySemester(@RequestBody SubjectsInSemesterParams params) {
		String uriPath = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/subject/semester")
				.toUriString();
		logger.info("uriPath = {}", uriPath);

		return ResponseEntity.ok().body(query.subjectsBySemester(params));
	}
}
