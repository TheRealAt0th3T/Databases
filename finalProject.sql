CREATE DATABASE IF NOT EXISTS finalProject;
USE finalProject;

CREATE TABLE students (
	students_id INTEGER PRIMARY KEY AUTO_INCREMENT,
	students_name VARCHAR(50) NOT NULL,
	students_username VARCHAR(50) NOT NULL,
	students_IDnum INTEGER NOT NULL
);

CREATE TABLE class(
	class_id INTEGER PRIMARY KEY AUTO_INCREMENT,
	class_courseNum INTEGER NOT NULL,
	class_term VARCHAR(20) NOT NULL,
	class_sectionNum INTEGER NOT NULL,
	class_description VARCHAR(50) NOT NULL,
	class_professor VARCHAR(20) NOT NULL, 
	class_title VARCHAR(10) NOT NULL
);

CREATE TABLE assignments(
	assignments_id INTEGER PRIMARY KEY AUTO_INCREMENT,
	assignments_name VARCHAR(20) UNIQUE NOT NULL, 
	assignments_description VARCHAR(50) NOT NULL, 
	assignments_pointValue INTEGER NOT NULL
);

CREATE TABLE categories(
	categories_id INTEGER PRIMARY KEY AUTO_INCREMENT,
	categories_name VARCHAR(10) NOT NULL
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

