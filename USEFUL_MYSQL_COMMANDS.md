#!/bin/bash

# ============================================================================

# Useful MySQL Commands for Music Hub

# ============================================================================

# Usage: Copy and paste these commands into MySQL CLI or terminal

# ============================================================================

# ============================================================================

# 1. BASIC CONNECTION & SETUP

# ============================================================================

# Login to MySQL (will prompt for password)

mysql -u root -p

# Login without password (for development)

mysql -u root

# Login to specific database

mysql -u root -p -h localhost -D bai_2

# Show MySQL version

mysql --version

# ============================================================================

# 2. DATABASE OPERATIONS

# ============================================================================

# Create database

CREATE DATABASE IF NOT EXISTS bai_2 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# Drop database (careful!)

DROP DATABASE IF EXISTS bai_2;

# Use/select database

USE bai_2;

# Show all databases

SHOW DATABASES;

# Show database size

SELECT table_schema AS database_name,
ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS size_mb
FROM information_schema.tables
WHERE table_schema = 'bai_2'
GROUP BY table_schema;

# ============================================================================

# 3. TABLE OPERATIONS

# ============================================================================

# List all tables

SHOW TABLES;

# Show table structure

DESC artist;
DESCRIBE artist; -- Alternative syntax

# Show create table statement

SHOW CREATE TABLE artist;

# Show column information

SELECT \* FROM information_schema.columns WHERE table_name = 'artist';

# Count rows in table

SELECT COUNT(_) FROM artist;
SELECT COUNT(_) FROM album;
SELECT COUNT(_) FROM song;
SELECT COUNT(_) FROM country;

# Show all tables with row counts

SELECT table_name, table_rows
FROM information_schema.tables
WHERE table_schema = 'bai_2';

# ============================================================================

# 4. USER MANAGEMENT

# ============================================================================

# Show current user

SELECT USER();

# Show all users

SELECT user, host FROM mysql.user;

# Create new user

CREATE USER 'musicuser'@'localhost' IDENTIFIED BY 'secure_password_123';

# Grant privileges to user

GRANT ALL PRIVILEGES ON bai_2._ TO 'musicuser'@'localhost';
GRANT SELECT, INSERT, UPDATE ON bai_2._ TO 'musicuser'@'localhost';

# Remove user

DROP USER 'musicuser'@'localhost';

# Change user password

ALTER USER 'musicuser'@'localhost' IDENTIFIED BY 'new_password';

# Flush privileges (apply changes)

FLUSH PRIVILEGES;

# ============================================================================

# 5. DATA INSPECTION

# ============================================================================

# View all artists

SELECT _ FROM artist;
SELECT _ FROM artist LIMIT 10; -- First 10 rows

# View artist details

SELECT id, name, stage_name, country_id FROM artist;

# View all albums

SELECT \* FROM album;

# View all songs

SELECT \* FROM song;

# View all countries

SELECT \* FROM country;

# View all genres

SELECT \* FROM genre;

# View all artist types

SELECT \* FROM artist_type;

# View all users

SELECT id, username, email, role FROM user;

# Count data

SELECT
(SELECT COUNT(_) FROM artist) AS total_artists,
(SELECT COUNT(_) FROM album) AS total_albums,
(SELECT COUNT(_) FROM song) AS total_songs,
(SELECT COUNT(_) FROM user) AS total_users;

# ============================================================================

# 6. SEARCH & FILTER

# ============================================================================

# Search artist by name

SELECT \* FROM artist WHERE name LIKE '%John%';

# Search artist by stage name

SELECT \* FROM artist WHERE stage_name LIKE '%Smith%';

# Find artists by country

SELECT a.\* FROM artist a
WHERE a.country_id = 1;

# Find all albums by specific artist

SELECT \* FROM album WHERE artist_id = 1;

# Find all songs by specific artist

SELECT s.\* FROM song s
WHERE s.artist_id = 1;

# Find songs by album

SELECT \* FROM song WHERE album_id = 1;

# Case-insensitive search

SELECT \* FROM artist WHERE LOWER(name) LIKE LOWER('%john%');

# ============================================================================

# 7. JOIN OPERATIONS

# ============================================================================

# Artist with country information

SELECT
a.id,
a.name,
a.stage_name,
c.name as country
FROM artist a
LEFT JOIN country c ON a.country_id = c.id;

# Album with artist information

SELECT
al.id,
al.title,
a.name as artist_name
FROM album al
JOIN artist a ON al.artist_id = a.id;

# Song with artist and album information

SELECT
s.id,
s.title,
a.name as artist_name,
al.title as album_title,
g.name as genre
FROM song s
LEFT JOIN artist a ON s.artist_id = a.id
LEFT JOIN album al ON s.album_id = al.id
LEFT JOIN genre g ON s.genre_id = g.id;

# ============================================================================

# 8. BACKUP & RESTORE

# ============================================================================

# Backup database to file

mysqldump -u root -p bai_2 > backup.sql

# Backup all databases

mysqldump -u root -p --all-databases > full_backup.sql

# Restore database from backup

mysql -u root -p bai_2 < backup.sql

