drop table ExpenditureWorker cascade constraints;
drop table ExpenditureManager cascade constraints;
drop table ProjectAssignedToEmployee cascade constraints;
drop table ProjectPresident cascade constraints;
drop table ProjectManager cascade constraints;
drop table Expenditure cascade constraints;
drop table Employee cascade constraints;
drop table Manager cascade constraints;
drop table President cascade constraints;
drop table Worker cascade constraints;
drop table ProjectBudget cascade constraints;

CREATE TABLE Worker (
	ID			NUMBER,
	Name		VARCHAR2(25),
	PhoneNumber	VARCHAR2(13),
	PRIMARY KEY (ID),
	UNIQUE (PhoneNumber),
	CONSTRAINT chk_phone CHECK (REGEXP_LIKE(PhoneNumber,'^\d{3}-\d{3}-\d{4}$','i'))
);

INSERT INTO Worker VALUES (1, 'Jack Sparrow', '113-555-8789');
INSERT INTO Worker VALUES (2, 'Daisy Duck', '457-898-4545');
INSERT INTO Worker VALUES (3, 'Huey Duck', '457-898-4546');
INSERT INTO Worker VALUES (4, 'Dewey Duck', '457-898-4547');
INSERT INTO Worker VALUES (5, 'Louie Duck', '457-898-4548');
INSERT INTO Worker VALUES (6, 'Naruto', '778-123-9499');
INSERT INTO Worker VALUES (7, 'Sasuke', '778-234-4573');
INSERT INTO Worker VALUES (8, 'Kakashi', '778-123-3747');
INSERT INTO Worker VALUES (9, 'Sakura', '778-129-9999');
INSERT INTO Worker VALUES (10, 'Minnie Mouse', '125-556-9899');
INSERT INTO Worker VALUES (11, 'Daffy Duck', '554-657-5656');
INSERT INTO Worker VALUES (12, 'Lightning McQueen', '113-555-8788');
INSERT INTO Worker VALUES (13, 'Buzz Lightyear', '554-657-5626');
INSERT INTO Worker VALUES (14, 'Sponge Bob', '656-987-9566');
INSERT INTO Worker VALUES (15, 'Patrick Star', '656-987-8329');
INSERT INTO Worker VALUES (16, 'Squidward Tentacles', '656-379-2349');
INSERT INTO Worker VALUES (17, 'Eugene H. Krabs', '656-234-1239');
INSERT INTO Worker VALUES (18, 'Sandy Cheeks', '656-239-1230');
INSERT INTO Worker VALUES (19, 'Gary', '656-239-0480');

INSERT INTO Worker VALUES (20, 'Mickey Mouse', '656-987-2567'); 
INSERT INTO Worker VALUES (21, 'Launchpad McQuack', '554-677-5656');
INSERT INTO Worker VALUES (22, 'Gyro Gearloose', '656-287-2566');
INSERT INTO Worker VALUES (23, 'Doofus Drake', '589-657-5656');
INSERT INTO Worker VALUES (24, 'Patrick Star', '666-987-2566');

INSERT INTO Worker VALUES (25, 'Goku', '321-738-9013');
INSERT INTO Worker VALUES (26, 'Gohan', '321-743-8271');
INSERT INTO Worker VALUES (27, 'Chi-Chi', '321-752-0192');
INSERT INTO Worker VALUES (28, 'Goten', '321-792-0129');
INSERT INTO Worker VALUES (29, 'Vegeta', '321-847-0195');
INSERT INTO Worker VALUES (30, 'Truncks', '321-738-5739');
INSERT INTO Worker VALUES (31, 'Bulma', '321-941-3927');
INSERT INTO Worker VALUES (32, 'Krillin', '321-837-2840');
INSERT INTO Worker VALUES (33, 'Android 18', '321-749-6920');
INSERT INTO Worker VALUES (34, 'Mr. Satan', '321-903-7439');
INSERT INTO Worker VALUES (35, 'Piccolo', '321-739-9341');
INSERT INTO Worker VALUES (36, 'Master Roshi', '321-573-3288');
INSERT INTO Worker VALUES (37, 'Majin Buu', '321-232-8888');
INSERT INTO Worker VALUES (38, 'Videl', '321-333-5737');

