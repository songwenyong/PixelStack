# PixelStack - Image Management System

A full-stack image management system built with Spring Boot and Vue 3, providing comprehensive image storage, album management, categorization, and social features.

## Project Structure

```
PixelStack/
├── pixel-backend/          # Spring Boot backend service
│   ├── src/                # Java source code
│   ├── sql/                # Database scripts
│   └── pom.xml             # Maven configuration
├── pixel-front/            # Vue 3 frontend application
│   ├── src/                # Vue source code
│   ├── dist/               # Production build output
│   └── package.json        # NPM configuration
└── README.md               # This file
```

## Tech Stack

### Backend
- **Java 1.8**
- **Spring Boot 2.7.18**
- **MySQL 8.0** - Primary database
- **Redis** - Caching and session management
- **MyBatis Plus 3.5.3.1** - ORM framework
- **JWT 0.11.5** - Authentication
- **FastJSON 2.0.25** - JSON processing

### Frontend
- **Vue 3.4** - Progressive JavaScript framework
- **TypeScript 5.3** - Type-safe JavaScript
- **Vite 5.0** - Fast build tool
- **Vue Router 4.2** - Client-side routing
- **Pinia 2.1** - State management
- **Naive UI 2.38** - UI component library
- **Axios 1.6** - HTTP client

## Features

### Core Features
- 🖼️ **Image Management** - Upload, browse, search, and delete images
- 📁 **Album Management** - Create albums and organize images
- 🏷️ **Category System** - Hierarchical category structure
- 🔖 **Tag System** - Flexible tagging for albums
- ⭐ **Social Features** - Star/favorite images and albums
- 👤 **User Management** - Registration, authentication, and profiles
- 🔐 **JWT Authentication** - Secure token-based authentication
- 💾 **MD5 Deduplication** - Automatic duplicate image detection

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK) 1.8+**
- **Maven 3.6+**
- **Node.js 18+** and npm
- **MySQL 8.0+**
- **Redis 6.0+**
- **Git**

## Quick Start

### 1. Clone the Repository

```bash
git clone <repository-url>
cd PixelStack
```

### 2. Database Setup

#### Start MySQL service

```bash
# macOS
brew services start mysql
# or
mysql.server start

# Linux
sudo systemctl start mysql
```

#### Create database and import schema

```bash
# Login to MySQL
mysql -u root -p

# Create database
CREATE DATABASE ims CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# Exit MySQL shell
exit

# Import database schema
mysql -u root -p ims < pixel-backend/sql/ims.sql
```

### 3. Redis Setup

```bash
# Start Redis service
# macOS
brew services start redis
# or
redis-server

# Linux
sudo systemctl start redis

# Verify Redis is running
redis-cli ping
# Should return: PONG
```

### 4. Backend Configuration

Edit `pixel-backend/src/main/resources/application-dev.yml` to configure your database credentials:

```yaml
spring:
  datasource:
    username: root
    password: your_mysql_password  # Change this to your MySQL password
```

#### Create upload directory

```bash
# Create directory for file uploads
sudo mkdir -p /data/ims/upload/dev
sudo chown -R $USER /data/ims

# Alternative: Use local directory (update application-dev.yml)
mkdir -p ~/ims-uploads
```

If using local directory, update `application-dev.yml`:

```yaml
file:
  upload:
    path: /Users/your_username/ims-uploads/
```

### 5. Start Backend Service

```bash
cd pixel-backend

# Build and run with Maven
mvn clean spring-boot:run

# Or build JAR and run
mvn clean package -DskipTests
java -jar target/ims-backend-1.0.0.jar
```

The backend API will be available at: **http://localhost:8080/api**

### 6. Start Frontend Development Server

```bash
cd pixel-front

# Install dependencies (if not already done)
npm install

# Start development server
npm run dev
```

The frontend will be available at: **http://localhost:5173**

### 7. Access the Application

1. Open your browser and navigate to: **http://localhost:5173**
2. Register a new user account
3. Login and start using the application!

## Development Scripts

### Backend Commands

```bash
cd pixel-backend

# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package without tests
mvn package -DskipTests

# Run application
mvn spring-boot:run

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Frontend Commands

```bash
cd pixel-front

# Install dependencies
npm install

