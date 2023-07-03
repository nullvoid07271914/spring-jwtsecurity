select stud.student_sch_id, sub.*
	from student_tbl stud,
		(select sub.id as subject_id, sub.title, sub.code, sub.description, sub.units, sub.start_time, sub.end_time,
			group_concat(sched.day separator ',') as schedule,
			concat(
				case
					when gender = 'male' then 'Mr. '
					when gender = 'female' then 'Ms. '
				end,
				ins.lastname, ', ', ins.firstname
			) as instructor, ins.instructor_sch_id

				from subject_tbl sub, subject_sched_tbl sched, instructor_tbl ins
					where sub.id = sched.subject_id
					and sub.instructor_id = ins.id
					group by sub.id) sub, stud_sub_tbl stud_sub, school_year_tbl sy

			where stud.id = stud_sub.student_id
				and stud_sub.subject_id = sub.subject_id
				and stud_sub.sy_id = sy.id
				and sy.semester = ? and sy.sy = ?
				and stud.student_sch_id = ?;