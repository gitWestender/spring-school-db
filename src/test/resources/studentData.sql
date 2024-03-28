--
-- CREATE TABLE students
-- (
--     id         INT,
--     name       VARCHAR(255),
--     age        INT,
--     faculty_id INT,
--     PRIMARY KEY (id),
--     FOREIGN KEY (faculty_id) REFERENCES faculties(id)
-- );
--
-- CREATE TABLE faculties
-- (
--     id    INT,
--     name  VARCHAR(255),
--     color VARCHAR(255),
--     PRIMARY KEY (id)
-- );

INSERT INTO faculties(id, name, color) VALUES (1, 'Gryffindor', 'Red');
INSERT INTO faculties(id, name, color) VALUES (2, 'Slytherin', 'Green');

INSERT INTO students (id, name, age, faculty_id) VALUES (1, 'TestName1', 16, 1);
INSERT INTO students (id, name, age, faculty_id) VALUES (2, 'TestName2', 20, 2);
INSERT INTO students (id, name, age) VALUES (3, 'TestName3', 18);
INSERT INTO students (id, name, age) VALUES (4, 'TestName4', 13);


