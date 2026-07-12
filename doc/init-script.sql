-- Create the user
CREATE USER "course-subscription-db-owner" WITH PASSWORD 'course-subscription-db-owner';

-- Create the database
CREATE DATABASE "course-subscription-db";

-- Give permissions to the user
GRANT ALL PRIVILEGES ON DATABASE "course-subscription-db"
TO "course-subscription-db-owner";

-- Give schema permission
GRANT ALL ON SCHEMA public TO "course-subscription-db-owner";

-- Allow table creation
ALTER SCHEMA public OWNER TO "course-subscription-db-owner";

-- Allow future tables (optionnal bu recommended)
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO "course-subscription-db-owner";
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO "course-subscription-db-owner";