INSERT INTO role VALUES (1, 'user');
INSERT INTO role VALUES (2, 'admin');
INSERT INTO role VALUES (3, 'enterprise_admin');
INSERT INTO person VALUES (1, 'adm01global', '$2a$10$uBQOILH2hDkzm2cFDVRw5O8HCzJPUbEIBagCQQeTZXZxWY4C44haK',
(select id from role where name = 'enterprise_admin'));

/*The password was encoded by BCrypt from "secret"*/