BEGIN TRANSACTION;
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
INSERT INTO `users` (username,id,password,question,answer,ageplace,imagepath,phone) VALUES ('vivek','1','vivek123','what','nothing','22 M, Hyderabad','',''),
 ('anshu','2','anshu123','why','nothing','22 F, Hyderabad','','
'),
 ('sunjay','3','sunjay','','','','',''),
 ('jai','4','jai','what','nothing ','','',''),
 ('hari','5','hari','','','','','');
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
INSERT INTO `household_products_table` (pid,name,phone,longitude,type,imagepath,latitude) VALUES ('H1','swami',54106352,'78.494310','carpenter','http://stationaryfuelcells.org/wp-content/uploads/2016/05/Carpenter-work-6.jpg','17.423261'),
 ('H2','NARSING',210512,'78.493521','plumbing','http://firstqualityplumbingfl.com/wp-content/uploads/2016/04/plumbing-questions.jpg','17.421769'),
 ('H3','DAYA',21052,'78.492299','electrical','http://images.wisegeek.com/electrician-working-on-system.jpg','17.422203');
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
INSERT INTO `groceries_products_table` (pid,name,phone,longitude,type,imagepath,latitude) VALUES ('G3','Sri Swathi medical Stores',918885657658,'78.491448','pharmacy','http://www.famhealthcare.org/wp-content/uploads/2015/04/Pharmacy.jpg','17.419325'),
 ('G2','Raj Stores',919963694916,'78.494756','kirana','http://indianonlineseller.com/wp-content/uploads/2015/06/kirana-shops.jpg','17.425987'),
 ('G1','Padmavathi traders',9145278954,'78.493921','hardware','http://www.nzziec.com/uploadfile/2012/0307/20120307034158263.jpg','17.422464');
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
INSERT INTO `food_products_table` (pid,name,phone,longitude,type,imagepath,latitude) VALUES ('F1','jai mata di','91 40 2763 4494','78.493120','chaat','http://www.ft.lk/ftadmin/wp-content/uploads/2012/11/Pani-Puri-Chaat.jpg',17.421785),
 ('F2','Abbai Tiffins',0,'78.490421','bakers','http://mumbaigloss.in/wp-content/uploads/2014/08/Melting-Delights.jpg',17.414916),
 ('F3','Green Bawarchi',0,'78.491194','restaurant','http://cityofwarangal.com/media/10521098_499272963528714_5877234464618848516_n.jpg',17.418162),
 ('F4','Ashirwaad',0,'78.493349','restaurant','http://madaboutshanghai.blogs.com/mad_about_shanghai/images/kungfu_counter_1.jpg',17.423999);
COMMIT;
