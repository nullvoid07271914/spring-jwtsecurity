package com.src.jwtsecurity.query;

import java.io.IOException;
import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.src.jwtsecurity.utils.SqlQueryUtils;

public class ExecutorQuery<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements Persistent<T, ID> {

	private final EntityManager manager;

	public ExecutorQuery(Class<T> domainClass, EntityManager entityManager) {
		super(domainClass, entityManager);
		this.manager = entityManager;
	}

	@SuppressWarnings("unchecked")
	public T findUserByUsername(String username) {
		try {
			String sql = SqlQueryUtils.getSql("user_roles_by_username");
			Query query = manager.createNativeQuery(sql);
			query.setParameter(1, username);
			T user = (T) query.getSingleResult();
			return user;
		} catch (IOException e) {

		}
		return null;
	}
}
