select students.name, students.age, f.name
    from students
    full join faculties f on students.faculty_id = f.id;

select students.name, students.age
    from students
    inner join public.avatars a on students.id = a.student_id;