INSERT INTO Worker VALUES (39, 'Beerus', '007-007-0007');

INSERT INTO Worker VALUES (40, 'Whis', '007-008-0008');


CREATE TABLE President (
	pres_ID	NUMBER,
	PRIMARY KEY (pres_ID),
	FOREIGN KEY (pres_ID) REFERENCES Worker
	ON DELETE CASCADE
);


INSERT INTO President VALUES (1); 	
INSERT INTO President VALUES (40);	

CREATE TABLE Manager (
	man_ID	NUMBER,
	pres_ID	NUMBER NOT NULL,
	PRIMARY KEY (man_ID),
	FOREIGN KEY (man_ID) REFERENCES Worker ON DELETE CASCADE,
	FOREIGN KEY (pres_ID) REFERENCES President ON DELETE CASCADE
);


INSERT INTO Manager VALUES (8, 40);		
INSERT INTO Manager VALUES (11, 1);		
INSERT INTO Manager VALUES (12, 1);		
INSERT INTO Manager VALUES (13, 1);		
INSERT INTO Manager VALUES (39, 40);	



CREATE TABLE Employee (
	emp_ID	NUMBER,
	man_ID	NUMBER NOT NULL,
	PRIMARY KEY (emp_ID),
	FOREIGN KEY (emp_ID) REFERENCES Worker ON DELETE CASCADE,
	FOREIGN KEY (man_ID) REFERENCES Manager ON DELETE CASCADE
);




INSERT INTO Employee VALUES (2, 11);
INSERT INTO Employee VALUES (3, 11);
INSERT INTO Employee VALUES (4, 11);
INSERT INTO Employee VALUES (5, 11);
INSERT INTO Employee VALUES (10, 11);

INSERT INTO Employee VALUES (6, 8);
INSERT INTO Employee VALUES (7, 8);
INSERT INTO Employee VALUES (9, 8);

INSERT INTO Employee VALUES (14, 12);
INSERT INTO Employee VALUES (15, 12);
INSERT INTO Employee VALUES (16, 12);
INSERT INTO Employee VALUES (17, 12);
INSERT INTO Employee VALUES (18, 12);
INSERT INTO Employee VALUES (19, 12);

INSERT INTO Employee VALUES (20, 13);
INSERT INTO Employee VALUES (21, 13);
INSERT INTO Employee VALUES (22, 13);
INSERT INTO Employee VALUES (23, 13);
INSERT INTO Employee VALUES (24, 13);

INSERT INTO Employee VALUES (25, 39);
INSERT INTO Employee VALUES (26, 39);
INSERT INTO Employee VALUES (27, 39);
INSERT INTO Employee VALUES (28, 39);
INSERT INTO Employee VALUES (29, 39);
INSERT INTO Employee VALUES (30, 39);
INSERT INTO Employee VALUES (31, 39);
INSERT INTO Employee VALUES (32, 39);
INSERT INTO Employee VALUES (33, 39);
INSERT INTO Employee VALUES (34, 39);
INSERT INTO Employee VALUES (35, 39);
INSERT INTO Employee VALUES (36, 39);
INSERT INTO Employee VALUES (37, 39);
INSERT INTO Employee VALUES (38, 39);


CREATE TABLE ProjectBudget (
	PID		NUMBER,
	Name	VARCHAR2(50),
	Budget		NUMBER NOT NULL,
	Description	VARCHAR2(1000),
	PRIMARY KEY (PID)
);


INSERT INTO ProjectBudget VALUES (001, 'Black Pearl', 10000000, 'Creating a ship for captain');
INSERT INTO ProjectBudget VALUES (002, 'Dying Gull', 2000000, 'Creating a ship for the fleet');
INSERT INTO ProjectBudget VALUES (003, 'Konoha', 100000, 'Creating a small ship for the fleet');
INSERT INTO ProjectBudget VALUES (004, 'Troubadour', 2000000, 'Creating a ship for the fleet');
INSERT INTO ProjectBudget VALUES (005, 'H. Time Chamber', 100000000, 'Goku needs to defeat cell');


