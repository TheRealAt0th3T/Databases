CREATE DATABASE IF NOT EXISTS finalProject;
USE finalProject;
#DROP DATABASE finalProject;

Select * From class
JOIN categories ON class.class_id = categories.class_id
JOIN assignments ON assignments.categories_id = categories.categories_id;

SELECT * FROM class
JOIN students on students.class_id = class.class_id
JOIN assignedHW on assignedHW.students_id = students.students_id;

##gets students within a specific class
SELECT students.students_username, students.students_IDnum, students.students_firstName, students.students_lastName FROM class
JOIN students ON students.class_id = class.class_id
WHERE class_courseNum = "CS410" and class_sectionNum = "2";

##getting all columns for grades
SELECT hasWeight_weight, assignedHW_grade, assignments.assignments_id, 
students.students_username, students.students_IDnum, 
students.students_firstName, students.students_lastName FROM class
left JOIN categories ON categories.class_id = class.class_id
left JOIN hasWeight ON hasWeight.categories_id = categories.categories_id
left JOIN assignments ON assignments.categories_id = categories.categories_id
left JOIN assignedHW ON assignedHW.assignments_id = assignments.assignments_id
left JOIN students ON students.students_id = assignedHW.students_id
WHERE class_courseNum = "CS410" AND class_sectionNum = "2";

CREATE TABLE class(
	class_id INTEGER PRIMARY KEY AUTO_INCREMENT,
	class_courseNum VARCHAR(10) NOT NULL,
	class_term VARCHAR(20) NOT NULL,
	class_sectionNum INTEGER NOT NULL,
	class_description VARCHAR(100) NOT NULL,
	isActive BOOLEAN
);

CREATE TABLE students (
	students_id INTEGER PRIMARY KEY AUTO_INCREMENT,
	students_firstName VARCHAR(50) NOT NULL,
	students_lastName VARCHAR(50) NOT NULL,
	students_username VARCHAR(50) NOT NULL,
	students_IDnum BIGINT NOT NULL,
	class_id INTEGER NOT NULL,
    FOREIGN KEY (class_id) REFERENCES class (class_id),
    INDEX (class_id)
);

CREATE TABLE categories(
	categories_id INTEGER PRIMARY KEY AUTO_INCREMENT,
	categories_name VARCHAR(50) NOT NULL,
    class_id INTEGER NOT NULL, 
    
	FOREIGN KEY (class_id) REFERENCES class (class_id),
    INDEX (class_id)
);

CREATE TABLE assignments(
	assignments_id INTEGER PRIMARY KEY AUTO_INCREMENT,
	assignments_name VARCHAR(20) UNIQUE NOT NULL, 
	assignments_description VARCHAR(100) NOT NULL, 
	assignments_pointValue INTEGER NOT NULL,
    #class_id INTEGER NOT NULL, 
	categories_id INTEGER NOT NULL, 

    #FOREIGN KEY (class_id) REFERENCES class (class_id),
    #INDEX (class_id),
    FOREIGN KEY (categories_id) REFERENCES categories (categories_id),
    INDEX (categories_id)
    
);

CREATE TABLE hasWeight(
	hasWeight_id INTEGER PRIMARY KEY AUTO_INCREMENT,
	hasWeight_weight INTEGER NOT NULL,
    class_id INTEGER NOT NULL, 
    categories_id INTEGER NOT NULL, 
	FOREIGN KEY (class_id) REFERENCES class (class_id),
    INDEX (class_id),

	FOREIGN KEY (categories_id) REFERENCES categories (categories_id),
    INDEX (categories_id)
);

CREATE TABLE assignedHW(
	assignedHW_id INTEGER PRIMARY KEY AUTO_INCREMENT,
	assignedHW_grade INTEGER NOT NULL,
    students_id INTEGER NOT NULL, 
    assignments_id INTEGER NOT NULL, 
	FOREIGN KEY (students_id) REFERENCES students (students_id),
    INDEX (students_id),

	FOREIGN KEY (assignments_id) REFERENCES assignments (assignments_id),
    INDEX (assignments_id)
);

