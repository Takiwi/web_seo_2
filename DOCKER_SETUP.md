# 🐳 Music Hub - Docker Setup Guide

## 📋 Yêu Cầu

- Docker Desktop (hoặc Docker Engine)
- Docker Compose
- 2GB RAM tối thiểu

## 🚀 Quick Start

### 1. Build và Chạy

```bash
# Build image
docker-compose build

# Chạy containers
docker-compose up -d
```

### 2. Kiểm Tra Status

```bash
# Xem logs
docker-compose logs -f

# Kiểm tra containers
docker-compose ps

# Kiểm tra MySQL
docker exec music-hub-db mysql -u root -proot_password -e "SELECT 1;"
```

### 3. Truy Cập Ứng Dụng

```
🌐 http://localhost:8080/home
```

## 📦 Services

### MySQL (music-hub-db)

- **Port:** 3306
- **Database:** bai_2
- **Root User:** root / root_password
- **App User:** app_user / app_password
- **Volume:** mysql_data (persistent storage)

### Spring Boot App (music-hub-app)

- **Port:** 8080
- **Upload Dir:** /app/upload
- **Health Check:** Enabled (checks /home endpoint)
- **Auto-restart:** Yes (unless manually stopped)

## 🔧 Common Commands

### Start/Stop

```bash
# Start containers
docker-compose up -d

# Stop containers
docker-compose down

# Stop and remove volumes
docker-compose down -v

# Restart services
docker-compose restart

# Restart specific service
docker-compose restart app
```

### Logs & Debugging

```bash
# View all logs
docker-compose logs -f

# View app logs only
docker-compose logs -f app

# View MySQL logs
docker-compose logs -f mysql

# Check container details
docker inspect music-hub-app
```

### Database Operations

```bash
# Connect to MySQL
docker exec -it music-hub-db mysql -u app_user -papp_password bai_2

# Backup database
docker exec music-hub-db mysqldump -u root -proot_password bai_2 > backup.sql

# Restore database
docker exec -i music-hub-db mysql -u root -proot_password bai_2 < backup.sql
```

### Upload Files

```bash
# View uploaded files
docker exec -it music-hub-app ls -la /app/upload/

# Copy file from container
docker cp music-hub-app:/app/upload/. ./upload/

# Copy file to container
docker cp ./local/file.jpg music-hub-app:/app/upload/artist/1/
```

## 🌍 Production Deployment

### For Production (Railway, Render, etc.)

1. **Update `docker-compose.yml`:**

   ```yaml
   app:
     environment:
       APP_SEO_SITEURL: https://yourdomain.com
       SPRING_DATASOURCE_URL: jdbc:mysql://mysql-host:3306/bai_2
   ```

2. **Set Strong Passwords:**

   ```yaml
   mysql:
     environment:
       MYSQL_ROOT_PASSWORD: ${DB_ROOT_PASSWORD}
       MYSQL_PASSWORD: ${DB_APP_PASSWORD}
   ```

3. **Use Environment Variables:**
   ```bash
   docker-compose up -d
   ```

## 🐛 Troubleshooting

### Port Already in Use

```bash
# Find and stop container using port
lsof -i :8080
kill -9 <PID>

# Or change port in docker-compose.yml
ports:
  - "8081:8080"  # Use 8081 instead
```

### Database Connection Failed

```bash
# Check MySQL container is running
docker ps | grep mysql

# Check MySQL is healthy
docker-compose ps mysql

# View MySQL logs
docker-compose logs mysql

# Restart MySQL
docker-compose restart mysql
```

### Out of Disk Space

```bash
# Clean up unused images/containers
docker system prune -a

# Remove old volumes
docker volume prune
```

### Permission Issues on Linux

```bash
# Add current user to docker group
sudo usermod -aG docker $USER

# Apply new group
newgrp docker
```

## 📊 Monitoring

### View Resource Usage

```bash
# CPU, Memory, Network
docker stats

# Continuous monitoring
docker stats --no-stream=false
```

### Container Health

```bash
# Check health status
docker inspect --format='{{.State.Health.Status}}' music-hub-app

# View health check details
docker inspect --format='{{.State.Health}}' music-hub-app
```

## 🔐 Security Notes

1. **Change Passwords:** Update MySQL passwords in production
2. **Use .env File:** Store secrets in `.env` (add to .gitignore)
3. **SSL/TLS:** Use reverse proxy (Nginx) for HTTPS
4. **Firewall:** Only expose necessary ports

## 📝 Example .env File

Create `.env` file:

```env
MYSQL_ROOT_PASSWORD=your_strong_root_password
MYSQL_PASSWORD=your_strong_app_password
DB_USER=app_user
APP_SEO_SITEURL=https://yourdomain.com
```

Use in docker-compose.yml:

```yaml
environment:
  MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
  MYSQL_PASSWORD: ${MYSQL_PASSWORD}
```

## 🎯 Next Steps

1. Customize environment variables
2. Mount volumes for persistent uploads
3. Setup nginx reverse proxy for production
4. Configure SSL certificates
5. Setup automated backups

---

**Need Help?**

- Check logs: `docker-compose logs -f`
- View status: `docker-compose ps`
- Rebuild: `docker-compose build --no-cache`