# Start development server
npm run dev

# Build for production
npm run build

# Type check
npm run type-check

# Lint and fix
npm run lint

# Format code
npm run format

# Preview production build
npm run preview
```

## API Documentation

### Base URL

```
http://localhost:8080/api
```

### Authentication

Most endpoints require JWT authentication. Include the token in the Authorization header:

```
Authorization: Bearer <your_jwt_token>
```

### Key Endpoints

#### User Management
- `POST /user/register` - Register new user
- `POST /user/login` - User login (returns JWT token)
- `GET /user/current` - Get current user info
- `PUT /user/update` - Update user profile

#### Image Management
- `POST /image/upload` - Upload image
- `GET /image/page` - Get images (paginated)
- `GET /image/stared/page` - Get starred images
- `POST /image/{id}/star` - Star an image
- `DELETE /image/{id}/star` - Unstar an image
- `DELETE /image/{id}` - Delete image

#### Album Management
- `POST /album` - Create album
- `GET /album/page` - Get albums (paginated)
- `GET /album/{id}` - Get album details
- `POST /album/{id}/images` - Add images to album
- `DELETE /album/{id}/images/{imageId}` - Remove image from album
- `POST /album/{id}/star` - Star an album
- `DELETE /album/{id}` - Delete album

#### Category & Tags
- `GET /category/tree` - Get category tree
- `POST /category` - Create category
- `DELETE /category/{id}` - Delete category

## Configuration

### Environment Variables

You can override configuration using environment variables:

```bash
# Database
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/ims
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=yourpassword

# Redis
SPRING_REDIS_HOST=localhost
SPRING_REDIS_PORT=6379

# JWT
JWT_SECRET=your-secret-key
JWT_EXPIRATION=604800000

# File Upload
FILE_UPLOAD_PATH=/data/ims/upload/
```

### Frontend Environment Files

- `.env` - Common environment variables
- `.env.development` - Development environment
- `.env.production` - Production environment

Edit `pixel-front/.env.development` to configure API endpoint:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

## Database Schema

Key tables:
- `t_user_info` - User accounts and profiles
- `t_image_info` - Image metadata and storage info
- `t_album` - Photo albums
- `t_category` - Hierarchical categories
- `t_tag` - Tags for organization
- `t_album_image_relation` - Album-image associations
- `t_album_tag_relation` - Album-tag associations
- `t_image_star_relation` - Image favorites
- `t_album_star_relation` - Album favorites

See `pixel-backend/sql/ims.sql` for complete schema.

## Troubleshooting

### Backend Issues

**Database connection failed**
- Verify MySQL is running: `mysql -u root -p`
- Check credentials in `application-dev.yml`
- Ensure database `ims` exists

**Redis connection failed**
- Verify Redis is running: `redis-cli ping`
- Check Redis configuration in `application-dev.yml`

**File upload failed**
- Check upload directory exists and has write permissions
- Verify file size is under 10MB limit

**Port 8080 already in use**
- Change port in `application.yml`: `server.port: 8081`

### Frontend Issues

**npm install fails**
- Clear npm cache: `npm cache clean --force`
- Delete `node_modules` and `package-lock.json`, then reinstall

**Cannot connect to backend API**
- Verify backend is running on port 8080
- Check CORS configuration
- Update API base URL in `.env.development`

## Production Deployment

### Backend

1. Update `application-prod.yml` with production settings
2. Build production JAR:
   ```bash
   mvn clean package -DskipTests
   ```
3. Run with production profile:
   ```bash
   java -jar -Dspring.profiles.active=prod target/ims-backend-1.0.0.jar
   ```

### Frontend

1. Update `.env.production` with production API URL
2. Build production bundle:
   ```bash
   npm run build
   ```
3. Deploy `dist/` directory to web server (Nginx, Apache, etc.)

## Project Documentation

- [Backend README](pixel-backend/README.md) - Detailed backend documentation
- [Backend Development Notes](pixel-backend/CLAUDE.md) - Development commands and notes
- [Frontend Quick Start](pixel-front/QUICK_START.md) - Frontend setup guide

## License

This project is licensed under the MIT License.

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Support

For issues and questions:
- Open an issue on GitHub
- Check existing documentation in project READMEs