# Backup with specific options

mysqldump -u root -p --no-create-info bai_2 > data_only.sql

# Backup table structure only

mysqldump -u root -p --no-data bai_2 > structure_only.sql

# ============================================================================

# 9. MAINTENANCE

# ============================================================================

# Check table status

CHECK TABLE artist;

# Repair table if corrupted

REPAIR TABLE artist;

# Optimize table

OPTIMIZE TABLE artist;

# Analyze table

ANALYZE TABLE artist;

# Show index information

SHOW INDEX FROM artist;

# Create index

CREATE INDEX idx_artist_name ON artist(name);
CREATE INDEX idx_album_artist ON album(artist_id);
CREATE INDEX idx_song_artist ON song(artist_id);

# Drop index

DROP INDEX idx_artist_name ON artist;

# View slow query log (if enabled)

SHOW VARIABLES LIKE 'slow_query_log%';

# ============================================================================

# 10. PERFORMANCE & MONITORING

# ============================================================================

# Show server variables

SHOW VARIABLES LIKE '%max_connections%';

# Show current connections

SHOW PROCESSLIST;

# Show variable values

SHOW VARIABLES LIKE '%timezone%';
SHOW VARIABLES LIKE '%character%';
SHOW VARIABLES LIKE '%collation%';

# Check database charset

SELECT DEFAULT_CHARACTER_SET_NAME, DEFAULT_COLLATION_NAME
FROM information_schema.SCHEMATA
WHERE SCHEMA_NAME = 'bai_2';

# Show table status

SHOW TABLE STATUS FROM bai_2;

# Query execution time

SET SESSION sql_mode='';
FLUSH QUERY CACHE;
SELECT \* FROM artist;

# ============================================================================

# 11. DATA MANIPULATION (CAREFUL!)

# ============================================================================

# Insert sample data

INSERT INTO country (name, code) VALUES ('Vietnam', 'VN');

# Update data

UPDATE artist SET stage_name = 'New Name' WHERE id = 1;

# Delete data (careful!)

DELETE FROM artist WHERE id = 1;

# Truncate table (remove all data, reset auto_increment)

TRUNCATE TABLE artist;

# ============================================================================

# 12. TRANSACTION OPERATIONS

# ============================================================================

# Start transaction

START TRANSACTION;

# Commit (save changes)

COMMIT;

# Rollback (undo changes)

ROLLBACK;

# Example transaction:

START TRANSACTION;
UPDATE artist SET name = 'New Name' WHERE id = 1;
UPDATE album SET title = 'New Title' WHERE artist_id = 1;
COMMIT;

# ============================================================================

# 13. USEFUL QUERIES FOR APPLICATION DEBUGGING

# ============================================================================

# Get data for home page (random artist)

SELECT \* FROM artist ORDER BY RAND() LIMIT 1;

# Get featured artists

SELECT \* FROM artist LIMIT 5;

# Get artist details with stats

SELECT
a.id,
a.name,
COUNT(DISTINCT al.id) as album_count,
COUNT(DISTINCT s.id) as song_count
FROM artist a
LEFT JOIN album al ON a.id = al.artist_id
LEFT JOIN song s ON al.id = s.album_id
GROUP BY a.id
ORDER BY song_count DESC;

# Most popular artists

SELECT a.\*, COUNT(s.id) as song_count
FROM artist a
LEFT JOIN song s ON a.id = s.artist_id
GROUP BY a.id
ORDER BY song_count DESC
LIMIT 10;

# Recently added songs

SELECT \* FROM song ORDER BY created_at DESC LIMIT 10;

# Search results simulation

SELECT \* FROM artist WHERE name LIKE '%search%' OR stage_name LIKE '%search%';

# ============================================================================

# 14. USEFUL MYSQL COMMAND LINE OPTIONS

# ============================================================================

# Verbose output

mysql -u root -p --verbose

# Show warnings

mysql -u root -p --show-warnings

# Execute query directly

mysql -u root -p -e "SELECT \* FROM artist LIMIT 5;"

# Execute from file

mysql -u root -p < script.sql

# Pipe output to file

mysql -u root -p -e "SELECT \* FROM artist;" > output.txt

# Tab-separated output

mysql -u root -p --batch < query.sql

# HTML output

mysql -u root -p --html < query.sql

# ============================================================================

# 15. TROUBLESHOOTING QUERIES

# ============================================================================

# Check if Flyway migrations ran

SELECT \* FROM flyway_schema_history;

# View character encoding

SHOW VARIABLES LIKE '%character%';

# View current timezone

SELECT @@global.time_zone, @@session.time_zone;

# Check auto_increment values

SELECT TABLE_NAME, AUTO_INCREMENT
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'bai_2';

# View foreign key constraints

SELECT CONSTRAINT_NAME, TABLE_NAME, COLUMN_NAME, REFERENCED_TABLE_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'bai_2' AND REFERENCED_TABLE_NAME IS NOT NULL;

# ============================================================================

# Note: Replace username 'root' with your actual MySQL user

# Note: Replace password when prompted

# Note: Replace 'bai_2' with your database name if different

# ============================================================================
