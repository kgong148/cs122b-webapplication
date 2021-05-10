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

ALTER TABLE employees MODIFY COLUMN password VARCHAR(128);
UPDATE employees SET password='RpQVP/xEt56zwoeunWtFe0F4Bwn2nuU7de8DixjUcZWBFb6zG8Bf0F6vsYDfuDtE' WHERE email='classta@email.edu';
