DROP PROCEDURE IF EXISTS add_movie;
DELIMITER $$ 
CREATE PROCEDURE add_movie (IN movie_title varchar(100), IN movie_year varchar(100), IN movie_director varchar(100), IN movie_price INT, IN star_name varchar(100), IN genre_name varchar(32))
BEGIN
    -- Variables 
	DECLARE starCount INT DEFAULT 0;
    DECLARE maxId varchar(10) DEFAULT null;
    DECLARE genreCount INT DEFAULT 0;
	DECLARE movieCount INT DEFAULT 0;
    DECLARE movieId varchar(10);
    DECLARE starId varchar(10);
    DECLARE genreId varchar(10);
    -- Raises error if movie already exists
    SELECT COUNT(*) 
    INTO movieCount 
    FROM movies 
    WHERE title = movie_title AND year = movie_year AND director = movie_director;
    IF (movieCount > 0) THEN
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'Duplicate movie';
    END IF;
    -- handles new star
	SELECT COUNT(*) 
    INTO starCount 
    FROM stars 
    WHERE name = star_name;
    IF (starCount = 0) THEN
    	SELECT MAX(id) INTO maxId from stars;
		INSERT INTO stars (id, name, birthYear) 
			VALUES (CONCAT(SUBSTRING(maxId, 1, 2),CAST(CAST(SUBSTRING(maxId, 3) AS UNSIGNED)+1 AS CHAR)),star_name, null);
    END IF;
    SELECT id INTO starId FROM stars WHERE name = star_name LIMIT 1;
    -- handles new genre
    SELECT COUNT(*) 
    INTO genreCount 
    FROM genres 
    WHERE name = genre_name;
    IF (genreCount = 0) THEN
		INSERT INTO genres (name) 
			VALUES (genre_name);
    END IF;
    SELECT id INTO genreId FROM genres WHERE name = genre_name;
    -- inserts movie into movies
    SELECT MAX(id) INTO maxId from movies;
    SELECT CONCAT(SUBSTRING(maxId, 1, 2),CAST(CAST(SUBSTRING(maxId, 3) AS UNSIGNED)+1 AS CHAR)) INTO movieId;
    INSERT INTO movies (id, title, year, director, price)
		VALUES (movieId, movie_title, movie_year, movie_director, movie_price);
    -- inserts movie, star into stars_in_movies
    INSERT INTO stars_in_movies (starId, movieId)
		VALUES (starId, movieId);
	-- inserts movie, genre into genres_in_movies
    INSERT INTO genres_in_movies (genreId, movieId)
		VALUES (genreId, movieId);
      	-- insert a default rating
    INSERT INTO ratings (movieId, rating)
		VALUES (movieId, null);
    END$$
    -- Change back DELIMITER to ; 
    DELIMITER ; 
