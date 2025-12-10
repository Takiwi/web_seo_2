# 🔧 MySQL Migration Troubleshooting Guide

## 🚨 Common Errors & Solutions

### 1. Connection Errors

#### ❌ Error: "Connection refused"

```
java.sql.SQLException: Cannot create a PoolingDriver
ERROR 1045 (28000): Access denied for user 'root'@'localhost'
```

**Cause:** MySQL server is not running

**Solutions:**

```bash
# Windows
mysql.server start

# macOS
brew services start mysql

# Linux
sudo systemctl start mysql

# Verify it's running
mysql --version
```

---

#### ❌ Error: "Access denied for user 'root'@'localhost'"

```
java.sql.SQLException: HikariPool-1 - Connection is not available
```

**Cause:** Wrong password or user doesn't exist

**Solutions:**

```bash
# Check if you can login manually
mysql -u root -p

# If you forgot the password, reset it (Windows)
mysql -u root

# If you need to create user
mysql -u root -p
CREATE USER 'root'@'localhost' IDENTIFIED BY '';
```

---

#### ❌ Error: "Unknown database 'bai_2'"

```
java.sql.SQLException: Unknown database 'bai_2'
```

**Cause:** Database hasn't been created yet

**Solutions:**

```bash
# Create database
mysql -u root -p < src/main/resources/db/mysql_setup.sql

# Or manually
mysql -u root -p
CREATE DATABASE bai_2 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

---

### 2. Driver & Dependency Errors

#### ❌ Error: "No suitable driver found for jdbc:mysql"

```
java.sql.SQLException: No suitable driver found for jdbc:mysql://localhost:3306/bai_2
```

**Cause:** MySQL JDBC driver not in classpath

**Solutions:**

```bash
# Verify pom.xml has the dependency
cat pom.xml | grep -A 3 "mysql-connector"

# Clean install to download dependencies
mvn clean install

# Check if download succeeded
ls ~/.m2/repository/com/mysql/mysql-connector-j/

# If not found, download manually
mvn dependency:resolve
```

**Check pom.xml has:**

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

---

#### ❌ Error: "Failed to initialize pool: Access denied"

```
HikariPool-1 - Failed to initialize pool: Access denied for user 'root'@'localhost' (using password: NO)
```

**Cause:** Application trying to connect without password but MySQL has one

**Solutions:**

```properties
# Option 1: Add password to application.properties
spring.datasource.password=your_password

# Option 2: Remove MySQL password
mysql -u root
ALTER USER 'root'@'localhost' IDENTIFIED BY '';
FLUSH PRIVILEGES;
```

---

### 3. Configuration Errors

#### ❌ Error: "The server time zone value 'UTC' is unrecognized"

```
java.sql.SQLException: The server time zone value 'UTC' is unrecognized
```

**Cause:** MySQL timezone not configured properly

**Solutions:**

```bash
# Check current timezone configuration
mysql -u root -p
SHOW VARIABLES LIKE 'time_zone';

# The connection URL already includes: serverTimezone=UTC
# If still issues, configure MySQL server timezone

# Windows - Check system timezone
# Control Panel > Date & Time > Time Zone

# Linux/macOS
# Set global timezone
mysql -u root -p
SET GLOBAL time_zone = 'UTC';
FLUSH PRIVILEGES;
```

---

#### ❌ Error: "Unexpected end of file while parsing"

```
org.springframework.beans.factory.BeanCreationException
SQL error in SQL statement
```

**Cause:** SQL syntax error in migration file (mixing SQL Server and MySQL syntax)

**Solutions:**

```bash
# Check migration file syntax
cat src/main/resources/db/migration/V1__*.sql

# Common differences:
# SQL Server: IDENTITY(1,1)        → MySQL: AUTO_INCREMENT
# SQL Server: NVARCHAR(MAX)        → MySQL: LONGTEXT
# SQL Server: GETDATE()            → MySQL: CURRENT_TIMESTAMP
# SQL Server: TrustServerCertificate → Not needed in MySQL

