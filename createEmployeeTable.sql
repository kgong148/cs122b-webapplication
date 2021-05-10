CREATE TABLE employees (
	email varchar(50),
	password varchar(20) not null,
	fullname varchar(100),
    PRIMARY KEY (email)
);

INSERT INTO employees (email, password, fullname) 
	VALUES ("classta@email.edu","classta", "TA CS122B");
    
ALTER TABLE movies DROP COLUMN price;
ALTER TABLE movies ADD COLUMN (price int DEFAULT 10);