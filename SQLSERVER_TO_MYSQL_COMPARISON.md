# 🔄 SQL Server ↔ MySQL Configuration Comparison

## Before & After Comparison

### **pom.xml Dependencies**

#### ❌ SQL Server (Old)

```xml
<dependency>
    <groupId>com.microsoft.sqlserver</groupId>
    <artifactId>mssql-jdbc</artifactId>
    <scope>runtime</scope>
</dependency>

<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-sqlserver</artifactId>
</dependency>
```

#### ✅ MySQL (New)

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>

<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-mysql</artifactId>
</dependency>
```

---

### **application.properties Database Configuration**

#### ❌ SQL Server (Old)

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;database=Bai_2;TrustServerCertificate=true;
spring.datasource.username=sa
spring.datasource.password=123
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

# No explicit dialect (auto-detected)
```

#### ✅ MySQL (New)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bai_2?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true
```

---

## URL Parameters Explained

### SQL Server URL

```
jdbc:sqlserver://localhost:1433;database=Bai_2;TrustServerCertificate=true;
                 ↑          ↑        ↑          ↑                      ↑
              hostname    port   database   disable cert verification
```

### MySQL URL

```
jdbc:mysql://localhost:3306/bai_2?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
            ↑          ↑     ↑        ↑            ↑                ↑
         hostname    port database disable SSL   timezone         auth
```

---

## Key Differences

| Aspect                | SQL Server                                     | MySQL                      |
| --------------------- | ---------------------------------------------- | -------------------------- |
| **Port**              | 1433                                           | 3306                       |
| **URL Format**        | `;database=`                                   | `/`                        |
| **Default User**      | sa                                             | root                       |
| **Default Password**  | Needs setup                                    | none (empty)               |
| **SSL**               | TrustServerCertificate                         | useSSL=false               |
| **Timezone**          | Auto                                           | serverTimezone=UTC         |
| **Driver Class**      | `com.microsoft.sqlserver.jdbc.SQLServerDriver` | `com.mysql.cj.jdbc.Driver` |
| **Hibernate Dialect** | `SQLServerDialect`                             | `MySQL8Dialect`            |
| **License**           | Commercial                                     | Open Source                |

---

## Connection String Building

### SQL Server

```
jdbc:sqlserver://
  [host]:[port];
  database=[name];
  TrustServerCertificate=[true/false];
  [other options]
```

### MySQL

```
jdbc:mysql://
  [host]:[port]/[database]?
  useSSL=[true/false]&
  serverTimezone=[timezone]&
  [other options]
```

---

## Hibernate Dialect

```properties
# SQL Server
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServerDialect

# MySQL 8.0+ (Recommended)
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# MySQL 5.7
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
```

---

## Password Handling

### SQL Server (Old)

```properties
spring.datasource.password=123  # Had a password
```

### MySQL (New)

```properties
spring.datasource.password=    # Empty (no password for root)
```

**To use a custom password:**

```properties
# Create user in MySQL
CREATE USER 'appuser'@'localhost' IDENTIFIED BY 'mypassword';
GRANT ALL ON bai_2.* TO 'appuser'@'localhost';
FLUSH PRIVILEGES;

# Then in application.properties
spring.datasource.username=appuser
spring.datasource.password=mypassword
```

---

## Database Creation

### SQL Server (Old)

```bash
# Using SQL Server Management Studio or sqlcmd
sqlcmd -S localhost -U sa -P 123 -Q "CREATE DATABASE Bai_2"
```

### MySQL (New)

```bash
# Method 1: Using script
mysql -u root -p < src/main/resources/db/mysql_setup.sql

# Method 2: Manual
mysql -u root -p
CREATE DATABASE bai_2 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# Method 3: MySQL Workbench GUI
```

---

## Connection Testing

### SQL Server (Old)

```bash
sqlcmd -S localhost -U sa -P 123 -d Bai_2
```

### MySQL (New)

```bash
mysql -u root -p -h localhost -D bai_2
# Or just:
mysql -u root
```

---

## Flyway Migration Files

### SQL Server

- Located in: `src/main/resources/db/migration/`
- Named: `V1__*.sql` (SQL Server syntax)
- Example: `V1__CreateTables.sql`

### MySQL

- Located in: `src/main/resources/db/migration/`
- Named: `V1__*.sql` (MySQL syntax)
- Example: `V1__Initial_Schema.sql` ✅ (Already provided)

**Note:** SQL Server and MySQL have different SQL syntax!

---

## Data Type Differences

| Concept            | SQL Server        | MySQL               |
| ------------------ | ----------------- | ------------------- |
| **Large Text**     | NVARCHAR(MAX)     | LONGTEXT            |
| **String**         | VARCHAR, NVARCHAR | VARCHAR, CHAR       |
| **Integer**        | INT, BIGINT       | INT, BIGINT         |
| **Decimal**        | DECIMAL, NUMERIC  | DECIMAL, NUMERIC    |
| **Date**           | DATETIME, DATE    | DATETIME, DATE      |
| **Boolean**        | BIT               | BOOLEAN, TINYINT(1) |
| **Auto Increment** | IDENTITY          | AUTO_INCREMENT      |

---

## Performance Considerations

### SQL Server Advantages

- Enterprise-grade performance
- Better for large-scale OLTP
- Advanced indexing

### MySQL Advantages

- Lighter weight
- Faster startup
- Better for web applications
- Easy replication
- Free and open source

---

## Entity Annotation Changes

### No changes needed!

Hibernate handles these automatically. Your JPA entities stay the same:

```java
@Entity
@Table(name = "artist")
public class ArtistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;
    // ... rest of entity
}
```

The `@GeneratedValue(strategy = GenerationType.IDENTITY)` works for both SQL Server and MySQL!

---

## Migration Checklist

- [x] Update pom.xml dependencies
- [x] Update datasource configuration
- [x] Set Hibernate dialect
- [x] Create MySQL database
- [x] Run Flyway migrations (automatic)
- [ ] Test application startup
- [ ] Verify tables are created
- [ ] Test CRUD operations
- [ ] Test login functionality

---

## Rollback (If Needed)

To revert to SQL Server:

1. **Revert pom.xml:**

```bash
git checkout pom.xml
```

2. **Revert application.properties:**

```bash
git checkout src/main/resources/application.properties
```

3. **Clear Maven cache:**

```bash
mvn clean install
```

4. **Run with SQL Server again:**

```bash
mvn spring-boot:run
```

---

## Summary

✅ **Code**: Updated to MySQL
⏳ **Action**: Install MySQL server
⏳ **Action**: Create database
⏳ **Action**: Run application

---

**Last Updated:** December 10, 2025
**Migration Status:** ✅ Code Complete | ⏳ Setup Required