# Run migration manually to see exact error
mysql -u root -p bai_2 < src/main/resources/db/migration/V1__*.sql
```

---

### 4. Character Encoding Errors

#### ❌ Error: "Incorrect string value for column"

```
java.sql.SQLException: Incorrect string value: '\xF0\x9F...' for column
```

**Cause:** UTF-8 multibyte characters not supported (emoji, special chars)

**Solutions:**

```bash
# Verify database charset
mysql -u root -p bai_2
SELECT DEFAULT_CHARACTER_SET_NAME FROM information_schema.SCHEMATA
WHERE SCHEMA_NAME = 'bai_2';

# Should be utf8mb4 (not utf8!)

# Fix database charset
ALTER DATABASE bai_2 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# Verify application.properties has correct URL
spring.datasource.url=jdbc:mysql://localhost:3306/bai_2?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true

# No charset parameter needed - driver auto-detects
```

---

### 5. Flyway Migration Errors

#### ❌ Error: "Flyway validation failed"

```
org.flywaydb.core.internal.command.DbValidate.validate()
Migration name mismatch
```

**Cause:** Migration file naming or content changed

**Solutions:**

```bash
# Check migration files
ls -la src/main/resources/db/migration/

# Must follow pattern: V[VERSION]__[DESCRIPTION].sql
# Good: V1__Initial_Schema.sql
# Bad: V01__Initial_Schema.sql (leading zero)

# Check Flyway history
mysql -u root -p bai_2
SELECT * FROM flyway_schema_history;

# If corrupted, reset (WARNING: deletes schema history)
mysql -u root -p bai_2
DROP TABLE flyway_schema_history;

# Then run application again (will re-create history)
mvn spring-boot:run
```

---

#### ❌ Error: "Cannot change database within a transaction"

```
org.flywaydb.core.internal.command.DbMigrate.migrateGroup()
```

**Cause:** Connection issues during migration

**Solutions:**

```bash
# Ensure clean database state
mysql -u root -p
DROP DATABASE bai_2;
CREATE DATABASE bai_2 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# Run application
mvn spring-boot:run
```

---

### 6. Hibernate Errors

#### ❌ Error: "Unknown Hibernate dialect"

```
java.lang.IllegalArgumentException: Unknown Hibernate Dialect
```

**Cause:** Wrong dialect in configuration

**Solutions:**

```properties
# Verify in application.properties
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Not: SQLServerDialect, MySQL5InnoDBDialect (outdated)

# For MySQL 5.7:
# spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
```

---

#### ❌ Error: "Generated identifier does not match a known identifier"

```
org.hibernate.id.IdentifierGenerationException
```

**Cause:** Entity doesn't match database table structure

**Solutions:**

```java
// Verify @GeneratedValue is correct
@Entity
@Table(name = "artist")
public class ArtistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Must be IDENTITY for MySQL
    private Integer id;

    // ... other fields
}
```

---

### 7. Table & Schema Errors

#### ❌ Error: "Table 'bai_2.artist' doesn't exist"

```
java.sql.SQLException: Table 'bai_2.artist' doesn't exist
```

**Cause:** Tables not created (migration didn't run)

**Solutions:**

```bash
# Check if tables exist
mysql -u root -p bai_2
SHOW TABLES;

# If empty, run migration manually
mysql -u root -p bai_2 < src/main/resources/db/migration/V1__Initial_Schema.sql

# Or let Hibernate create them
# Add to application.properties:
spring.jpa.hibernate.ddl-auto=create  # WARNING: Drops existing data!

# Then run application
mvn spring-boot:run
```

---

#### ❌ Error: "Column doesn't exist"

```
java.sql.SQLException: Unknown column 'artist.slug' in 'on clause'
```

**Cause:** Column name mismatch between entity and database

**Solutions:**

```bash
# Check table structure
mysql -u root -p bai_2
DESC artist;

