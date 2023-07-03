select sub.id as subject_id, sub.title, sub.code, sub.description, sub.units, sub.start_time, sub.end_time,
	group_concat(sched.day separator ',') as schedule,
    concat(
		case
			when gender = 'male' then 'Mr. '
			when gender = 'female' then 'Ms. '
		end, ins.lastname, ', ', ins.firstname
    ) as `instructor`, ins.instructor_sch_id
	from subject_tbl sub, subject_sched_tbl sched, instructor_tbl ins
		where sub.id = sched.subject_id
		and sub.instructor_id = ins.id
        and sub.sy_id in (select id from school_year_tbl
			where semester = ?
			and sy = ?)
        group by sub.id;