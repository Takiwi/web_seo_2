# 🗄️ MySQL Migration Guide - Music Hub

## 📋 Thay Đổi Đã Thực Hiện

Các file sau đã được cập nhật để sử dụng MySQL:

### ✅ **pom.xml**
```xml
<!-- Replaced SQL Server driver -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Replaced Flyway dialect -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-mysql</artifactId>
</dependency>
```

### ✅ **application.properties**
```properties
# Datasource Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/bai_2?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true
```

---

## 🚀 Quick Start Setup

### Step 1: Cài Đặt MySQL Server

**Windows:**
- Download MySQL từ https://dev.mysql.com/downloads/mysql/
- Chạy installer
- Chọn Server mặc định
- Port mặc định: 3306

**macOS:**
```bash
brew install mysql
brew services start mysql
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt-get install mysql-server
sudo systemctl start mysql
```

### Step 2: Tạo Database

**Option 1: Dùng mysql_setup.sql**
```bash
mysql -u root -p < src/main/resources/db/mysql_setup.sql
```

**Option 2: Manual (MySQL Workbench hoặc Command Line)**
```sql
CREATE DATABASE IF NOT EXISTS bai_2 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;
```

### Step 3: Xác Nhận Kết Nối

```bash
mysql -u root -p bai_2
```

Nếu thành công, bạn sẽ thấy:
```
Welcome to MySQL monitor...
mysql>
```

### Step 4: Chạy Ứng Dụng

```bash
mvn clean install
mvn spring-boot:run
```

---

## 🔧 Configuration Details

### Database URL Breakdown

```
jdbc:mysql://localhost:3306/bai_2?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
│         │          │    │        │             │                     │
│         │          │    │        │             │                     └─ Allow public key auth
│         │          │    │        │             └─ Set timezone (important!)
│         │          │    │        └─ Disable SSL for localhost
│         │          │    └─ Database name
│         │          └─ Default MySQL port
│         └─ localhost (your machine)
└─ MySQL JDBC URL protocol
```

### Default Connection Settings

| Property | Value | Notes |
|----------|-------|-------|
| Host | localhost | Change if MySQL on different server |
| Port | 3306 | Default MySQL port |
| Database | bai_2 | Must be created first |
| Username | root | Can change if you create a user |
| Password | (empty) | Leave empty for root without password |
| SSL | Disabled | For local development only |
| Timezone | UTC | Required for proper timestamp handling |

---

## 📝 Using Custom Credentials

Nếu bạn muốn sử dụng user khác thay vì root:

### Step 1: Tạo User trong MySQL

```sql
CREATE USER 'musicuser'@'localhost' IDENTIFIED BY 'secure_password_123';
GRANT ALL PRIVILEGES ON bai_2.* TO 'musicuser'@'localhost';
FLUSH PRIVILEGES;
```

### Step 2: Cập Nhật application.properties

```properties
spring.datasource.username=musicuser
spring.datasource.password=secure_password_123
```

---

## 🔄 Data Migration từ SQL Server

Nếu bạn có data cũ trong SQL Server:

### Option 1: Export từ SQL Server → Import vào MySQL

**Bước 1: Backup SQL Server**
```bash
sqlcmd -S localhost -U sa -P 123 -Q "BACKUP DATABASE Bai_2 TO DISK='C:\Backup\bai_2.bak'"
```

**Bước 2: Chuyển đổi Schema**
- Dùng tools như Navicat hoặc MySQL Workbench
- Export từ SQL Server
- Import vào MySQL

**Bước 3: Đảm bảo Compatibility**
- Check data types (SQL Server `INT` → MySQL `INT`)
- Check collations (UTF8 compatibility)
- Xóa Flyway history và chạy migration từ đầu

### Option 2: Tạo Database Mới (Recommended)
Nếu đây là development, tạo database sạch:
```bash
mvn spring-boot:run
```
Hibernate sẽ tự động tạo tables từ entities.

---

## 📊 Hibernate Dialect Configuration

MySQL 8.0+ được hỗ trợ tốt nhất:

```properties
# MySQL 8.0+ (Recommended)
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# MySQL 5.7
# spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
```

---

## ✅ Checklist Sau Migration

