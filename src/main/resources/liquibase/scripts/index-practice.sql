-- liquibase formatted sql

-- changeset osimakov:1
create index students_name_index on students (name);

-- changeset osimakov:2
create index faculties_name_color_index on faculties (name, color);