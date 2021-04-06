DROP DATABASE IF EXISTS moviedb;
CREATE DATABASE moviedb;
USE moviedb;

CREATE TABLE movies (
  id VARCHAR(100) ,
  title varchar(100) NOT NULL ,
  year INTEGER NOT NULL,
  director varchar(100) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE stars (
  id varchar(10),
  name varchar(100) NOT NULL,
  birthYear integer,
  PRIMARY KEY (id)
);

CREATE TABLE stars_in_movies (
  starId varchar(10), 
  movieId varchar(10),
  FOREIGN KEY (starId) REFERENCES stars (id),
  FOREIGN KEY (movieId) REFERENCES movies (id)
);

CREATE TABLE genres (
  id integer AUTO_INCREMENT,
  name varchar(32) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE genres_in_movies (
  genreId integer,
  movieId varchar(10),
  FOREIGN KEY (genreId) REFERENCES genres (id),
  FOREIGN KEY (movieId) REFERENCES movies (id)
);


CREATE TABLE creditcards (
  id varchar(20),
  firstName varchar(50) NOT NULL,
  lastName varchar(50) NOT NULL,
  expiration date NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE customers (
  id integer,
  firstName varchar(50) NOT NULL,
  lastName varchar(50) NOT NULL,
  ccId varchar(20) NOT NULL,
  address varchar(200) NOT NULL,
  email varchar(50) NOT NULL,
  password varchar(20) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (ccId) REFERENCES creditcards (id)
);

CREATE TABLE sales (
  id integer AUTO_INCREMENT,
  customerId integer,
  movieId varchar(10) DEFAULT '',
  saleDate date NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (customerId) REFERENCES customers (id),
  FOREIGN KEY (movieId) REFERENCES movies (id)
);

CREATE TABLE ratings (
  movieId varchar(10),
  rating float,
  numVotes integer,
  FOREIGN KEY (movieId) REFERENCES movies (id)
);