CREATE TABLE ProjectPresident (
	PID		NUMBER,
	pres_ID	NUMBER NOT NULL,
	PRIMARY KEY (PID),
	FOREIGN KEY (PID) REFERENCES ProjectBudget ON DELETE CASCADE,
	FOREIGN KEY (pres_ID) REFERENCES President ON DELETE CASCADE
);

INSERT INTO ProjectPresident VALUES (001, 1);
INSERT INTO ProjectPresident VALUES (002, 1);
INSERT INTO ProjectPresident VALUES (003, 1);
INSERT INTO ProjectPresident VALUES (004, 1);
INSERT INTO ProjectPresident VALUES (005, 40);

CREATE TABLE ProjectManager (
	PID		NUMBER,
	man_ID	NUMBER NOT NULL,
	PRIMARY KEY (PID),
	FOREIGN KEY (man_ID) REFERENCES Manager ON DELETE CASCADE,
	FOREIGN KEY (PID) REFERENCES ProjectBudget ON DELETE CASCADE
);


INSERT INTO ProjectManager VALUES (001, 12);
INSERT INTO ProjectManager VALUES (002, 11);
INSERT INTO ProjectManager VALUES (003, 8);
INSERT INTO ProjectManager VALUES (004, 13);
INSERT INTO ProjectManager VALUES (005, 39);


CREATE TABLE Expenditure (
	EID		NUMBER,
	Type		VARCHAR2(100),
	Description	VARCHAR2(1000),
	Expenditure_Date		Timestamp,
	Amount	NUMBER,
	PRIMARY KEY (EID)
);


INSERT INTO Expenditure VALUES (1001, 'Equipment', 'Building equipment for ship', '2017-03-06 23:03:20', 5000000);
INSERT INTO Expenditure VALUES (1002, 'Equipment', 'Building equipment for chamber', '2017-03-07 09:00:00', 20000000);
INSERT INTO Expenditure VALUES (1003, 'Food', 'Lunch for staff', '2017-03-11 12:00:00', 300);
INSERT INTO Expenditure VALUES (1004, 'Materials', 'Building materials for ship', '2017-03-07 09:00:00', 400);
INSERT INTO Expenditure VALUES (1005, 'Materials', 'Building materials for ship', '2017-03-07 09:30:00', 200);
INSERT INTO Expenditure VALUES (1006, 'Materials', 'Building materials for ship', '2017-03-07 10:34:54', 1000);
INSERT INTO Expenditure VALUES (1007, 'Materials', 'Building materials for ship', '2017-03-07 11:12:32', 700);
INSERT INTO Expenditure VALUES (1008, 'Food', 'Lunch for staff', '2017-03-12 12:00:00', 317);
INSERT INTO Expenditure VALUES (1009, 'Salary', 'Paid to temporary employee', '2017-03-07 18:00:00', 1000);
INSERT INTO Expenditure VALUES (1010, 'Materials', 'Building materials for ship', '2017-03-08 09:00:00', 400);
INSERT INTO Expenditure VALUES (1011, 'Materials', 'Building materials for ship', '2017-03-08 09:10:00', 300);
INSERT INTO Expenditure VALUES (1012, 'Materials', 'Building materials for chamber', '2017-03-08 09:54:54', 1500);
INSERT INTO Expenditure VALUES (1013, 'Materials', 'Building materials for chamber', '2017-03-08 10:22:32', 850);
INSERT INTO Expenditure VALUES (1014, 'Materials', 'Building materials for chamber', '2017-03-08 10:28:12', 728);
INSERT INTO Expenditure VALUES (1015, 'Materials', 'Building materials for chamber', '2017-03-08 10:30:05', 170);
INSERT INTO Expenditure VALUES (1016, 'Materials', 'Building materials for ship', '2017-03-08 10:34:54', 1000);
INSERT INTO Expenditure VALUES (1017, 'Materials', 'Building materials for ship', '2017-03-08 11:12:32', 700);
INSERT INTO Expenditure VALUES (1018, 'Food', 'Lunch for staff', '2017-03-08 12:00:00', 304);
INSERT INTO Expenditure VALUES (1019, 'Food', 'Snacks throughout the day', '2017-03-08 14:00:00', 150);
INSERT INTO Expenditure VALUES (1020, 'Food', 'Dinner for staff', '2017-03-08 20:00:00', 600);
INSERT INTO Expenditure VALUES (1021, 'Salary', 'Paid to temporary employee', '2017-03-08 18:00:00', 450);
INSERT INTO Expenditure VALUES (1022, 'Salary', 'Paid to temporary employee', '2017-03-08 18:00:00', 3500);
INSERT INTO Expenditure VALUES (1023, 'Salary', 'Paid to temporary employee', '2017-03-08 18:00:00', 1200);
INSERT INTO Expenditure VALUES (1024, 'Salary', 'Paid to temporary employee', '2017-03-08 18:00:00', 5000);
INSERT INTO Expenditure VALUES (1025, 'Materials', 'Building materials for ship', '2017-03-09 09:05:00', 400);
INSERT INTO Expenditure VALUES (1026, 'Materials', 'Building materials for ship', '2017-03-09 09:17:00', 300);
INSERT INTO Expenditure VALUES (1027, 'Materials', 'Building materials for chamber', '2017-03-09 09:34:54', 1500);
INSERT INTO Expenditure VALUES (1028, 'Materials', 'Building materials for ship', '2017-03-09 10:02:32', 8500);
INSERT INTO Expenditure VALUES (1029, 'Materials', 'Building materials for chamber', '2017-03-09 10:18:12', 1728);

