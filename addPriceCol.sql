ALTER TABLE movies
ADD COLUMN price INT generated always as (10);