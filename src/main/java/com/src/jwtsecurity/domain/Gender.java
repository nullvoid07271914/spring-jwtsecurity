package com.src.jwtsecurity.domain;

public enum Gender {

	MALE("Male"), FEMALE("Female");

	private final String gender;

	private Gender(String gender) {
		this.gender = gender;
	}

	public String value() {
		return gender;
	}
}
