INSERT INTO UNIVERSITY (universityName) VALUES ("");
INSERT INTO UNIVERSITY (universityName) VALUES ("Georgia Regents University");
INSERT INTO UNIVERSITY (universityName) VALUES ("University of South Carolina - Aiken");
INSERT INTO PROFESSOR (professorFname, professorLname) VALUES ('Onyeka', 'Ezenwoye');
INSERT INTO PROFESSOR (professorFname, professorLname) VALUES ('Michael', 'Dowell');
INSERT INTO PROFESSOR (professorFname, professorLname) VALUES ('Ryan', 'Wilson');
INSERT INTO CLASS (classId, universityName, className) VALUES ("CSCI 1301", "GEORGIA REGENTS UNIVERSITY", "Principles of Programming I" );
INSERT INTO CLASS (classId, universityName, className) VALUES ("CSCI 1302", "GEORGIA REGENTS UNIVERSITY", "Principles of Programming II" );
INSERT INTO CLASS (classId, universityName, className) VALUES ("CSCI 3400", "GEORGIA REGENTS UNIVERSITY", "Data Structures" );
INSERT INTO CLASS (classId, universityName, className) VALUES ("CSCI 3520", "GEORGIA REGENTS UNIVERSITY", "Introduction to Cyber Security" );

INSERT INTO TEACHING (professorId, classId) 
SELECT professorId, classId 
FROM PROFESSOR, CLASS 
WHERE professorFname = 'Onyeka' 
AND professorLname = 'Ezenwoye' 
AND classId = 'CSCI 1301' 
AND universityName = 'Georgia Regents University';

INSERT INTO TEACHING (professorId, classId) 
SELECT professorId, classId 
FROM PROFESSOR, CLASS 
WHERE professorFname = 'Michael' 
AND professorLname = 'Dowell' 
AND classId = 'CSCI 1302' 
AND universityName = 'Georgia Regents University';

INSERT INTO TEACHING (professorId, classId) 
SELECT professorId, classId 
FROM PROFESSOR, CLASS 
WHERE professorFname = 'Michael' 
AND professorLname = 'Dowell' 
AND classId = 'CSCI 3400' 
AND universityName = 'Georgia Regents University';

INSERT INTO TEACHING (professorId, classId) 
SELECT professorId, classId 
FROM PROFESSOR, CLASS 
WHERE professorFname = 'Ryan' 
AND professorLname = 'Wilson' 
AND classId = 'CSCI 3520' 
AND universityName = 'Georgia Regents University';