# PixelStack Setup Checklist

Quick setup guide to get PixelStack up and running.

## ✅ Prerequisites Verification

Run these commands to verify your system is ready:

```bash
# Check Java (Required: JDK 1.8+)
java -version

# Check Maven (Required: 3.6+)
mvn -version

# Check Node.js (Required: 18+)
node --version

# Check npm
npm --version

# Check MySQL (Required: 8.0+)
mysql --version

# Check Redis (Required: 6.0+)
redis-cli --version

# Verify Redis is running
redis-cli ping
# Should return: PONG
```

## 📋 Setup Steps

### Step 1: Database Setup

```bash
# 1. Start MySQL service
brew services start mysql  # macOS
# OR
sudo systemctl start mysql  # Linux

# 2. Create database
mysql -u root -p
```

In MySQL shell:
```sql
CREATE DATABASE ims CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
exit
```

```bash
# 3. Import schema
mysql -u root -p ims < pixel-backend/sql/ims.sql
```

### Step 2: Configure Backend

Edit `pixel-backend/src/main/resources/application-dev.yml`:

```yaml
spring:
  datasource:
    username: root
    password: YOUR_MYSQL_PASSWORD  # ⚠️ Change this!
```

### Step 3: Create Upload Directory

```bash
# Option 1: System directory (requires sudo)
sudo mkdir -p /data/ims/upload/dev
sudo chown -R $USER /data/ims

# Option 2: User directory (recommended for development)
mkdir -p ~/ims-uploads
```

If using Option 2, update `pixel-backend/src/main/resources/application-dev.yml`:
```yaml
file:
  upload:
    path: /Users/YOUR_USERNAME/ims-uploads/  # ⚠️ Use your actual path
```

### Step 4: Start Services

#### Easy Method (Using Scripts)

```bash
# Make scripts executable (first time only)
chmod +x *.sh

# Start both backend and frontend
./start-all.sh

# Or start individually:
./start-backend.sh   # Backend only
./start-frontend.sh  # Frontend only
```

#### Manual Method

**Terminal 1 - Backend:**
```bash
cd pixel-backend
mvn spring-boot:run
```

Wait for message: `Started ImsBackendApplication in X seconds`

**Terminal 2 - Frontend:**
```bash
cd pixel-front
npm run dev
```

### Step 5: Access the Application

1. **Frontend**: http://localhost:5173
2. **Backend API**: http://localhost:8080/api
3. **API Health Check**: http://localhost:8080/api/actuator/health (if enabled)

### Step 6: Create Your First User

1. Navigate to http://localhost:5173
2. Click "Register" or "Sign Up"
3. Fill in registration form:
   - Username
   - Email
   - Password
4. Login with your credentials
5. Start uploading images!

## 🔧 Troubleshooting

### Backend won't start

**Problem**: Port 8080 already in use
```bash
# Find process using port 8080
lsof -i :8080

# Kill the process (replace PID)
kill -9 <PID>
```

**Problem**: Database connection failed
- Verify MySQL is running: `mysql -u root -p`
- Check credentials in `application-dev.yml`
- Ensure database `ims` exists: `SHOW DATABASES;`

**Problem**: Redis connection failed
```bash
# Start Redis
brew services start redis  # macOS
redis-server              # Manual start
```

### Frontend won't start

**Problem**: Port 5173 already in use
```bash
# Find and kill process
lsof -i :5173
kill -9 <PID>
```

**Problem**: Dependencies error
```bash
cd pixel-front
rm -rf node_modules package-lock.json
npm install
```

**Problem**: Build errors
```bash
# Clear cache and rebuild
npm cache clean --force
rm -rf node_modules dist
npm install
npm run dev
```

### File Upload Issues

**Problem**: Cannot upload files

1. Check upload directory exists:
   ```bash
   ls -la /data/ims/upload/dev
   # OR
   ls -la ~/ims-uploads
   ```

2. Check permissions:
   ```bash
   # System directory
   sudo chown -R $USER /data/ims

   # User directory
   chmod -R 755 ~/ims-uploads
   ```

3. Verify configuration in `application-dev.yml` matches your directory

## 🚀 Quick Commands Reference

### Backend
```bash
cd pixel-backend

# Build without tests
mvn clean package -DskipTests

# Run tests
mvn test

# Clean build directory
mvn clean

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### Frontend
```bash
cd pixel-front

# Development server
npm run dev

# Production build
npm run build

# Type check
npm run type-check

# Lint code
npm run lint

# Format code
npm run format
```

### Database
```bash
# Backup database
mysqldump -u root -p ims > backup_$(date +%Y%m%d).sql

# Restore database
mysql -u root -p ims < backup_20241009.sql

# Connect to database
mysql -u root -p ims
```

### Services
```bash
# MySQL
brew services start mysql   # Start
brew services stop mysql    # Stop
brew services restart mysql # Restart

# Redis
brew services start redis   # Start
brew services stop redis    # Stop
redis-cli                   # Connect to Redis
redis-cli FLUSHALL         # Clear all Redis data
```

## 📁 Important Files

- `README.md` - Main project documentation
- `pixel-backend/README.md` - Backend-specific docs
- `pixel-backend/CLAUDE.md` - Development notes and commands
- `pixel-backend/src/main/resources/application.yml` - Main config
- `pixel-backend/src/main/resources/application-dev.yml` - Dev config
- `pixel-front/.env.development` - Frontend dev environment
- `pixel-front/QUICK_START.md` - Frontend quick start guide

## 🎯 Next Steps

After successful setup:

1. ✅ Register a new user account
2. ✅ Login to the application
3. ✅ Upload your first image
4. ✅ Create an album
5. ✅ Organize images with categories and tags
6. ✅ Star your favorite images
7. 📖 Read the API documentation in main README.md
8. 🛠️ Start developing new features!

## 💡 Tips

- Use the startup scripts for easier development
- Keep Redis and MySQL running in the background
- Check logs if something goes wrong
- Backend logs: Console output from Spring Boot
- Frontend logs: Browser console (F12)
- Database logs: MySQL error log location varies by OS

## 🆘 Need Help?

1. Check the main [README.md](README.md) for detailed documentation
2. Review troubleshooting section above
3. Check backend logs for error messages
4. Verify all prerequisites are installed and running
5. Ensure all configuration files are properly set up

## 📝 Configuration Checklist

- [ ] MySQL installed and running
- [ ] Redis installed and running
- [ ] Database `ims` created and schema imported
- [ ] MySQL credentials configured in `application-dev.yml`
- [ ] Upload directory created with proper permissions
- [ ] Upload path configured in `application-dev.yml`
- [ ] Backend builds successfully (`mvn clean compile`)
- [ ] Frontend dependencies installed (`npm install`)
- [ ] Backend starts without errors
- [ ] Frontend dev server starts successfully
- [ ] Can access frontend at http://localhost:5173
- [ ] Can register and login
- [ ] Can upload images

## ✨ You're All Set!

Once all checkboxes are ticked, you're ready to develop with PixelStack! Happy coding!
