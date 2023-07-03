package com.src.jwtsecurity.query;

import java.util.List;

import com.src.jwtsecurity.api.body.StudentSubjectsInSemesterParams;
import com.src.jwtsecurity.dto.StudentSubjectsBySemester;

public interface StudentJpaQuery {

	public List<StudentSubjectsBySemester> studentSubjectsBySemester(StudentSubjectsInSemesterParams params);
}
