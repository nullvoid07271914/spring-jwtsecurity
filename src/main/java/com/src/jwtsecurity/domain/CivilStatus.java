package com.src.jwtsecurity.domain;

public enum CivilStatus {

	SINGLE("Single"), MARRIED("Married"), WIDOWED("Widowed");

	private final String status;

	private CivilStatus(String status) {
		this.status = status;
	}

	public String value() {
		return status;
	}
}