- [ ] Cài MySQL Server
- [ ] Tạo database `bai_2`
- [ ] Maven clean install (downloads new dependencies)
- [ ] Chạy ứng dụng lần đầu
- [ ] Kiểm tra console không có connection errors
- [ ] Xác nhận tables được tạo trong MySQL
- [ ] Test CRUD operations
- [ ] Test login/register
- [ ] Test search functionality
- [ ] Test image upload

---

## 🆘 Troubleshooting

### Error: "No suitable driver found for jdbc:mysql"
**Solution:** Đảm bảo `pom.xml` có MySQL connector:
```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>
```
Chạy: `mvn clean install`

### Error: "Access denied for user 'root'@'localhost'"
**Solution:** 
1. Kiểm tra password trong `application.properties`
2. Reset password MySQL nếu quên:
   ```bash
   mysql -u root
   ALTER USER 'root'@'localhost' IDENTIFIED BY '';
   FLUSH PRIVILEGES;
   ```

### Error: "Unknown database 'bai_2'"
**Solution:** Tạo database trước:
```bash
mysql -u root -p
CREATE DATABASE bai_2;
```

### Error: "The server time zone value 'UTC' is unrecognized"
**Solution:** Đã fix trong URL (serverTimezone=UTC), nhưng có thể cần:
```bash
# Windows
mysql_tzinfo_to_sql %WINDIR%\System32\drivers\etc\hosts | mysql -u root -p mysql

# Linux/Mac
mysql_tzinfo_to_sql /etc/hosts | mysql -u root -p mysql
```

### Slow Connection/Performance
**Solution:** Thêm connection pool settings:
```properties
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
```

---

## 🚀 Performance Tips

### 1. Database Optimization
```sql
-- Create indexes for frequently queried fields
CREATE INDEX idx_artist_slug ON artist(slug);
CREATE INDEX idx_artist_name ON artist(name);
CREATE INDEX idx_album_slug ON album(slug);
CREATE INDEX idx_song_slug ON song(slug);
```

### 2. Connection Pooling (Already Configured)
```properties
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
```

### 3. JPA Configuration
```properties
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.use_sql_comments=true
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true
```

---

## 📚 Useful MySQL Commands

```bash
# Login
mysql -u root -p

# List databases
SHOW DATABASES;

# Select database
USE bai_2;

# List tables
SHOW TABLES;

# Describe table structure
DESCRIBE artist;

# Show table row count
SELECT COUNT(*) FROM artist;

# Backup
mysqldump -u root -p bai_2 > backup.sql

# Restore
mysql -u root -p bai_2 < backup.sql

# Check MySQL version
SELECT VERSION();

# Show current user
SELECT USER();
```

---

## 🔐 Security Notes

### Development (Localhost)
```properties
# Current secure settings for localhost
spring.datasource.url=jdbc:mysql://localhost:3306/bai_2?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
```

### Production (Remote Server)
```properties
# Change to use SSL
spring.datasource.url=jdbc:mysql://prod-server:3306/bai_2?useSSL=true&serverTimezone=UTC
# Use environment variables for credentials
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

---

## 📈 Comparison: SQL Server vs MySQL

| Feature | SQL Server | MySQL |
|---------|-----------|-------|
| License | Paid/Express | Open Source |
| Performance | Enterprise-grade | Great for web |
| Ease of setup | Complex | Very simple |
| Community | Large | Very large |
| Learning curve | Steep | Gentle |
| Cost | High | Free |
| Scalability | Excellent | Good |
| Replication | Built-in | Available |

---

## ✨ Next Steps

1. ✅ Update pom.xml - **DONE**
2. ✅ Update application.properties - **DONE**
3. ⏳ **YOU:** Install MySQL Server
4. ⏳ **YOU:** Create database using mysql_setup.sql
5. ⏳ **YOU:** Run `mvn clean install`
6. ⏳ **YOU:** Test the application

---

## 📞 Additional Resources

- **MySQL Official:** https://dev.mysql.com/
- **MySQL Workbench:** https://dev.mysql.com/downloads/workbench/
- **Hibernate MySQL:** https://hibernate.org/
- **Spring Data JPA:** https://spring.io/projects/spring-data-jpa

---

**Status:** ✅ Code Migration Complete | ⏳ Database Setup Required

*Last Updated: December 10, 2025*
