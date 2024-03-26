-- select * from students where age between 16 and 17 order by id;
-- select students.name from students;
-- select * from students where upper(name) like upper('%H%');
-- select * from students where age<id;
-- select * from students order by age, id;
DELETE FROM students WHERE id = (SELECT id FROM students ORDER BY id DESC LIMIT 1)