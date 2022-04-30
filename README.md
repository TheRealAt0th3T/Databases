## Databases Final Project

* Author: Amara Tariq and Brandon Fung
* Class: CS410/510

## Overview

This program is a java command shell program that implements our sql application. The application is created for keeping track of classes, students, assignments, 
and related information. 

## Manifest

* model.pdf - diagram of ER model
* finalProject.sql - SQL script containing our DDL
* finalProject.java - java command shell program which implements sql app
* dump.sql - a file containing all inserts with example data
* README.md - this file

## Implementation
The main function in our java class takes user input and sends it into a switch statement, which then in turn will call the correct function
for the type of inputs that were passed in. 

SQL statements are then prepared within the function. These statements take the input provided by the user and format it into the proper
SQL format. These functions will also check additional statements and values as needed, i.e. checking if a user exists before actually adding the user
into the database. 