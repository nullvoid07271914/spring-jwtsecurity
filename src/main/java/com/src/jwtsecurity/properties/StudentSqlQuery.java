package com.src.jwtsecurity.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:path/student_sql_path.properties")
public class StudentSqlQuery {

	public static String SUBJECTS_BY_SEMESTER_AND_SCHOOL_YEAR;

	@Value("${subjects.by.semester.and.school.year}")
	public void setSubjectsBySemesterAndSchoolYear(String sqlPath) {
		SUBJECTS_BY_SEMESTER_AND_SCHOOL_YEAR = sqlPath;
	}
}
