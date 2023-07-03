package com.src.jwtsecurity.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StudentSubjectsBySemester implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final DateFormat FORMAT = new SimpleDateFormat("hh:mm aa");

	@JsonProperty("student_sch_id")
	private String studentSchoolId;

	@JsonProperty("subject_id")
	private BigInteger subjectId;

	@JsonProperty("title")
	private String title;

	@JsonProperty("code")
	private String code;

	@JsonProperty("description")
	private String description;

	@JsonProperty("units")
	private Integer units;

	@JsonProperty("start_time")
	private String startTime;

	@JsonProperty("end_time")
	private String endTime;

	@JsonProperty("schedule")
	private String schedule;

	@JsonProperty("instructor")
	private String instructor;

	@JsonProperty("instructor_sch_id")
	private String instructorSchoolId;

	public StudentSubjectsBySemester(String studentSchoolId, BigInteger subjectId, String title, String code,
			String description, Integer units, Time startTime, Time endTime, String schedule, String instructor,
			String instructorSchoolId) {
		setStudentSchoolId(studentSchoolId);
		setSubjectId(subjectId);
		setTitle(title);
		setCode(code);
		setDescription(description);
		setUnits(units);
		setStartTime(startTime);
		setEndTime(endTime);
		setSchedule(schedule);
		setInstructor(instructor);
		setInstructorSchoolId(instructorSchoolId);
	}

	public String getStudentSchoolId() {
		return studentSchoolId;
	}

	public void setStudentSchoolId(String studentSchoolId) {
		this.studentSchoolId = studentSchoolId;
	}

	public BigInteger getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(BigInteger subjectId) {
		this.subjectId = subjectId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getUnits() {
		return units;
	}

	public void setUnits(Integer units) {
		this.units = units;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = FORMAT.format(startTime).toString();
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = FORMAT.format(endTime).toString();
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public String getInstructor() {
		return instructor;
	}

	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}

	public String getInstructorSchoolId() {
		return instructorSchoolId;
	}

	public void setInstructorSchoolId(String instructorSchoolId) {
		this.instructorSchoolId = instructorSchoolId;
	}

}
