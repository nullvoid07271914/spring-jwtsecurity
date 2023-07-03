package com.src.jwtsecurity.query;

import java.util.List;

import com.src.jwtsecurity.api.body.SubjectsInSemesterParams;
import com.src.jwtsecurity.dto.SubjectBySemester;

public interface SubjectJpaQuery {

	public List<SubjectBySemester> subjectsBySemester(SubjectsInSemesterParams params);
}
