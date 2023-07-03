package com.src.jwtsecurity.query;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface Persistent<T, ID extends Serializable> extends JpaRepository<T, ID> {

}
