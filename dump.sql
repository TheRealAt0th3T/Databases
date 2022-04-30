#
# class
#
insert into class (class_courseNum, class_term, class_sectionNum, class_description) values ('CS410', 'Sp22', 2, 'etium iaculis diam erat fermentum justo nec condimentum neque sapien placerat ante nulla');
insert into class (class_courseNum, class_term, class_sectionNum, class_description) values ('CS410', 'Sp22', 1, 'etium iaculis diam erat fermentum justo nec condimentum neque sapien placerat ante nulla');
insert into class (class_courseNum, class_term, class_sectionNum, class_description) values ('CS361', 'Fa20', 1, 'vitae nisi nam ultrices libero non mattis pulvinar nulla pede ullamcorper');
insert into class (class_courseNum, class_term, class_sectionNum, class_description) values ('CS457', 'Fa21', 1, 'pellentesque viverra pede ac diam cras pellentesque volutpat dui maecenas');


#
# students
#
insert into students (students_firstName, students_lastName, students_username, students_IDnum, class_id) values ('Wenonah', 'Mawhinney', 'wmawhinney0', '9003397414', 1);
insert into students (students_firstName, students_lastName, students_username, students_IDnum, class_id) values ('Cobby', 'Paley', 'cpaley0', '5217236248', 1);
insert into students (students_firstName, students_lastName, students_username, students_IDnum, class_id) values ('Sigismond', 'Foystone', 'sfoystone1', '4145106652', 2);
insert into students (students_firstName, students_lastName, students_username, students_IDnum, class_id) values ('Ivar', 'Ashborn', 'iashborn2', '9544840133', 4);
insert into students (students_firstName, students_lastName, students_username, students_IDnum, class_id) values ('Bel', 'Bridgestock', 'bbridgestock3', '5711527541', 4);
insert into students (students_firstName, students_lastName, students_username, students_IDnum, class_id) values ('Noe', 'Alaway', 'nalaway4', '8649159958', 3);
insert into students (students_firstName, students_lastName, students_username, students_IDnum, class_id) values ('Haywood', 'Hagley', 'hhagley5', '2844339522', 3);
insert into students (students_firstName, students_lastName, students_username, students_IDnum, class_id) values ('Jaymie', 'Carbery', 'jcarbery6', '9972352897', 2);
insert into students (students_firstName, students_lastName, students_username, students_IDnum, class_id) values ('Quent', 'Mapson', 'qmapson7', '1465503668', 1);
insert into students (students_firstName, students_lastName, students_username, students_IDnum, class_id) values ('Ede', 'Londsdale', 'elondsdale8', '8497646010', 1);
insert into students (students_firstName, students_lastName, students_username, students_IDnum, class_id) values ('Charity', 'Woodall', 'cwoodall9', '0061470783', 4);

#
# categories
#
insert into categories (categories_name, class_id) values ('Redhold', 2);
insert into categories (categories_name, class_id) values ('Viva', 1);
insert into categories (categories_name, class_id) values ('Prodder', 1);
insert into categories (categories_name, class_id) values ('Tresom', 2);
insert into categories (categories_name, class_id) values ('Stringtough', 1);
insert into categories (categories_name, class_id) values ('Hatity', 2);
insert into categories (categories_name, class_id) values ('Tres-Zap', 3);
insert into categories (categories_name, class_id) values ('Alphazap', 1);
insert into categories (categories_name, class_id) values ('Otcom', 3);
insert into categories (categories_name, class_id) values ('Zaam-Dox', 4);
insert into categories (categories_name, class_id) values ('Home Ing', 2);
insert into categories (categories_name, class_id) values ('Sonair', 3);
insert into categories (categories_name, class_id) values ('Duobam', 1);


#
# hasWeights
#
insert into hasWeight (hasWeight_weight, categories_id, class_id) values ('25', 1, 2);
insert into hasWeight (hasWeight_weight, categories_id, class_id) values ('20', 2, 1);
insert into hasWeight (hasWeight_weight, categories_id, class_id) values ('20', 3, 1);
insert into hasWeight (hasWeight_weight, categories_id, class_id) values ('25', 4, 2);
insert into hasWeight (hasWeight_weight, categories_id, class_id) values ('20', 5, 1);
insert into hasWeight (hasWeight_weight, categories_id, class_id) values ('25', 6, 2);
insert into hasWeight (hasWeight_weight, categories_id, class_id) values ('50', 7, 3);
insert into hasWeight (hasWeight_weight, categories_id, class_id) values ('20', 8, 1);
insert into hasWeight (hasWeight_weight, categories_id, class_id) values ('25', 9, 3);
insert into hasWeight (hasWeight_weight, categories_id, class_id) values ('100', 10, 4);
insert into hasWeight (hasWeight_weight, categories_id, class_id) values ('25', 11, 2);
insert into hasWeight (hasWeight_weight, categories_id, class_id) values ('25', 12, 3);
insert into hasWeight (hasWeight_weight, categories_id, class_id) values ('20', 13, 1);

#
# assignments
#
insert into assignments (assignments_name, assignments_description, assignments_pointValue, categories_id) values ('Home Ing', 'feugiat non pretium', 1,2);
insert into assignments (assignments_name, assignments_description, assignments_pointValue, categories_id) values ('Tres-Zap', 'in felis', 61,1);
insert into assignments (assignments_name, assignments_description, assignments_pointValue, categories_id) values ('Dep', 'iaugue eu massa commodo', 27,7);
insert into assignments (assignments_name, assignments_description, assignments_pointValue, categories_id) values ('Helm-To', 'dignissim neque', 48,10);
insert into assignments (assignments_name, assignments_description, assignments_pointValue, categories_id) values ('Quiz', 'est et eleifend', 1,2);
insert into assignments (assignments_name, assignments_description, assignments_pointValue, categories_id) values ('Test', 'vel fringilla', 61,1);
insert into assignments (assignments_name, assignments_description, assignments_pointValue, categories_id) values ('Report', 'consequat fermentum', 27,7);
insert into assignments (assignments_name, assignments_description, assignments_pointValue, categories_id) values ('TakeHome', 'dignissim aoidu', 48,10);
insert into assignments (assignments_name, assignments_description, assignments_pointValue, categories_id) values ('testing', 'dignissim aoidu', 20,10);


#
# assignedHW
#
insert into assignedHW (assignedHW_grade, students_id, assignments_id) values (1, 1, 1);
insert into assignedHW (assignedHW_grade, students_id, assignments_id) values (19, 2, 2);
insert into assignedHW (assignedHW_grade, students_id, assignments_id) values (27, 3, 3);
insert into assignedHW (assignedHW_grade, students_id, assignments_id) values (29, 4, 4);
insert into assignedHW (assignedHW_grade, students_id, assignments_id) values (48, 5, 4);
insert into assignedHW (assignedHW_grade, students_id, assignments_id) values (27, 6, 3);
insert into assignedHW (assignedHW_grade, students_id, assignments_id) values (12, 7, 3);
insert into assignedHW (assignedHW_grade, students_id, assignments_id) values (61, 8, 2);
insert into assignedHW (assignedHW_grade, students_id, assignments_id) values (1, 9, 1);
insert into assignedHW (assignedHW_grade, students_id, assignments_id) values (0, 10, 1);
insert into assignedHW (assignedHW_grade, students_id, assignments_id) values (4, 10, 4);

