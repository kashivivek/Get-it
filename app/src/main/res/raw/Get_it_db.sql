
BEGIN TRANSACTION;


DROP TABLE IF EXISTS users;
CREATE TABLE `users` (
	`username`	TEXT,
	`id`	TEXT,
	`password`	TEXT,
	`question`	TEXT,
	`answer`	TEXT,
	`ageplace`	TEXT,
	`imagepath`	TEXT,
	`phone`	INTEGER,
	PRIMARY KEY(id)
);
INSERT INTO users (username, id, password, question, answer, ageplace, imagepath, phone) VALUES ('vivek', '1', 'vivek123', 'what', 'nothing', '22 M, Hyderabad', '', '');
INSERT INTO users (username, id, password, question, answer, ageplace, imagepath, phone) VALUES ('anshu', '2', 'anshu123', 'why', 'nothing', '22 F, Hyderabad', '', '
');
INSERT INTO users (username, id, password, question, answer, ageplace, imagepath, phone) VALUES ('sunjay', '3', 'sunjay', '', '', '', '', '');
INSERT INTO users (username, id, password, question, answer, ageplace, imagepath, phone) VALUES ('jai', '4', 'jai', 'what', 'nothing ', '', '', '');
INSERT INTO users (username, id, password, question, answer, ageplace, imagepath, phone) VALUES ('hari', '5', 'hari', '', '', '', '', '');

DROP TABLE IF EXISTS household_products_table;
CREATE TABLE "household_products_table" (
	`pid`	TEXT,
	`name`	TEXT,
	`phone`	INTEGER,
	`longitude`	TEXT,
	`type`	TEXT,
	`imagepath`	TEXT,
	`latitude`	TEXT,
	PRIMARY KEY(pid)
);
INSERT INTO household_products_table (pid, name, phone, longitude, type, imagepath, latitude) VALUES ('H1', 'swami', 54106352, '17.423261', 'household', '', '78.494310');
INSERT INTO household_products_table (pid, name, phone, longitude, type, imagepath, latitude) VALUES ('H2', 'NARSING', 210512, '17.421769', 'household', '', '78.493521');
INSERT INTO household_products_table (pid, name, phone, longitude, type, imagepath, latitude) VALUES ('H3', 'DAYA', 21052, '17.422203', 'household', '', '78.492299');


DROP TABLE IF EXISTS food_products_table;
CREATE TABLE "food_products_table" (
	`pid`	TEXT,
	`name`	TEXT,
	`phone`	INTEGER,
	`longitude`	TEXT,
	`type`	TEXT,
	`imagepath`	TEXT,
	`latitude`	INTEGER,
	PRIMARY KEY(pid)
);
INSERT INTO food_products_table (pid, name, phone, longitude, type, imagepath, latitude) VALUES ('F1', 'Bawarchi', '91 40 2763 4494', '17.406141', 'Food', '', 78.497658);
INSERT INTO food_products_table (pid, name, phone, longitude, type, imagepath, latitude) VALUES ('F2', 'Abbai Tiffins', 0, '17.413481', 'Food', '', 78.490258);
INSERT INTO food_products_table (pid, name, phone, longitude, type, imagepath, latitude) VALUES ('F3', 'Green Bawarchi', 0, '17.418159', 'Food', '', 78.49092899999999);
INSERT INTO food_products_table (pid, name, phone, longitude, type, imagepath, latitude) VALUES ('F4', 'Ashirwaad', 0, '17.423963', 'Food', '', 78.49320299999999);


DROP TABLE IF EXISTS groceries_products_table;
CREATE TABLE "groceries_products_table" (
	`pid`	TEXT,
	`name`	TEXT,
	`phone`	INTEGER,
	`longitude`	TEXT,
	`type`	TEXT,
	`imagepath`	TEXT,
	`latitude`	TEXT,
	PRIMARY KEY(pid)
);
INSERT INTO groceries_products_table (pid, name, phone, longitude, type, imagepath, latitude) VALUES ('G3', 'Sri Shakthi General Stores', 1000, '17.424167', 'Groceries', '', '78.492540');
INSERT INTO groceries_products_table (pid, name, phone, longitude, type, imagepath, latitude) VALUES ('G2', 'Raj Stores', 0, '17.425946', 'Groceries', '', '78.494765');
INSERT INTO groceries_products_table (pid, name, phone, longitude, type, imagepath, latitude) VALUES ('G1', 'Shakthi', 0, '17.423887', 'Groceries', '', '78.492696');

COMMIT TRANSACTION;