INSERT INTO Expenditure VALUES (1031, 'Materials', 'Building materials for ship', '2017-03-09 10:47:54', 5000);
INSERT INTO Expenditure VALUES (1032, 'Materials', 'Building materials for chamber', '2017-03-09 11:42:32', 700);
INSERT INTO Expenditure VALUES (1033, 'Food', 'Lunch for staff', '2017-03-09 12:00:00', 304);
INSERT INTO Expenditure VALUES (1034, 'Materials', 'Building materials for ship', '2017-03-09 13:22:32', 850);
INSERT INTO Expenditure VALUES (1035, 'Materials', 'Building materials for ship', '2017-03-09 13:28:12', 728);
INSERT INTO Expenditure VALUES (1036, 'Materials', 'Building materials for chamber', '2017-03-09 14:30:05', 170);
INSERT INTO Expenditure VALUES (1037, 'Food', 'Dinner for staff', '2017-03-09 20:00:00', 600);
INSERT INTO Expenditure VALUES (1038, 'Materials', 'Building materials for ship', '2017-03-10 09:03:00', 400);
INSERT INTO Expenditure VALUES (1039, 'Materials', 'Building materials for ship', '2017-03-10 09:18:00', 300);
INSERT INTO Expenditure VALUES (1040, 'Materials', 'Building materials for chamber', '2017-03-10 09:33:54', 1500);
INSERT INTO Expenditure VALUES (1041, 'Materials', 'Building materials for ship', '2017-03-10 10:09:32', 8500);
INSERT INTO Expenditure VALUES (1042, 'Materials', 'Building materials for chamber', '2017-03-10 10:21:42', 850);
INSERT INTO Expenditure VALUES (1043, 'Materials', 'Building materials for chamber', '2017-03-10 10:26:22', 728);
INSERT INTO Expenditure VALUES (1044, 'Materials', 'Building materials for chamber', '2017-03-10 10:28:15', 170);
INSERT INTO Expenditure VALUES (1045, 'Materials', 'Building materials for ship', '2017-03-10 11:31:42', 3400);
INSERT INTO Expenditure VALUES (1046, 'Food', 'Lunch for staff', '2017-03-10 12:00:00', 450);
INSERT INTO Expenditure VALUES (1047, 'Salary', 'Paid to temporary employee', '2017-03-10 15:00:00', 150);
INSERT INTO Expenditure VALUES (1048, 'Salary', 'Paid to temporary employee', '2017-03-10 15:00:00', 7500);
INSERT INTO Expenditure VALUES (1049, 'Salary', 'Paid to temporary employee', '2017-03-10 15:00:00', 600);
INSERT INTO Expenditure VALUES (1050, 'Food', 'Dinner for staff', '2017-03-09 18:00:00', 1000);
INSERT INTO Expenditure VALUES (1051, 'Misc', 'Party for staff', '2017-03-09 19:00:00', 450);



