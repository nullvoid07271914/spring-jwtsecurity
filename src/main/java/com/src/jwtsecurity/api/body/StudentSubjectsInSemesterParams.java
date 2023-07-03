package com.src.jwtsecurity.api.body;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StudentSubjectsInSemesterParams implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("semester")
	private String semester;

	@JsonProperty("school_year")
	private String schoolYear;

	@JsonProperty("student_sch_id")
	private String studentSchoolId;

	public String getStudentSchoolId() {
		return studentSchoolId;
	}

	public void setStudentSchoolId(String studentSchoolId) {
		this.studentSchoolId = studentSchoolId;
	}

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

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		String newLine = System.getProperty("line.separator");

		Class<?> clss = getClass();
		Field[] fields = clss.getDeclaredFields();

		result.append(clss.getName());
		result.append(" {");
		result.append(newLine);

		for (Field field : fields) {
			int mod = field.getModifiers();
			boolean isStatic = Modifier.isStatic(mod);
			boolean isFinal = Modifier.isFinal(mod);

			if (!isStatic && !isFinal) {
				result.append("    ");
				try {
					result.append(field.getName());
					result.append(": ");
					result.append(field.getType().getSimpleName());
					result.append(" = ");

					// requires access to private field:
					result.append(field.get(this));
				} catch (IllegalAccessException ex) {

				}
				result.append(newLine);
			}
		}
		result.append("}");

		return result.toString();
	}
}
