package com.src.jwtsecurity.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:path/subject_sql_path.properties")
public class SubjectSqlQuery {

	public static String SEMESTER_AND_SCHOOL_YEAR;

	@Value("${semester.and.school.year}")
	public void setSemesterAndSchoolYear(String sqlPath) {
		SEMESTER_AND_SCHOOL_YEAR = sqlPath;
	}
}