CREATE TABLE ExpenditureManager (
	EID		NUMBER,
	PID		NUMBER NOT NULL,
	ID		NUMBER NOT NULL,
	PRIMARY KEY(EID),
	FOREIGN KEY(EID) REFERENCES Expenditure ON DELETE CASCADE,
	FOREIGN KEY(PID) REFERENCES ProjectManager ON DELETE CASCADE,
	FOREIGN KEY(ID) REFERENCES Manager ON DELETE CASCADE
);

INSERT INTO ExpenditureManager VALUES (1001, 001, 12);
INSERT INTO ExpenditureManager VALUES (1002, 005, 39);
INSERT INTO ExpenditureManager VALUES (1003, 002, 11);
INSERT INTO ExpenditureManager VALUES (1004, 002, 11);
INSERT INTO ExpenditureManager VALUES (1005, 003, 8);
INSERT INTO ExpenditureManager VALUES (1006, 004, 13);
INSERT INTO ExpenditureManager VALUES (1007, 002, 11);
INSERT INTO ExpenditureManager VALUES (1008, 002, 11);
INSERT INTO ExpenditureManager VALUES (1009, 005, 39);
INSERT INTO ExpenditureManager VALUES (1010, 003, 8);
INSERT INTO ExpenditureManager VALUES (1011, 003, 8);
INSERT INTO ExpenditureManager VALUES (1012, 005, 39);
INSERT INTO ExpenditureManager VALUES (1013, 005, 39);
INSERT INTO ExpenditureManager VALUES (1014, 005, 39);
INSERT INTO ExpenditureManager VALUES (1015, 005, 39);
INSERT INTO ExpenditureManager VALUES (1016, 004, 13);
INSERT INTO ExpenditureManager VALUES (1017, 004, 13);
INSERT INTO ExpenditureManager VALUES (1018, 002, 11);
INSERT INTO ExpenditureManager VALUES (1019, 002, 11);
INSERT INTO ExpenditureManager VALUES (1020, 002, 11);
INSERT INTO ExpenditureManager VALUES (1021, 005, 39);
INSERT INTO ExpenditureManager VALUES (1022, 005, 39);
INSERT INTO ExpenditureManager VALUES (1023, 005, 39);
INSERT INTO ExpenditureManager VALUES (1024, 005, 39);
INSERT INTO ExpenditureManager VALUES (1025, 003, 8);
INSERT INTO ExpenditureManager VALUES (1026, 004, 13);
INSERT INTO ExpenditureManager VALUES (1027, 005, 39);
INSERT INTO ExpenditureManager VALUES (1028, 004, 13);
INSERT INTO ExpenditureManager VALUES (1029, 005, 39);

INSERT INTO ExpenditureManager VALUES (1031, 002, 11);
INSERT INTO ExpenditureManager VALUES (1032, 005, 39);
INSERT INTO ExpenditureManager VALUES (1033, 002, 11);
INSERT INTO ExpenditureManager VALUES (1034, 002, 11);
INSERT INTO ExpenditureManager VALUES (1035, 002, 11);
INSERT INTO ExpenditureManager VALUES (1036, 005, 39);
INSERT INTO ExpenditureManager VALUES (1037, 002, 11);
INSERT INTO ExpenditureManager VALUES (1038, 004, 13);
INSERT INTO ExpenditureManager VALUES (1039, 004, 13);
INSERT INTO ExpenditureManager VALUES (1040, 005, 39);
INSERT INTO ExpenditureManager VALUES (1041, 001, 12);
INSERT INTO ExpenditureManager VALUES (1042, 005, 39);
INSERT INTO ExpenditureManager VALUES (1043, 005, 39);
INSERT INTO ExpenditureManager VALUES (1044, 005, 39);
INSERT INTO ExpenditureManager VALUES (1045, 002, 11);
INSERT INTO ExpenditureManager VALUES (1046, 002, 11);
INSERT INTO ExpenditureManager VALUES (1047, 005, 39);
INSERT INTO ExpenditureManager VALUES (1048, 005, 39);
INSERT INTO ExpenditureManager VALUES (1049, 005, 39);
INSERT INTO ExpenditureManager VALUES (1050, 002, 11);
INSERT INTO ExpenditureManager VALUES (1051, 002, 11);


