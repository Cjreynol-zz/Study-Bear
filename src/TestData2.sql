INSERT INTO USER_ENROLLMENT 
SELECT userName, teachingId, 'A'
FROM USER A, TEACHING B, CLASS C, PROFESSOR D 
WHERE A.userName = 'jbusch'
AND B.classId = C.classId
AND B.classId = 'CSCI 1301'
AND B.professorId = D.professorId
AND B.professorId = 1;

INSERT INTO USER_ENROLLMENT 
SELECT userName, teachingId, 'A'
FROM USER A, TEACHING B, CLASS C, PROFESSOR D 
WHERE A.userName = 'jbusch'
AND B.classId = C.classId
AND B.classId = 'CSCI 1302'
AND B.professorId = D.professorId
AND B.professorId = 2;

INSERT INTO USER_ENROLLMENT 
SELECT userName, teachingId, 'A'
FROM USER A, TEACHING B, CLASS C, PROFESSOR D 
WHERE A.userName = 'jbusch'
AND B.classId = C.classId
AND B.classId = 'CSCI 3400'
AND B.professorId = D.professorId
AND B.professorId = 2;

INSERT INTO USER_ENROLLMENT 
SELECT userName, teachingId, 'A'
FROM USER A, TEACHING B, CLASS C, PROFESSOR D 
WHERE A.userName = 'jbusch'
AND B.classId = C.classId
AND B.classId = 'CSCI 3520'
AND B.professorId = D.professorId
AND B.professorId = 3;