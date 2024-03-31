ALTER TABLE students
    add constraint age_constrain check ( age > 16 ),
    add constraint name_unique unique (name),
    alter column name set not null,
    alter column age set default 20;

alter table faculties
    add constraint name_color_unique UNIQUE (name, color)