CREATE TABLE ProjectAssignedToEmployee (
	emp_ID	NUMBER,
	PID		NUMBER,
	Role	VARCHAR2(20),
	PRIMARY KEY (emp_ID, PID),
	FOREIGN KEY (emp_ID) REFERENCES Employee ON DELETE CASCADE,
	FOREIGN KEY (PID) REFERENCES ProjectBudget ON DELETE CASCADE
);


INSERT INTO ProjectAssignedToEmployee VALUES (2, 002, 'Wood Processor');
INSERT INTO ProjectAssignedToEmployee VALUES (3, 002, 'Metal Processor');
INSERT INTO ProjectAssignedToEmployee VALUES (4, 002, 'Labourer');
INSERT INTO ProjectAssignedToEmployee VALUES (5, 002, 'Welder');
INSERT INTO ProjectAssignedToEmployee VALUES (10, 002, 'Wood Processor');

INSERT INTO ProjectAssignedToEmployee VALUES (6, 003, 'Wood Processor');
INSERT INTO ProjectAssignedToEmployee VALUES (7, 003, 'Labourer');
INSERT INTO ProjectAssignedToEmployee VALUES (9, 003, 'Cleaner');

INSERT INTO ProjectAssignedToEmployee VALUES (14, 001, 'Wood Processor');
INSERT INTO ProjectAssignedToEmployee VALUES (15, 001, 'Metal Processor');
INSERT INTO ProjectAssignedToEmployee VALUES (16, 001, 'Labourer');
INSERT INTO ProjectAssignedToEmployee VALUES (17, 001, 'Cleaner');
INSERT INTO ProjectAssignedToEmployee VALUES (18, 001, 'Wood Processor');
INSERT INTO ProjectAssignedToEmployee VALUES (19, 001, 'Metal Processor');

INSERT INTO ProjectAssignedToEmployee VALUES (20, 004, 'Wood Processor');
INSERT INTO ProjectAssignedToEmployee VALUES (21, 004, 'Metal Processor');
INSERT INTO ProjectAssignedToEmployee VALUES (22, 004, 'Labourer');
INSERT INTO ProjectAssignedToEmployee VALUES (23, 004, 'Cleaner');
INSERT INTO ProjectAssignedToEmployee VALUES (24, 004, 'Metal Processor');

INSERT INTO ProjectAssignedToEmployee VALUES (25, 005, 'Metal Processor');
INSERT INTO ProjectAssignedToEmployee VALUES (26, 005, 'Metal Processor');
INSERT INTO ProjectAssignedToEmployee VALUES (27, 005, 'Metal Processor');
INSERT INTO ProjectAssignedToEmployee VALUES (28, 005, 'Wood Processor');
INSERT INTO ProjectAssignedToEmployee VALUES (29, 005, 'Wood Processor');
INSERT INTO ProjectAssignedToEmployee VALUES (30, 005, 'Wood Processor');
INSERT INTO ProjectAssignedToEmployee VALUES (31, 005, 'Labourer');
INSERT INTO ProjectAssignedToEmployee VALUES (32, 005, 'Labourer');
INSERT INTO ProjectAssignedToEmployee VALUES (33, 005, 'Labourer');
INSERT INTO ProjectAssignedToEmployee VALUES (34, 005, 'Cleaner');
INSERT INTO ProjectAssignedToEmployee VALUES (35, 005, 'Cleaner');
INSERT INTO ProjectAssignedToEmployee VALUES (36, 005, 'Polisher');
INSERT INTO ProjectAssignedToEmployee VALUES (37, 005, 'Polisher');
INSERT INTO ProjectAssignedToEmployee VALUES (38, 005, 'Polisher');


