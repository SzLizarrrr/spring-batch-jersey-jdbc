DROP TABLE IF EXISTS people;

CREATE TABLE people  (
    person_id INT NOT NULL PRIMARY KEY auto_increment,
    first_name VARCHAR(20),
    last_name VARCHAR(20)
);
