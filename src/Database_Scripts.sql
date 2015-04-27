CREATE TABLE UNIVERSITY ( 
universityName VARCHAR(80) PRIMARY KEY
)ENGINE=INNODB;

CREATE TABLE USER (userName VARCHAR(20) PRIMARY KEY,
firstName VARCHAR(20),
lastName VARCHAR(20),
password VARCHAR(60),
email VARCHAR(30),
biography VARCHAR(200),
universityName VARCHAR(80), 
accountStatus VARCHAR(1),
FOREIGN KEY (universityName) REFERENCES UNIVERSITY(universityName)
)ENGINE=INNODB;

CREATE TABLE USER_BLOCKED (
userName VARCHAR(20),
blockeduserName VARCHAR(20),
FOREIGN KEY (userName) REFERENCES USER(userName),
FOREIGN KEY (blockeduserName) REFERENCES USER(userName)
)ENGINE=INNODB;

CREATE TABLE MESSAGES(
msgId INTEGER(10) AUTO_INCREMENT PRIMARY KEY,
sendingUser VARCHAR(20), 
receivingUser VARCHAR(20), 
body VARCHAR(250),
subject VARCHAR(100),
dateTime DATETIME,
FOREIGN KEY (sendingUser) REFERENCES USER(userName),
FOREIGN KEY (receivingUser) REFERENCES USER(userName)
)ENGINE=INNODB;

CREATE TABLE CLASS (
classId VARCHAR(10),
universityName VARCHAR(80), 
className VARCHAR(50),
PRIMARY KEY(classId, universityName),
FOREIGN KEY (universityName) REFERENCES UNIVERSITY(universityName)
)ENGINE=INNODB;

CREATE TABLE PROFESSOR(
professorId INTEGER(10) AUTO_INCREMENT UNIQUE KEY,
professorFname VARCHAR(20),
professorLname VARCHAR(20),
PRIMARY KEY(professorFname, professorLname)
)ENGINE = INNODB;

CREATE TABLE TEACHING(
teachingId INTEGER(10) NOT NULL AUTO_INCREMENT UNIQUE KEY,
professorId INTEGER(10),
classId VARCHAR(10),
PRIMARY KEY (professorId, classId),
FOREIGN KEY(professorId) REFERENCES PROFESSOR(professorId),
FOREIGN KEY(classId) REFERENCES CLASS(classId)
)ENGINE = INNODB;

CREATE TABLE USER_ENROLLMENT(
userName VARCHAR(20),
professorId INTEGER(10),
classId VARCHAR(10),
enrollmentStatus VARCHAR(1),
PRIMARY KEY (userName, professorId, classId),
FOREIGN KEY (userName) REFERENCES USER(userName),
FOREIGN KEY (professorId) REFERENCES TEACHING(professorId),
FOREIGN KEY (classId) REFERENCES TEACHING(classId)
)ENGINE=INNODB;