CREATE TABLE ExpenditureWorker (
	EID			NUMBER,
	PID			NUMBER NOT NULL,
	ID			NUMBER NOT NULL,
	PRIMARY KEY (EID),
	FOREIGN KEY (EID) REFERENCES Expenditure ON DELETE CASCADE,
	FOREIGN KEY (ID, PID) REFERENCES ProjectAssignedToEmployee ON DELETE CASCADE
);

INSERT INTO ExpenditureWorker VALUES (1001, 001, 14);  
INSERT INTO ExpenditureWorker VALUES (1002, 005, 25);
INSERT INTO ExpenditureWorker VALUES (1003, 002, 2);
INSERT INTO ExpenditureWorker VALUES (1004, 002, 3);
INSERT INTO ExpenditureWorker VALUES (1005, 003, 6);
INSERT INTO ExpenditureWorker VALUES (1006, 004, 20);
INSERT INTO ExpenditureWorker VALUES (1007, 002, 2);
INSERT INTO ExpenditureWorker VALUES (1008, 002, 2);
INSERT INTO ExpenditureWorker VALUES (1009, 005, 25);
INSERT INTO ExpenditureWorker VALUES (1010, 003, 7);
INSERT INTO ExpenditureWorker VALUES (1011, 003, 6);
INSERT INTO ExpenditureWorker VALUES (1012, 005, 26);
INSERT INTO ExpenditureWorker VALUES (1013, 005, 25);
INSERT INTO ExpenditureWorker VALUES (1014, 005, 27);
INSERT INTO ExpenditureWorker VALUES (1015, 005, 28);
INSERT INTO ExpenditureWorker VALUES (1016, 004, 20);
INSERT INTO ExpenditureWorker VALUES (1017, 004, 21);
INSERT INTO ExpenditureWorker VALUES (1018, 002, 4);
INSERT INTO ExpenditureWorker VALUES (1019, 002, 4);
INSERT INTO ExpenditureWorker VALUES (1020, 002, 2);
INSERT INTO ExpenditureWorker VALUES (1021, 005, 29);
INSERT INTO ExpenditureWorker VALUES (1022, 005, 30);
INSERT INTO ExpenditureWorker VALUES (1023, 005, 31);
INSERT INTO ExpenditureWorker VALUES (1024, 005, 32);
INSERT INTO ExpenditureWorker VALUES (1025, 003, 9);
INSERT INTO ExpenditureWorker VALUES (1026, 004, 22);
INSERT INTO ExpenditureWorker VALUES (1027, 005, 33);
INSERT INTO ExpenditureWorker VALUES (1028, 004, 23);
INSERT INTO ExpenditureWorker VALUES (1029, 005, 34);

INSERT INTO ExpenditureWorker VALUES (1031, 002, 2);
INSERT INTO ExpenditureWorker VALUES (1032, 005, 31);
INSERT INTO ExpenditureWorker VALUES (1033, 002, 2);
INSERT INTO ExpenditureWorker VALUES (1034, 002, 3);
INSERT INTO ExpenditureWorker VALUES (1035, 002, 2);
INSERT INTO ExpenditureWorker VALUES (1036, 005, 32);
INSERT INTO ExpenditureWorker VALUES (1037, 002, 3);
INSERT INTO ExpenditureWorker VALUES (1038, 004, 23);
INSERT INTO ExpenditureWorker VALUES (1039, 004, 20);
INSERT INTO ExpenditureWorker VALUES (1040, 005, 30);
INSERT INTO ExpenditureWorker VALUES (1041, 001, 14);
INSERT INTO ExpenditureWorker VALUES (1042, 005, 28);
INSERT INTO ExpenditureWorker VALUES (1043, 005, 25);
INSERT INTO ExpenditureWorker VALUES (1044, 005, 28);
INSERT INTO ExpenditureWorker VALUES (1045, 002, 2);
INSERT INTO ExpenditureWorker VALUES (1046, 002, 10);
INSERT INTO ExpenditureWorker VALUES (1047, 005, 31);
INSERT INTO ExpenditureWorker VALUES (1048, 005, 38);
INSERT INTO ExpenditureWorker VALUES (1049, 005, 37);
INSERT INTO ExpenditureWorker VALUES (1050, 002, 5);
INSERT INTO ExpenditureWorker VALUES (1051, 002, 2);
