package com.src.jwtsecurity.api.body;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubjectsInSemesterParams {

	@JsonProperty("semester")
	private String semester;

	@JsonProperty("school_year")
	private String schoolYear;

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(String schoolYear) {
		this.schoolYear = schoolYear;
	}

}
