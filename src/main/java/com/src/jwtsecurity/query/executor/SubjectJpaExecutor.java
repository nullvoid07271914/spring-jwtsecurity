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

import com.src.jwtsecurity.api.body.SubjectsInSemesterParams;
import com.src.jwtsecurity.dto.SubjectBySemester;
import com.src.jwtsecurity.properties.SubjectSqlQuery;
import com.src.jwtsecurity.query.SubjectJpaQuery;
import com.src.jwtsecurity.utils.SqlQueryUtils;

@Repository
@Transactional
public class SubjectJpaExecutor extends JpaObjectMapper implements SubjectJpaQuery {

	private static final Logger logger = LoggerFactory.getLogger(SubjectJpaExecutor.class);

	private static final int EMPTY_RESULT = 0;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SubjectSqlQuery sqlQuery;

	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	public List<SubjectBySemester> subjectsBySemester(SubjectsInSemesterParams params) {
		try {
			String sql = SqlQueryUtils.getSql(sqlQuery.SEMESTER_AND_SCHOOL_YEAR);
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, params.getSemester());
			query.setParameter(2, params.getSchoolYear());
			List<Object[]> records = query.getResultList();
			return mapper(SubjectBySemester.class, records);
		} catch (IOException e) {
			logger.error("IOException: {}", e);
		} catch (Exception e) {
			logger.error("Exception: {}", e);
		}

		return new ArrayList<SubjectBySemester>(EMPTY_RESULT);
	}
}
