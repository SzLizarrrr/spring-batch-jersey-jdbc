DROP TABLE IF EXISTS PEOPLE;
DROP TABLE IF EXISTS USER;

CREATE TABLE PEOPLE  (
    person_id INT NOT NULL PRIMARY KEY auto_increment,
    first_name VARCHAR(20),
    last_name VARCHAR(20)
);

CREATE TABLE USER (
    id INT NOT NULL PRIMARY KEY auto_increment,
    name VARCHAR(20),
    age INT
);

