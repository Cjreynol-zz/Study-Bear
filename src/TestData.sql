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

insert into messages (sendingUser, receivingUser, body, subject, dateTime) values('odarcie', 'jbeezy88', 'hey! we have some classes together. Wanna study sometime?', 'hi' , now());
insert into messages (sendingUser, receivingUser, body, subject, dateTime) values ('jbeezy88', 'odarcie', 'sure, when can you meet up?', 'hi', now());
insert into messages (sendingUser, receivingUser, body, subject, dateTime) values ('odarcie', 'jbeezy88', 'does 6pm tomorrow at the school work for you?', 'hi', now());
insert into messages (sendingUser, receivingUser, body, subject, dateTime) values('jbeezy88', 'odarcie', 'yeah, lets meet at allgood and find a study room', 'hi' , now());
insert into messages (sendingUser, receivingUser, body, subject, dateTime) values('odarcie', 'jbeezy88', 'perfect! see you tomorrow!', 'hi' , now());
insert into messages (sendingUser, receivingUser, body, subject, dateTime) values ('jbeezy88', 'odarcie', 'cheeeeeese', 'hi', now());


