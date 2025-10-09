# Claude Code Development Notes

## Project Structure
```
pixel-backend/
├── src/main/java/com/pixelstack/ims/
│   ├── ImsBackendApplication.java          # Main application class
│   ├── controller/                         # REST controllers
│   │   ├── UserController.java            # User management
│   │   ├── ImageController.java           # Image management
│   │   ├── AlbumController.java           # Album management
│   │   ├── CategoryController.java        # Category management
│   │   └── FileController.java            # File upload/download
│   ├── service/                           # Business logic
│   │   ├── UserService.java
│   │   ├── ImageService.java
│   │   ├── AlbumService.java
│   │   ├── CategoryService.java
│   │   └── impl/                          # Service implementations
│   ├── mapper/                            # MyBatis mappers
│   ├── entity/                            # Database entities
│   ├── dto/                               # Data transfer objects
│   ├── config/                            # Configuration classes
│   ├── util/                              # Utility classes
│   └── exception/                         # Exception handling
└── src/main/resources/
    ├── application.yml                    # Main configuration
    ├── application-dev.yml               # Development config
    ├── application-prod.yml              # Production config
    └── mapper/                           # MyBatis XML files
```

## Build and Run Commands

### Build the project
```bash
mvn clean compile
```

### Run tests
```bash
mvn test
```

### Package the application
```bash
mvn clean package
```

### Run the application
```bash
mvn spring-boot:run
```

Or run the JAR file:
```bash
java -jar target/ims-backend-1.0.0.jar
```

## Development Commands

### Create database and tables
```sql
-- Execute the SQL script
source sql/ims.sql
```

### Test API endpoints
```bash
# Register a new user
curl -X POST http://localhost:8080/api/user/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"123456","email":"test@example.com"}'

# Login
curl -X POST http://localhost:8080/api/user/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"123456"}'

# Upload image (with authentication)
curl -X POST http://localhost:8080/api/image/upload \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -F "file=@/path/to/image.jpg" \
  -F "title=Test Image"
```

## Environment Setup

### Prerequisites
- Java 1.8+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

### Database Setup
1. Create database: `CREATE DATABASE ims CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;`
2. Execute SQL script: `source sql/ims.sql`
3. Update database credentials in `application.yml`

### File Storage Setup
1. Create upload directory: `mkdir -p /data/ims/upload/`
2. Ensure write permissions for the application
3. Update file paths in `application.yml` if needed

## Configuration Notes

### Application Profiles
- `dev` - Development environment
- `prod` - Production environment

### Key Configuration Properties
- `server.port` - Application port (default: 8080)
- `spring.datasource.*` - Database connection settings
- `spring.redis.*` - Redis connection settings
- `jwt.secret` - JWT signing secret
- `jwt.expiration` - JWT token expiration time
- `file.upload.path` - File upload directory
- `file.upload.url-prefix` - File access URL prefix

### Security Notes
- All endpoints except `/user/login`, `/user/register`, and `/files/**` require JWT authentication
- JWT tokens are valid for 7 days by default
- File uploads are limited to 10MB per file
- CORS is configured to allow all origins (adjust for production)

## Troubleshooting

### Common Issues
1. **Database connection failed**: Check MySQL service and credentials
2. **Redis connection failed**: Check Redis service and configuration
3. **File upload failed**: Check directory permissions and disk space
4. **JWT validation failed**: Check token format and expiration

### Logging
- Application logs are configured for DEBUG level in development
- MyBatis SQL logging is enabled for debugging
- Adjust logging levels in `application.yml` as needed