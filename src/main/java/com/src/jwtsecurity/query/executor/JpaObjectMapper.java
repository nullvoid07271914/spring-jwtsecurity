package com.src.jwtsecurity.query.executor;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class JpaObjectMapper {

	@SuppressWarnings("unchecked")
	public <T> List<T> mapper(Class<?> type, List<Object[]> records) throws Exception {
		List<T> result = new ArrayList<>();
		for (Object[] record : records) {
			List<Class<?>> tupleTypes = new ArrayList<>();
			for (Object field : record) {
				tupleTypes.add(field.getClass());
			}

			Class<?>[] paramTypeClass = tupleTypes.toArray(new Class[record.length]);
			Constructor<?> constructor = type.getConstructor(paramTypeClass);

			T obj = (T) constructor.newInstance(record);
			result.add(obj);
		}

		return result;
	}
}
