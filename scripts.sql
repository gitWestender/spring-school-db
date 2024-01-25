select * from students where age between 15 and 18;

select students.name from students;

select * from students where upper(name) like upper('%H%');

select * from students where age < id;

select * from students order by age, id;