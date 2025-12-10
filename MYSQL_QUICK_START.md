# 🚀 MySQL Migration Quick Start

## ⚡ 5 Minutes Setup

### 1️⃣ **Code Changes** ✅ (Already Done)

```
✅ pom.xml - Dependency updated
✅ application.properties - Config updated
✅ Hibernate dialect - Set to MySQL8Dialect
```

### 2️⃣ **Install MySQL** (5 minutes)

**Windows:**

- Download: https://dev.mysql.com/downloads/mysql/
- Run installer
- Default settings (port 3306)

**macOS:**

```bash
brew install mysql
brew services start mysql
```

**Linux:**

```bash
sudo apt-get install mysql-server
sudo systemctl start mysql
```

### 3️⃣ **Create Database** (1 minute)

**Option A: Command Line**

```bash
mysql -u root -p < src/main/resources/db/mysql_setup.sql
```

**Option B: MySQL Workbench**

1. Open MySQL Workbench
2. New SQL Tab
3. Copy & paste content from `mysql_setup.sql`
4. Execute (Ctrl+Enter)

**Option C: Manual**

```bash
mysql -u root -p
CREATE DATABASE bai_2 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 4️⃣ **Run Application** (1 minute)

```bash
# Navigate to project
cd d:\Workspace\Study\SelfStudy\code_for_FUN\Learn_SpringBoot\web_seo

# Clean build
mvn clean install

# Run
mvn spring-boot:run
```

### 5️⃣ **Verify Setup** (1 minute)

✅ Check console - no `Connection refused` errors
✅ Open http://localhost:8080/home
✅ No database errors in console

---

## 🎯 Common Issues & Quick Fixes

| Issue                | Fix                                               |
| -------------------- | ------------------------------------------------- |
| "Connection refused" | Ensure MySQL is running: `mysql.server status`    |
| "Access denied"      | Check username/password in application.properties |
| "Unknown database"   | Run mysql_setup.sql to create database            |
| "Driver not found"   | Run `mvn clean install` again                     |
| "Unknown time zone"  | Already fixed in connection URL                   |

---

## 📊 Verify Database

```bash
# Connect to MySQL
mysql -u root -p bai_2

# Show tables
SHOW TABLES;

# Check artist table
DESC artist;

# See row count
SELECT COUNT(*) as total_tables FROM information_schema.tables
WHERE table_schema = 'bai_2';
```

---

## ✨ You're Done!

Your application now uses MySQL instead of SQL Server! 🎉

---

## 📚 More Info

- Full guide: `MYSQL_MIGRATION_GUIDE.md`
- Troubleshooting: `MYSQL_MIGRATION_GUIDE.md#troubleshooting`
- Performance tips: `MYSQL_MIGRATION_GUIDE.md#performance-tips`

---

**Last Updated:** December 10, 2025
