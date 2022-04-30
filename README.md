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
The main SQL file (finalProject.sql) contains all the create statements for the database and tables. All the attributes within each table follow 
the outline that was made in the ER model (model.pdf). Once the main SQL file has fully ran, the dump.sql file can be run. This file inserts data 
the database for each table. Once this was all done, we moved to creating the java file to create the command shell. 

We implemented all functions and methods within a class. The main function in our java class takes user input and sends it into a switch statement, 
which then in turn will call the correct function for the type of inputs that were passed in. We also include log messages within each switch case.

SQL statements are then prepared within the function. These statements take the input provided by the user and format it into the proper
SQL format. These functions will also check additional statements and values as needed, i.e. checking if a user exists before actually adding the user
into the database.

## Testing
All testing that was conducted was manual. This included running multiple SQL statements within MySQLWorkbench to make sure the initial setup of the 
database and tables were correct. For the java application, we ran it off of onyx. We ran and tested each method, all with sample input. 

We ran into a few issues, mainly with selecting a main, active class. The issue had to do something with scoping within our java file. Our solution for
this was to create a boolean variable within the class table in order to identify which class is being selected. This then solved our active class issue.

The remaining issue we were unable to solve were the commands for checking grades. This was mainly due to not understanding the type of query needed to 
be able to obtain the type of layout/output that was described in the project assignment. 