# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

PixelStack is a Spring Boot image management system (IMS) that provides image upload, storage, sharing, and social features. The application is built with Java 8, Spring Boot 2.1.3, MyBatis, MySQL, and Redis.

## Development Commands

### Build and Package
```bash
mvn clean package
```

### Run Application
```bash
# Start the application using the provided script
sh bin/ims.sh ims_pixelStack start

# Stop the application
sh bin/ims.sh ims_pixelStack stop

# Restart the application
sh bin/ims.sh ims_pixelStack restart

# Check application status
sh bin/ims.sh ims_pixelStack status

# Run directly with Maven (for development)
mvn spring-boot:run
```

### Testing
```bash
# Run tests
mvn test

# Test endpoints
curl http://localhost:8080/hello
curl http://localhost:8080/image/getImageList
```

## Architecture

### Package Structure
- `com.pixelstack.ims.controller` - REST API controllers
- `com.pixelstack.ims.service` - Business logic layer
- `com.pixelstack.ims.mapper` - Data access layer (MyBatis)
- `com.pixelstack.ims.domain` - Entity/domain models
- `com.pixelstack.ims.entity` - Data transfer objects
- `com.pixelstack.ims.common` - Shared utilities and configurations
  - `Auth` - JWT authentication and authorization
  - `ImageHelper` - Image processing utilities
  - `Redis` - Redis configuration
  - `Configuration` - Application configuration
  - `exception` - Custom exception handling

### Key Technologies
- **Spring Boot 2.1.3** - Main framework
- **MyBatis** - ORM for database operations using annotations (@Select, @Insert, @Update, @Delete)
- **MySQL 8.0.13** - Primary database
- **Redis** - Caching and session storage
- **JWT** - Token-based authentication
- **Lombok** - Code generation
- **PageHelper** - Pagination support
- **Im4java** - Image processing
- **FastJSON** - JSON serialization

### Database Configuration
Configure MySQL and Redis settings in `src/main/resources/application.properties`:
- Database connection details (lines 2-6)
- Redis configuration (lines 31-38)
- File upload limits (lines 41-46)
- Static resource mapping (lines 22-26)

### Authentication
The application uses JWT tokens for authentication. Controllers requiring authentication should use the `@UserLoginToken` annotation.

### Image Storage
Images are stored locally with the path configured in:
1. `application.properties` line 26 for resource mapping
2. `ImageService.java` line 33 for file storage path

### API Structure
- Base URL: `http://localhost:8080`
- User management: `/user/*`
- Image operations: `/image/*`
- Comments: `/Comment/*`
- Admin functions: `/admin/*`

The application follows RESTful conventions with @RestController, @RequestMapping, @GetMapping, and @PostMapping annotations.

## Configuration Requirements

Before running the application:
1. Install MySQL and create `pixelstack` database
2. Install Redis server
3. Update database credentials in `application.properties`
4. Ensure image storage directory exists and has proper permissions
5. Configure Java 8 environment

## File Upload
- Max file size: 5MB
- Max request size: 50MB
- Supports batch uploads
- Images are processed and stored in multiple sizes (original, small, big)