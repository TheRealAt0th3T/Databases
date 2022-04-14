CREATE DATABASE IF NOT EXISTS finalProject;
USE finalProject;

CREATE TABLE Students (
	students_id INTEGER PRIMARY KEY AUTO_INCREMENT,
	students_name VARCHAR(50) NOT NULL,
	students_username VARCHAR(50) NOT NULL,
	students_IDnum VARCHAR(50) NOT NULL,
	users_createDate DATE NOT NULL
);