# Verify entity has correct @Column names
@Entity
public class ArtistEntity {
    @Column(name = "slug")  // Must match database
    private String slug;
}

# Drop and recreate tables
DROP TABLE artist;
# Then run application to auto-create
```

---

### 8. Performance Errors

#### ❌ Error: "Out of memory" or "Slow queries"

```
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
```

**Cause:** Too many connections or large data sets

**Solutions:**

```properties
# Increase Java heap (in terminal before running)
export MAVEN_OPTS="-Xmx2048m"

# Configure connection pool
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000

# Enable query logging to find slow queries
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Add database indexes
mysql -u root -p bai_2
CREATE INDEX idx_artist_slug ON artist(slug);
CREATE INDEX idx_album_artist ON album(artist_id);
```

---

### 9. Authentication & Permission Errors

#### ❌ Error: "Access denied for user 'musicuser'"

```
HikariPool-1 - Connection is not available, request timed out after 30000ms.
```

**Cause:** User doesn't have required permissions

**Solutions:**

```bash
# Grant all privileges
mysql -u root -p
GRANT ALL PRIVILEGES ON bai_2.* TO 'musicuser'@'localhost';
FLUSH PRIVILEGES;

# Or grant specific permissions
GRANT SELECT, INSERT, UPDATE, DELETE ON bai_2.* TO 'musicuser'@'localhost';
FLUSH PRIVILEGES;

# Verify permissions
SHOW GRANTS FOR 'musicuser'@'localhost';
```

---

### 10. Quick Diagnostics Script

```bash
#!/bin/bash
# Run this to diagnose common issues

echo "=== MySQL Diagnostics ==="

# Check if MySQL is running
echo "1. Checking MySQL server..."
mysql --version
mysql -u root -p -e "SELECT 'MySQL is running';" 2>/dev/null || echo "❌ MySQL not running"

# Check database exists
echo -e "\n2. Checking database..."
mysql -u root -p -e "USE bai_2; SELECT COUNT(*) as tables FROM information_schema.tables WHERE table_schema='bai_2';" 2>/dev/null

# Check tables
echo -e "\n3. Checking tables..."
mysql -u root -p -e "USE bai_2; SHOW TABLES;" 2>/dev/null

# Check charset
echo -e "\n4. Checking charset..."
mysql -u root -p -e "SELECT DEFAULT_CHARACTER_SET_NAME FROM information_schema.SCHEMATA WHERE SCHEMA_NAME='bai_2';" 2>/dev/null

# Check timezone
echo -e "\n5. Checking timezone..."
mysql -u root -p -e "SELECT @@global.time_zone, @@session.time_zone;" 2>/dev/null

echo -e "\n=== If all tests pass, run: mvn spring-boot:run ==="
```

---

## 📞 Still Having Issues?

1. **Check the logs:**

   - Spring console output
   - MySQL error log
   - Flyway logs

2. **Useful MySQL logs:**

   ```bash
   # Linux
   tail -f /var/log/mysql/error.log

   # Windows (find MySQL installation)
   # Usually: C:\Program Files\MySQL\MySQL Server 8.0\data\
   ```

3. **Enable debug logging:**

   ```properties
   logging.level.org.springframework.jdbc=DEBUG
   logging.level.org.hibernate.SQL=DEBUG
   ```

4. **Community support:**
   - Stack Overflow: Tag `mysql`, `spring-boot`
   - MySQL Forums: https://forums.mysql.com/
   - Spring Community: https://spring.io/support

---

## ✅ Success Indicators

When everything is working:

- ✅ No red errors in console (warnings OK)
- ✅ "Spring has fully started" message
- ✅ Tables appear in database
- ✅ Can access http://localhost:8080/home
- ✅ No database errors in browser
- ✅ Can login/register users
- ✅ Can create/read artists, albums, songs

---

**Last Updated:** December 10, 2025
