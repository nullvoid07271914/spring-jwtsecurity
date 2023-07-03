package com.src.jwtsecurity.query.executor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.src.jwtsecurity.api.body.StudentSubjectsInSemesterParams;
import com.src.jwtsecurity.dto.StudentSubjectsBySemester;
import com.src.jwtsecurity.properties.StudentSqlQuery;
import com.src.jwtsecurity.query.StudentJpaQuery;
import com.src.jwtsecurity.utils.SqlQueryUtils;

@Repository
@Transactional
public class StudentJpaExecutor extends JpaObjectMapper implements StudentJpaQuery {

	private static final Logger logger = LoggerFactory.getLogger(StudentJpaExecutor.class);

	private static final int EMPTY_RESULT = 0;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private StudentSqlQuery sqlQuery;

	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	public List<StudentSubjectsBySemester> studentSubjectsBySemester(StudentSubjectsInSemesterParams params) {
		try {
			String sql = SqlQueryUtils.getSql(sqlQuery.SUBJECTS_BY_SEMESTER_AND_SCHOOL_YEAR);
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, params.getSemester());
			query.setParameter(2, params.getSchoolYear());
			query.setParameter(3, params.getStudentSchoolId());

			logger.info("sql: {}", sql);
			logger.info("params: {}", params);

			List<Object[]> records = query.getResultList();
			return mapper(StudentSubjectsBySemester.class, records);

		} catch (IOException e) {
			logger.error("IOException: {}", e);
		} catch (Exception e) {
			logger.error("Exception: {}", e);
		}

		return new ArrayList<StudentSubjectsBySemester>(EMPTY_RESULT);
	}
}
