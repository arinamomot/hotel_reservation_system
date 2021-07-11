-- This is export of test Data on the server before testing sequence. All passwords are 12345

INSERT INTO public.address (id,city,country,housenumber,postcode,street) VALUES
(2,'Brno','Czech Republic','42','54321','Novákova'),
(4,'Praha','Czech Republic','25','121212','Purkyněho'),
(6,'Budislav u Litomyšle','Czech Republic','1','750000','Ulice 42'),
(52,'Praha','Czech Republic','3','15003','Dvojitaá'),
(54,'Praha','Czech Republic','4','15003','Dvojitaá'),
(56,'Praha','Czech Republic','4','15003','Dvojitaá'),
(58,'Praha','Czech Republic','1','15003','Podivná'),
(60,'Praha','Czech Republic','2','15003','Podivná'),
(62,'Praha','Czech Republic','3','15003','Podivná');INSERT INTO public.hotel (id,disabled,"name",address_id) VALUES
(1,false,'U Pepy',2),
(3,false,'U Jána',4),
(5,false,'Miroslavův hotel',6);INSERT INTO public.hotel_user (id,user_type,disabled,email,firstname,lastname,"password",hotel_id,dateofbirth,phonenumber,address_id) VALUES
(1,'ADMIN',false,'admin@admin.cz','Adam','Adámica','$2a$10$n4QZnHy24s/bKPTnIgoho.DNM8ZoOdgC24VZEFtnJt7rvDiHmEqp2',NULL,NULL,NULL,NULL),
(22,'ADMIN',false,'pepa@admin.cz','Pepa','Horák','$2a$10$1yJlSgT2Mr0Po6dHAnJ7beUrUNCPgOELgNrZzCF7xQPBH/mWcbVF2',1,NULL,NULL,NULL),
(23,'ADMIN',false,'jan@admin.cz','Ján','Životabudič','$2a$10$xeGOQ8Jx6YEOrz4F1mRNwOvj7ZKH8i22sJIoSvxoFlIwG1SD112EK',3,NULL,NULL,NULL),
(24,'ADMIN',false,'miroslav@admin.cz','Miroslav','Deadline','$2a$10$SXrP0t1FBZc0lCzFbbkEzeUpOpMQ4Yw7L8W6sctZKhiJ2r4AWS/J.',5,NULL,NULL,NULL),
(51,'CUSTOMER',false,'jakub@nadiva.cz','Jakub','Nádiva','$2a$10$vp6We89CgFYVrYjq4G4YSeTzTJQlMm3X1lFN33c7V0SKkulHNoZZy',NULL,'2020-01-01 01:00:00','123456789',52),
(55,'CUSTOMER',false,'mina@adolf.cz','Mina','Adolfová','$2a$10$QnZGfjBwH.Da7JMfMg1DUe9Y6srhrxCgIHoBl0eiLhebI0y9Vsxoa',NULL,'2020-01-01 01:00:00','123456789',56),
(57,'CUSTOMER',false,'tp@email.cz','Terry','Pratchett','$2a$10$sK8yaZNLFQuBZj7kne.eweGmc6rEmGLANDL/JMRZPJ4HK31Rei8g.',NULL,'2020-01-01 01:00:00','123456789',58),
(59,'CUSTOMER',false,'se@email.cz','Steven','Erikson','$2a$10$Y.Ea8rGj0d/oShUXwgUOq.yEQDANWgwclPB77as2hI/ib.ER6seS.',NULL,'2020-01-01 01:00:00','123456789',60),
(61,'CUSTOMER',false,'ap@email.cz','Andrzej','Pilipiuk','$2a$10$2iK//fqpoFdcMmzS8UGyJevXlVnU4pCu4sZEtyyFJSG1u6SoHYyLC',NULL,'2020-01-01 01:00:00','123456789',62),
(53,'CUSTOMER',true,'zdenek@adolf.cz','Zdeněk','Adolf','$2a$10$zElZid23avD9pWkQ5iLVUO8YVpxEOJGZ4RYdDikjVaog2ITJ3wbmC',NULL,'2020-01-01 01:00:00','123456789',54);INSERT INTO public.reservation (id,checkindate,checkoutdate,numberofaccommodated,reservationcreateddate,status,customer_id) VALUES
(103,'2021-02-11','2021-02-20',6,'2021-01-03 21:43:09.255352','WAITING',51),
(102,'2021-02-01','2021-02-10',8,'2021-01-03 21:42:28.501724','APPROVED',51),
(104,'2021-02-01','2021-02-20',15,'2021-01-03 21:44:02.915919','APPROVED',51),
(101,'2021-02-01','2021-02-10',2,'2021-01-03 21:41:55.370776','DECLINED',51);INSERT INTO public.reservation_room (reservation_id,room_id) VALUES
(101,7),
(102,8),
(102,9),
(103,7),
(103,8),
(103,9),
(104,13),
(104,14),
(104,15),
(104,16);
INSERT INTO public.reservation_room (reservation_id,room_id) VALUES
(104,17);INSERT INTO public.room (id,disabled,floor,"number",numberofbeds,pricepernight,"type",hotel_id) VALUES
(7,false,1,101,4,5000,'STANDARD',1),
(8,false,1,102,4,5000,'STANDARD',1),
(9,false,1,103,4,5000,'STANDARD',1),
(10,false,1,104,4,5000,'STANDARD',1),
(11,false,2,201,2,8000,'DELUXE',1),
(12,false,2,202,2,10000,'APPARTEMENT',1),
(13,false,1,101,3,5000,'STANDARD',3),
(14,false,1,102,3,5000,'STANDARD',3),
(15,false,1,103,3,5000,'STANDARD',3),
(16,false,1,104,3,5000,'STANDARD',3);
INSERT INTO public.room (id,disabled,floor,"number",numberofbeds,pricepernight,"type",hotel_id) VALUES
(17,false,1,105,3,5000,'STANDARD',3),
(18,false,1,106,3,5000,'STANDARD',3),
(19,false,1,101,10,1000,'STANDARD',5),
(20,false,1,102,10,1000,'STANDARD',5),
(21,false,1,103,10,2000,'DELUXE',5);INSERT INTO public."sequence" (seq_name,seq_count) VALUES
('SEQ_GEN',150);