package com.src.jwtsecurity.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.springframework.core.io.ClassPathResource;

public abstract class SqlQueryUtils {

	public static String getSql(String sql) throws IOException {
		File file = new ClassPathResource(sql).getFile();
		byte[] bytes = Files.readAllBytes(file.toPath());
		return new String(bytes, StandardCharsets.UTF_8);
	}
}
